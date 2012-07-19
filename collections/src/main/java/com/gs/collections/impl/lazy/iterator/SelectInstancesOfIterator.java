/*
 * Copyright 2012 Goldman Sachs.
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

public final class SelectInstancesOfIterator<T>
        implements Iterator<T>
{
    private static final Object NULL = new Object();
    private final Iterator<?> iterator;
    private final Class<T> clazz;
    private Object next = NULL;

    public SelectInstancesOfIterator(Iterable<?> iterable, Class<T> clazz)
    {
        this(iterable.iterator(), clazz);
    }

    public SelectInstancesOfIterator(Iterator<?> iterator, Class<T> clazz)
    {
        this.iterator = iterator;
        this.clazz = clazz;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot remove from a selectInstances iterator");
    }

    public boolean hasNext()
    {
        if (this.next != NULL)
        {
            return true;
        }
        while (this.iterator.hasNext())
        {
            Object temp = this.iterator.next();
            if (this.clazz.isInstance(temp))
            {
                this.next = temp;
                return true;
            }
        }
        return false;
    }

    public T next()
    {
        if (this.next != NULL || this.hasNext())
        {
            Object temp = this.next;
            this.next = NULL;
            return (T) temp;
        }
        throw new NoSuchElementException();
    }
}
