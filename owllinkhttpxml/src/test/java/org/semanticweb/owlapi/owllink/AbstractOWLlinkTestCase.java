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

import junit.framework.TestCase;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.BufferingMode;

import java.util.Set;

/**
 * @author Olaf Noppens
 */
public abstract class AbstractOWLlinkTestCase extends TestCase {

    protected OWLlinkReasoner reasoner;
    protected OWLDataFactory dataFactory;
    protected OWLOntologyManager manager;
    private IRI uriBase;
    protected IRI reasonerIRI;
    OWLOntology rootOntology;


    protected void setUp() throws Exception {
        this.manager = OWLManager.createOWLOntologyManager();
        rootOntology = this.manager.createOntology();
        reasoner = new OWLlinkHTTPXMLReasoner(rootOntology, new OWLlinkReasonerConfiguration(), BufferingMode.NON_BUFFERING);
        uriBase = IRI.create("http://www.semanticweb.org/owlapi/owllink/test");
        reasonerIRI = uriBase;
    }

    public IRI getKBIRI() {
        return this.reasonerIRI;
    }


    public OWLOntology getRootOntology() {
        return this.rootOntology;
    }

    public OWLOntology getOWLOntology(String name) {
        try {
            IRI iri = IRI.create(uriBase + "/" + name);
            if (manager.contains(iri)) {
                return manager.getOntology(iri);
            } else {
                return manager.createOntology(iri);
            }
        }
        catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
    }


    protected void tearDown() throws Exception {
        this.manager.removeOntology(rootOntology);
        super.tearDown();
    }

    protected OWLOntologyManager getManager() {
        return this.manager;
    }

    protected OWLDataFactory getDataFactory() {
        return getManager().getOWLDataFactory();
    }


    public OWLClass getOWLClass(String name) {
        return getDataFactory().getOWLClass(IRI.create(uriBase + "#" + name));
    }


    public OWLObjectProperty getOWLObjectProperty(String name) {
        return getDataFactory().getOWLObjectProperty(IRI.create(uriBase + "#" + name));
    }


    public OWLDataProperty getOWLDataProperty(String name) {
        return getDataFactory().getOWLDataProperty(IRI.create(uriBase + "#" + name));
    }


    public OWLNamedIndividual getOWLIndividual(String name) {
        return getDataFactory().getOWLNamedIndividual(IRI.create(uriBase + "#" + name));
    }

    public OWLDatatype getOWLDatatype(String name) {
        return getDataFactory().getOWLDatatype(IRI.create(uriBase + "#" + name));
    }

    public OWLAnnotationProperty getOWLAnnotationProperty(String name) {
        return getDataFactory().getOWLAnnotationProperty(IRI.create(uriBase + "#" + name));
    }

    public OWLLiteral getLiteral(int value) {
        return getDataFactory().getOWLLiteral(value);
    }


    public void addAxiom(OWLOntology ont, OWLAxiom ax) {
        try {
            manager.addAxiom(ont, ax);
        }
        catch (OWLOntologyChangeException e) {
            fail(e.getMessage() + " " + e.getStackTrace().toString());
        }
    }

    public void removeAxiom(OWLOntology ont, OWLAxiom ax) {
        try {
            manager.removeAxiom(ont, ax);
        }
        catch (OWLOntologyChangeException e) {
            fail(e.getMessage() + " " + e.getStackTrace().toString());
        }
    }

    protected abstract Set<? extends OWLAxiom> createAxioms();


}
