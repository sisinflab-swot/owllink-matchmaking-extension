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

import org.coode.owlapi.owlxmlparser.AbstractOWLElementHandler;
import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.Request;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 21.10.2009
 */
public abstract class AbstractOWLlinkElementHandler<O> extends AbstractOWLElementHandler<O> implements OWLlinkElementHandler<O> {
    OWLlinkXMLParserHandler handler;

    public AbstractOWLlinkElementHandler(OWLXMLParserHandler handler) {
        super(handler);
        this.handler = (OWLlinkXMLParserHandler) handler;
    }

    public void handleChild(OWLlinkClassSubClassesPairElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataPropertySubDataPropertiesPairElementHandler handler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleChild(OWLlinkObjectPropertySubPropertiesPairElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkSubDataPropertySynsetsElementHandler handler) throws OWLXMLParserException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleChild(OWLlinkSubObjectPropertySynsetsElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkSubClassSynsetsElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkResponseElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkErrorElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkBooleanResponseElementHandler handler) {
    }

    public void handleChild(OWLlinkConfigurationElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataRangeElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkLiteralElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkPrefixElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkProtocolVersionElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkReasonerVersionElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkPublicKBElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkSupportedExtensionElemenetHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkClassSynsetElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkSettingElementHandler handler) throws OWLXMLParserException {

    }

    public void handleChild(OWLlinkPropertyElementHandler handler) throws OWLXMLParserException {

    }


    public void handleChild(OWLlinkObjectPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkIndividualSynsetElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkResponseMessageElementHandler handler) throws OWLXMLParserException {
    }

    public abstract O getOWLLinkObject() throws OWLXMLParserException;

    public void handleChild(OWLlinkDescriptionElementHandler handler) {
    }

    public O getOWLObject() throws OWLXMLParserException {
        return this.getOWLLinkObject();
    }


    public IRI getFullIRI(String value) throws OWLXMLParserException, OWLParserException {
        return super.getIRI(value);
    }

    protected OWLlinkElementHandler getParentHandler() {
        return (OWLlinkElementHandler) super.getParentHandler();
    }

    protected Request getRequest() {
        int index = handler.responseMessageHandler.getOWLLinkObject().size();
        return handler.getRequest(index);
    }
}
