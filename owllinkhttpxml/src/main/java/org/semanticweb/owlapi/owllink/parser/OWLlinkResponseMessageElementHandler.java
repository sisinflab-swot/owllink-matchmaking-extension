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
import org.semanticweb.owlapi.owllink.Response;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class OWLlinkResponseMessageElementHandler extends AbstractOWLlinkElementHandler<List<Object>> {
    protected List<Object> responses;

    public OWLlinkResponseMessageElementHandler(OWLlinkXMLParserHandler handler) {
        super(handler);
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
    }

    public void startElement(String name) throws OWLXMLParserException {
        this.responses = new Vector<Object>();
    }

    public List<Object> getOWLLinkObject() {
        return this.responses;
    }

    public void handleChild(OWLlinkElementHandler handler) throws OWLXMLParserException {
        try {
            if (handler.getOWLObject() instanceof Response)
                this.responses.add(handler.getOWLObject());
        } catch (OWLXMLParserException e) {
            e.printStackTrace();
        }
    }

    public void endElement() throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkResponseElementHandler handler) throws OWLXMLParserException {
        //if response ==> Prefixes ==> which kb? prov.put(kb, prefixes);
        this.responses.add(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkErrorElementHandler handler) throws OWLXMLParserException {
        this.responses.add(handler.getOWLLinkObject());
    }

    protected void handle(Response response) {
        this.responses.add(response);
    }

    public void handleChild(OWLlinkClassSynsetElementHandler handler) throws OWLXMLParserException {
    }


    public void handleChild(OWLlinkObjectPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkDataPropertySynsetElementHandler handler) throws OWLXMLParserException {
    }

    public void handleChild(OWLlinkBooleanResponseElementHandler handler) {
        handle(handler.getOWLLinkObject());
    }

    public void handleChild(OWLlinkStringResponseElementHandler handler) {
        handle(handler.getOWLLinkObject());
    }


}
