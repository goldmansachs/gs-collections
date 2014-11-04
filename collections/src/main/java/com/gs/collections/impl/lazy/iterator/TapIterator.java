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

package com.gs.collections.impl.lazy.iterator;

import java.util.Iterator;

import com.gs.collections.api.block.procedure.Procedure;

public class TapIterator<T> implements Iterator<T>
{
    private final Iterator<T> iterator;
    private final Procedure<? super T> procedure;

    public TapIterator(Iterable<T> iterable, Procedure<? super T> procedure)
    {
        this(iterable.iterator(), procedure);
    }

    public TapIterator(Iterator<T> iterator, Procedure<? super T> procedure)
    {
        this.iterator = iterator;
        this.procedure = procedure;
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public T next()
    {
        T next = this.iterator.next();
        this.procedure.value(next);
        return next;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a tap iterator");
    }
}
