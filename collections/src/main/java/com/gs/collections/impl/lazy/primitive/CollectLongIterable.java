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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.LongToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.LongPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.LongProcedure;
import com.gs.collections.api.iterator.LongIterator;
import com.gs.collections.impl.block.factory.primitive.LongPredicates;
import net.jcip.annotations.Immutable;

/**
 * A CollectLongIterable is an iterable that transforms a source iterable using an LongFunction as it iterates.
 */
@Immutable
public class CollectLongIterable<T>
        implements LongIterable
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

    public int size()
    {
        return this.iterable.size();
    }

    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }

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

    public LongIterable select(LongPredicate predicate)
    {
        return new SelectLongIterable(this, predicate);
    }

    public long detectIfNone(LongPredicate predicate, long ifNone)
    {
        LongIterator iterator = this.longIterator();
        while (iterator.hasNext())
        {
            long next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public LongIterable reject(LongPredicate predicate)
    {
        return new SelectLongIterable(this, LongPredicates.not(predicate));
    }

    public <V> RichIterable<V> collect(LongToObjectFunction<? extends V> function)
    {
        return new CollectLongToObjectIterable<V>(this, function);
    }

    public long sum()
    {
        return this.iterable.injectInto(0, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return longValue + CollectLongIterable.this.function.longValueOf(each);
            }
        });
    }

    public long max()
    {
        return this.iterable.injectInto(Integer.MIN_VALUE, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return Math.max(longValue, CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    public long min()
    {
        return this.iterable.injectInto(Integer.MAX_VALUE, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return Math.min(longValue, CollectLongIterable.this.function.longValueOf(each));
            }
        });
    }

    public double average()
    {
        return (double) this.sum() / (double) this.size();
    }

    public double median()
    {
        long[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            long first = sortedArray[i];
            long second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

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

    public long[] toSortedArray()
    {
        long[] array = this.toArray();
        Arrays.sort(array);
        return array;
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
