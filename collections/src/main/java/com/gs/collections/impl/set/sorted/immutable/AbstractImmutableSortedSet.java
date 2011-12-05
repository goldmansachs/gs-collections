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

package com.gs.collections.impl.set.sorted.immutable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.RejectProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.multimap.set.sorted.TreeSortedSetMultimap;
import com.gs.collections.impl.partition.set.sorted.PartitionTreeSortedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.SetIterables;
import com.gs.collections.impl.utility.internal.SortedSetIterables;
import net.jcip.annotations.Immutable;

/**
 * This class is the parent class for all ImmutableSortedSets.  All implementations of ImmutableSortedSet must implement the SortedSet
 * interface so an TreeSet.equals(anImmutablesortedSet) can return true when the contents are the same.
 */
@Immutable
abstract class AbstractImmutableSortedSet<T> extends AbstractImmutableCollection<T>
        implements ImmutableSortedSet<T>, SortedSet<T>
{
    public SortedSet<T> castToSortedSet()
    {
        return this;
    }

    public ImmutableSortedSet<T> newWith(T element)
    {
        if (!this.contains(element))
        {
            return TreeSortedSet.newSet(this).with(element).toImmutable();
        }
        return this;
    }

    public ImmutableSortedSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            TreeSortedSet<T> result = TreeSortedSet.newSet(this);
            result.remove(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSortedSet<T> newWithAll(Iterable<? extends T> elements)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this);
        result.addAllIterable(elements);
        return result.toImmutable();
    }

    public ImmutableSortedSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this);
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public T getFirst()
    {
        return this.first();
    }

    public T getLast()
    {
        return this.last();
    }

    public abstract Iterator<T> iterator();

    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return TreeSortedSet.newSet(this.comparator());
    }

    public ImmutableSortedSet<T> select(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new SelectProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public ImmutableSortedSet<T> reject(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new RejectProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public PartitionImmutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionTreeSortedSet.of(this, predicate).toImmutable();
    }

    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new CollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new CollectIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new FlatCollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <V> ImmutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    public <V> ImmutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    public <S> ImmutableSortedSet<Pair<T, S>> zip(Iterable<S> that)
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, S>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, S>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zip(this, that, pairs).toImmutable();
        }
        return Iterate.zip(this, that, TreeSortedSet.<Pair<T, S>>newSet(Comparators.<T>byFirstOfPair(comparator))).toImmutable();
    }

    public ImmutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, Integer>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, Integer>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zipWithIndex(this, pairs).toImmutable();
        }
        return Iterate.zipWithIndex(this, TreeSortedSet.<Pair<T, Integer>>newSet(Comparators.<T>byFirstOfPair(comparator))).toImmutable();
    }

    public ImmutableSortedSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.unionInto(this, set, TreeSortedSet.<T>newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public ImmutableSortedSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersectInto(this, set, TreeSortedSet.<T>newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public ImmutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, TreeSortedSet.<T>newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public ImmutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifferenceInto(this, setB, TreeSortedSet.<T>newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    public ImmutableSortedSet<SortedSetIterable<T>> powerSet()
    {
        return (ImmutableSortedSet<SortedSetIterable<T>>) (ImmutableSortedSet<?>) SortedSetIterables.immutablePowerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    public SortedSet<T> subSet(T fromElement, T toElement)
    {
        throw new UnsupportedOperationException();
    }

    public SortedSet<T> headSet(T toElement)
    {
        throw new UnsupportedOperationException();
    }

    public SortedSet<T> tailSet(T fromElement)
    {
        throw new UnsupportedOperationException();
    }
}
