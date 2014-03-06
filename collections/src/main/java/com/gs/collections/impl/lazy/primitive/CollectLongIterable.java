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

package com.gs.collections.impl.lazy.primitive;

import java.util.Arrays;
import java.util.Iterator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.LongPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.LongProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.LongIterator;
import com.gs.collections.api.list.primitive.MutableLongList;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectLongIterable is an iterable that transforms a source iterable using an LongFunction as it iterates.
 */
@Immutable
public class CollectLongIterable<T>
        extends AbstractLazyLongIterable
{
    private final LazyIterable<T> iterable;
    private final LongFunction<? super T> function;
    private final LongFunctionToProcedure intFunctionToProcedure = new LongFunctionToProcedure();

    public CollectLongIterable(LazyIterable<T> adapted, LongFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public LongIterator longIterator()
    {
        return new LongIterator()
        {
            private final Iterator<T> iterator = CollectLongIterable.this.iterable.iterator();

            public long next()
            {
                return CollectLongIterable.this.function.longValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(LongProcedure procedure)
    {
        this.iterable.forEachWith(this.intFunctionToProcedure, procedure);
    }

    @Override
    public int size()
    {
        return this.iterable.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    @Override
    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }

    @Override
    public int count(final LongPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final LongPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final LongPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final LongPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    @Override
    public long[] toArray()
    {
        final long[] array = new long[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectLongIterable.this.function.longValueOf(each);
            }
        });
        return array;
    }

    @Override
    public long[] toSortedArray()
    {
        long[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableLongList toList()
    {
        return LongArrayList.newList(this);
    }

    @Override
    public MutableLongList toSortedList()
    {
        return LongArrayList.newList(this).sortThis();
    }

    @Override
    public MutableLongSet toSet()
    {
        return LongHashSet.newSet(this);
    }

    @Override
    public MutableLongBag toBag()
    {
        return LongHashBag.newBag(this);
    }

    @Override
    public boolean containsAll(long... source)
    {
        for (long value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(LongIterable source)
    {
        for (LongIterator iterator = source.longIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class LongFunctionToProcedure implements Procedure2<T, LongProcedure>
    {
        private static final long serialVersionUID = 3445125933721627554L;

        public void value(T each, LongProcedure parm)
        {
            parm.value(CollectLongIterable.this.function.longValueOf(each));
        }
    }
}
