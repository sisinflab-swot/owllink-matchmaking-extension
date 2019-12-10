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

import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkErrorResponseException;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseMessage;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * @author Olaf Noppens
 */
public interface OWLlinkReasoner extends OWLReasoner {

    /**
     * Answers the given request. If the OWLlink server answers with an Error response (or
     * any subtype thereof) an OWLlinkErrorResponseException (or an appropriate
     * subtype thereof) will be thrown.
     *
     * @param request Request to be performed
     * @return Response object of the given request
     * @throws OWLlinkErrorResponseException In case that the OWLlink server answers with
     *                                       an Error response an approproate OWLlinkErrorResponseException will be thrown.
     */
    <R extends Response> R answer(Request<R> request) throws OWLlinkErrorResponseException;

    /**
     * Answers the given list of {@link Request requests} in
     * exactly the given ordering.
     *
     * @param request Requests to be answered
     * @return ResponseMessage for the given requests.
     */
    ResponseMessage answer(Request... request);
}
