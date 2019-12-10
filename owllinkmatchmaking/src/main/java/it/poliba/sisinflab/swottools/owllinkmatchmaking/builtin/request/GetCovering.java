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


package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request;

import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.SetOfIndividualsWithClassExpressionAndNorm;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.builtin.requests.AbstractKBRequestWithOneObject;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;

public class GetCovering extends AbstractKBRequestWithOneObject<SetOfIndividualsWithClassExpressionAndNorm,
        SetOfIndividualsAndClassExpression> {


    public GetCovering(IRI kb, SetOfIndividualsAndClassExpression setOfIndividualsAndClassExpression) {
        super(kb, setOfIndividualsAndClassExpression);
    }


    public SetOfIndividuals getSetOfIndividuals() {
        return super.getObject().getOWLNamedIndividualSet();
    }

    public ExpressionOrIndividual getRequest() {
        return super.getObject().getExpressionOrIndividual();
    }

    @Override
    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}