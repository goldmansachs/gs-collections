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

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public final class ImmutableListIterator<T> extends ImmutableIterator<T> implements ListIterator<T>
{
    public ImmutableListIterator(List<T> list, int index)
    {
        super(list);
        this.currentIndex = index;
    }

    public boolean hasPrevious()
    {
        return this.currentIndex != 0;
    }

    public T previous()
    {
        try
        {
            int i = this.currentIndex - 1;
            T previous = this.list.get(i);
            this.currentIndex = i;
            return previous;
        }
        catch (IndexOutOfBoundsException ignored)
        {
            throw new NoSuchElementException();
        }
    }

    public int nextIndex()
    {
        return this.currentIndex;
    }

    public int previousIndex()
    {
        return this.currentIndex - 1;
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
