/*
 * Copyright 2013 Goldman Sachs.
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIntIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.iterator.IntIterator;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectIntIterable<T>
        implements LazyIntIterable
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

    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
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

    public LazyIntIterable select(IntPredicate predicate)
    {
        return new SelectIntIterable(this, predicate);
    }

    public LazyIntIterable reject(IntPredicate predicate)
    {
        return new SelectIntIterable(this, IntPredicates.not(predicate));
    }

    public int detectIfNone(IntPredicate predicate, int ifNone)
    {
        IntIterator iterator = this.intIterator();
        while (iterator.hasNext())
        {
            int next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> LazyIterable<V> collect(IntToObjectFunction<? extends V> function)
    {
        return new CollectIntToObjectIterable<V>(this, function);
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
        IntIterator iterator = this.intIterator();
        int max = iterator.next();
        while (iterator.hasNext())
        {
            max = Math.max(max, iterator.next());
        }
        return max;
    }

    public int min()
    {
        IntIterator iterator = this.intIterator();
        int min = iterator.next();
        while (iterator.hasNext())
        {
            min = Math.min(min, iterator.next());
        }
        return min;
    }

    public int minIfEmpty(int defaultValue)
    {
        try
        {
            return this.min();
        }
        catch (NoSuchElementException ex)
        {
        }
        return defaultValue;
    }

    public int maxIfEmpty(int defaultValue)
    {
        try
        {
            return this.max();
        }
        catch (NoSuchElementException ex)
        {
        }
        return defaultValue;
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

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            IntIterator iterator = this.intIterator();
            if (iterator.hasNext())
            {
                appendable.append(String.valueOf(iterator.next()));
                while (iterator.hasNext())
                {
                    appendable.append(separator);
                    appendable.append(String.valueOf(iterator.next()));
                }
            }

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public MutableIntList toList()
    {
        return IntArrayList.newList(this);
    }

    public MutableIntList toSortedList()
    {
        return IntArrayList.newList(this).sortThis();
    }

    public MutableIntSet toSet()
    {
        return IntHashSet.newSet(this);
    }

    public MutableIntBag toBag()
    {
        return IntHashBag.newBag(this);
    }

    public LazyIntIterable asLazy()
    {
        return this;
    }

    public boolean contains(int value)
    {
        return this.anySatisfy(IntPredicates.equal(value));
    }

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
