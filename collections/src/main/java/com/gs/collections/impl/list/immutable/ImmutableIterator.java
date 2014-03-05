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

package com.gs.collections.impl.list.immutable;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ImmutableIterator<T> implements Iterator<T>
{
    /**
     * Index of element to be returned by subsequent call to next.
     */
    protected int currentIndex;

    protected final List<T> list;

    public ImmutableIterator(List<T> list)
    {
        this.list = list;
    }

    public boolean hasNext()
    {
        return this.currentIndex != this.list.size();
    }

    public T next()
    {
        try
        {
            T result = this.list.get(this.currentIndex);
            this.currentIndex++;
            return result;
        }
        catch (IndexOutOfBoundsException ignored)
        {
            throw new NoSuchElementException();
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }
}
