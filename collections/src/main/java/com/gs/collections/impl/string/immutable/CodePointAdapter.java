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

package com.gs.collections.impl.string.immutable;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;

import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LazyIntIterable;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.block.function.primitive.IntToIntFunction;
import com.gs.collections.api.block.function.primitive.IntToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectIntIntToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.primitive.IntIntProcedure;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.api.iterator.IntIterator;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.IntList;
import com.gs.collections.api.list.primitive.MutableIntList;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.lazy.primitive.ReverseIntIterable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.primitive.AbstractIntIterable;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;

/**
 * Calculates and provides the code points stored in a String as an ImmutableIntList.  This is a cleaner more OO way of
 * providing many of the iterable protocols available in StringIterate for code points.
 *
 * @since 7.0
 */
public class CodePointAdapter extends AbstractIntIterable implements CharSequence, ImmutableIntList, Serializable
{
    private static final long serialVersionUID = 1L;

    private final String adapted;

    public CodePointAdapter(String value)
    {
        this.adapted = value;
    }

    public static CodePointAdapter adapt(String value)
    {
        return new CodePointAdapter(value);
    }

    public static CodePointAdapter from(int... codePoints)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < codePoints.length; i++)
        {
            int codePoint = codePoints[i];
            builder.appendCodePoint(codePoint);
        }
        return new CodePointAdapter(builder.toString());
    }

    public static CodePointAdapter from(IntIterable iterable)
    {
        if (iterable instanceof CodePointAdapter)
        {
            return new CodePointAdapter(iterable.toString());
        }
        StringBuilder builder = iterable.injectInto(new StringBuilder(), new ObjectIntToObjectFunction<StringBuilder, StringBuilder>()
        {
            public StringBuilder valueOf(StringBuilder builder, int value)
            {
                return builder.appendCodePoint(value);
            }
        });
        return new CodePointAdapter(builder.toString());
    }

    public char charAt(int index)
    {
        return this.adapted.charAt(index);
    }

    public int length()
    {
        return this.adapted.length();
    }

    public String subSequence(int start, int end)
    {
        return this.adapted.substring(start, end);
    }

    public StringBuilder toStringBuilder()
    {
        return new StringBuilder(this.adapted);
    }

    @Override
    public String toString()
    {
        return this.adapted;
    }

    public IntIterator intIterator()
    {
        return new InternalIntIterator();
    }

    public int[] toArray()
    {
        return this.toList().toArray();
    }

    public boolean contains(int expected)
    {
        int length = this.adapted.length();
        for (int i = 0; i < length; )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (expected == codePoint)
            {
                return true;
            }
            i += Character.charCount(codePoint);
        }
        return false;
    }

    public void forEach(IntProcedure procedure)
    {
        this.each(procedure);
    }

    public void each(IntProcedure procedure)
    {
        int length = this.adapted.length();
        for (int i = 0; i < length; )
        {
            int codePoint = this.adapted.codePointAt(i);
            procedure.value(codePoint);
            i += Character.charCount(codePoint);
        }
    }

    public CodePointAdapter distinct()
    {
        StringBuilder builder = new StringBuilder();
        IntHashSet seenSoFar = new IntHashSet();

        int length = this.adapted.length();
        for (int i = 0; i < length; )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (seenSoFar.add(codePoint))
            {
                builder.appendCodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }
        return new CodePointAdapter(builder.toString());
    }

    public CodePointAdapter newWith(int element)
    {
        StringBuilder builder = new StringBuilder(this.adapted);
        builder.appendCodePoint(element);
        return new CodePointAdapter(builder.toString());
    }

    public CodePointAdapter newWithout(int element)
    {
        StringBuilder builder = new StringBuilder();
        int indexToRemove = this.indexOf(element);
        if (indexToRemove < 0)
        {
            return this;
        }
        int currentIndex = 0;
        int length = this.adapted.length();
        for (int i = 0; i < length; )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (currentIndex++ != indexToRemove)
            {
                builder.appendCodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }
        return new CodePointAdapter(builder.toString());
    }

    public CodePointAdapter newWithAll(IntIterable elements)
    {
        final StringBuilder builder = new StringBuilder(this.adapted);
        elements.each(new IntProcedure()
        {
            public void value(int each)
            {
                builder.appendCodePoint(each);
            }
        });
        return new CodePointAdapter(builder.toString());
    }

    public CodePointAdapter newWithoutAll(IntIterable elements)
    {
        MutableIntList mutableIntList = this.toList();
        mutableIntList.removeAll(elements);
        return CodePointAdapter.from(mutableIntList.toArray());
    }

    public CodePointAdapter toReversed()
    {
        final StringBuilder builder = new StringBuilder();
        LazyIntIterable reversed = this.asReversed();
        reversed.each(new IntProcedure()
        {
            public void value(int codePoint)
            {
                builder.appendCodePoint(codePoint);
            }
        });
        return new CodePointAdapter(builder.toString());
    }

    public ImmutableIntList subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException("SubList is not implemented on CodePointAdapter");
    }

    public int get(int index)
    {
        int currentIndex = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (index == currentIndex)
            {
                return codePoint;
            }
            i += Character.charCount(codePoint);
            currentIndex++;
        }
        throw new IndexOutOfBoundsException("Index out of bounds");
    }

    public long dotProduct(IntList list)
    {
        throw new UnsupportedOperationException("DotProduct is not implemented on CodePointAdapter");
    }

    public int binarySearch(int value)
    {
        throw new UnsupportedOperationException("BinarySearch is not implemented on CodePointAdapter");
    }

    public int lastIndexOf(int value)
    {
        for (int i = this.size() - 1; i >= 0; i--)
        {
            int codePoint = this.get(i);
            if (codePoint == value)
            {
                return i;
            }
        }
        return -1;
    }

    public ImmutableIntList toImmutable()
    {
        return this;
    }

    public int getLast()
    {
        return this.get(this.size() - 1);
    }

    public LazyIntIterable asReversed()
    {
        return ReverseIntIterable.adapt(this);
    }

    public <T> T injectIntoWithIndex(T injectedValue, ObjectIntIntToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            result = function.valueOf(result, codePoint, i);
            i += Character.charCount(codePoint);
        }
        return result;
    }

    public int getFirst()
    {
        return this.get(0);
    }

    public int indexOf(int value)
    {
        int currentIndex = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (codePoint == value)
            {
                return currentIndex;
            }
            i += Character.charCount(codePoint);
            currentIndex++;
        }
        return -1;
    }

    public void forEachWithIndex(IntIntProcedure procedure)
    {
        int currentIndex = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            procedure.value(codePoint, currentIndex);
            i += Character.charCount(codePoint);
            currentIndex++;
        }
    }

    public CodePointAdapter select(IntPredicate predicate)
    {
        StringBuilder selected = new StringBuilder();
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                selected.appendCodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }
        return new CodePointAdapter(selected.toString());
    }

    public CodePointAdapter reject(IntPredicate predicate)
    {
        StringBuilder rejected = new StringBuilder();
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (!predicate.accept(codePoint))
            {
                rejected.appendCodePoint(codePoint);
            }
            i += Character.charCount(codePoint);
        }
        return new CodePointAdapter(rejected.toString());
    }

    public <V> ImmutableList<V> collect(IntToObjectFunction<? extends V> function)
    {
        FastList<V> list = FastList.newList(this.adapted.length());
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            list.add(function.valueOf(codePoint));
            i += Character.charCount(codePoint);
        }
        return list.toImmutable();
    }

    public CodePointAdapter collectInt(IntToIntFunction function)
    {
        StringBuilder collected = new StringBuilder(this.length());
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            collected.appendCodePoint(function.valueOf(codePoint));
            i += Character.charCount(codePoint);
        }
        return CodePointAdapter.adapt(collected.toString());
    }

    public int detectIfNone(IntPredicate predicate, int ifNone)
    {
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                return codePoint;
            }
            i += Character.charCount(codePoint);
        }
        return ifNone;
    }

    public int count(IntPredicate predicate)
    {
        int count = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                count++;
            }
            i += Character.charCount(codePoint);
        }
        return count;
    }

    public boolean anySatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                return true;
            }
            i += Character.charCount(codePoint);
        }
        return false;
    }

    public boolean allSatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (!predicate.accept(codePoint))
            {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        return true;
    }

    public boolean noneSatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        return true;
    }

    @Override
    public MutableIntList toList()
    {
        IntArrayList list = new IntArrayList(this.adapted.length());
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            list.add(codePoint);
            i += Character.charCount(codePoint);
        }
        return list;
    }

    @Override
    public MutableIntSet toSet()
    {
        IntHashSet set = new IntHashSet(this.adapted.length());
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            set.add(codePoint);
            i += Character.charCount(codePoint);
        }
        return set;
    }

    @Override
    public MutableIntBag toBag()
    {
        IntHashBag bag = new IntHashBag(this.adapted.length());
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            bag.add(codePoint);
            i += Character.charCount(codePoint);
        }
        return bag;
    }

    public <T> T injectInto(T injectedValue, ObjectIntToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            result = function.valueOf(result, codePoint);
            i += Character.charCount(codePoint);
        }
        return result;
    }

    public long sum()
    {
        long sum = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            sum += codePoint;
            i += Character.charCount(codePoint);
        }
        return sum;
    }

    public int max()
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }
        int max = this.get(0);
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (max < codePoint)
            {
                max = codePoint;
            }
            i += Character.charCount(codePoint);
        }
        return max;
    }

    public int min()
    {
        if (this.isEmpty())
        {
            throw new NoSuchElementException();
        }
        int min = this.get(0);
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (codePoint < min)
            {
                min = codePoint;
            }
            i += Character.charCount(codePoint);
        }
        return min;
    }

    public int size()
    {
        int size = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            i += Character.charCount(codePoint);
            size++;
        }
        return size;
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            for (int i = 0; i < this.adapted.length(); )
            {
                if (i > 0)
                {
                    appendable.append(separator);
                }
                int codePoint = this.adapted.codePointAt(i);
                if (appendable instanceof StringBuilder)
                {
                    ((StringBuilder) appendable).appendCodePoint(codePoint);
                }
                else if (appendable instanceof StringBuffer)
                {
                    ((StringBuffer) appendable).appendCodePoint(codePoint);
                }
                else
                {
                    char[] chars = Character.toChars(codePoint);
                    for (int j = 0; j < chars.length; j++)
                    {
                        appendable.append(chars[j]);
                    }
                }
                i += Character.charCount(codePoint);
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object otherList)
    {
        if (otherList == this)
        {
            return true;
        }
        if (otherList instanceof CodePointAdapter)
        {
            return this.equalsCodePointAdapter((CodePointAdapter) otherList);
        }
        if (otherList instanceof IntList)
        {
            return this.equalsIntList((IntList) otherList);
        }
        return false;
    }

    public boolean equalsIntList(IntList list)
    {
        int size = 0;
        for (int i = 0; i < this.adapted.length(); )
        {
            size++;
            int codePoint = this.adapted.codePointAt(i);
            if (size > list.size() || codePoint != list.get(size - 1))
            {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        if (size < list.size())
        {
            return false;
        }
        return true;
    }

    private boolean equalsCodePointAdapter(CodePointAdapter adapter)
    {
        if (this.adapted.length() != adapter.adapted.length())
        {
            return false;
        }
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            if (codePoint != adapter.adapted.codePointAt(i))
            {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        for (int i = 0; i < this.adapted.length(); )
        {
            int codePoint = this.adapted.codePointAt(i);
            hashCode = 31 * hashCode + codePoint;
            i += Character.charCount(codePoint);
        }
        return hashCode;
    }

    private class InternalIntIterator implements IntIterator
    {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        private int currentIndex;

        public boolean hasNext()
        {
            return this.currentIndex != CodePointAdapter.this.adapted.length();
        }

        public int next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            int next = CodePointAdapter.this.adapted.codePointAt(this.currentIndex);
            this.currentIndex += Character.charCount(next);
            return next;
        }
    }
}
