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

import com.gs.collections.api.CharIterable;
import com.gs.collections.api.LazyCharIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.CharToObjectFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ObjectCharToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.CharPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.CharProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.CharIterator;
import com.gs.collections.api.list.primitive.MutableCharList;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.block.factory.primitive.CharPredicates;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectCharIterable<T>
        implements LazyCharIterable
{
    private final LazyIterable<T> iterable;
    private final CharFunction<? super T> function;
    private final CharFunctionToProcedure charFunctionToProcedure = new CharFunctionToProcedure();

    public CollectCharIterable(LazyIterable<T> adapted, CharFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public CharIterator charIterator()
    {
        return new CharIterator()
        {
            private final Iterator<T> iterator = CollectCharIterable.this.iterable.iterator();

            public char next()
            {
                return CollectCharIterable.this.function.charValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(CharProcedure procedure)
    {
        this.iterable.forEachWith(this.charFunctionToProcedure, procedure);
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

    public int count(final CharPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    public boolean anySatisfy(final CharPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    public boolean allSatisfy(final CharPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    public boolean noneSatisfy(final CharPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectCharIterable.this.function.charValueOf(each));
            }
        });
    }

    public LazyCharIterable select(CharPredicate predicate)
    {
        return new SelectCharIterable(this, predicate);
    }

    public LazyCharIterable reject(CharPredicate predicate)
    {
        return new SelectCharIterable(this, CharPredicates.not(predicate));
    }

    public char detectIfNone(CharPredicate predicate, char ifNone)
    {
        CharIterator iterator = this.charIterator();
        while (iterator.hasNext())
        {
            char next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> LazyIterable<V> collect(CharToObjectFunction<? extends V> function)
    {
        return new CollectCharToObjectIterable<V>(this, function);
    }

    public long sum()
    {
        return this.iterable.injectInto(0, new LongObjectToLongFunction<T>()
        {
            public long longValueOf(long longValue, T each)
            {
                return longValue + (long) CollectCharIterable.this.function.charValueOf(each);
            }
        });
    }

    public char max()
    {
        CharIterator iterator = this.charIterator();
        char max = iterator.next();
        while (iterator.hasNext())
        {
            char next = iterator.next();
            max = next > max ? next : max;
        }
        return max;
    }

    public char min()
    {
        CharIterator iterator = this.charIterator();
        char min = iterator.next();
        while (iterator.hasNext())
        {
            char next = iterator.next();
            min = min < next ? min : next;
        }
        return min;
    }

    public char minIfEmpty(char defaultValue)
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

    public char maxIfEmpty(char defaultValue)
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
        char[] sortedArray = this.toSortedArray();
        int i = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            int first = sortedArray[i];
            int second = sortedArray[i - 1];
            return ((double) first + (double) second) / 2.0d;
        }
        return (double) sortedArray[i];
    }

    public char[] toArray()
    {
        final char[] array = new char[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectCharIterable.this.function.charValueOf(each);
            }
        });
        return array;
    }

    public char[] toSortedArray()
    {
        char[] array = this.toArray();
        Arrays.sort(array);
        return array;
    }

    public <T> T injectInto(T injectedValue, ObjectCharToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        for (CharIterator iterator = this.charIterator(); iterator.hasNext(); )
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

            CharIterator iterator = this.charIterator();
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

    public MutableCharList toList()
    {
        return CharArrayList.newList(this);
    }

    public MutableCharList toSortedList()
    {
        return CharArrayList.newList(this).sortThis();
    }

    public MutableCharSet toSet()
    {
        return CharHashSet.newSet(this);
    }

    public MutableCharBag toBag()
    {
        return CharHashBag.newBag(this);
    }

    public LazyCharIterable asLazy()
    {
        return this;
    }

    public boolean contains(char value)
    {
        return this.anySatisfy(CharPredicates.equal(value));
    }

    public boolean containsAll(char... source)
    {
        for (char value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(CharIterable source)
    {
        for (CharIterator iterator = source.charIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class CharFunctionToProcedure implements Procedure2<T, CharProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, CharProcedure parm)
        {
            parm.value(CollectCharIterable.this.function.charValueOf(each));
        }
    }
}
