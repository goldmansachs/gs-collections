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

package com.gs.collections.impl.lazy.primitive;

import java.util.Arrays;
import java.util.Iterator;

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.iterator.IntIterator;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectIntIterable<T>
        implements IntIterable
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

    public int size()
    {
        return this.iterable.size();
    }

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

    public <V> LazyIterable<V> collect(IntToObjectFunction<? extends V> function)
    {
        return new CollectIntToObjectIterable<T, V>(this, function);
    }

    public long sum()
    {
        return this.iterable.injectInto(0, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return longValue + (long) CollectIntIterable.this.function.intValueOf(each);
            }
        });
    }

    public int max()
    {
        return this.iterable.injectInto(Integer.MIN_VALUE, new IntObjectToIntFunction<T>()
        {
            public int intValueOf(int intParameter, T each)
            {
                return Math.max(intParameter, CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    public int min()
    {
        return this.iterable.injectInto(Integer.MAX_VALUE, new IntObjectToIntFunction<T>()
        {
            public int intValueOf(int intParameter, T each)
            {
                return Math.min(intParameter, CollectIntIterable.this.function.intValueOf(each));
            }
        });
    }

    public double average()
    {
        return (double) this.sum() / (double) this.size();
    }

    public double median()
    {
        int[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            int first = sortedArray[i];
            int second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

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

    public int[] toSortedArray()
    {
        int[] array = this.toArray();
        Arrays.sort(array);
        return array;
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
