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
import com.gs.collections.api.LazyShortIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.ShortPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.primitive.ShortProcedure;
import com.gs.collections.api.iterator.ShortIterator;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectShortIterable<T>
        extends AbstractLazyShortIterable
{
    private final LazyIterable<T> iterable;
    private final ShortFunction<? super T> function;
    private final ShortFunctionToProcedure shortFunctionToProcedure = new ShortFunctionToProcedure();

    public CollectShortIterable(LazyIterable<T> adapted, ShortFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public ShortIterator shortIterator()
    {
        return new ShortIterator()
        {
            private final Iterator<T> iterator = CollectShortIterable.this.iterable.iterator();

            public short next()
            {
                return CollectShortIterable.this.function.shortValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(ShortProcedure procedure)
    {
        this.iterable.forEachWith(this.shortFunctionToProcedure, procedure);
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
    public int count(final ShortPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectShortIterable.this.function.shortValueOf(each));
            }
        });
    }

    @Override
    public boolean anySatisfy(final ShortPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectShortIterable.this.function.shortValueOf(each));
            }
        });
    }

    @Override
    public boolean allSatisfy(final ShortPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectShortIterable.this.function.shortValueOf(each));
            }
        });
    }

    @Override
    public boolean noneSatisfy(final ShortPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectShortIterable.this.function.shortValueOf(each));
            }
        });
    }

    @Override
    public short[] toArray()
    {
        final short[] array = new short[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectShortIterable.this.function.shortValueOf(each);
            }
        });
        return array;
    }

    @Override
    public short[] toSortedArray()
    {
        short[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    @Override
    public MutableShortList toList()
    {
        return ShortArrayList.newList(this);
    }

    @Override
    public MutableShortList toSortedList()
    {
        return ShortArrayList.newList(this).sortThis();
    }

    @Override
    public MutableShortSet toSet()
    {
        return ShortHashSet.newSet(this);
    }

    @Override
    public MutableShortBag toBag()
    {
        return ShortHashBag.newBag(this);
    }

    @Override
    public LazyShortIterable asLazy()
    {
        return this;
    }

    @Override
    public boolean containsAll(short... source)
    {
        for (short value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(ShortIterable source)
    {
        for (ShortIterator iterator = source.shortIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class ShortFunctionToProcedure implements Procedure2<T, ShortProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, ShortProcedure parm)
        {
            parm.value(CollectShortIterable.this.function.shortValueOf(each));
        }
    }
}
