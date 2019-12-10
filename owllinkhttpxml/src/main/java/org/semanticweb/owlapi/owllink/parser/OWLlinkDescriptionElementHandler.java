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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.builtin.response.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: noppens
 * Date: 21.10.2009
 * Time: 17:47:55
 * To change this template use File | Settings | File Templates.
 */
public class OWLlinkDescriptionElementHandler extends AbstractOWLlinkResponseElementHandler<Description> {
    private String name;
    private String message;
    private Set<PublicKB> publicKBs;
    private Set<Configuration> configurations;
    private ProtocolVersion pVersion;
    private ReasonerVersion rVersion;
    private Set<IRI> supportedExtensions;

    public OWLlinkDescriptionElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.name = null;
        this.message = null;
        this.publicKBs = new HashSet<PublicKB>();
        this.configurations = new HashSet<Configuration>();
        this.supportedExtensions = new HashSet<IRI>();
        this.pVersion = null;
        this.rVersion = null;
    }


    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equals(localName)) {
            this.name = value;
        } else if (OWLlinkXMLVocabulary.MESSAGE_ATTRIBUTE.getShortName().equals(localName)) {
            this.message = value;
        }
    }

    public void endElement() throws OWLXMLParserException {
        ((OWLlinkElementHandler) getParentHandler()).handleChild(this);
    }

    public void handleChild(OWLlinkSettingElementHandler handler) throws OWLXMLParserException {
        configurations.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkPropertyElementHandler handler) throws OWLXMLParserException {
        configurations.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkPublicKBElementHandler handler) throws OWLXMLParserException {
        publicKBs.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkSupportedExtensionElemenetHandler handler) throws OWLXMLParserException {
        supportedExtensions.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkReasonerVersionElementHandler handler) throws OWLXMLParserException {
        this.rVersion = handler.getOWLLinkObject();
    }

    public void handleChild(OWLlinkProtocolVersionElementHandler handler) throws OWLXMLParserException {
        this.pVersion = handler.getOWLLinkObject();
    }

    public Description getOWLLinkObject() {
        return new DescriptionImpl(name, configurations, rVersion, pVersion, supportedExtensions, publicKBs);
    }
}
