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
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.owllink.KBRequest;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;

/**
 * Abstract handler for all OWLlink KBResponse.
 * <p/>
 * This implementation handles the correct prefixname2prefix mapping
 * for handling abbreviated IRIs.
 * <p/>
 * Note that KBResponses that need the prefixname2prefix mapping for
 * abbreviated IRIs should derive from this abstract element handler class.
 * Otherwise the correct IRI resolution is not guaranteed.
 * <p/>
 * <p/>
 * Author: Olaf Noppens
 * Date: 30.11.2009
 */
public abstract class AbstractOWLlinkKBResponseElementHandler<R extends KBResponse> extends AbstractConfirmationElementHandler<R> {

    public AbstractOWLlinkKBResponseElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }

    public void startElement(String s) throws OWLXMLParserException {
        super.startElement(s);
        PrefixManagerProvider prefixProvider = handler.prov;
        IRI kb = getRequest().getKB();
        PrefixManager prefixes = prefixProvider.getPrefixes(kb);
        handler.setPrefixName2PrefixMap(prefixes.getPrefixName2PrefixMap());
    }

    protected KBRequest getRequest() {
        int index = handler.responseMessageHandler.getOWLLinkObject().size();
        return (KBRequest) handler.getRequest(index);
    }

}
