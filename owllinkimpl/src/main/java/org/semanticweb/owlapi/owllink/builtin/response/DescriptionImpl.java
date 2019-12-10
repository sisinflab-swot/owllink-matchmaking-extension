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

import org.semanticweb.owlapi.model.IRI;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 24.10.2009
 */
public class DescriptionImpl extends ConfirmationImpl implements Description {
    private final String name;
    private final String message;
    private final Set<IRI> supportedExtensions;
    private final Set<Configuration> configurations;
    private final Set<PublicKB> publicKBs;
    private ReasonerVersion rVersion;
    private ProtocolVersion pVersion;


    public DescriptionImpl(String warning, String name, String message, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<Configuration> configurations, Set<PublicKB> publicKBs) {
        super(warning);
        this.name = name;
        this.message = message;
        this.supportedExtensions = (supportedExtensions == null ? Collections.<IRI>emptySet() : Collections.unmodifiableSet(supportedExtensions));
        this.configurations = (configurations == null ? Collections.<Configuration>emptySet() : Collections.unmodifiableSet(configurations));
        this.publicKBs = (publicKBs == null ? Collections.<PublicKB>emptySet() : Collections.unmodifiableSet(publicKBs));
        this.pVersion = pVersion;
        this.rVersion = rVersion;
    }

    public DescriptionImpl(String name, String message, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<Configuration> configurations, Set<PublicKB> publicKBs) {
        this.name = name;
        this.message = message;
        this.supportedExtensions = (supportedExtensions == null ? Collections.<IRI>emptySet() : Collections.unmodifiableSet(supportedExtensions));
        this.configurations = (configurations == null ? Collections.<Configuration>emptySet() : Collections.unmodifiableSet(configurations));
        this.publicKBs = (publicKBs == null ? Collections.<PublicKB>emptySet() : Collections.unmodifiableSet(publicKBs));
        this.pVersion = pVersion;
        this.rVersion = rVersion;
    }

    public DescriptionImpl(String name, Set<Configuration> configurations, ReasonerVersion rVersion, ProtocolVersion pVersion, Set<IRI> supportedExtensions, Set<PublicKB> publicKBs) {
        this(null, name, null, rVersion, pVersion, supportedExtensions, configurations, publicKBs);
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean hasMessage() {
        return getMessage() != null;
    }

    public Set<PublicKB> getPublicKBs() {
        return this.publicKBs;
    }

    public Set<Configuration> getDefaults() {
        return this.configurations;
    }

    public Set<IRI> getSupportedExtensions() {
        return this.supportedExtensions;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.pVersion;
    }

    public ReasonerVersion getReasonerVersion() {
        return this.rVersion;
    }

    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }
}
