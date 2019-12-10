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

import java.util.*;

/**
 * Author: Olaf Noppens
 * Date: 24.11.2009
 */
public abstract class AbstractSetOfImpl<E> implements Set<E> {
    Set<E> delegateSet;
    E singleton;

    public AbstractSetOfImpl(E e) {
        this.singleton = e;
    }

    public AbstractSetOfImpl(Collection<E> elements) {
        this.delegateSet = (createSet(elements.size()));
        this.delegateSet.addAll(elements);
        this.delegateSet = Collections.unmodifiableSet(this.delegateSet);
    }

    protected Set<E> createSet(int size) {
        return new HashSet<E>(size);
    }

    public boolean isSingleton() {
        return singleton != null;
    }

    public E getSingletonElement() {
        if (!isSingleton()) {
            throw new RuntimeException("Not a singleton set");
        }
        return singleton;
    }

    public int size() {
        return isSingleton() ? 1 : delegateSet.size();
    }

    public boolean isEmpty() {
        return isSingleton() ? false : delegateSet.isEmpty();
    }

    public boolean contains(Object o) {
        if (isSingleton())
            return singleton == o;
        return delegateSet.contains(o);
    }

    public boolean containsAll(Collection<?> objects) {
        if (isSingleton()) {
            return objects.size() == 1 && this.singleton == objects.iterator().next();
        }
        return this.delegateSet.containsAll(objects);
    }

    public boolean addAll(Collection<? extends E> es) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> objects) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> objects) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Iterator<E> iterator() {
        if (isSingleton()) {
            return Collections.singleton(singleton).iterator();
        }
        return Collections.unmodifiableCollection(this.delegateSet).iterator();
    }

    public Object[] toArray() {
        if (isSingleton()) {
            Object[] o = new Object[1];
            o[0] = this.singleton;
        }
        return this.delegateSet.toArray();
    }

    public <T> T[] toArray(T[] a) {
        if (isSingleton()) {
            T[] result;
            if (a.length == 1) {
                result = a;
            } else {
                result = a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), 1);
            }
            result[0] = (T) this.singleton;
            return result;
        }
        return this.delegateSet.toArray(a);
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
}

