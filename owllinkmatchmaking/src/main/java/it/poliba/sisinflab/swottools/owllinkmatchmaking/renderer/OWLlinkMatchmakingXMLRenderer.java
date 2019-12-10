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

package it.poliba.sisinflab.swottools.owllinkmatchmaking.renderer;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.GetCovering;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkXMLRenderer;

import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_COVERING;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS;


public class OWLlinkMatchmakingXMLRenderer extends OWLlinkXMLRenderer {
    @Override
    public void answer(Request request) {
        if (request instanceof KBRequestWithTwoExpressionOrIndividuals) {
            KBRequestWithTwoExpressionsOrIndividualsImpl query = (KBRequestWithTwoExpressionsOrIndividualsImpl) request;
            writer.writeStartElement(OWLlinkXMLVocabulary.valueOf(query.getRequestType().name()));
            final IRI kb = query.getKB();
            writer.writeKBAttribute(kb);
            writer.writeOWLObject(query.getResource().getOWLObject(), kb);
            writer.writeOWLObject(query.getRequest().getOWLObject(), kb);
            writer.writeEndElement();
        } else if (request instanceof GetCovering) {
            GetCovering query = (GetCovering) request;
            writer.writeStartElement(GET_COVERING);
            final IRI kb = query.getKB();
            writer.writeKBAttribute(kb);
            writer.writeOWLObject(query.getRequest().getOWLObject(), kb);
            writer.writeStartElement(SET_OF_INDIVIDUALS);
            query.getSetOfIndividuals().forEach(a -> {
                writer.writeOWLObject(a, kb);
            });
            writer.writeEndElement();


            writer.writeEndElement();
        }
    }


}
