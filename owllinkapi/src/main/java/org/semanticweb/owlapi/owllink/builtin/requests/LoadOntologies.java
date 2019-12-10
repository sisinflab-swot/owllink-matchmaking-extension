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
import org.semanticweb.owlapi.owllink.builtin.response.OK;
import org.semanticweb.owlapi.util.CollectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a <a href="http://www.owllink.org/owllink-20091116/#Tell">LoadOntologies</a>
 * request in the OWLlink specification.
 *
 * Author: Olaf Noppens
 * Date: 23.10.2009
 */
public class LoadOntologies extends AbstractKBRequest<OK> {
    protected boolean considerImports;
    protected java.util.Set<IRI> ontologyIRIs;
    protected List<IRIMapping> irimapping;


    public LoadOntologies(IRI kb, java.util.Set<IRI> ontologyIRIs, List<IRIMapping> iriMapping, boolean considerImports) {
        super(kb);
        this.ontologyIRIs = Collections.unmodifiableSet(new HashSet<IRI>(ontologyIRIs));
        this.irimapping = (iriMapping == null ? Collections.<IRIMapping>emptyList() : Collections.unmodifiableList(iriMapping));
        this.considerImports = considerImports;
    }

    public LoadOntologies(IRI kb, java.util.Set<IRI> ontologyIRIs) {
        this(kb, ontologyIRIs, null, false);
    }

    public LoadOntologies(IRI kb, IRI... ontologyIRIs) {
        this(kb, CollectionFactory.createSet(ontologyIRIs), null, false);
    }

    public boolean isConsideringImports() {
        return this.considerImports;
    }

    public java.util.Set<IRI> getOntologyIRIs() {
        return this.ontologyIRIs;
    }

    public void accept(RequestVisitor visitor) {
        visitor.answer(this);
    }

    public List<IRIMapping> getIRIMapping() {
        final List<IRIMapping> mapping = new ArrayList<IRIMapping>();
        mapping.addAll(this.irimapping);
        return mapping;
    }
}
