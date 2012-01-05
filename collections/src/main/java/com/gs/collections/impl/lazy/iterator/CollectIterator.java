/*
 * Copyright 2011 Goldman Sachs.
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
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function;
import net.jcip.annotations.Immutable;

@Immutable
public final class CollectIterator<T, V>
        implements Iterator<V>
{
    private final Iterator<T> iterator;
    private final Function<? super T, ? extends V> function;

    public CollectIterator(Iterable<T> iterable, Function<? super T, ? extends V> function)
    {
        this(iterable.iterator(), function);
    }

    public CollectIterator(Iterator<T> newIterator, Function<? super T, ? extends V> function)
    {
        this.iterator = newIterator;
        this.function = function;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a collect iterator");
    }

    public boolean hasNext()
    {
        return this.iterator.hasNext();
    }

    public V next()
    {
        if (this.hasNext())
        {
            return this.function.valueOf(this.iterator.next());
        }
        throw new NoSuchElementException();
    }
}
