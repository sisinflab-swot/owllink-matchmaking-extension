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

package org.semanticweb.owlapi.owllink.builtin.response;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.impl.DefaultNode;
import org.semanticweb.owlapi.reasoner.impl.NodeFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 18.02.2010
 */
public class IndividualSynsetImpl implements IndividualSynset {
    Set<OWLIndividual> individuals;

    public IndividualSynsetImpl(Set<OWLIndividual> individuals) {
        if (individuals.isEmpty())
            throw new IllegalArgumentException("IndividualSynsets must not be empty!");
        this.individuals = new HashSet<OWLIndividual>();
        this.individuals.addAll(individuals);
    }

    public IndividualSynsetImpl(Node<OWLNamedIndividual> node) {
        this(convert(node));
    }

    static Set<OWLIndividual> convert(Node<OWLNamedIndividual> node) {
        Set<OWLIndividual> indis = new HashSet<OWLIndividual>();
        indis.addAll(node.getEntities());
        return indis;
    }

    public Iterator<OWLIndividual> iterator() {
        return this.individuals.iterator();
    }

    public boolean isSingleton() {
        return this.individuals.size() == 1;
    }

    public OWLIndividual getSingletonElement() {
        return this.individuals.iterator().next();
    }

    public Set<OWLIndividual> getIndividuals() {
        return Collections.unmodifiableSet(this.individuals);
    }

    public boolean contains(OWLIndividual individual) {
        return this.individuals.contains(individual);
    }

    public boolean isNode() {
        for (OWLIndividual indi : this.individuals)
            if (indi.isAnonymous()) return false;
        return true;
    }

    public Node<OWLNamedIndividual> asNode() {
        if (!isNode())
            throw new OWLRuntimeException("Contains anonymous individuals. Conversion not possible. See isNode()");
        DefaultNode<OWLNamedIndividual> node = NodeFactory.getOWLNamedIndividualNode();
        for (OWLIndividual indi : this.individuals)
            node.add(indi.asOWLNamedIndividual());
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndividualSynsetImpl that = (IndividualSynsetImpl) o;

        if (individuals != null ? !individuals.equals(that.individuals) : that.individuals != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return individuals != null ? individuals.hashCode() : 0;
    }
}
