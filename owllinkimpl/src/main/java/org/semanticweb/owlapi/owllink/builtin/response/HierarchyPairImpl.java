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

import org.semanticweb.owlapi.model.OWLLogicalEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.Node;

/**
 * Author: Olaf Noppens
 * Date: 02.11.2009
 */
public class HierarchyPairImpl<O extends OWLObject> implements HierarchyPair<O> {
    private final Node<O> superSynset;
    private final SubEntitySynsets<O> subs;

    public HierarchyPairImpl(Node<O> superSynset, SubEntitySynsets<O> subs) {
        this.superSynset = superSynset;
        this.subs = subs;
    }

    public Node<O> getSuper() {
        return this.superSynset;
    }

    public SubEntitySynsets<O> getSubs() {
        return this.subs;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HierarchyPairImpl)) return false;

        HierarchyPairImpl that = (HierarchyPairImpl) o;

        if (!subs.equals(that.subs)) return false;
        if (!superSynset.equals(that.superSynset)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = superSynset.hashCode();
        result = 31 * result + subs.hashCode();
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HierarchyPair[ ");
        sb.append(superSynset.toString());
        sb.append(", ");
        sb.append(subs.toString());
        sb.append("]");
        return sb.toString();
    }

}
