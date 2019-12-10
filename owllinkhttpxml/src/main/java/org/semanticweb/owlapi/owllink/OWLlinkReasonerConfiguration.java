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

import org.semanticweb.owlapi.reasoner.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Olaf Noppens
 * Date: 19.02.2010
 */
public class OWLlinkReasonerConfiguration extends SimpleConfiguration {
    private static URL defaultURL;
    URL reasonerURL;

    static {
        try {
            defaultURL = new URL("http://localhost:8080");
        } catch (MalformedURLException e) {
            defaultURL = null;
        }
    }


    public OWLlinkReasonerConfiguration(ReasonerProgressMonitor progressMonitor, URL reasonerURL, IndividualNodeSetPolicy individualNodeSetPolicy) {
        super(progressMonitor, FreshEntityPolicy.DISALLOW, Long.MAX_VALUE, individualNodeSetPolicy);
        this.reasonerURL = reasonerURL;
    }

    public OWLlinkReasonerConfiguration(ReasonerProgressMonitor progressMonitor, IndividualNodeSetPolicy policy) {
        this(progressMonitor, defaultURL, policy);
    }

    public OWLlinkReasonerConfiguration(URL reasonerURL, IndividualNodeSetPolicy policy) {
        this(new NullReasonerProgressMonitor(), reasonerURL, policy);
    }

    public OWLlinkReasonerConfiguration(IndividualNodeSetPolicy policy) {
        this(defaultURL, policy);
    }

     public OWLlinkReasonerConfiguration(URL reasonerURL) {
        this(reasonerURL, IndividualNodeSetPolicy.BY_SAME_AS);
    }

    public OWLlinkReasonerConfiguration() {
        this(IndividualNodeSetPolicy.BY_SAME_AS);
    }


    public URL getReasonerURL() {
        return this.reasonerURL;
    }
}
