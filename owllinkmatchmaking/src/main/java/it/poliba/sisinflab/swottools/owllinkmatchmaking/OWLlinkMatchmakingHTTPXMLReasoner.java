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
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionOrIndividuals;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.request.KBRequestWithTwoExpressionsOrIndividualsImpl;
import it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response.ClassExpressionWithNorm;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.owllink.OWLlinkHTTPXMLReasoner;
import org.semanticweb.owlapi.owllink.OWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.BufferingMode;

public class OWLlinkMatchmakingHTTPXMLReasoner extends OWLlinkHTTPXMLReasoner {
    public OWLlinkMatchmakingHTTPXMLReasoner(OWLOntology rootOntology, OWLlinkReasonerConfiguration configuration, BufferingMode bufferingMode) {
        super(rootOntology, configuration, bufferingMode);
        session = new HTTPSessionImplMatchmaking(rootOntology.getOWLOntologyManager(), configuration.getReasonerURL(), prov);
    }

    public ClassExpressionWithNorm getAbduction(ExpressionOrIndividual request, ExpressionOrIndividual resource) {
        KBRequestWithTwoExpressionsOrIndividualsImpl query = new KBRequestWithTwoExpressionsOrIndividualsImpl(defaultKnowledgeBase, request, resource, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_ABDUCTION);
        return performRequest(query);
    }

    public ClassExpressionWithNorm getDifference(ExpressionOrIndividual request, ExpressionOrIndividual resource) {
        KBRequestWithTwoExpressionsOrIndividualsImpl query = new KBRequestWithTwoExpressionsOrIndividualsImpl(defaultKnowledgeBase, request, resource, KBRequestWithTwoExpressionOrIndividuals.RequestType.GET_DIFFERENCE);
        return performRequest(query);
    }


}
