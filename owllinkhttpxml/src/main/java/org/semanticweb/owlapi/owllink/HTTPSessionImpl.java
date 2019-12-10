/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, derivo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkErrorResponseException;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessage;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessageImpl;
import org.semanticweb.owlapi.owllink.parser.OWLlinkXMLParserHandler;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkXMLRenderer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @author Olaf Noppens
 */
public class HTTPSessionImpl implements HTTPSession {
    /**
     * threshold for using gzip, the value corresponds to characters not bytes.
     */
    private int gzipThreshold = 5000;
    protected boolean useCompression = true;

    protected URL reasonerURL;
    protected OWLOntologyManager manager;
    protected PrefixManagerProvider prov;
    protected boolean serverAcceptsGzipEncoding = false;

    /**
     * @param manager manager
     * @param prov prov
     * @throws MalformedURLException MalformedURLException
     */
    public HTTPSessionImpl(OWLOntologyManager manager, PrefixManagerProvider prov) throws MalformedURLException {
        this(manager, new URL("http://localhost:8080"), prov);
    }

    /**
     * @param manaager manaager
     * @param reasonerURL reasonerURL
     * @param prov prov
     */
    public HTTPSessionImpl(OWLOntologyManager manaager, URL reasonerURL, PrefixManagerProvider prov) {
        this.reasonerURL = reasonerURL;
        this.manager = manaager;
        this.prov = prov;
    }

    /** @param threshold threshold */
    public void setThresholdForCompressedContent(int threshold) {
        this.gzipThreshold = threshold;
    }

    /** @return threshold */
    public int getThresholdForCompressedContent() {
        return this.gzipThreshold;
    }

    /**
     * Determines whether content encoding for request should be used if (a) the server supports it and (b)
     * the content is above the {@link #getThresholdForCompressedContent()}.
     *
     * @param compression determines whether compression should be used or not
     */
    public void setUseCompression(boolean compression) {
        this.useCompression = compression;
    }

    /**
     * Sets the URL of the inference.
     *
     * @param url The URL
     */
    public void setReasonerURL(URL url) {
        // Pass this on to the reasoner connection
        reasonerURL = url;
    }

    /**
     * Gets the URL of the reasoner.
     *
     * @return url of the reasoner
     */
    public String getReasonerURL() {
        return reasonerURL.toString();
    }

    /** @return true if gzip is accepted */
    public boolean serverAcceptsGzipEncoding() {
        return this.serverAcceptsGzipEncoding;
    }

    /** @param request request
     * @return response */
    public ResponseMessage performRequests(Request<?>... request)  {
        OWLlinkXMLFactoryRegistry registry = OWLlinkXMLFactoryRegistry.getInstance();
        try (StringWriter out = new StringWriter();
             PrintWriter writer = new PrintWriter( out)) {
            //Handle the request
            OWLlinkXMLRenderer renderer = new OWLlinkXMLRenderer();
            renderer.addFactories(registry.getRequestRendererFactories());

            Request<?>[] askedRequests = renderer.render(writer, prov, request);

            HttpURLConnection conn = (HttpURLConnection) reasonerURL.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            writer.flush();
            String buffer = out.toString();

            conn.setRequestProperty("Content-Length", "" + buffer.length());   //todo length is wrong when compressing but we don't want to cache all the stuff in a buffer!
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.connect();

            OutputStream os;
            if (this.useCompression && this.serverAcceptsGzipEncoding() && buffer.length() > getThresholdForCompressedContent()) {
                os = new GZIPOutputStream(conn.getOutputStream());
            } else {
                os = conn.getOutputStream();
            }
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(buffer);
            if (os instanceof GZIPOutputStream) {
                ((GZIPOutputStream) os).finish();
            }
            osw.flush();
            osw.close();
            if (!serverAcceptsGzipEncoding) {
                Map<String, List<String>> map = conn.getHeaderFields();
                if (map.containsKey("Accept-Encoding")) {
                    String field = map.get("Accept-Encoding").toString();
                    if (field != null) {
                        StringTokenizer tokenizer = new StringTokenizer(field, ",");
                        while (tokenizer.hasMoreTokens()) {
                            if ("gzip".equals(tokenizer.nextToken())) {
                                serverAcceptsGzipEncoding = true;
                                break;
                            }
                        }
                    }
                }
            }
            // Get the response  if it is gzip or deflate handle it appropriate.
            InputStream is;
            if ("gzip".equals(conn.getContentEncoding())) {
                is = new BufferedInputStream(new GZIPInputStream(conn.getInputStream()));
            } else if ("deflate".equals(conn.getContentEncoding())) {
                is = new BufferedInputStream(new InflaterInputStream(conn.getInputStream(), new Inflater(true)));
            } else {
                is = conn.getInputStream();
            }
            try (Reader reader = new InputStreamReader(is)) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                SAXParser parser = factory.newSAXParser();
                OWLlinkXMLParserHandler handler = new OWLlinkXMLParserHandler(manager.createOntology(), prov, askedRequests, null);
                handler.addFactories(registry.getParserFactories());
                parser.parse(is, handler);
                reader.close();
                conn.disconnect();
                List<Object> responses = handler.getResponses();

                ResponseMessageImpl responseMessage = new ResponseMessageImpl(request);
                int i = 0;
                for (Object response : responses) {
                    //if additional inserts prefix query do not add it to responseMessage
                    if (!(request[i] instanceof OWLlinkXMLRenderer.InternalRequest)) {
                        if (response instanceof Response)
                            responseMessage.add((Response) response, i);
                        else if (response instanceof OWLlinkErrorResponseException)
                            responseMessage.add((OWLlinkErrorResponseException) response, i);
                    } else
                        throw new IllegalArgumentException("One request is not an internal request");
                    i++;
                }
                return responseMessage;
            }
        }
        catch (IOException e) {
            throw new OWLlinkReasonerIOException(e.getMessage(), e);
        } catch (Exception e) {
            throw new OWLlinkReasonerRuntimeException(e.getMessage(), e);
        }
    }
}