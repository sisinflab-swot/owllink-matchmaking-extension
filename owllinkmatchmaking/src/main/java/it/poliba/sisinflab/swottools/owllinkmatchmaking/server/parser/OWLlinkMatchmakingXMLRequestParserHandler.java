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

package it.poliba.sisinflab.swottools.owllinkmatchmaking.server.parser;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import org.coode.owlapi.owlxmlparser.OWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.server.parser.AbstractOWLlinkElementHandlerFactory;
import org.semanticweb.owlapi.owllink.server.parser.OWLlinkRequestElementHandler;
import org.semanticweb.owlapi.owllink.server.parser.OWLlinkXMLRequestParserHandler;


public class OWLlinkMatchmakingXMLRequestParserHandler extends OWLlinkXMLRequestParserHandler {


    public OWLlinkMatchmakingXMLRequestParserHandler(OWLOntology ontology, PrefixManagerProvider prov, OWLElementHandler topHandler) {
        super(ontology, prov, topHandler);
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_ABDUCTION) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetNonStandardInferenceSingleElementHandler(handler, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_ABDUCTION);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_DIFFERENCE) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetNonStandardInferenceSingleElementHandler(handler, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_DIFFERENCE);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_BONUS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetNonStandardInferenceSingleElementHandler(handler, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_BONUS);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_CONTRACTION) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetNonStandardInferenceSingleElementHandler(handler, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_CONTRACTION);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.GET_COVERING) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkGetCoveringSingleElementHandler(handler);
            }
        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS) {
            public OWLlinkRequestElementHandler createHandler(OWLXMLParserHandler handler) {
                return new SetOfIndividualsElementHandler(handler);
            }

        });
    }
}
