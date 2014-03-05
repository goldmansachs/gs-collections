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

import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.tuple.Tuples;

public final class ZipWithIndexIterator<T>
        implements Iterator<Pair<T, Integer>>
{
    private final Iterator<T> iterator;
    private int index = 0;

    public ZipWithIndexIterator(Iterable<T> iterable)
    {
        this.iterator = iterable.iterator();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public Pair<T, Integer> next()
    {
        try
        {
            return Tuples.pair(this.iterator.next(), this.index);
        }
        finally
        {
            this.index += 1;
        }
    }
}
