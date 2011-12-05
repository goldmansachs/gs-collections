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

import com.gs.collections.impl.block.predicate.DropIterablePredicate;
import net.jcip.annotations.Immutable;

/**
 * Iterates over the elements of the iterator skipping the first count elements or the full iterator if the count is
 * non-positive.
 */
@Immutable
public final class DropIterator<T> implements Iterator<T>
{
    private final Iterator<T> delegateIterator;

    public DropIterator(Iterable<T> iterable, int count)
    {
        this(iterable.iterator(), count);
    }

    public DropIterator(Iterator<T> iterator, int count)
    {
        this.delegateIterator = new SelectIterator<T>(iterator, new DropIterablePredicate<T>(count));
    }

    public boolean hasNext()
    {
        return this.delegateIterator.hasNext();
    }

    public T next()
    {
        return this.delegateIterator.next();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a drop iterator");
    }
}
