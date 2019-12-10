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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Map;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 25.10.2009
 */
public class OWLlinkCreateKBElementHandler extends AbstractOWLlinkRequestElementHandler<CreateKB> {
    protected IRI kb;
    protected String name;
    protected Set<OWLlinkPrefixElementHandler.Prefix> prefixes;
    OWLlinkXMLRequestParserHandler handler;

    public OWLlinkCreateKBElementHandler(OWLXMLParserHandler handler) {
        super(handler);
        this.handler = (OWLlinkXMLRequestParserHandler) handler;
    }

    public void attribute(String localName, String value) throws OWLXMLParserException {
        if (OWLlinkXMLVocabulary.KB_ATTRIBUTE.getShortName().equalsIgnoreCase(localName)) {
            this.kb = IRI.create(value);
        } else if (OWLlinkXMLVocabulary.NAME_Attribute.getShortName().equalsIgnoreCase(localName)) {
            this.name = value;
        }
    }

    public void startElement(String name) throws OWLXMLParserException {
        super.startElement(name);
        this.kb = null;
        this.name = null;
        this.prefixes = CollectionFactory.createSet();
    }

    public IRI getKB() {
        return this.kb;
    }

    public void handleChild(OWLlinkPrefixElementHandler handler) {
        prefixes.add(handler.getOWLlinkObject());
    }

    @Override
    public void endElement() throws OWLXMLParserException {
        /*   DefaultPrefixManager manager = new DefaultPrefixManager();
       manager.clear();
       manager.setPrefix("owl:", Namespaces.OWL.toString());
       manager.setPrefix("xsd:", Namespaces.XSD.toString());
       manager.setPrefix("rdf:", Namespaces.RDF.toString());
       manager.setPrefix("rdfs:", Namespaces.RDFS.toString());

       Map<String, String> map = CollectionFactory.createMap();
       for (OWLlinkPrefixElementHandler.Prefix prefix : prefixes) {
           if (!prefix.name.endsWith(":"))
               map.put(prefix.name + ":", prefix.iri.toString());
           else
               map.put(prefix.name, prefix.iri.toString());
       }
       for (Map.Entry<String, String> entry : map.entrySet())
           manager.setPrefix(entry.getKey(), entry.getValue());

       handler.prov.putPrefixes(kb, manager);*/
        super.endElement();
    }

    public CreateKB getOWLObject() throws OWLXMLParserException {
        Map<String, String> map = CollectionFactory.createMap();
        for (OWLlinkPrefixElementHandler.Prefix prefix : prefixes) {
            if (!prefix.name.endsWith(":"))
                map.put(prefix.name + ":", prefix.iri.toString());
            else
                map.put(prefix.name, prefix.iri.toString());
        }
        if (this.name == null) {
            return new CreateKB(kb, map);
        }
        return new CreateKB(kb, name, map);
    }
}
