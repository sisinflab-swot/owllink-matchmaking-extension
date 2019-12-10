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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfClasses;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfDataProperties;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfObjectProperties;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkGetEntitiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("B")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("A"), getOWLClass("C")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("B"), getOWLClass("D")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("E"), getOWLClass("F")));
        axioms.add(getDataFactory().getOWLSubClassOfAxiom(getOWLClass("E"),
                getDataFactory().getOWLObjectExactCardinality(1, getOWLObjectProperty("p"))));

        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oA"), getOWLObjectProperty("oB")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oB"), getOWLObjectProperty("oC")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oC"), getOWLObjectProperty("oD")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oD"), getOWLObjectProperty("oE")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oE"), getOWLObjectProperty("oF")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("oG"), getOWLObjectProperty("oH")));

        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dA"), getOWLDataProperty("dB")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dB"), getOWLDataProperty("dC")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dC"), getOWLDataProperty("dD")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dD"), getOWLDataProperty("dE")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dE"), getOWLDataProperty("dF")));
        axioms.add(getDataFactory().getOWLSubDataPropertyOfAxiom(getOWLDataProperty("dG"), getOWLDataProperty("dH")));

        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("A"), getOWLIndividual("i1")));
        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("B"), getOWLIndividual("i2")));
        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("X"), getOWLIndividual("i3")));
        axioms.add(getDataFactory().getOWLClassAssertionAxiom(getOWLClass("Y"), getOWLIndividual("i4")));

        return axioms;
    }

    public void testGetAllClasses() throws Exception {
        GetAllClasses query = new GetAllClasses(getKBIRI());
        SetOfClasses answer = reasoner.answer(query);
        Set<OWLClass> classes = new HashSet<OWLClass>(getOntology().getClassesInSignature());
        assertTrue(answer.containsAll(classes));
        assertTrue(answer.size() == classes.size());
    }

    public void testGetAllDataProperties() throws Exception {
        GetAllDataProperties query = new GetAllDataProperties(getKBIRI());
        SetOfDataProperties answer = reasoner.answer(query);
        assertTrue(answer.containsAll(getOntology().getDataPropertiesInSignature()));
        assertTrue(answer.size() == getOntology().getDataPropertiesInSignature().size());
    }

    public void testGetAllObjectProperties() throws Exception {
        GetAllObjectProperties query = new GetAllObjectProperties(getKBIRI());
        SetOfObjectProperties answer = reasoner.answer(query);
        Set<OWLObjectProperty> props = getOntology().getObjectPropertiesInSignature();
        assertTrue(answer.containsAll(getOntology().getObjectPropertiesInSignature()));
        assertTrue(answer.size() == getOntology().getObjectPropertiesInSignature().size());
    }

    public void testGetAllIndividuals() throws Exception {
        GetAllIndividuals query = new GetAllIndividuals(getKBIRI());
        SetOfIndividuals answer = reasoner.answer(query);
        Set<OWLIndividual> individuals = CollectionFactory.createSet();
        individuals.addAll(getOntology().getIndividualsInSignature());
        individuals.addAll(getOntology().getReferencedAnonymousIndividuals());
        assertTrue(answer.containsAll(individuals));
        assertTrue(answer.size() == individuals.size());
    }

}
