/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, derivo GmbH
 * Copyright (c) 2019 SisInf Lab, Polytechnic University of Bari
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
 * Copyright (c) 2019 SisInf Lab, Polytechnic University of Bari
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

package it.poliba.sisinflab.swottools.owllinkmatchmaking.server;


import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.ClassExpressionWithNormImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.server.parser.OWLlinkMatchmakingXMLRequestParserHandler;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.server.renderer.OWLlinkMatchmakingXMLResponseRenderer;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.GetCovering;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.NormImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.PairOfClassExpressionsWithNormImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.SetOfIndividualsWithClassExpressionAndNormImpl;
import org.mortbay.http.HttpResponse;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.Response;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualsImpl;
import org.semanticweb.owlapi.owllink.server.OWLlinkReasonerBridge;
import org.semanticweb.owlapi.owllink.server.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.owllink.server.parser.OWLLinkRequestListener;
import org.semanticweb.owlapi.owllink.server.parser.OWLlinkXMLRequestParserHandler;
import org.semanticweb.owlapi.reasoner.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

/**
 * The <code>OWLlinkReasonerBridge</code> mediates between OWLlink and an implementation of OWLReasoner.
 * It supports the core OWLlink queries (as can be simulated via the OWL API) as well as the retraction extension.
 * <p/>
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public abstract class OWLlinkMatchmakingReasonerBridge extends OWLlinkReasonerBridge implements OWLlinkMatchmakingReasonerBridgeInterface {

    public OWLlinkMatchmakingReasonerBridge(OWLReasonerFactory factory, OWLlinkReasonerConfiguration configuration) {
        super(factory, configuration);
    }
    @Override
    public void answer(Request query) {

        if (query instanceof KBRequestWithTwoExpressionsOrIndividualsImpl) {
            KBRequestWithTwoExpressionsOrIndividualsImpl request = (KBRequestWithTwoExpressionsOrIndividualsImpl) query;
            switch (((KBRequestWithTwoExpressionsOrIndividualsImpl) query).getRequestType()){
                case GET_ABDUCTION:
                    getAbduction(request);
                    break;
                case GET_BONUS:
                    getBonus(request);
                    break;
                case GET_DIFFERENCE:
                    getDifference(request);
                    break;
                case GET_CONTRACTION:
                    getContraction(request);
                    break;
            }

        }
        else if (query instanceof GetCovering) {
            getCovering((GetCovering)query);
        }
        else
           super.answer(query);
    }


    @Override
    protected void reloadReasoner(IRI kb){
        OWLlinkReasonerConfiguration configuration = getReasonerConfiguration(kb, true);
        OWLReasoner reasoner;
        OWLOntology ontology = this.getOntologyManager(kb).getOntology(kb);
        if (configuration.getOWLReasonerConfiguration() == null)
            reasoner = factory.createNonBufferingReasoner(ontology);
        else
            reasoner = factory.createNonBufferingReasoner(ontology, configuration.getOWLReasonerConfiguration());
        this.reasonersByKB.put(kb, reasoner);
    }
    @Override
    public synchronized boolean process(InputStream in, OutputStream out, HttpResponse response, boolean zipContentIfAppropriate) throws SAXException, IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            OWLOntology ontology = manager.createOntology();
            final OWLlinkXMLRequestParserHandler handler = new OWLlinkMatchmakingXMLRequestParserHandler(ontology, prov, null);
            final List<Response> responses = new ArrayList<Response>();

            final OWLLinkRequestListener listener = new OWLLinkRequestListener() {
                public void requestAdded(Request request) {
                    try {
                        request.accept(OWLlinkMatchmakingReasonerBridge.this);
                    } catch (Exception e) {
                        handle(e);
                    }
                    responses.add(getResponse());
                }
            };
            handler.setRequestListener(listener);
            parser.parse(in, handler);

            boolean useCompression = false;
            if (responses.size() > 20 && zipContentIfAppropriate) {
                useCompression = true;
                response.setField("content-encoding", "gzip");
            }
            GZIPOutputStream gzipOut = useCompression ? new GZIPOutputStream(out) : null;
            OutputStreamWriter writer = new OutputStreamWriter(gzipOut == null ? out : gzipOut);
            OWLlinkMatchmakingXMLResponseRenderer renderer = new OWLlinkMatchmakingXMLResponseRenderer();
            List<Request> requests = handler.getRequest();
            renderer.render(writer, prov, requests, responses);
            writer.flush();
            if (gzipOut != null)
                gzipOut.finish();

            return zipContentIfAppropriate;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}


