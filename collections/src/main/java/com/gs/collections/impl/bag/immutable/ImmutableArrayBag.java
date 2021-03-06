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

package com.gs.collections.impl.bag.immutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;

/**
 * @since 1.0
 */
public class ImmutableArrayBag<T>
        extends AbstractImmutableBag<T>
        implements Serializable
{
    static final int MAXIMUM_USEFUL_ARRAY_BAG_SIZE = 20;

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
        return ImmutableArrayBag.copyFrom(Bags.mutable.with(elements));
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

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        for (int i = 0; i < this.keys.length; i++)
        {
            objectIntProcedure.value(this.keys[i], this.counts[i]);
        }
    }

    public int sizeDistinct()
    {
        return this.keys.length;
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

    public int occurrencesOf(Object item)
    {
        int index = ArrayIterate.detectIndexWith(this.keys, Predicates2.equal(), item);
        if (index > -1)
        {
            return this.counts[index];
        }
        return 0;
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

    public ImmutableBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return Bags.immutable.withAll(Iterate.addAllTo(elements, HashBag.newBag(this)));
    }

    public ImmutableBag<T> selectByOccurrences(final IntPredicate predicate)
    {
        final MutableBag<T> result = HashBag.newBag();
        this.forEachWithOccurrences(new ObjectIntProcedure<T>()
        {
            public void value(T each, int occurrences)
            {
                if (predicate.accept(occurrences))
                {
                    result.addOccurrences(each, occurrences);
                }
            }
        });
        return result.toImmutable();
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

    public ImmutableBag<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, HashBag.<T>newBag()).toImmutable();
    }

    public ImmutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, HashBag.<T>newBag()).toImmutable();
    }

    public <V> ImmutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        MutableBag<V> result = this.collect(function, HashBag.<V>newBag());
        return ImmutableArrayBag.copyFrom(result);
    }

    public <V> ImmutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        MutableBag<V> result = this.collectIf(predicate, function, HashBag.<V>newBag());
        return ImmutableArrayBag.copyFrom(result);
    }

    public <V> ImmutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, HashBag.<V>newBag()).toImmutable();
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

    public void each(Procedure<? super T> procedure)
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

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.anySatisfy(this.keys, predicate);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.anySatisfyWith(this.keys, predicate, parameter);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.allSatisfy(this.keys, predicate);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.allSatisfyWith(this.keys, predicate, parameter);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return ArrayIterate.noneSatisfy(this.keys, predicate);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.noneSatisfyWith(this.keys, predicate, parameter);
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return ArrayIterate.detect(this.keys, predicate);
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return ArrayIterate.detectWith(this.keys, predicate, parameter);
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
        return ArrayIterate.minBy(this.keys, function);
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        return ArrayIterate.maxBy(this.keys, function);
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> ImmutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, HashBag.<Pair<T, S>>newBag()).toImmutable();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public ImmutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<T, Integer>>newSet()).toImmutable();
    }

    protected Object writeReplace()
    {
        return new ImmutableBagSerializationProxy<T>(this);
    }

    private final class ArrayBagIterator
            implements Iterator<T>
    {
        private int position;
        private int remainingOccurrences = -1;

        private ArrayBagIterator()
        {
            this.remainingOccurrences = ImmutableArrayBag.this.sizeDistinct() > 0 ? ImmutableArrayBag.this.counts[0] : 0;
        }

        public boolean hasNext()
        {
            return this.position != ImmutableArrayBag.this.keys.length
                    && !(this.position == ImmutableArrayBag.this.keys.length - 1 && this.remainingOccurrences == 0);
        }

        public T next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }

            T result = ImmutableArrayBag.this.keys[this.position];

            this.remainingOccurrences--;
            if (this.remainingOccurrences == 0)
            {
                this.position++;
                if (this.position != ImmutableArrayBag.this.keys.length)
                {
                    this.remainingOccurrences = ImmutableArrayBag.this.counts[this.position];
                }
            }
            return result;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from an ImmutableArrayBag");
        }
    }
}
