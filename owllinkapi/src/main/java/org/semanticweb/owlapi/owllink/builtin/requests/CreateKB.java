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

package org.semanticweb.owlapi.owllink.builtin.requests;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.builtin.response.KB;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#CreateKB">CreateKB</a>
 * request in the OWLlink specification.
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class CreateKB implements Request<KB> {
    final String name;
    final IRI kb;
    Map<String, String> prefixes;

    public CreateKB() {
        this(null, null, null);
    }

    public CreateKB(IRI kb, String name) {
        this.kb = kb;
        this.name = name;
    }

    public CreateKB(IRI kb, String name, Map<String, String> prefixes) {
        this.kb = kb;
        this.name = name;
        this.prefixes = prefixes == null ? Collections.<String, String>emptyMap() : new HashMap<String, String>(prefixes);
    }

    public CreateKB(IRI kb) {
        this(kb, null, null);
    }

    public CreateKB(IRI kb, Map<String, String> prefixes) {
        this(kb, null, prefixes);
    }

    public CreateKB(Map<String, String> prefixes) {
        this(null, null, prefixes);
    }

    public IRI getKB() {
        return this.kb;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasName() {
        return getName() != null;
    }

    public Map<String, String> getPrefixes() {
        return Collections.unmodifiableMap(this.prefixes);
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }
}
