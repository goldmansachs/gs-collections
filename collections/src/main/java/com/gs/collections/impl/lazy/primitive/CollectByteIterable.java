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

import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.LazyByteIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.ByteToObjectFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.BytePredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ByteProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.ByteIterator;
import com.gs.collections.api.list.primitive.MutableByteList;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.block.factory.primitive.BytePredicates;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectByteIterable<T>
        implements LazyByteIterable
{
    private final LazyIterable<T> iterable;
    private final ByteFunction<? super T> function;
    private final ByteFunctionToProcedure byteFunctionToProcedure = new ByteFunctionToProcedure();

    public CollectByteIterable(LazyIterable<T> adapted, ByteFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public ByteIterator byteIterator()
    {
        return new ByteIterator()
        {
            private final Iterator<T> iterator = CollectByteIterable.this.iterable.iterator();

            public byte next()
            {
                return CollectByteIterable.this.function.byteValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(ByteProcedure procedure)
    {
        this.iterable.forEachWith(this.byteFunctionToProcedure, procedure);
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

    public int count(final BytePredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    public boolean anySatisfy(final BytePredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    public boolean allSatisfy(final BytePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    public boolean noneSatisfy(final BytePredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectByteIterable.this.function.byteValueOf(each));
            }
        });
    }

    public LazyByteIterable select(BytePredicate predicate)
    {
        return new SelectByteIterable(this, predicate);
    }

    public LazyByteIterable reject(BytePredicate predicate)
    {
        return new SelectByteIterable(this, BytePredicates.not(predicate));
    }

    public byte detectIfNone(BytePredicate predicate, byte ifNone)
    {
        ByteIterator iterator = this.byteIterator();
        while (iterator.hasNext())
        {
            byte next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> LazyIterable<V> collect(ByteToObjectFunction<? extends V> function)
    {
        return new CollectByteToObjectIterable<V>(this, function);
    }

    public long sum()
    {
        return this.iterable.injectInto(0, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return longValue + (long) CollectByteIterable.this.function.byteValueOf(each);
            }
        });
    }

    public byte max()
    {
        ByteIterator iterator = this.byteIterator();
        byte max = iterator.next();
        while (iterator.hasNext())
        {
            byte next = iterator.next();
            max = next > max ? next : max;
        }
        return max;
    }

    public byte min()
    {
        ByteIterator iterator = this.byteIterator();
        byte min = iterator.next();
        while (iterator.hasNext())
        {
            byte next = iterator.next();
            min = min < next ? min : next;
        }
        return min;
    }

    public byte minIfEmpty(byte defaultValue)
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

    public byte maxIfEmpty(byte defaultValue)
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
        byte[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            int first = sortedArray[i];
            int second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

    public byte[] toArray()
    {
        final byte[] array = new byte[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectByteIterable.this.function.byteValueOf(each);
            }
        });
        return array;
    }

    public byte[] toSortedArray()
    {
        byte[] array = this.toArray();
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

            ByteIterator iterator = this.byteIterator();
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

    public MutableByteList toList()
    {
        return ByteArrayList.newList(this);
    }

    public MutableByteList toSortedList()
    {
        return ByteArrayList.newList(this).sortThis();
    }

    public MutableByteSet toSet()
    {
        return ByteHashSet.newSet(this);
    }

    public MutableByteBag toBag()
    {
        return ByteHashBag.newBag(this);
    }

    public LazyByteIterable asLazy()
    {
        return this;
    }

    public boolean contains(byte value)
    {
        return this.anySatisfy(BytePredicates.equal(value));
    }

    public boolean containsAll(byte... source)
    {
        for (byte value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(ByteIterable source)
    {
        for (ByteIterator iterator = source.byteIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class ByteFunctionToProcedure implements Procedure2<T, ByteProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, ByteProcedure parm)
        {
            parm.value(CollectByteIterable.this.function.byteValueOf(each));
        }
    }
}
