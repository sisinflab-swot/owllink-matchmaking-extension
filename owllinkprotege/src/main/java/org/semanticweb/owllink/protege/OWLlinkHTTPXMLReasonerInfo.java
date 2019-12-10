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
package org.semanticweb.owllink.protege;

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkReasoner;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Olaf Noppens
 * Date: 27.10.2010
 */
public class OWLlinkHTTPXMLReasonerInfo extends AbstractProtegeOWLReasonerInfo {
    private org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory factory;

    @Override
    public void initialise() throws Exception {
        super.initialise();
        factory = new org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasonerFactory() {
            @Override
            public OWLlinkReasoner createNonBufferingReasoner(OWLOntology ontology, OWLReasonerConfiguration config) throws IllegalConfigurationException {
                if (config instanceof OWLlinkReasonerConfiguration)
                    return new OWLlinkHTTPXMLProtegeReasoner(ontology, (OWLlinkReasonerConfiguration) config, BufferingMode.NON_BUFFERING);
                else
                    throw new IllegalConfigurationException("Configuration must be a OWLlinkReasonerConfiguration", config);
            }

            @Override
            public OWLlinkReasoner createReasoner(OWLOntology ontology, OWLReasonerConfiguration config) throws IllegalConfigurationException {
                if (config instanceof OWLlinkReasonerConfiguration)
                    return new OWLlinkHTTPXMLProtegeReasoner(ontology, (OWLlinkReasonerConfiguration) config, BufferingMode.BUFFERING);
                else
                    throw new IllegalConfigurationException("Configuration must be a OWLlinkReasonerConfiguration", config);
            }
        };
    }

    public BufferingMode getRecommendedBuffering() {
        return BufferingMode.NON_BUFFERING;
    }

    public OWLReasonerFactory getReasonerFactory() {
        return factory;
    }

    @Override
    public OWLReasonerConfiguration getConfiguration(ReasonerProgressMonitor monitor) {
        OWLlinkHTTPXMLReasonerPreferences prefs = OWLlinkHTTPXMLReasonerPreferences.getInstance();
        URL reasonerURL = null;
        try {
            reasonerURL = new URL(prefs.getServerEndpointURL() + ":" + prefs.getServerEndpointPort());
        } catch (MalformedURLException e) {
            try {
                reasonerURL = new URL("localhost:8080");
            } catch (MalformedURLException e1) {
            }
        }
        return new OWLlinkReasonerConfiguration(monitor, reasonerURL, IndividualNodeSetPolicy.BY_SAME_AS);
    }
}
