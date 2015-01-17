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
import java.util.ListIterator;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.impl.block.factory.Procedures;
import net.jcip.annotations.Immutable;

/**
 * A ReverseIterable is an iterable that wraps another iterable and iterates in reverse order.
 */
@Immutable
public class ReverseIterable<T>
        extends AbstractLazyIterable<T>
{
    private final ListIterable<T> adapted;

    public ReverseIterable(ListIterable<T> newAdapted)
    {
        this.adapted = newAdapted;
    }

    public static <T> ReverseIterable<T> adapt(ListIterable<T> listIterable)
    {
        return new ReverseIterable<T>(listIterable);
    }

    public void each(Procedure<? super T> procedure)
    {
        this.adapted.reverseForEach(procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.adapted.reverseForEach(Procedures.fromObjectIntProcedure(objectIntProcedure));
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.adapted.reverseForEach(Procedures.bind(procedure, parameter));
    }

    @Override
    public int size()
    {
        return this.adapted.size();
    }

    @Override
    public T getFirst()
    {
        return this.adapted.getLast();
    }

    @Override
    public T getLast()
    {
        return this.adapted.getFirst();
    }

    @Override
    public boolean isEmpty()
    {
        return this.adapted.isEmpty();
    }

    public Iterator<T> iterator()
    {
        ListIterator<T> listIterator = this.adapted.listIterator(this.adapted.size());
        return new ReverseIterator<T>(listIterator);
    }

    private static final class ReverseIterator<T> implements Iterator<T>
    {
        private final ListIterator<T> listIterator;

        private ReverseIterator(ListIterator<T> listIterator)
        {
            this.listIterator = listIterator;
        }

        public boolean hasNext()
        {
            return this.listIterator.hasPrevious();
        }

        public T next()
        {
            return this.listIterator.previous();
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
        }
    }
}
