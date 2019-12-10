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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.ReleaseKB;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;
import org.semanticweb.owlapi.owllink.builtin.response.KB;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public abstract class AbstractOWLlinkAxiomsTestCase extends AbstractOWLlinkTestCase {

    private Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
    OWLOntology ontology;

    protected void setUp() throws Exception {
        super.setUp();
        this.ontology = createOntology();
        addAxiomsToRootOntology();
    }

    protected void tearDown() throws Exception {
        removeAxiomsFromRootOntology();
        ReleaseKB releaseKB = new ReleaseKB(getKBIRI());
        this.reasoner.answer(releaseKB);
        super.tearDown();
    }

    public final OWLOntology getOntology() {
        return this.ontology;
    }

    final protected OWLOntology createOntology() {
        try {
            OWLOntology ont = getOWLOntology("Ont");
            axioms.clear();
            axioms.addAll(createAxioms());
            getManager().addAxioms(ont, axioms);

            CreateKB createKB = new CreateKB();
            KB kb = reasoner.answer(createKB);
            this.reasonerIRI = kb.getKB();

            Tell tell = new Tell(getKBIRI(), axioms);
            super.reasoner.answer(tell);
            return ont;
        } catch (OWLOntologyChangeException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addAxiomsToRootOntology() {
        for (OWLAxiom axiom : createAxioms())
            addAxiom(getRootOntology(), axiom);
    }

    protected void removeAxiomsFromRootOntology() {
        for (OWLAxiom axiom : createAxioms())
            removeAxiom(getRootOntology(), axiom);
    }

    protected abstract Set<? extends OWLAxiom> createAxioms();

}
