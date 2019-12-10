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


package it.poliba.sisinflab.swottools.owllinkmatchmaking;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividual;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.ExpressionOrIndividualImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import org.junit.Before;
import org.junit.runners.Parameterized;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.ReleaseKB;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;
import org.semanticweb.owlapi.owllink.builtin.response.KB;
import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualsImpl;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public abstract class AbstractOWLlinkMatchmakingTestCase extends AbstractOWLlinkTestCase {





    OWLOntology ontology;
    @Parameterized.Parameter(0)
    public String ontologyName;

    @Parameterized.Parameters
    public static Collection ontologies() {
        return Arrays.asList(new String[] {
                 //"agriculture.owl", please insert ontologies here

        });
    }
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.ontology = createOntology();

    }

    final protected OWLOntology createOntology() {
        try {
            this.manager = OWLManager.createOWLOntologyManager();
            this.dataFactory = manager.getOWLDataFactory();

            File file = new File(""); //please set base path ontologies

            URI fileUri = file.toURI();

            IRI iri = IRI.create(fileUri +  ontologyName);
            OWLOntology ont = manager.loadOntologyFromOntologyDocument(iri);



            CreateKB createKB = new CreateKB();
            KB kb = reasoner.answer(createKB);
            this.reasonerIRI = kb.getKB();

            Tell tell = new Tell(getKBIRI(), ont.getAxioms());
            super.reasoner.answer(tell);
            return ont;
        } catch (OWLOntologyChangeException | OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }
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

    public SetOfIndividuals getOWLNamedIndividuals(int size){
        Collection<OWLIndividual> setOfOwlNamedIndividuals = new HashSet<>();
        Object[] individuals = ontology.getIndividualsInSignature().toArray();
        for (int i = 1; i <= size; i++) {
            setOfOwlNamedIndividuals.add(dataFactory.getOWLNamedIndividual(((OWLNamedIndividual)individuals[i]).getIRI()));
        }

        return new SetOfIndividualsImpl(setOfOwlNamedIndividuals);
    }


    public void testKBRequestWithTwoExpressionOrIndividuals(boolean isExpression, KBRequestWithTwoExpressionOrIndividuals.RequestType requestType) throws Exception {
        ExpressionOrIndividual resource = getExpressionOrIndividual(true, isExpression);
        ExpressionOrIndividual request = getExpressionOrIndividual(false, isExpression);

        KBRequestWithTwoExpressionsOrIndividualsImpl query = new KBRequestWithTwoExpressionsOrIndividualsImpl(getKBIRI(), resource, request, requestType);
        KBResponse result = super.reasoner.answer(query);

    }




    public ExpressionOrIndividual getExpressionOrIndividual(boolean isResource, boolean isExpression){
        ExpressionOrIndividual result;
        OWLClassExpression owlClassExpression;
        Object[] individuals = ontology.getIndividualsInSignature().toArray();
        Set<OWLClassExpression> set;
        int index = isResource?1:0;
        if(isExpression) {
            set = ((OWLNamedIndividual) individuals[index]).getTypes(ontology);
            if(set.size()>1)
                owlClassExpression = getDataFactory().getOWLObjectIntersectionOf(set);
            else owlClassExpression = (OWLClassExpression) set.toArray()[0];
            result = new ExpressionOrIndividualImpl(owlClassExpression);
        }
        else {

            OWLNamedIndividual ind = getDataFactory().getOWLNamedIndividual(((OWLNamedIndividual) individuals[index]).getIRI());
            result = new ExpressionOrIndividualImpl(ind);
        }
        return result;


    }


    protected void tearDown() throws Exception {

        ReleaseKB releaseKB = new ReleaseKB(getKBIRI());
        this.reasoner.answer(releaseKB);
        super.tearDown();
    }

}
