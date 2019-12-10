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

package org.semanticweb.owlapi.owllink.parser;

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 20.11.2009
 */
public class OWLlinkPrefixElementHandler extends AbstractOWLlinkElementHandler<OWLlinkPrefixElementHandler.Prefix> {
    protected String name;
    protected IRI iri;

    public OWLlinkPrefixElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.name = null;

    }

    public void attribute(String localName, String value) throws OWLParserException {
        if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equalsIgnoreCase(localName)) {
            this.name = value;
        }
        if (OWLlinkXMLVocabulary.IRI_ATTRIBUTE.getShortName().equals(localName)) {
            this.iri = getIRIFromAttribute(localName, value);
        }
    }


    public void endElement() throws OWLXMLParserException {
        getParentHandler().handleChild(this);
    }

    @Override
    public Prefix getOWLLinkObject() {
        return new Prefix(name, iri);
    }

    public static class Prefix {
        public final String name;
        public final IRI iri;

        public Prefix(String name, IRI iri) {
            this.name = name;
            this.iri = iri;
        }
    }
}