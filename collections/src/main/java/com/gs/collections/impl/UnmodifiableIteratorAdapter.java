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

package com.gs.collections.impl;

import java.util.Iterator;

/**
 * An iterator that adapts another iterator and throws unsupported operation exceptions when calls
 * to the remove method are made.
 */
public class UnmodifiableIteratorAdapter<E>
        implements Iterator<E>
{
    private final Iterator<? extends E> iterator;

    public UnmodifiableIteratorAdapter(Iterator<? extends E> iterator)
    {
        this.iterator = iterator;
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public E next()
    {
        return this.iterator.next();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }
}
