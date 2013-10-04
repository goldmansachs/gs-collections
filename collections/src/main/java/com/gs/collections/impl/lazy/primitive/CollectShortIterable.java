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

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.LazyShortIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ObjectShortToObjectFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.function.primitive.ShortToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.ShortPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.primitive.ShortProcedure;
import com.gs.collections.api.iterator.ShortIterator;
import com.gs.collections.api.list.primitive.MutableShortList;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.primitive.ShortPredicates;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectShortIterable<T>
        implements LazyShortIterable
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

    public LazyShortIterable select(ShortPredicate predicate)
    {
        return new SelectShortIterable(this, predicate);
    }

    public LazyShortIterable reject(ShortPredicate predicate)
    {
        return new SelectShortIterable(this, ShortPredicates.not(predicate));
    }

    public short detectIfNone(ShortPredicate predicate, short ifNone)
    {
        ShortIterator iterator = this.shortIterator();
        while (iterator.hasNext())
        {
            short next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> LazyIterable<V> collect(ShortToObjectFunction<? extends V> function)
    {
        return new CollectShortToObjectIterable<V>(this, function);
    }

    public long sum()
    {
        return this.iterable.injectInto(0, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return longValue + (long) CollectShortIterable.this.function.shortValueOf(each);
            }
        });
    }

    public short max()
    {
        ShortIterator iterator = this.shortIterator();
        short max = iterator.next();
        while (iterator.hasNext())
        {
            short next = iterator.next();
            max = next > max ? next : max;
        }
        return max;
    }

    public short min()
    {
        ShortIterator iterator = this.shortIterator();
        short min = iterator.next();
        while (iterator.hasNext())
        {
            short next = iterator.next();
            min = min < next ? min : next;
        }
        return min;
    }

    public short minIfEmpty(short defaultValue)
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

    public short maxIfEmpty(short defaultValue)
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
        if (this.isEmpty())
        {
            throw new ArithmeticException();
        }
        return (double) this.sum() / (double) this.size();
    }

    public double median()
    {
        if (this.isEmpty())
        {
            throw new ArithmeticException();
        }
        short[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            int first = sortedArray[i];
            int second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

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

    public short[] toSortedArray()
    {
        short[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    public <T> T injectInto(T injectedValue, ObjectShortToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        for (ShortIterator iterator = this.shortIterator(); iterator.hasNext(); )
        {
            result = function.valueOf(result, iterator.next());
        }
        return result;
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

            ShortIterator iterator = this.shortIterator();
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

    public MutableShortList toList()
    {
        return ShortArrayList.newList(this);
    }

    public MutableShortList toSortedList()
    {
        return ShortArrayList.newList(this).sortThis();
    }

    public MutableShortSet toSet()
    {
        return ShortHashSet.newSet(this);
    }

    public MutableShortBag toBag()
    {
        return ShortHashBag.newBag(this);
    }

    public LazyShortIterable asLazy()
    {
        return this;
    }

    public boolean contains(short value)
    {
        return this.anySatisfy(ShortPredicates.equal(value));
    }

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
