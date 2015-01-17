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
import java.util.NoSuchElementException;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.utility.Iterate;
import net.jcip.annotations.Immutable;

@Immutable
public final class CompositeIterable<E>
        extends AbstractLazyIterable<E>
{
    private final MutableList<Iterable<E>> iterables;

    private CompositeIterable(MutableList<Iterable<E>> newIterables)
    {
        this.iterables = newIterables;
    }

    public CompositeIterable()
    {
        this(FastList.<Iterable<E>>newList());
    }

    public static <T> CompositeIterable<T> with(Iterable<T>... iterables)
    {
        return new CompositeIterable<T>(FastList.newListWith(iterables));
    }

    public void each(final Procedure<? super E> procedure)
    {
        this.iterables.forEach(new Procedure<Iterable<E>>()
        {
            public void value(Iterable<E> iterable)
            {
                Iterate.forEach(iterable, procedure);
            }
        });
    }

    @Override
    public void forEachWithIndex(final ObjectIntProcedure<? super E> objectIntProcedure)
    {
        final Counter index = new Counter();
        this.iterables.forEach(new Procedure<Iterable<E>>()
        {
            public void value(Iterable<E> iterable)
            {
                Iterate.forEach(iterable, new Procedure<E>()
                {
                    public void value(E object)
                    {
                        objectIntProcedure.value(object, index.getCount());
                        index.increment();
                    }
                });
            }
        });
    }

    @Override
    public <P> void forEachWith(final Procedure2<? super E, ? super P> procedure, final P parameter)
    {
        this.iterables.forEach(new Procedure<Iterable<E>>()
        {
            public void value(Iterable<E> iterable)
            {
                Iterate.forEachWith(iterable, procedure, parameter);
            }
        });
    }

    public void add(Iterable<E> iterable)
    {
        this.iterables.add(iterable);
    }

    public Iterator<E> iterator()
    {
        return new CompositeIterator(this.iterables);
    }

    private final class CompositeIterator
            implements Iterator<E>
    {
        private final Iterator<Iterable<E>> iterablesIterator;
        private Iterator<E> innerIterator;

        private CompositeIterator(MutableList<Iterable<E>> iterables)
        {
            this.iterablesIterator = iterables.listIterator();
            this.innerIterator = EmptyIterator.getInstance();
        }

        public boolean hasNext()
        {
            while (true)
            {
                if (this.innerIterator.hasNext())
                {
                    return true;
                }
                if (!this.iterablesIterator.hasNext())
                {
                    return false;
                }
                this.innerIterator = this.iterablesIterator.next().iterator();
            }
        }

        public E next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            return this.innerIterator.next();
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from a composite iterator");
        }
    }
}
