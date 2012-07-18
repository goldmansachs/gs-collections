/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.bag.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.IterableIterate;

/**
 * @since 1.0
 */
public class ImmutableArrayBag<T>
        extends AbstractImmutableBag<T>
        implements Serializable
{
    static final int MAXIMUM_USEFUL_ARRAY_BAG_SIZE = 10;

    private static final long serialVersionUID = 1L;

    private final T[] keys;
    private final int[] counts;

    ImmutableArrayBag(T[] keys, int[] counts)
    {
        this.keys = keys;
        this.counts = counts;

        if (this.keys.length != this.counts.length)
        {
            throw new IllegalArgumentException();
        }
    }

    public static <T> ImmutableArrayBag<T> newBagWith(T... elements)
    {
        return ImmutableArrayBag.copyFrom(Bags.mutable.of(elements));
    }

    public static <T> ImmutableArrayBag<T> copyFrom(Bag<T> bag)
    {
        int distinctItemCount = bag.sizeDistinct();
        final T[] newKeys = (T[]) new Object[distinctItemCount];
        final int[] newCounts = new int[distinctItemCount];
        bag.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            private int index;

            public void value(T each, int count)
            {
                newKeys[this.index] = each;
                newCounts[this.index] = count;
                this.index++;
            }
        });
        return new ImmutableArrayBag<T>(newKeys, newCounts);
    }

    public int size()
    {
        int sum = 0;
        for (int value : this.counts)
        {
            sum += value;
        }
        return sum;
    }

    public int sizeDistinct()
    {
        return this.keys.length;
    }

    public int occurrencesOf(Object item)
    {
        int index = ArrayIterate.detectIndexWith(this.keys, Predicates2.equal(), item);
        if (index > -1)
        {
            return this.counts[index];
        }
        return 0;
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            objectIntProcedure.value(this.keys[i], this.counts[i]);
        }
    }

    public ImmutableBag<T> newWith(T element)
    {
        int elementIndex = ArrayIterate.detectIndexWith(this.keys, Predicates2.equal(), element);
        int distinctItemCount = this.sizeDistinct() + (elementIndex == -1 ? 1 : 0);
        if (distinctItemCount > MAXIMUM_USEFUL_ARRAY_BAG_SIZE)
        {
            return HashBag.newBag(this).with(element).toImmutable();
        }
        return this.newArrayBagWith(element, elementIndex, distinctItemCount);
    }

    private ImmutableBag<T> newArrayBagWith(T element, int elementIndex, int distinctItemCount)
    {
        T[] newKeys = (T[]) new Object[distinctItemCount];
        int[] newCounts = new int[distinctItemCount];
        System.arraycopy(this.keys, 0, newKeys, 0, this.keys.length);
        System.arraycopy(this.counts, 0, newCounts, 0, this.counts.length);
        if (elementIndex == -1)
        {
            newKeys[distinctItemCount - 1] = element;
            newCounts[distinctItemCount - 1] = 1;
        }
        else
        {
            newCounts[elementIndex]++;
        }
        return new ImmutableArrayBag<T>(newKeys, newCounts);
    }

    public ImmutableBag<T> newWithout(T element)
    {
        int elementIndex = ArrayIterate.detectIndexWith(this.keys, Predicates2.equal(), element);
        if (elementIndex > -1)
        {
            int distinctItemCount = this.sizeDistinct() - (this.counts[elementIndex] == 1 ? 1 : 0);
            T[] newKeys = (T[]) new Object[distinctItemCount];
            int[] newCounts = new int[distinctItemCount];
            if (distinctItemCount == this.sizeDistinct())
            {
                System.arraycopy(this.keys, 0, newKeys, 0, distinctItemCount);
                System.arraycopy(this.counts, 0, newCounts, 0, distinctItemCount);
                newCounts[elementIndex]--;
            }
            else
            {
                System.arraycopy(this.keys, 0, newKeys, 0, elementIndex);
                System.arraycopy(this.counts, 0, newCounts, 0, elementIndex);
                System.arraycopy(this.keys, elementIndex + 1, newKeys, elementIndex, newKeys.length - elementIndex);
                System.arraycopy(this.counts, elementIndex + 1, newCounts, elementIndex, newCounts.length - elementIndex);
            }
            return new ImmutableArrayBag<T>(newKeys, newCounts);
        }
        return this;
    }

    public MutableMap<T, Integer> toMapOfItemToCount()
    {
        final MutableMap<T, Integer> map = UnifiedMap.newMap(this.size());
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T item, int count)
            {
                map.put(item, count);
            }
        });
        return map;
    }

    public ImmutableBag<T> toImmutable()
    {
        return this;
    }

    public ImmutableBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return Bags.immutable.ofAll(Iterate.addAllTo(elements, HashBag.newBag(this)));
    }

    public ImmutableBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this.reject(Predicates.in(elements));
    }

    public ImmutableBag<T> select(final Predicate<? super T> predicate)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                if (predicate.accept(each))
                {
                    result.addOccurrences(each, index);
                }
            }
        });
        return ImmutableArrayBag.copyFrom(result);
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.selectWith(this, predicate, parameter, targetCollection);
    }

    public ImmutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this.select(Predicates.not(predicate));
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.rejectWith(this, predicate, parameter, targetCollection);
    }

    public PartitionImmutableBag<T> partition(Predicate<? super T> predicate)
    {
        return PartitionHashBag.of(this, predicate).toImmutable();
    }

    public <S> ImmutableBag<S> selectInstancesOf(final Class<S> clazz)
    {
        final MutableBag<S> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                if (clazz.isInstance(each))
                {
                    result.addOccurrences((S) each, index);
                }
            }
        });
        return ImmutableArrayBag.copyFrom(result);
    }

    public <V> ImmutableBag<V> collect(final Function<? super T, ? extends V> function)
    {
        final MutableBag<V> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                result.addOccurrences(function.valueOf(each), index);
            }
        });
        return ImmutableArrayBag.copyFrom(result);
    }

    @Override
    public <P, V, R extends Collection<V>> R collectWith(
            Function2<? super T, ? super P, ? extends V> function,
            P parameter,
            R targetCollection)
    {
        return IterableIterate.collectWith(this, function, parameter, targetCollection);
    }

    public <V> ImmutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return ImmutableArrayBag.copyFrom(IterableIterate.collectIf(this, predicate, function, HashBag.<V>newBag()));
    }

    public <V> ImmutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, HashBagMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V> ImmutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, HashBagMultimap.<V, T>newMultimap()).toImmutable();
    }

    public T getFirst()
    {
        return ArrayIterate.getFirst(this.keys);
    }

    public T getLast()
    {
        return ArrayIterate.getLast(this.keys);
    }

    public <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        FlatCollectProcedure<T, V> procedure = new FlatCollectProcedure<T, V>(function, HashBag.<V>newBag());
        this.forEach(procedure);
        return ((MutableBag<V>) procedure.getCollection()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(
            Function<? super T, ? extends V> function, R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(
            Function<? super T, ? extends Iterable<V>> function, R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
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
        if (this.size() != bag.size())
        {
            return false;
        }
        for (int i = 0; i < this.keys.length; i++)
        {
            if (this.counts[i] != bag.occurrencesOf(this.keys[i]))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int sum = 0;
        for (int i = 0; i < this.keys.length; i++)
        {
            T each = this.keys[i];
            sum += (each == null ? 0 : each.hashCode()) ^ this.counts[i];
        }
        return sum;
    }

    public void forEach(Procedure<? super T> procedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            T key = this.keys[i];
            for (int j = 1; j <= this.counts[i]; j++)
            {
                procedure.value(key);
            }
        }
    }

    public Iterator<T> iterator()
    {
        return new ArrayBagIterator();
    }

    private final class ArrayBagIterator
            implements Iterator<T>
    {
        private int keyCount = -1;
        private int countCount = -1;

        private ArrayBagIterator()
        {
            this.keyCount = ImmutableArrayBag.this.keys.length - 1;
            this.countCount = this.keyCount < 0 ? -1 : ImmutableArrayBag.this.counts[ImmutableArrayBag.this.keys.length - 1];
        }

        public boolean hasNext()
        {
            return this.keyCount >= 0;
        }

        public T next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            T result = ImmutableArrayBag.this.keys[this.keyCount];
            --this.countCount;
            if (this.countCount == 0)
            {
                --this.keyCount;
                this.countCount = this.keyCount < 0 ? 0 : ImmutableArrayBag.this.counts[this.keyCount];
            }
            return result;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        return ArrayIterate.min(this.keys, comparator);
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        return ArrayIterate.max(this.keys, comparator);
    }

    @Override
    public T min()
    {
        return ArrayIterate.min(this.keys);
    }

    @Override
    public T max()
    {
        return ArrayIterate.max(this.keys);
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.min(this.keys, Comparators.byFunction(function));
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.max(this.keys, Comparators.byFunction(function));
    }

    public <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, HashBag.<Pair<T, S>>newBag()).toImmutable();
    }

    public ImmutableBag<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(HashBag.<Pair<T, Integer>>newBag()).toImmutable();
    }

    protected Object writeReplace()
    {
        return new ImmutableBagSerializationProxy<T>(this);
    }
}
