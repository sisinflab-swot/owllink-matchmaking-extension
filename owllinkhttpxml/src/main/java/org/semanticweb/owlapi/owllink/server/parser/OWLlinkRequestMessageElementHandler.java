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

package org.semanticweb.owlapi.owllink.server.parser;

import org.coode.owlapi.owlxmlparser.OWLXMLParserException;
import org.coode.owlapi.owlxmlparser.OWLXMLParserHandler;
import org.semanticweb.owlapi.owllink.Request;

import java.util.List;
import java.util.Vector;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkRequestMessageElementHandler extends AbstractOWLlinkElementHandler<List<Request>>
        implements OWLlinkElementHandler<List<Request>> {
    private List<Request> requests;
    private OWLLinkRequestListener listener;

    public OWLlinkRequestMessageElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.requests = new Vector<Request>();
    }

    public void handleChild(OWLlinkRequestElementHandler handler) {
        try {
            if (handler.getOWLObject() instanceof Request) {
                requests.add((Request) handler.getOWLObject());
                if (this.listener != null)
                    this.listener.requestAdded((Request) handler.getOWLlinkObject());
            }
        } catch (OWLXMLParserException e) {
        }
    }

    public void endElement() throws OWLXMLParserException {
    }

    public List<Request> getOWLObject() throws OWLXMLParserException {
        return this.requests;
    }

    public void setRequestListener(OWLLinkRequestListener listener) {
        this.listener = listener;
    }
}
