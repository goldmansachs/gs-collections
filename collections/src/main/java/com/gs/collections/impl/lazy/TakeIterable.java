/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.lazy;

import java.util.Iterator;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.impl.lazy.iterator.TakeIterator;
import net.jcip.annotations.Immutable;

/**
 * Iterates over the first count elements of the adapted Iterable or the full size of the adapted
 * iterable if the count is greater than the length of the receiver.
 */
@Immutable
public class TakeIterable<T> extends AbstractLazyIterable<T>
{
    private final Iterable<T> adapted;
    private final int count;

    public TakeIterable(Iterable<T> newAdapted, int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        this.adapted = newAdapted;
        this.count = count;
    }

    // TODO: implement in terms of LazyIterate.whileDo() when it is added.

    public void each(Procedure<? super T> procedure)
    {
        int i = 0;
        Iterator<T> iterator = this.adapted.iterator();
        while (i < this.count && iterator.hasNext())
        {
            procedure.value(iterator.next());
            i++;
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> procedure)
    {
        int i = 0;
        Iterator<T> iterator = this.adapted.iterator();
        while (i < this.count && iterator.hasNext())
        {
            procedure.value(iterator.next(), i);
            i++;
        }
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        int i = 0;
        Iterator<T> iterator = this.adapted.iterator();
        while (i < this.count && iterator.hasNext())
        {
            procedure.value(iterator.next(), parameter);
            i++;
        }
    }

    @Override
    public Object[] toArray()
    {
        final Object[] result = new Object[this.count];
        this.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                result[index] = each;
            }
        });
        return result;
    }

    public Iterator<T> iterator()
    {
        return new TakeIterator<T>(this.adapted, this.count);
    }
}
