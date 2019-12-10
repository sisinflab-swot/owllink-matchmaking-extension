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

import org.semanticweb.owlapi.owllink.Response;

/**
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public interface ResponseVisitor<O> {

    O visit(Response response);

    O visit(KBResponse response);

    O visit(BooleanResponse response);

    O visit(Classes response);

    O visit(ClassHierarchy response);

    O visit(ClassSynsets response);

    O visit(DataPropertyHierarchy response);

    O visit(DataPropertySynsets response);

    O visit(DataPropertySynonyms response);

    O visit(Description response);

    O visit(IndividualSynonyms response);

    O visit(KB response);

    O visit(ObjectPropertyHierarchy response);

    O visit(ObjectPropertySynsets response);

    O visit(OK response);

    O visit(Prefixes response);

    O visit(SetOfAnnotationProperties response);

    O visit(SetOfClasses response);

    O visit(SetOfClassSynsets resposne);

    O visit(SetOfDataProperties response);

    O visit(SetOfDataPropertySynsets response);

    O visit(SetOfDatatypes response);

    O visit(SetOfIndividuals response);

    O visit(SetOfIndividualSynsets response);

    O visit(SetOfLiterals response);

    O visit(SetOfObjectProperties response);

    O visit(SetOfObjectPropertySynsets response);

    O visit(Settings response);

    O visit(StringResponse response);

}
