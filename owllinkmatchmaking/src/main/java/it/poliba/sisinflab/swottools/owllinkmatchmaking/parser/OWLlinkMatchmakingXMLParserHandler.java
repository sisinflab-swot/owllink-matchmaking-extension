/*
 * This file is part of the OWLlink API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
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


package it.poliba.sisinflab.swottools.owllinkmatchmaking.parser;

import org.coode.owlapi.owlxmlparser.OWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.parser.AbstractOWLlinkElementHandlerFactory;
import org.semanticweb.owlapi.owllink.parser.OWLlinkElementHandler;
import org.semanticweb.owlapi.owllink.parser.OWLlinkXMLParserHandler;


public class OWLlinkMatchmakingXMLParserHandler extends OWLlinkXMLParserHandler {

    public OWLlinkMatchmakingXMLParserHandler(OWLOntology ontology, PrefixManagerProvider provider, Request[] requests, OWLElementHandler topHandler) {
        super(ontology, provider, requests, topHandler);
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.CLASS_EXPRESSION_WITH_NORM) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkClassExpressionWithNormElementHandler(handler);
            }

        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.PAIR_OF_CLASS_EXPRESSION_WITH_NORM) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkPairOfClassExpressionWithNormElementHandler(handler);
            }

        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS_WITH_CLASS_EXPRESSION_AND_NORM) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkSetOfIndividualsWithClassExpressionAndNormElementHandler(handler);
            }

        });
        addFactory(new AbstractOWLlinkElementHandlerFactory(OWLlinkXMLVocabulary.NORM) {
            public OWLlinkElementHandler createHandler(OWLXMLParserHandler handler) {
                return new OWLlinkNormElementHandler(handler);
            }

        });
    }
}
