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

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNodeSet;

import java.util.Set;

/**
 * Author: Olaf Noppens
 * Date: 09.12.2009
 */
public class ClassSynsetsImpl extends OWLClassNodeSet implements ClassSynsets {
    private String warning;

    public ClassSynsetsImpl(Set<Node<OWLClass>> synonymsets, String warning) {
        super(synonymsets);
        this.warning = warning;
    }

    public ClassSynsetsImpl(Set<Node<OWLClass>> synonymsets) {
        this(synonymsets, null);
    }

    public boolean hasWarning() {
        return this.warning != null;
    }

    public String getWarning() {
        return warning;
    }


    public <O> O accept(ResponseVisitor<O> visitor) {
        return visitor.visit(this);
    }


    /* public static <E extends OWLLogicalEntity>Set<Node<E>> convertToNode(final Set<EntitySynset<E>> set) {
        Set<Node<E>> set1 = new Set<Node<E>>() {
            public int size() {
                return set.size();
            }

            public boolean isEmpty() {
                return size() == 0;
            }

            public boolean contains(Object o) {
                return set.contains(o);
            }

            public Iterator<Node<E>> iterator() {
                final Iterator<EntitySynset<E>> iter = set.iterator();
                return new Iterator<Node<E>>() {
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Node<E> next() {
                        return iter.next();
                    }

                    public void remove() {
                    }
                };
            };

            public Object[] toArray() {
                return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            public <T> T[] toArray(T[] a) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean add(Node<E> o) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean remove(Object o) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean containsAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean addAll(Collection<? extends Node<E>> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean retainAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean removeAll(Collection<?> c) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void clear() {
            }
        };


        Set<Node<E>> classes = new HashSet<Node<E>>();
        for (EntitySynset<E> clazz : set) {
            classes.add(clazz);
        }
        return classes;
    }*/
}
