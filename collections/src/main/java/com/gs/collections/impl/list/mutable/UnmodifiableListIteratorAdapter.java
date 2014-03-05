/*
 * Copyright 2014 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.list.mutable;

import java.util.ListIterator;

public class UnmodifiableListIteratorAdapter<T>
        implements ListIterator<T>
{
    private final ListIterator<? extends T> iterator;

    public UnmodifiableListIteratorAdapter(ListIterator<T> iterator)
    {
        this.iterator = iterator;
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public T next()
    {
        return this.iterator.next();
    }

    public boolean hasPrevious()
    {
        return this.iterator.hasPrevious();
    }

    public T previous()
    {
        return this.iterator.previous();
    }

    public int nextIndex()
    {
        return this.iterator.nextIndex();
    }

    public int previousIndex()
    {
        return this.iterator.previousIndex();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public void set(T o)
    {
        throw new UnsupportedOperationException("Cannot call set() on " + this.getClass().getSimpleName());
    }

    public void add(T o)
    {
        throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
    }
}
