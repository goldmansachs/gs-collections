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

package com.gs.collections.impl.list.primitive;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
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
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.lazy.primitive.CollectIntToObjectIterable;
import com.gs.collections.impl.lazy.primitive.LazyIntIterableAdapter;
import com.gs.collections.impl.lazy.primitive.ReverseIntIterable;
import com.gs.collections.impl.lazy.primitive.SelectIntIterable;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;

/**
 * An IntInterval is a range of ints that may be iterated over using a step value.
 */
public final class IntInterval
        implements ImmutableIntList, Serializable
{
    private static final long serialVersionUID = 1L;

    private final int from;
    private final int to;
    private final int step;

    private IntInterval(int from, int to, int step)
    {
        this.from = from;
        this.to = to;
        this.step = step;
    }

    /**
     * This static {@code from} method allows IntInterval to act as a fluent builder for itself.
     * It works in conjunction with the instance methods {@link #to(int)} and {@link #by(int)}.
     * <p>
     * Usage Example:
     * <pre>
     * IntInterval interval1 = IntInterval.from(1).to(5);         // results in: 1, 2, 3, 4, 5.
     * IntInterval interval2 = IntInterval.from(1).to(10).by(2);  // results in: 1, 3, 5, 7, 9.
     * </pre>
     */
    public static IntInterval from(int newFrom)
    {
        return IntInterval.fromToBy(newFrom, newFrom, 1);
    }

    /**
     * This instance {@code to} method allows IntInterval to act as a fluent builder for itself.
     * It works in conjunction with the static method {@link #from(int)} and instance method {@link #by(int)}.
     * <p>
     * Usage Example:
     * <pre>
     * IntInterval interval1 = IntInterval.from(1).to(5);         // results in: 1, 2, 3, 4, 5.
     * IntInterval interval2 = IntInterval.from(1).to(10).by(2);  // results in: 1, 3, 5, 7, 9.
     * </pre>
     */
    public IntInterval to(int newTo)
    {
        return IntInterval.fromToBy(this.from, newTo, this.step);
    }

    /**
     * This instance {@code by} method allows IntInterval to act as a fluent builder for itself.
     * It works in conjunction with the static method {@link #from(int)} and instance method {@link #to(int)}.
     * <p>
     * Usage Example:
     * <pre>
     * IntInterval interval1 = IntInterval.from(1).to(5);         // results in: 1, 2, 3, 4, 5.
     * IntInterval interval2 = IntInterval.from(1).to(10).by(2);  // results in: 1, 3, 5, 7, 9.
     * </pre>
     */
    public IntInterval by(int newStep)
    {
        return IntInterval.fromToBy(this.from, this.to, newStep);
    }

    /**
     * Returns an IntInterval starting at zero.
     * <p>
     * Usage Example:
     * <pre>
     * IntInterval interval1 = IntInterval.zero().to(5);         // results in: 0, 1, 2, 3, 4, 5.
     * IntInterval interval2 = IntInterval.zero().to(10).by(2);  // results in: 0, 2, 4, 6, 8, 10.
     * </pre>
     */
    public static IntInterval zero()
    {
        return IntInterval.from(0);
    }

    /**
     * Returns an IntInterval starting from 1 to the specified count value with a step value of 1.
     */
    public static IntInterval oneTo(int count)
    {
        return IntInterval.oneToBy(count, 1);
    }

    /**
     * Returns an IntInterval starting from 1 to the specified count value with a step value of step.
     */
    public static IntInterval oneToBy(int count, int step)
    {
        if (count < 1)
        {
            throw new IllegalArgumentException("Only positive ranges allowed using oneToBy");
        }
        return IntInterval.fromToBy(1, count, step);
    }

    /**
     * Returns an IntInterval starting from 0 to the specified count value with a step value of 1.
     */
    public static IntInterval zeroTo(int count)
    {
        return IntInterval.zeroToBy(count, 1);
    }

    /**
     * Returns an IntInterval starting from 0 to the specified count value with a step value of step.
     */
    public static IntInterval zeroToBy(int count, int step)
    {
        return IntInterval.fromToBy(0, count, step);
    }

    /**
     * Returns an IntInterval starting from the value from to the specified value to with a step value of 1.
     */
    public static IntInterval fromTo(int from, int to)
    {
        if (from <= to)
        {
            return IntInterval.fromToBy(from, to, 1);
        }
        return IntInterval.fromToBy(from, to, -1);
    }

    /**
     * Returns an IntInterval representing the even values from the value from to the value to.
     */
    public static IntInterval evensFromTo(int from, int to)
    {
        if (from % 2 != 0)
        {
            if (from < to)
            {
                from++;
            }
            else
            {
                from--;
            }
        }
        if (to % 2 != 0)
        {
            if (to > from)
            {
                to--;
            }
            else
            {
                to++;
            }
        }
        return IntInterval.fromToBy(from, to, to > from ? 2 : -2);
    }

    /**
     * Returns an IntInterval representing the odd values from the value from to the value to.
     */
    public static IntInterval oddsFromTo(int from, int to)
    {
        if (from % 2 == 0)
        {
            if (from < to)
            {
                from++;
            }
            else
            {
                from--;
            }
        }
        if (to % 2 == 0)
        {
            if (to > from)
            {
                to--;
            }
            else
            {
                to++;
            }
        }
        return IntInterval.fromToBy(from, to, to > from ? 2 : -2);
    }

    /**
     * Returns an IntInterval for the range of integers inclusively between from and to with the specified
     * stepBy value.
     */
    public static IntInterval fromToBy(int from, int to, int stepBy)
    {
        if (stepBy == 0)
        {
            throw new IllegalArgumentException("Cannot use a step by of 0");
        }
        if (from > to && stepBy > 0 || from < to && stepBy < 0)
        {
            throw new IllegalArgumentException("Step by is incorrect for the range");
        }
        return new IntInterval(from, to, stepBy);
    }

    /**
     * Returns true if the IntInterval contains all of the specified int values.
     */
    public boolean containsAll(int... values)
    {
        for (int value : values)
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

    /**
     * Returns true if the IntInterval contains none of the specified int values.
     */
    public boolean containsNone(int... values)
    {
        for (int value : values)
        {
            if (this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the IntInterval contains the specified int value.
     */
    public boolean contains(int value)
    {
        return this.isWithinBoundaries(value) && (value - this.from) % this.step == 0;
    }

    private boolean isWithinBoundaries(int value)
    {
        return this.step > 0 && this.from <= value && value <= this.to
                || this.step < 0 && this.to <= value && value <= this.from;
    }

    public void forEachWithIndex(IntIntProcedure procedure)
    {
        int index = 0;
        if (this.from <= this.to)
        {
            for (int i = this.from; i <= this.to; i += this.step)
            {
                procedure.value(i, index++);
            }
        }
        else
        {
            for (int i = this.from; i >= this.to; i += this.step)
            {
                procedure.value(i, index++);
            }
        }
    }

    public void forEach(IntProcedure procedure)
    {
        this.each(procedure);
    }

    /**
     * @since 7.0.
     */
    public void each(IntProcedure procedure)
    {
        if (this.from <= this.to)
        {
            for (int i = this.from; i <= this.to; i += this.step)
            {
                procedure.value(i);
            }
        }
        else
        {
            for (int i = this.from; i >= this.to; i += this.step)
            {
                procedure.value(i);
            }
        }
    }

    public int count(IntPredicate predicate)
    {
        int count = 0;
        for (int i = 0; i < this.size(); i++)
        {
            if (predicate.accept(this.get(i)))
            {
                count++;
            }
        }
        return count;
    }

    public boolean anySatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.size(); i++)
        {
            if (predicate.accept(this.get(i)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean allSatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.size(); i++)
        {
            if (!predicate.accept(this.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    public boolean noneSatisfy(IntPredicate predicate)
    {
        for (int i = 0; i < this.size(); i++)
        {
            if (predicate.accept(this.get(i)))
            {
                return false;
            }
        }
        return true;
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
        if (this.size() != list.size())
        {
            return false;
        }
        if (this.from <= this.to)
        {
            int listIndex = 0;
            for (int i = this.from; i <= this.to; i += this.step)
            {
                if (i != list.get(listIndex++))
                {
                    return false;
                }
            }
        }
        else
        {
            int listIndex = 0;
            for (int i = this.from; i >= this.to; i += this.step)
            {
                if (i != list.get(listIndex++))
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        if (this.from <= this.to)
        {
            for (int i = this.from; i <= this.to; i += this.step)
            {
                hashCode = 31 * hashCode + i;
            }
        }
        else
        {
            for (int i = this.from; i >= this.to; i += this.step)
            {
                hashCode = 31 * hashCode + i;
            }
        }
        return hashCode;
    }

    /**
     * Returns a new IntInterval with the from and to values reversed and the step value negated.
     */
    public IntInterval toReversed()
    {
        return IntInterval.fromToBy(this.to, this.from, -this.step);
    }

    /**
     * @since 6.0
     */
    public ImmutableIntList distinct()
    {
        return this;
    }

    public ImmutableIntList subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException("subList not yet implemented!");
    }

    /**
     * Calculates and returns the size of the interval.
     */
    public int size()
    {
        return (this.to - this.from) / this.step + 1;
    }

    public long dotProduct(IntList list)
    {
        if (this.size() != list.size())
        {
            throw new IllegalArgumentException("Lists used in dotProduct must be the same size");
        }
        long sum = 0L;
        for (int i = 0; i < this.size(); i++)
        {
            sum += (long) this.get(i) * list.get(i);
        }
        return sum;
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean notEmpty()
    {
        return !this.isEmpty();
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

    public void appendString(
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        try
        {
            appendable.append(start);
            for (int i = 0; i < this.size(); i++)
            {
                if (i > 0)
                {
                    appendable.append(separator);
                }
                int value = this.get(i);
                appendable.append(String.valueOf(value));
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int[] toArray()
    {
        final int[] result = new int[this.size()];
        this.forEachWithIndex(new IntIntProcedure()
        {
            public void value(int each, int index)
            {
                result[index] = each;
            }
        });
        return result;
    }

    public <T> T injectInto(T injectedValue, ObjectIntToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        if (this.from <= this.to)
        {
            for (int i = this.from; i <= this.to; i += this.step)
            {
                result = function.valueOf(result, i);
            }
        }
        else
        {
            for (int i = this.from; i >= this.to; i += this.step)
            {
                result = function.valueOf(result, i);
            }
        }
        return result;
    }

    public <T> T injectIntoWithIndex(T injectedValue, ObjectIntIntToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        int index = 0;

        if (this.from <= this.to)
        {
            for (int i = this.from; i <= this.to; i += this.step)
            {
                result = function.valueOf(result, i, index);
                index++;
            }
        }
        else
        {
            for (int i = this.from; i >= this.to; i += this.step)
            {
                result = function.valueOf(result, i, index);
                index++;
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public IntIterator intIterator()
    {
        return new IntIntervalIterator();
    }

    public int getFirst()
    {
        return this.from;
    }

    public int getLast()
    {
        return this.locationAfterN(this.size() - 1);
    }

    public int get(int index)
    {
        this.checkBounds("index", index);
        return this.locationAfterN(index);
    }

    private void checkBounds(String name, int index)
    {
        if (index < 0 || index >= this.size())
        {
            throw new IndexOutOfBoundsException(name + ": " + index + ' ' + this.toString());
        }
    }

    private int locationAfterN(int index)
    {
        if (index <= 0)
        {
            return this.from;
        }
        if (this.step > 0)
        {
            return (int) Math.min((long) this.from + (long) this.step * (long) index, this.to);
        }
        return (int) Math.max((long) this.from + (long) this.step * (long) index, this.to);
    }

    public int indexOf(int value)
    {
        if (!this.isWithinBoundaries(value))
        {
            return -1;
        }
        int diff = value - this.from;
        if (diff % this.step == 0)
        {
            return diff / this.step;
        }

        return -1;
    }

    public int lastIndexOf(int value)
    {
        return this.indexOf(value);
    }

    public ImmutableIntList select(IntPredicate predicate)
    {
        return IntArrayList.newList(new SelectIntIterable(this, predicate)).toImmutable();
    }

    public ImmutableIntList reject(IntPredicate predicate)
    {
        return IntArrayList.newList(new SelectIntIterable(this, IntPredicates.not(predicate))).toImmutable();
    }

    public int detectIfNone(IntPredicate predicate, int ifNone)
    {
        return new SelectIntIterable(this, predicate).detectIfNone(predicate, ifNone);
    }

    public <V> ImmutableList<V> collect(IntToObjectFunction<? extends V> function)
    {
        return new CollectIntToObjectIterable<V>(this, function).toList().toImmutable();
    }

    public LazyIntIterable asReversed()
    {
        return ReverseIntIterable.adapt(this);
    }

    public long sum()
    {
        long sum = 0L;
        for (IntIterator intIterator = this.intIterator(); intIterator.hasNext(); )
        {
            sum += intIterator.next();
        }
        return sum;
    }

    public int max()
    {
        if (this.from >= this.to)
        {
            return this.getFirst();
        }
        return this.getLast();
    }

    public int min()
    {
        if (this.from <= this.to)
        {
            return this.getFirst();
        }
        return this.getLast();
    }

    public int minIfEmpty(int defaultValue)
    {
        return this.min();
    }

    public int maxIfEmpty(int defaultValue)
    {
        return this.max();
    }

    public double average()
    {
        return (double) this.sum() / (double) this.size();
    }

    public double median()
    {
        int[] sortedArray = this.toSortedArray();
        int middleIndex = sortedArray.length >> 1;
        if (sortedArray.length > 1 && (sortedArray.length & 1) == 0)
        {
            int first = sortedArray[middleIndex];
            int second = sortedArray[middleIndex - 1];
            return ((double) first + (double) second) / 2.0;
        }
        return (double) sortedArray[middleIndex];
    }

    public int binarySearch(int value)
    {
        if (this.step > 0 && this.from > value || this.step < 0 && this.from < value)
        {
            return -1;
        }

        if (this.step > 0 && this.to < value || this.step < 0 && this.to > value)
        {
            return -1 - this.size();
        }

        int diff = value - this.from;
        int index = diff / this.step;
        return diff % this.step == 0 ? index : (index + 2) * -1;
    }

    public int[] toSortedArray()
    {
        int[] array = this.toArray();
        Arrays.sort(array);
        return array;
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
        return new LazyIntIterableAdapter(this);
    }

    public ImmutableIntList toImmutable()
    {
        return this;
    }

    public ImmutableIntList newWith(int element)
    {
        return IntArrayList.newList(this).with(element).toImmutable();
    }

    public ImmutableIntList newWithout(int element)
    {
        return IntArrayList.newList(this).without(element).toImmutable();
    }

    public ImmutableIntList newWithAll(IntIterable elements)
    {
        return IntArrayList.newList(this).withAll(elements).toImmutable();
    }

    public ImmutableIntList newWithoutAll(IntIterable elements)
    {
        return IntArrayList.newList(this).withoutAll(elements).toImmutable();
    }

    private class IntIntervalIterator implements IntIterator
    {
        private int current = IntInterval.this.from;

        public boolean hasNext()
        {
            if (IntInterval.this.from <= IntInterval.this.to)
            {
                return this.current <= IntInterval.this.to;
            }
            return this.current >= IntInterval.this.to;
        }

        public int next()
        {
            if (this.hasNext())
            {
                int result = this.current;
                this.current += IntInterval.this.step;
                return result;
            }
            throw new NoSuchElementException();
        }
    }
}
