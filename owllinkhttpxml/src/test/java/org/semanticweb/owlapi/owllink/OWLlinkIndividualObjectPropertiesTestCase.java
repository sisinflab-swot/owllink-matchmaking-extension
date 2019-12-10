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
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.owllink.builtin.requests.*;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfObjectPropertySynsets;
import org.semanticweb.owlapi.reasoner.Node;
import static org.semanticweb.owlapi.util.CollectionFactory.createSet;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 03.11.2009
 */
public class OWLlinkIndividualObjectPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = createSet();
        axioms.add(getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("p"), getOWLIndividual("i"), getOWLIndividual("j")));
        axioms.add(getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("p"), getOWLIndividual("i"), getOWLIndividual("k")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("p"), getOWLObjectProperty("q")));
        axioms.add(getDataFactory().getOWLEquivalentObjectPropertiesAxiom(getOWLObjectProperty("p"), getOWLObjectProperty("r")));

        return axioms;
    }


    public void testGetObjectPropertiesOfSource() throws Exception {
        GetObjectPropertiesOfSource query = new GetObjectPropertiesOfSource(getKBIRI(), getOWLIndividual("i"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 4);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("p")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("q")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("r")));
        assertTrue(response.getFlattened().contains(manager.getOWLDataFactory().getOWLTopObjectProperty()));

        /* Set<Node<OWLObjectProperty>> synsets = CollectionFactory.createSet();
        Synset<OWLObjectProperty> synset = new SynsetImpl<OWLObjectProperty>(getOWLObjectProperty("p"), getOWLObjectProperty("r"));
        synsets.add(synset);
        synset = new SynsetImpl<OWLObjectProperty>(getOWLObjectProperty("q"));
        synsets.add(synset);
        for (Synset<OWLObjectProperty> set : response) {
            synsets.remove(set);
        }
        assertTrue(synsets.isEmpty());*/
    }

    public void testGetObjectPropertiesOfTarget() throws Exception {
        GetObjectPropertiesOfTarget query = new GetObjectPropertiesOfTarget(getKBIRI(), getOWLIndividual("k"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 4);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("p")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("q")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("r")));
        assertTrue(response.getFlattened().contains(manager.getOWLDataFactory().getOWLTopObjectProperty()));
    }

    public void testGetObjectPropertiesBetween() throws Exception {
        GetObjectPropertiesBetween query = new GetObjectPropertiesBetween(getKBIRI(), getOWLIndividual("i"), getOWLIndividual("j"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 4);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("p")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("q")));
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("r")));
        assertTrue(response.getFlattened().contains(manager.getOWLDataFactory().getOWLTopObjectProperty()));
        for (Node<OWLObjectProperty> synset : response) {
            if (synset.contains(getOWLObjectProperty("r")))
                assertFalse(synset.isSingleton());
            else
                assertTrue(synset.isSingleton());
        }
    }

    public void testAreIndividualsRelated() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("p"), getOWLIndividual("i"), getOWLIndividual("j")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("r"), getOWLIndividual("i"), getOWLIndividual("j")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("p"), getOWLIndividual("i"), getOWLIndividual("k")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("q"), getOWLIndividual("i"), getOWLIndividual("k")));
        assertTrue(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("r"), getOWLIndividual("j"), getOWLIndividual("j")));
        assertFalse(reasoner.answer(query).getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLObjectPropertyAssertionAxiom(getOWLObjectProperty("p"), getOWLIndividual("j"), getOWLIndividual("i")));
        assertFalse(reasoner.answer(query).getResult());
    }

    public void testGetObjectPropertyTargets() throws Exception {
        GetObjectPropertyTargets query = new GetObjectPropertyTargets(getKBIRI(), getOWLIndividual("i"), getOWLObjectProperty("p"));
        SetOfIndividualSynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 2);
        assertTrue(response.getFlattened().contains(getOWLIndividual("k")));
    }

    public void testGetObjectPropertySources() throws Exception {
        GetObjectPropertySources query = new GetObjectPropertySources(getKBIRI(), getOWLIndividual("j"), getOWLObjectProperty("p"));
        SetOfIndividualSynsets response = super.reasoner.answer(query);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLIndividual("i")));
    }

    public void testGetFlattenedObjectPropertyTargets() throws Exception {
        GetFlattenedObjectPropertyTargets query = new GetFlattenedObjectPropertyTargets(getKBIRI(), getOWLIndividual("i"), getOWLObjectProperty("p"));
        SetOfIndividuals response = super.reasoner.answer(query);
        assertTrue(response.size() == 2);
        assertTrue(response.contains(getOWLIndividual("k")));
    }

    public void testGetFlattenedObjectPropertySources() throws Exception {
        GetFlattenedObjectPropertySources query = new GetFlattenedObjectPropertySources(getKBIRI(), getOWLIndividual("j"), getOWLObjectProperty("p"));
        SetOfIndividuals response = super.reasoner.answer(query);
        assertTrue(response.size() == 1);
        assertTrue(response.contains(getOWLIndividual("i")));
    }
}
