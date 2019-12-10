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

package org.semanticweb.owlapi.owllink.server.serverfactory;

import org.semanticweb.owlapi.owllink.server.AbstractOWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.owllink.server.OWLlinkHTTPXMLServer;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.lang.reflect.Field;

/**
 * Author: Olaf Noppens
 * Date: 25.02.2010
 */
public class HermiTServerFactory implements OWLlinkServerFactory {

    public static final String FACTORY_CLASSNAME = "org.semanticweb.HermiT.Reasoner$ReasonerFactory";
    public static final String HERMIT_REASONERFACTORY = "org.semanticweb.HermiT.Configuration";

    public OWLlinkHTTPXMLServer createServer(int port) {
       OWLReasonerConfiguration configuration = null;
        try {
            Class reasonerFactoryClass = Class.forName(HERMIT_REASONERFACTORY);
            configuration = (OWLReasonerConfiguration) reasonerFactoryClass.newInstance();
            Field field = reasonerFactoryClass.getDeclaredField("individualNodeSetPolicy");
            field.set(configuration, IndividualNodeSetPolicy.BY_SAME_AS);

        } catch (Exception e) {
            System.err.println("Can't set individualNodeSetPolicy to SAME_AS");
        }

        AbstractOWLlinkReasonerConfiguration config = new AbstractOWLlinkReasonerConfiguration(configuration);
        config.setSupportedDatatypes(OWL2Datatype.XSD_LONG.getIRI(),
                OWL2Datatype.XSD_INT.getIRI(),
                OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_SHORT.getIRI(),
                OWL2Datatype.OWL_REAL.getIRI());
        try {
            Class factoryClass = Class.forName(FACTORY_CLASSNAME);
            OWLReasonerFactory factory = (OWLReasonerFactory) factoryClass.newInstance();
            OWLlinkHTTPXMLServer server = new OWLlinkHTTPXMLServer(factory, config, port);
            return server;
        } catch (ClassNotFoundException e) {
            throw new OWLlinkServerSetupException(e);
        } catch (InstantiationException e) {
            throw new OWLlinkServerSetupException(e);
        } catch (IllegalAccessException e) {
            throw new OWLlinkServerSetupException(e);
        }

    }

    static void usage() {
        System.out.println("HermiT as OWLlink Server");
        System.out.println("OWLlink server that is backed by HermiT reasoner");
        System.out.println("");
        System.out.println("Usage: java HermiTServerFactory [-port portNum]");
        System.out.println("   -port portNum           The port number user by the server (default");
        System.out.println("                           port number used is 8080)");
        System.out.println("   -help                   Print this information");
        System.out.println("Make sure that the FaCT++ binaries are in your classpath or set it via \"-cp\"");
    }


    public static void main(String args[]) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equalsIgnoreCase("-help")) {
                usage();
                System.exit(0);
            } else if (arg.equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e1) {
                    System.err.println("Invalid port number: " + args[i]);
                    System.exit(1);
                }
            } else {
                System.err.println("Unrecognized option: " + arg);
                usage();
                System.exit(1);
            }
        }
        HermiTServerFactory factory = new HermiTServerFactory();
        OWLlinkHTTPXMLServer server = factory.createServer(port);
        server.run();
    }
}
