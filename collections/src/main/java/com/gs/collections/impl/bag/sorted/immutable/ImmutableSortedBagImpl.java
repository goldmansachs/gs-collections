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

package com.gs.collections.impl.bag.sorted.immutable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.primitive.ObjectIntPair;
import com.gs.collections.impl.Counter;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import com.gs.collections.impl.partition.bag.sorted.PartitionImmutableSortedBagImpl;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.SortedBagIterables;
import net.jcip.annotations.Immutable;

@Immutable
class ImmutableSortedBagImpl<T> extends AbstractImmutableSortedBag<T> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final T[] elements;
    private final int[] occurrences;
    private final Comparator<? super T> comparator;
    private final int size;

    ImmutableSortedBagImpl(SortedBag<T> sortedBag)
    {
        if (sortedBag.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.comparator = sortedBag.comparator();
        this.elements = (T[]) new Object[sortedBag.sizeDistinct()];
        this.occurrences = new int[sortedBag.sizeDistinct()];
        this.size = sortedBag.size();

        sortedBag.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            private int i;

            public void value(T each, int occurrencesOfEach)
            {
                ImmutableSortedBagImpl.this.elements[this.i] = each;
                ImmutableSortedBagImpl.this.occurrences[this.i] = occurrencesOfEach;
                this.i++;
            }
        });
    }

    public ImmutableSortedBag<T> newWith(T element)
    {
        return TreeBag.newBag(this).with(element).toImmutable();
    }

    public ImmutableSortedBag<T> newWithout(T element)
    {
        MutableSortedBag<T> result = TreeBag.newBag(this);
        result.remove(element);

        return result.toImmutable();
    }

    public ImmutableSortedBag<T> newWithAll(Iterable<? extends T> elements)
    {
        MutableSortedBag<T> result = TreeBag.newBag(this);
        result.addAllIterable(elements);
        return result.toImmutable();
    }

    public ImmutableSortedBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        MutableSortedBag<T> result = TreeBag.newBag(this);
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public Comparator<? super T> comparator()
    {
        return this.comparator;
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return ArrayIterate.min(this.elements, comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return ArrayIterate.max(this.elements, comparator);
    }

    @Override
    public T min()
    {
        return ArrayIterate.min(this.elements);
    }

    @Override
    public T max()
    {
        return ArrayIterate.max(this.elements);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.maxBy(this.elements, function);
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.minBy(this.elements, function);
    }

    public ImmutableSortedBag<T> takeWhile(Predicate<? super T> predicate)
    {
        MutableSortedBag<T> bag = TreeBag.newBag(this.comparator);
        for (int i = 0; i < this.elements.length; i++)
        {
            if (predicate.accept(this.elements[i]))
            {
                bag.addOccurrences(this.elements[i], this.occurrences[i]);
            }
            else
            {
                return bag.toImmutable();
            }
        }
        return bag.toImmutable();
    }

    public ImmutableSortedBag<T> dropWhile(Predicate<? super T> predicate)
    {
        MutableSortedBag<T> bag = TreeBag.newBag(this.comparator);
        int startIndex = this.detectNotIndex(predicate);
        for (int i = startIndex; i < this.elements.length; i++)
        {
            bag.addOccurrences(this.elements[i], this.occurrences[i]);
        }
        return bag.toImmutable();
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        int result = 0;
        for (int i = 0; i < this.elements.length; i++)
        {
            if (predicate.accept(this.elements[i]))
            {
                return result;
            }
            result += this.occurrences[i];
        }
        return -1;
    }

    private int detectNotIndex(Predicate<? super T> predicate)
    {
        for (int index = 0; index < this.elements.length; index++)
        {
            if (!predicate.accept(this.elements[index]))
            {
                return index;
            }
        }
        return this.elements.length;
    }

    public PartitionImmutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionTreeBag<T> result = new PartitionTreeBag<T>(this.comparator());
        MutableSortedBag<T> selected = result.getSelected();
        MutableSortedBag<T> rejected = result.getRejected();

        int partitionIndex = this.detectNotIndex(predicate);
        for (int i = 0; i < partitionIndex; i++)
        {
            selected.addOccurrences(this.elements[i], this.occurrences[i]);
        }
        for (int j = partitionIndex; j < this.elements.length; j++)
        {
            rejected.addOccurrences(this.elements[j], this.occurrences[j]);
        }
        return new PartitionImmutableSortedBagImpl<T>(result);
    }

    public int size()
    {
        return this.size;
    }

    public int sizeDistinct()
    {
        return this.elements.length;
    }

    public int indexOf(Object object)
    {
        int result = 0;
        for (int i = 0; i < this.elements.length; i++)
        {
            if (object.equals(this.elements[i]))
            {
                return result;
            }
            result += this.occurrences[i];
        }
        return -1;
    }

    public T getFirst()
    {
        return ArrayIterate.getFirst(this.elements);
    }

    public T getLast()
    {
        return ArrayIterate.getLast(this.elements);
    }

    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        int index = fromIndex;
        ListIterate.rangeCheck(index, toIndex, this.size());

        if (index > toIndex)
        {
            throw new IllegalArgumentException("fromIndex must not be greater than toIndex");
        }
        int i = 0;
        int beginningIndex = 0;

        while (beginningIndex <= index)
        {
            beginningIndex += this.occurrences[i];
            if (beginningIndex <= index)
            {
                i++;
            }
        }
        int numberOfIterations = beginningIndex - index;

        for (int j = 0; j < numberOfIterations && index <= toIndex; j++)
        {
            procedure.value(this.elements[i]);
            index++;
        }

        while (index <= toIndex)
        {
            i++;
            for (int j = 0; j < this.occurrences[i] && index <= toIndex; j++)
            {
                procedure.value(this.elements[i]);
                index++;
            }
        }
    }

    public void each(Procedure<? super T> procedure)
    {
        for (int i = 0; i < this.elements.length; i++)
        {
            T element = this.elements[i];
            int occurrences = this.occurrences[i];
            for (int j = 0; j < occurrences; j++)
            {
                procedure.value(element);
            }
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int index = 0;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                objectIntProcedure.value(this.elements[i], index);
                index++;
            }
        }
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(fromIndex, toIndex, this.size());

        if (fromIndex > toIndex)
        {
            throw new IllegalArgumentException("fromIndex must not be greater than toIndex");
        }

        int arrayIndex = 0;
        int index = fromIndex;
        int i = 0;
        while (i <= index)
        {
            i += this.occurrences[arrayIndex];
            if (i <= index)
            {
                arrayIndex++;
            }
        }
        int numberOfIterations = i - index;

        for (int j = 0; j < numberOfIterations && index <= toIndex; j++)
        {
            objectIntProcedure.value(this.elements[arrayIndex], index);
            index++;
        }

        while (index <= toIndex)
        {
            arrayIndex++;
            for (int j = 0; j < this.occurrences[arrayIndex] && index <= toIndex; j++)
            {
                objectIntProcedure.value(this.elements[arrayIndex], index);
                index++;
            }
        }
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
        for (int i = 0; i < this.occurrences.length; i++)
        {
            procedure.value(this.elements[i], this.occurrences[i]);
        }
    }

    public int occurrencesOf(Object item)
    {
        int index = Arrays.binarySearch(this.elements, (T) item, this.comparator);
        if (index > -1)
        {
            return this.occurrences[index];
        }
        return 0;
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, TreeBagMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, TreeBagMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public ImmutableSortedSet<T> distinct()
    {
        return SortedSets.immutable.with(this.comparator(), this.elements);
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        if (this.size != other.size())
        {
            return false;
        }

        if (other instanceof RandomAccess)
        {
            List<S> otherList = (List<S>) other;
            int otherListIndex = 0;
            for (int i = 0; i < this.elements.length; i++)
            {
                for (int j = 0; j < this.occurrences[i]; j++)
                {
                    if (!predicate.accept(this.elements[i], otherList.get(otherListIndex)))
                    {
                        return false;
                    }
                    otherListIndex++;
                }
            }
            return true;
        }

        Iterator<S> otherIterator = other.iterator();
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                if (!predicate.accept(this.elements[i], otherIterator.next()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public MutableList<T> toSortedList()
    {
        return this.toSortedList(Comparators.naturalOrder());
    }

    @Override
    public MutableList<T> toSortedList(final Comparator<? super T> comparator)
    {
        MutableList<ObjectIntPair<T>> sorted = this.toListWithOccurrences().sortThis(new Comparator<ObjectIntPair<T>>()
        {
            public int compare(ObjectIntPair<T> o1, ObjectIntPair<T> o2)
            {
                return comparator.compare(o1.getOne(), o2.getOne());
            }
        });

        final MutableList<T> result = FastList.newList(this.size());
        sorted.forEach(new Procedure<ObjectIntPair<T>>()
        {
            public void value(ObjectIntPair<T> each)
            {
                T object = each.getOne();
                int occurrences = each.getTwo();
                for (int i = 0; i < occurrences; i++)
                {
                    result.add(object);
                }
            }
        });
        return result;
    }

    @Override
    public MutableSortedSet<T> toSortedSet()
    {
        return SortedSets.mutable.with(this.elements);
    }

    @Override
    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        return SortedSets.mutable.with(comparator, this.elements);
    }

    @Override
    public MutableSortedBag<T> toSortedBag()
    {
        final MutableSortedBag<T> bag = TreeBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                bag.addOccurrences(each, occurrences);
            }
        });

        return bag;
    }

    @Override
    public MutableSortedBag<T> toSortedBag(Comparator<? super T> comparator)
    {
        final MutableSortedBag<T> bag = TreeBag.newBag(comparator);
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                bag.addOccurrences(each, occurrences);
            }
        });

        return bag;
    }

    @Override
    public Object[] toArray()
    {
        final Object[] result = new Object[this.size()];
        this.each(new Procedure<T>()
        {
            private int i;

            public void value(T each)
            {
                result[this.i] = each;
                this.i++;
            }
        });

        return result;
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        E[] array1 = array;
        if (array1.length < this.size)
        {
            array1 = (E[]) Array.newInstance(array1.getClass().getComponentType(), this.size);
        }
        T[] items = (T[]) this.toArray();
        System.arraycopy(items, 0, array1, 0, this.size);
        if (array1.length > this.size)
        {
            array1[this.size] = null;
        }
        return array1;
    }

    public MutableSortedMap<T, Integer> toMapOfItemToCount()
    {
        final MutableSortedMap<T, Integer> map = TreeSortedMap.newMap(this.comparator());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                map.put(item, count);
            }
        });
        return map;
    }

    public String toStringOfItemToCount()
    {
        if (this.isEmpty())
        {
            return "{}";
        }
        StringBuilder builder = new StringBuilder().append('{');

        for (int i = 0; i < this.elements.length; i++)
        {
            builder.append(this.elements[i]);
            builder.append('=');
            builder.append(this.occurrences[i]);
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        return builder.append('}').toString();
    }

    public MutableStack<T> toStack()
    {
        return Stacks.mutable.withAll(this);
    }

    @Override
    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        MutableList<RichIterable<T>> result = Lists.mutable.empty();
        T[] objects = (T[]) this.toArray();
        MutableCollection<T> batch = this.newMutable(size);
        int j = 0;

        while (j < objects.length)
        {
            for (int i = 0; i < size && j < objects.length; i++)
            {
                batch.add(objects[j]);
                j++;
            }
            result.add(batch.toImmutable());
        }
        return result.toImmutable();
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        IV result = injectedValue;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                result = function.value(result, this.elements[i]);
            }
        }
        return result;
    }

    @Override
    public int injectInto(int injectedValue, IntObjectToIntFunction<? super T> function)
    {
        int result = injectedValue;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                result = function.intValueOf(result, this.elements[i]);
            }
        }
        return result;
    }

    @Override
    public long injectInto(long injectedValue, LongObjectToLongFunction<? super T> function)
    {
        long result = injectedValue;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                result = function.longValueOf(result, this.elements[i]);
            }
        }
        return result;
    }

    @Override
    public double injectInto(double injectedValue, DoubleObjectToDoubleFunction<? super T> function)
    {
        double result = injectedValue;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                result = function.doubleValueOf(result, this.elements[i]);
            }
        }
        return result;
    }

    @Override
    public float injectInto(float injectedValue, FloatObjectToFloatFunction<? super T> function)
    {
        float result = injectedValue;
        for (int i = 0; i < this.elements.length; i++)
        {
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                result = function.floatValueOf(result, this.elements[i]);
            }
        }
        return result;
    }

    @Override
    public long sumOfInt(IntFunction<? super T> function)
    {
        long sum = 0L;
        for (int i = 0; i < this.elements.length; i++)
        {
            long value = (long) function.intValueOf(this.elements[i]);
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public double sumOfFloat(FloatFunction<? super T> function)
    {
        double sum = 0.0d;
        double compensation = 0.0d;
        for (int i = 0; i < this.elements.length; i++)
        {
            float value = function.floatValueOf(this.elements[i]);
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                double adjustedValue = value - compensation;
                double nextSum = sum + adjustedValue;
                compensation = nextSum - sum - adjustedValue;
                sum = nextSum;
            }
        }
        return sum;
    }

    @Override
    public long sumOfLong(LongFunction<? super T> function)
    {
        long sum = 0L;
        for (int i = 0; i < this.elements.length; i++)
        {
            long value = function.longValueOf(this.elements[i]);
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                sum += value;
            }
        }
        return sum;
    }

    @Override
    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        double sum = 0.0d;
        double compensation = 0.0d;
        for (int i = 0; i < this.elements.length; i++)
        {
            double value = function.doubleValueOf(this.elements[i]);
            for (int j = 0; j < this.occurrences[i]; j++)
            {
                double adjustedValue = value - compensation;
                double nextSum = sum + adjustedValue;
                compensation = nextSum - sum - adjustedValue;
                sum = nextSum;
            }
        }
        return sum;
    }

    public int compareTo(SortedBag<T> otherBag)
    {
        return SortedBagIterables.compare(this, otherBag);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.allSatisfy(this.elements, predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.allSatisfyWith(this.elements, predicate, parameter);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.anySatisfy(this.elements, predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.anySatisfyWith(this.elements, predicate, parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.noneSatisfy(this.elements, predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.noneSatisfyWith(this.elements, predicate, parameter);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ArrayIterate.detect(this.elements, predicate);
    }

    @Override
    public boolean contains(Object object)
    {
        return Arrays.binarySearch(this.elements, (T) object, this.comparator) >= 0;
    }

    public Iterator<T> iterator()
    {
        return new InternalIterator();
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Bag))
        {
            return false;
        }
        Bag<?> bag = (Bag<?>) other;
        if (this.sizeDistinct() != bag.sizeDistinct())
        {
            return false;
        }

        for (int i = 0; i < this.elements.length; i++)
        {
            if (bag.occurrencesOf(this.elements[i]) != this.occurrences[i])
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        final Counter counter = new Counter();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int count)
            {
                counter.add((each == null ? 0 : each.hashCode()) ^ count);
            }
        });
        return counter.getCount();
    }

    private class InternalIterator implements Iterator<T>
    {
        private int position;
        private int occurrencesRemaining = ImmutableSortedBagImpl.this.isEmpty() ? 0 : ImmutableSortedBagImpl.this.occurrences[0];

        public boolean hasNext()
        {
            return this.position < ImmutableSortedBagImpl.this.elements.length - 1 || this.occurrencesRemaining != 0;
        }

        public T next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            if (this.occurrencesRemaining == 0)
            {
                this.position++;
                this.occurrencesRemaining = ImmutableSortedBagImpl.this.occurrences[this.position];
            }
            this.occurrencesRemaining--;
            return ImmutableSortedBagImpl.this.elements[this.position];
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
        }
    }
}
