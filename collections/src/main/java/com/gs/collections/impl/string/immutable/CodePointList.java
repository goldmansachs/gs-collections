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
public class CodePointList extends AbstractIntIterable implements ImmutableIntList, Serializable
{
    private static final long serialVersionUID = 1L;

    private final int[] codePoints;

    public CodePointList(String value)
    {
        int stringSize = value.length();
        IntArrayList list = new IntArrayList(stringSize);
        for (int i = 0; i < stringSize; )
        {
            int codePoint = value.codePointAt(i);
            i += Character.charCount(codePoint);
            list.add(codePoint);
        }
        this.codePoints = list.toArray();
    }

    private CodePointList(int... codePoints)
    {
        this.codePoints = codePoints;
    }

    public static CodePointList from(String value)
    {
        return new CodePointList(value);
    }

    public static CodePointList build(int... codePoints)
    {
        int[] copy = new int[codePoints.length];
        System.arraycopy(codePoints, 0, copy, 0, codePoints.length);
        return new CodePointList(copy);
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
        for (int i = 0; i < this.size(); i++)
        {
            if (expected == this.get(i))
            {
                return true;
            }
        }
        return false;
    }

    public void forEach(IntProcedure procedure)
    {
        this.each(procedure);
    }

    public void each(IntProcedure procedure)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            procedure.value(codePoint);
        }
    }

    public CodePointList distinct()
    {
        IntArrayList result = new IntArrayList();
        IntHashSet seenSoFar = new IntHashSet();

        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (seenSoFar.add(codePoint))
            {
                result.add(codePoint);
            }
        }
        return new CodePointList(result.toArray());
    }

    public CodePointList newWith(int element)
    {
        int oldSize = this.size();
        int[] target = new int[oldSize + 1];
        System.arraycopy(this.codePoints, 0, target, 0, oldSize);
        target[oldSize] = element;
        return new CodePointList(target);
    }

    public CodePointList newWithout(int element)
    {
        //todo optimize based on code points array
        IntArrayList list = new IntArrayList();
        int indexToRemove = this.indexOf(element);
        if (indexToRemove < 0)
        {
            return this;
        }
        for (int i = 0; i < this.size(); i++)
        {
            if (i != indexToRemove)
            {
                int codePoint = this.get(i);
                list.add(codePoint);
            }
        }
        return new CodePointList(list.toArray());
    }

    public String buildString()
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.size(); i++)
        {
            builder.appendCodePoint(this.get(i));
        }
        return builder.toString();
    }

    public CodePointList newWithAll(IntIterable elements)
    {
        MutableIntList mutableIntList = this.toList();
        mutableIntList.addAll(elements);
        return new CodePointList(mutableIntList.toArray());
    }

    public CodePointList newWithoutAll(IntIterable elements)
    {
        MutableIntList mutableIntList = this.toList();
        mutableIntList.removeAll(elements);
        return new CodePointList(mutableIntList.toArray());
    }

    public CodePointList toReversed()
    {
        int[] reversed = new int[this.size()];
        int swapIndex = this.size() - 1;
        for (int i = 0; i < this.size(); i++)
        {
            int value = this.get(i);
            reversed[swapIndex--] = value;
        }
        return new CodePointList(reversed);
    }

    public ImmutableIntList subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException("SubList is not implemented on CodePointList");
    }

    public int get(int index)
    {
        return this.codePoints[index];
    }

    public long dotProduct(IntList list)
    {
        throw new UnsupportedOperationException("DotProduct is not implemented on CodePointList");
    }

    public int binarySearch(int value)
    {
        throw new UnsupportedOperationException("BinarySearch is not implemented on CodePointList");
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
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            result = function.valueOf(result, codePoint, i);
        }
        return result;
    }

    public int getFirst()
    {
        return this.get(0);
    }

    public int indexOf(int value)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (codePoint == value)
            {
                return i;
            }
        }
        return -1;
    }

    public void forEachWithIndex(IntIntProcedure procedure)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            procedure.value(codePoint, i);
        }
    }

    public CodePointList select(IntPredicate predicate)
    {
        int size = this.size();
        IntArrayList selected = new IntArrayList();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (predicate.accept(codePoint))
            {
                selected.add(codePoint);
            }
        }
        return new CodePointList(selected.toArray());
    }

    public CodePointList reject(IntPredicate predicate)
    {
        int size = this.size();
        IntArrayList rejected = new IntArrayList();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (!predicate.accept(codePoint))
            {
                rejected.add(codePoint);
            }
        }
        return new CodePointList(rejected.toArray());
    }

    public <V> ImmutableList<V> collect(IntToObjectFunction<? extends V> function)
    {
        int size = this.size();
        FastList<V> list = FastList.newList(size);
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            list.add(function.valueOf(codePoint));
        }
        return list.toImmutable();
    }

    public int detectIfNone(IntPredicate predicate, int ifNone)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (predicate.accept(codePoint))
            {
                return codePoint;
            }
        }
        return ifNone;
    }

    public int count(IntPredicate predicate)
    {
        int count = 0;
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (predicate.accept(codePoint))
            {
                count++;
            }
        }
        return count;
    }

    public boolean anySatisfy(IntPredicate predicate)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (predicate.accept(codePoint))
            {
                return true;
            }
        }
        return false;
    }

    public boolean allSatisfy(IntPredicate predicate)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (!predicate.accept(codePoint))
            {
                return false;
            }
        }
        return true;
    }

    public boolean noneSatisfy(IntPredicate predicate)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (predicate.accept(codePoint))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public MutableIntList toList()
    {
        int size = this.size();
        IntArrayList list = new IntArrayList(size);
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            list.add(codePoint);
        }
        return list;
    }

    @Override
    public MutableIntSet toSet()
    {
        int size = this.size();
        IntHashSet set = new IntHashSet(size);
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            set.add(codePoint);
        }
        return set;
    }

    @Override
    public MutableIntBag toBag()
    {
        int size = this.size();
        IntHashBag bag = new IntHashBag(size);
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            bag.add(codePoint);
        }
        return bag;
    }

    public <T> T injectInto(T injectedValue, ObjectIntToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            result = function.valueOf(result, codePoint);
        }
        return result;
    }

    public long sum()
    {
        long sum = 0;
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            sum += codePoint;
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
        int size = this.size();
        for (int i = 1; i < size; i++)
        {
            int codePoint = this.get(i);
            if (max < codePoint)
            {
                max = codePoint;
            }
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
        int size = this.size();
        for (int i = 1; i < size; i++)
        {
            int codePoint = this.get(i);
            if (codePoint < min)
            {
                min = codePoint;
            }
        }
        return min;
    }

    public int size()
    {
        return this.codePoints.length;
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            int size = this.size();
            for (int i = 0; i < size; i++)
            {
                if (i > 0)
                {
                    appendable.append(separator);
                }
                int codePoint = this.get(i);
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
        if (!(otherList instanceof IntList))
        {
            return false;
        }
        IntList list = (IntList) otherList;
        int size = this.size();
        if (size != list.size())
        {
            return false;
        }
        for (int i = 0; i < size; i++)
        {
            int codePoint = this.get(i);
            if (codePoint != list.get(i))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        for (int i = 0; i < this.size(); i++)
        {
            int codePoint = this.get(i);
            hashCode = 31 * hashCode + codePoint;
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
            return this.currentIndex != CodePointList.this.size();
        }

        public int next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            int next = CodePointList.this.get(this.currentIndex);
            this.currentIndex++;
            return next;
        }
    }
}
