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


import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.SetOfIndividualsWithClassExpressionAndNorm;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.GetCovering;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.SetOfIndividualsAndClassExpression;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.SetOfIndividualsAndClassExpressionImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.semanticweb.owlapi.model.*;

import java.util.Set;

@RunWith(Parameterized.class)
public class OWLlinkMatchmakingTestCase extends AbstractOWLlinkMatchmakingTestCase {

    @Override
    protected Set<? extends OWLAxiom> createAxioms() {
        return null;
    }

    @Test
    public void testIsAbductionIndividualCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(false, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_ABDUCTION);

    }

    @Test
    public void testIsAbductionExpressionCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(true, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_ABDUCTION);

    }

    @Test
    public void testIsBonusIndividualCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(false, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_BONUS);

    }

    @Test
    public void testIsBonusExpressionCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(true, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_BONUS);

    }

    @Test
    public void testIsDifferenceIndividualCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(false, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_DIFFERENCE);

    }

    @Test
    public void testIsDifferenceExpressionCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(true, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_DIFFERENCE);

    }

    @Test
    public void testIsContractionIndividualCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(false, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_CONTRACTION);

    }

    @Test
    public void testIsContractionExpressionCorrect() throws Exception {
        testKBRequestWithTwoExpressionOrIndividuals(true, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_CONTRACTION);

    }


    @Test
    public void testIsCoveringIndividualCorrect() throws Exception {
        SetOfIndividualsAndClassExpression data = new SetOfIndividualsAndClassExpressionImpl(getExpressionOrIndividual(false, false), getOWLNamedIndividuals(5));
        GetCovering query = new GetCovering(getKBIRI(), data);
        SetOfIndividualsWithClassExpressionAndNorm result = super.reasoner.answer(query);

    }

    @Test
    public void testIsCoveringExpressionCorrect() throws Exception {

        SetOfIndividualsAndClassExpression data = new SetOfIndividualsAndClassExpressionImpl(getExpressionOrIndividual(false, true), getOWLNamedIndividuals(5));
        GetCovering query = new GetCovering(getKBIRI(), data);
        SetOfIndividualsWithClassExpressionAndNorm result = super.reasoner.answer(query);

    }

}
