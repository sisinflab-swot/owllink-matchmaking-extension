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

package org.semanticweb.owlapi.owllink.server;

import org.mortbay.http.*;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.util.MultiException;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.net.BindException;

/**
 * @author Olaf Noppens
 */
public class OWLlinkHTTPXMLServer extends AbstractHttpHandler implements HttpHandler, OWLlinkServer {
    private static final long serialVersionUID = 5605350732186236386L;
    public static int DEFAULT_PORT = 8080;

    private int port;
    private OWLlinkReasonerBridge bridge;

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLReasonerConfiguration configuration, int port) {
        this(reasonerFactory, new AbstractOWLlinkReasonerConfiguration(configuration), port);
    }

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLlinkReasonerConfiguration configuration, int port) {
        this(reasonerFactory, new AbstractOWLlinkReasonerConfiguration(configuration), port, new OWLlinkReasonerBridge(reasonerFactory, configuration));
    }

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLlinkReasonerConfiguration configuration, int port, OWLlinkReasonerBridge bridge) {
        this.bridge = bridge;
        this.port = port;
    }

    public OWLlinkHTTPXMLServer(OWLReasonerFactory reasonerFactory, OWLReasonerConfiguration configuration) {
        this(reasonerFactory, configuration, DEFAULT_PORT);
    }

    public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) {
        try {
            response.setContentType("text/xml");
            bridge.process(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            // Create the server
            HttpServer server = new HttpServer();
            // Create a port listener
            SocketListener listener = new SocketListener();
            //listener.setHost("localhost");
            listener.setPort(port);
            listener.setMinThreads(2);
            listener.setMaxThreads(10);
            server.addListener(listener);
            // Create a context
            HttpContext context = server.addContext("/");
            context.addHandler(this);
            // Start the http server
            server.start();
        }
        catch (Exception e) {
            if (e instanceof MultiException && ((MultiException) e).getException(0) instanceof BindException)
                System.err.println("Cannot start server. Port " + port + " is already in use!");
            else
                e.printStackTrace();
            System.exit(0);
        }
    }


}
