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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.sorted.ImmutableSortedBag;
import com.gs.collections.api.bag.sorted.SortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.sorted.PartitionImmutableSortedBag;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.EmptyIterator;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import com.gs.collections.impl.partition.bag.sorted.PartitionImmutableSortedBagImpl;
import com.gs.collections.impl.partition.bag.sorted.PartitionTreeBag;
import com.gs.collections.impl.utility.OrderedIterate;
import net.jcip.annotations.Immutable;

@Immutable
class ImmutableEmptySortedBag<T>
        extends AbstractImmutableSortedBag<T>
        implements Serializable
{
    static final ImmutableSortedBag<?> INSTANCE = new ImmutableEmptySortedBag<Object>();

    private static final long serialVersionUID = 1L;
    private final Comparator<? super T> comparator;

    private ImmutableEmptySortedBag()
    {
        this.comparator = null;
    }

    ImmutableEmptySortedBag(Comparator<? super T> comparator)
    {
        this.comparator = comparator;
    }

    public ImmutableSortedBag<T> newWith(T element)
    {
        return SortedBags.immutable.with(this.comparator, element);
    }

    public ImmutableSortedBag<T> newWithAll(Iterable<? extends T> elements)
    {
        return SortedBags.immutable.withAll(this.comparator, elements);
    }

    public ImmutableSortedBag<T> newWithout(T element)
    {
        return this;
    }

    public ImmutableSortedBag<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        return other instanceof Bag && ((Collection<?>) other).isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> procedure)
    {
    }

    public int sizeDistinct()
    {
        return 0;
    }

    public int size()
    {
        return 0;
    }

    @Override
    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return Lists.immutable.empty();
    }

    @Override
    public ImmutableSortedBag<T> select(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public ImmutableSortedBag<T> reject(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public <P> ImmutableSortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this;
    }

    @Override
    public <P> ImmutableSortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this;
    }

    @Override
    public <S> ImmutableSortedBag<S> selectInstancesOf(Class<S> clazz)
    {
        return (ImmutableSortedBag<S>) this;
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return TreeBagMultimap.<V, T>newMultimap(this.comparator).toImmutable();
    }

    public <V> ImmutableSortedBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return TreeBagMultimap.<V, T>newMultimap(this.comparator).toImmutable();
    }

    public <V> ImmutableMap<V, T> groupByUniqueKey(Function<? super T, ? extends V> function)
    {
        return Maps.immutable.empty();
    }

    @Override
    public boolean contains(Object object)
    {
        if (object == null)
        {
            throw new NullPointerException();
        }
        return false;
    }

    @Override
    public ImmutableSortedBag<T> tap(Procedure<? super T> procedure)
    {
        return this;
    }

    @Override
    public ImmutableSortedBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this;
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return OrderedIterate.corresponds(this, other, predicate);
    }

    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
    }

    public void each(Procedure<? super T> procedure)
    {
    }

    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
    }

    public int occurrencesOf(Object item)
    {
        return 0;
    }

    public Iterator<T> iterator()
    {
        return EmptyIterator.getInstance();
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T min()
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max()
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    public Comparator<? super T> comparator()
    {
        return this.comparator;
    }

    @Override
    public ImmutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        return SortedSets.immutable.with((Comparator<? super Pair<T, Integer>>) this.comparator);
    }

    public PartitionImmutableSortedBag<T> partitionWhile(Predicate<? super T> predicate)
    {
        return new PartitionImmutableSortedBagImpl<T>(new PartitionTreeBag<T>(this.comparator()));
    }

    public ImmutableSortedSet<T> distinct()
    {
        return SortedSets.immutable.with(this.comparator);
    }

    public T getFirst()
    {
        return null;
    }

    public T getLast()
    {
        return null;
    }

    public ImmutableSortedBag<T> takeWhile(Predicate<? super T> predicate)
    {
        return this;
    }

    public ImmutableSortedBag<T> dropWhile(Predicate<? super T> predicate)
    {
        return this;
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        return -1;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    @Override
    public MutableStack<T> toStack()
    {
        return Stacks.mutable.empty();
    }

    public int indexOf(Object object)
    {
        return -1;
    }

    public int compareTo(SortedBag<T> o)
    {
        return o.size() * -1;
    }

    @Override
    public RichIterable<RichIterable<T>> chunk(int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }
        return Lists.immutable.empty();
    }

    public MutableSortedMap<T, Integer> toMapOfItemToCount()
    {
        return SortedMaps.mutable.of(this.comparator);
    }

    public ImmutableSortedBag<T> take(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        return this;
    }

    public ImmutableSortedBag<T> drop(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        return this;
    }
}
