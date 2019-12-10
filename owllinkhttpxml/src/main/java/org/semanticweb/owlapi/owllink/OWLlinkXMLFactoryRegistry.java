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

import org.semanticweb.owlapi.owllink.parser.OWLlinkElementHandlerFactory;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkRequestRendererFactory;
import org.semanticweb.owlapi.owllink.retraction.OWLlinkXMLRetractionRequestRendererFactory;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Author: Olaf Noppens
 * Date: 28.04.2010
 */
public class OWLlinkXMLFactoryRegistry {
    private static OWLlinkXMLFactoryRegistry instance;

    List<OWLlinkRequestRendererFactory> rendererFactories;
    List<OWLlinkElementHandlerFactory> parserFactories;

    private OWLlinkXMLFactoryRegistry() {
        this.rendererFactories = new Vector<OWLlinkRequestRendererFactory>();
        this.parserFactories = new Vector<OWLlinkElementHandlerFactory>();

        register(new OWLlinkXMLRetractionRequestRendererFactory());

    }


    public synchronized static OWLlinkXMLFactoryRegistry getInstance() {
        if (instance == null)
            instance = new OWLlinkXMLFactoryRegistry();
        return instance;
    }


    public synchronized List<OWLlinkRequestRendererFactory> getRequestRendererFactories() {
        return Collections.unmodifiableList(rendererFactories);
    }

    public synchronized List<OWLlinkElementHandlerFactory> getParserFactories() {
        return Collections.unmodifiableList(parserFactories);
    }

    public synchronized void register(OWLlinkRequestRendererFactory factory) {
        if (!rendererFactories.contains(factory))
            rendererFactories.add(factory);
    }

    public synchronized void register(OWLlinkElementHandlerFactory factory) {
        if (!parserFactories.contains(factory))
            parserFactories.add(factory);
    }

    public synchronized void unregister(OWLlinkRequestRendererFactory factory) {
        rendererFactories.remove(factory);
    }

    public synchronized void unregister(OWLlinkElementHandlerFactory factory) {
        parserFactories.remove(factory);
    }

}
