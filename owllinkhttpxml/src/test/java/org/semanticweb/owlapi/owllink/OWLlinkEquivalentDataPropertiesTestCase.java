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

package org.semanticweb.owlapi.owllink;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.DataPropertySynonyms;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkEquivalentDataPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("A"), getOWLDataProperty("B")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("B"), getOWLDataProperty("C")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("C"), getOWLDataProperty("A")));
        axioms.add(getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("D"), getOWLDataProperty("E")));
        return axioms;
    }

    public void testAreDataPropertiesEquivalent() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B")));
        BooleanResponse result = super.reasoner.answer(query);
        assertTrue(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("C")));
        result = super.reasoner.answer(query);
        assertTrue(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("D"), getOWLDataProperty("E"), getOWLDataProperty("A")));
        result = super.reasoner.answer(query);
        assertFalse(result.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("D"), getOWLDataProperty("E")));
        result = super.reasoner.answer(query);
        assertTrue(result.getResult());
    }

    public void testAreDataPropertiesEquivalentViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("A"), getOWLDataProperty("B"), getOWLDataProperty("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("D"), getOWLDataProperty("E"), getOWLDataProperty("A"));
        assertFalse(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLEquivalentDataPropertiesAxiom(getOWLDataProperty("D"), getOWLDataProperty("E"));
        assertTrue(super.reasoner.isEntailed(axiom));
    }

    public void testGetEquivalentDataProperties() throws Exception {
        GetEquivalentDataProperties query = new GetEquivalentDataProperties(getKBIRI(), getOWLDataProperty("A"));
        DataPropertySynonyms result = super.reasoner.answer(query);
        assertTrue(result.getSize() == 3);
        assertTrue(result.contains(getOWLDataProperty("A")));
        assertTrue(result.contains(getOWLDataProperty("B")));
        assertTrue(result.contains(getOWLDataProperty("C")));
    }

    public void testGetEquivalentDataPropertiesViaOWLReasoner() throws Exception {
        Node<OWLDataProperty> result = super.reasoner.getEquivalentDataProperties(getOWLDataProperty("A"));
        assertTrue(result.getSize() == 3);
        assertTrue(result.contains(getOWLDataProperty("A")));
        assertTrue(result.contains(getOWLDataProperty("B")));
        assertTrue(result.contains(getOWLDataProperty("C")));
    }
}
