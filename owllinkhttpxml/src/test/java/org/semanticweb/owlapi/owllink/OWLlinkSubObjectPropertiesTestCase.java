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
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.response.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.impl.OWLObjectPropertyNode;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class OWLlinkSubObjectPropertiesTestCase extends AbstractOWLlinkAxiomsTestCase {

    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = CollectionFactory.createSet();

        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("B"), getOWLObjectProperty("C")));
        axioms.add(getDataFactory().getOWLSubObjectPropertyOfAxiom(getOWLObjectProperty("D"), getOWLObjectProperty("C")));

        return axioms;
    }

    public void testSubsumedBy() throws Exception {
        IsEntailed query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("B")));
        BooleanResponse response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("C")));
        response = super.reasoner.answer(query);
        assertTrue(response.getResult());

        query = new IsEntailed(getKBIRI(), getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("D"), getOWLObjectProperty("B")));
        response = super.reasoner.answer(query);
        assertFalse(response.getResult());
    }

    public void testSubsumedByViaOWLReasoner() throws Exception {
        OWLAxiom axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("B"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("A"), getOWLObjectProperty("C"));
        assertTrue(super.reasoner.isEntailed(axiom));

        axiom = getDataFactory().getOWLSubObjectPropertyOfAxiom(
                getOWLObjectProperty("D"), getOWLObjectProperty("B"));
        assertFalse(super.reasoner.isEntailed(axiom));
    }


    public void testGetSubObjectProperties() throws Exception {
        //indirect case
        GetSubObjectProperties query = new GetSubObjectProperties(getKBIRI(), getOWLObjectProperty("B"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);

        assertTrue(response.getNodes().size() == 2);
        Node<OWLObjectProperty> synset = response.iterator().next();
        assertTrue(synset.getSize() == 1);
        assertTrue(synset.contains(getOWLObjectProperty("A")));

        Set<OWLObjectProperty> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLObjectProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomObjectProperty()));
    }

    public void testGetSubObjectPropertiesViaOWLReasoner() throws Exception {
        //indirect case
        NodeSet<OWLObjectPropertyExpression> response = super.reasoner.getSubObjectProperties(getOWLObjectProperty("B"), false);

        assertTrue(response.getNodes().size() == 2);
        Node<OWLObjectPropertyExpression> synset = response.iterator().next();
        assertTrue(synset.getSize() == 1);
        assertTrue(synset.contains(getOWLObjectProperty("A")));

        Set<OWLObjectPropertyExpression> flattenedClasses = response.getFlattened();
        assertTrue(flattenedClasses.size() == 2);
        assertTrue(flattenedClasses.contains(getOWLObjectProperty("A")));
        assertTrue(flattenedClasses.contains(manager.getOWLDataFactory().getOWLBottomObjectProperty()));
    }

    public void testGetDirectSubObjectProperties() throws Exception {
        //direct case
        GetSubObjectProperties query = new GetSubObjectProperties(getKBIRI(), getOWLObjectProperty("B"), true);
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("A")));
    }

    public void testGetDirectSubObjectPropertiesViaOWLReasoner() throws Exception {
        //direct case
        NodeSet<OWLObjectPropertyExpression> response = super.reasoner.getSubObjectProperties(getOWLObjectProperty("B"), true);
        assertTrue(response.getNodes().size() == 1);
        assertTrue(response.getFlattened().size() == 1);
        assertTrue(response.getFlattened().contains(getOWLObjectProperty("A")));
    }


    public void testGetSuperProperties() throws Exception {
        GetSuperObjectProperties query = new GetSuperObjectProperties(getKBIRI(), getOWLObjectProperty("A"));
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 3);
        Node<OWLObjectPropertyExpression> synset = new OWLObjectPropertyNode(getOWLObjectProperty("B"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(getOWLObjectProperty("C"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(manager.getOWLDataFactory().getOWLTopObjectProperty());
        assertTrue(response.getNodes().contains(synset));
    }

    public void testGetSuperPropertiesViaOWLReasoner() throws Exception {
        NodeSet<OWLObjectPropertyExpression> response = super.reasoner.getSuperObjectProperties(getOWLObjectProperty("A"), false);
        assertTrue(response.getNodes().size() == 3);
        Node<OWLObjectPropertyExpression> synset = new OWLObjectPropertyNode(getOWLObjectProperty("B"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(getOWLObjectProperty("C"));
        assertTrue(response.getNodes().contains(synset));
        synset = new OWLObjectPropertyNode(manager.getOWLDataFactory().getOWLTopObjectProperty());
        assertTrue(response.getNodes().contains(synset));
    }

    public void testGetSuperPropertiesDirect() throws Exception {
        GetSuperObjectProperties query = new GetSuperObjectProperties(getKBIRI(), getOWLObjectProperty("A"), true);
        SetOfObjectPropertySynsets response = super.reasoner.answer(query);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testGetSuperPropertiesDirectViaOWLReasoner() throws Exception {
        NodeSet<OWLObjectPropertyExpression> response = super.reasoner.getSuperObjectProperties(getOWLObjectProperty("A"), true);
        assertTrue(response.getNodes().size() == 1);
    }

    public void testSubPropertyHierarchy() throws Exception {
        GetSubObjectPropertyHierarchy query = new GetSubObjectPropertyHierarchy(getKBIRI());
        ObjectPropertyHierarchy response = super.reasoner.answer(query);
        Set<HierarchyPair<OWLObjectProperty>> pairs = response.getPairs();
        assertFalse(pairs.isEmpty());
        assertTrue(pairs.size() == 3);

        Set<HierarchyPair<OWLObjectProperty>> expectedSet = CollectionFactory.createSet();
        Node<OWLObjectProperty> synset = new OWLlinkOWLObjectPropertyNode(getDataFactory().getOWLTopObjectProperty());
        Set<Node<OWLObjectProperty>> set = CollectionFactory.createSet();
        set.add(new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("C")));
        SubEntitySynsets<OWLObjectProperty> setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        synset = new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("C"));
        set = CollectionFactory.createSet();
        set.add(new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("B")));
        set.add(new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("D")));

        setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        synset = new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("B"));
        set = CollectionFactory.createSet();
        set.add(new OWLlinkOWLObjectPropertyNode(getOWLObjectProperty("A")));
        setOfSynsets = new SubObjectPropertySynsets(set);
        expectedSet.add(new HierarchyPairImpl<OWLObjectProperty>(synset, setOfSynsets));

        for (HierarchyPair pair : pairs) {
            expectedSet.remove(pair);
        }
        assertTrue(expectedSet.isEmpty());
    }

}
