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
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkDifferentIndividualsTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLDifferentIndividualsAxiom(getOWLIndividual("A"), getOWLIndividual("B"), getOWLIndividual("C")));
        axioms.add(getDataFactory().getOWLSameIndividualAxiom(getOWLIndividual("A"), getOWLIndividual("D")));
        return axioms;
    }

    public void testAreIndividualsDisjoint() throws Exception {
        Set<OWLIndividual> indis = CollectionFactory.createSet();
        indis.add(getOWLIndividual("A"));
        indis.add(getOWLIndividual("B"));
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDifferentIndividualsAxiom(indis));
        BooleanResponse answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());
        indis.add(getOWLIndividual("C"));

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLDifferentIndividualsAxiom(indis));
        answer = super.reasoner.answer(query);
        assertTrue(answer.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSameIndividualAxiom(indis));
        answer = super.reasoner.answer(query);
        assertFalse(answer.getResult());
    }

    public void testAreIndividualsDisjointViaOWLReasoner() throws Exception {
        Set<OWLIndividual> indis = CollectionFactory.createSet();
        indis.add(getOWLIndividual("A"));
        indis.add(getOWLIndividual("B"));
        OWLAxiom axiom = getDataFactory().getOWLDifferentIndividualsAxiom(indis);
        assertTrue(super.reasoner.isEntailed(axiom));

        indis.add(getOWLIndividual("C"));

        axiom = getDataFactory().getOWLDifferentIndividualsAxiom(indis);
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSameIndividualAxiom(indis);
        assertFalse(super.reasoner.isEntailed(axiom));
    }

    public void testGetDisjointIndividuals() throws Exception {
        GetDifferentIndividuals query = new GetDifferentIndividuals(getKBIRI(), getOWLIndividual("B"));
        SetOfIndividualSynsets response = super.reasoner.answer(query);
        assertTrue(response.getSynsets().size() == 2);
    }


    public void testGetDisjointIndividualsWithOWLReasoner() throws Exception {
        NodeSet<OWLNamedIndividual> response = super.reasoner.getDifferentIndividuals(getOWLIndividual("B"));
        assertTrue(response.getFlattened().size() == 2);
    }

    public void testGetFlattenedDisjointIndividuals() throws Exception {
        GetFlattenedDifferentIndividuals query = new GetFlattenedDifferentIndividuals(getKBIRI(), getOWLIndividual("B"));
        SetOfIndividuals response = super.reasoner.answer(query);

        assertTrue(response.size() == 3);
        assertTrue(response.contains(getOWLIndividual("A")));
        assertTrue(response.contains(getOWLIndividual("C")));
        assertTrue(response.contains(getOWLIndividual("D")));

    }
}
