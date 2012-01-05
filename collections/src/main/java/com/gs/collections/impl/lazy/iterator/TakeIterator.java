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

/**
 * Iterates over the first count elements of the iterator or the full size of the iterator
 * if the count is greater than the length of the receiver.
 */
public final class TakeIterator<T> implements Iterator<T>
{
    private final Iterator<T> iterator;
    private final int count;

    private int currentIndex;

    public TakeIterator(Iterable<T> iterable, int count)
    {
        this(iterable.iterator(), count);
    }

    public TakeIterator(Iterator<T> iterator, int count)
    {
        this.iterator = iterator;
        this.count = count;
    }

    public boolean hasNext()
    {
        return this.currentIndex < this.count && this.iterator.hasNext();
    }

    public T next()
    {
        if (this.hasNext())
        {
            this.currentIndex++;
            return this.iterator.next();
        }
        throw new NoSuchElementException();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a take iterator");
    }
}
