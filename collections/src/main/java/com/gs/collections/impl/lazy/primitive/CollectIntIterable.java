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

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.IntIterator;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectIntIterable<T> extends AbstractLazyIntIterable
{
    private final LazyIterable<T> iterable;
    private final IntFunction<? super T> function;
    private final IntFunctionToProcedure intFunctionToProcedure = new IntFunctionToProcedure();

    public CollectIntIterable(LazyIterable<T> adapted, IntFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public IntIterator intIterator()
    {
        return new IntIterator()
        {
            private final Iterator<T> iterator = CollectIntIterable.this.iterable.iterator();

            public int next()
            {
                return CollectIntIterable.this.function.intValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(IntProcedure procedure)
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
    public int count(final IntPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final IntPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final IntPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final IntPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    @Override
    public int[] toArray()
    {
        final int[] array = new int[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectIntIterable.this.function.intValueOf(each);
            }
        });
        return array;
    }

    @Override
    public int[] toSortedArray()
    {
        int[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableIntList toList()
    {
        return IntArrayList.newList(this);
    }

    @Override
    public MutableIntList toSortedList()
    {
        return IntArrayList.newList(this).sortThis();
    }

    @Override
    public MutableIntSet toSet()
    {
        return IntHashSet.newSet(this);
    }

    @Override
    public MutableIntBag toBag()
    {
        return IntHashBag.newBag(this);
    }

    @Override
    public boolean containsAll(int... source)
    {
        for (int value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(IntIterable source)
    {
        for (IntIterator iterator = source.intIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class IntFunctionToProcedure implements Procedure2<T, IntProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, IntProcedure parm)
        {
            parm.value(CollectIntIterable.this.function.intValueOf(each));
        }
    }
}
