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

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.reasoner.impl.OWLDataPropertyNode;

import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 11.12.2009
 */
public class DataPropertySynonymsImpl extends OWLDataPropertyNode implements DataPropertySynonyms {
    private String warning;

    public DataPropertySynonymsImpl(OWLDataProperty prop) {
        this(prop, null);
    }

    public DataPropertySynonymsImpl(OWLDataProperty prop, String warning) {
        super(prop);
        this.warning = warning;
    }

    public DataPropertySynonymsImpl(Set<OWLDataProperty> elements) {
        this(elements, null);
    }

    public DataPropertySynonymsImpl(Set<OWLDataProperty> elements, String warning) {
        super(elements);
        this.warning = warning;
        if (elements.size() < 1) throw new IllegalArgumentException("size of elements must be greater than zero");
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }

    public boolean hasWarning() {
        return this.warning != null;
    }

    public String getWarning() {
        return this.warning;
    }
}
