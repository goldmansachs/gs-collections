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

package com.webguys.ponzu.impl.set.sorted.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.multimap.sortedset.MutableSortedSetMultimap;
import com.webguys.ponzu.api.partition.set.sorted.PartitionMutableSortedSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.set.sorted.ImmutableSortedSet;
import com.webguys.ponzu.api.set.sorted.MutableSortedSet;
import com.webguys.ponzu.api.set.sorted.SortedSetIterable;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.collection.mutable.UnmodifiableMutableCollection;
import com.webguys.ponzu.impl.factory.SortedSets;

/**
 * An unmodifiable view of a SortedSet.
 *
 * @see MutableSortedSet#asUnmodifiable()
 */
public class UnmodifiableSortedSet<T>
        extends UnmodifiableMutableCollection<T>
        implements MutableSortedSet<T>
{
    private static final long serialVersionUID = 1L;

    protected UnmodifiableSortedSet(MutableSortedSet<? extends T> sortedSet)
    {
        super(sortedSet);
    }

    /**
     * This method will take a MutableSortedSet and wrap it directly in a UnmodifiableSortedSet.  It will
     * take any other non-GS-SortedSet and first adapt it will a SortedSetAdapter, and then return a
     * UnmodifiableSortedSet that wraps the adapter.
     */
    public static <E, S extends SortedSet<E>> UnmodifiableSortedSet<E> of(S set)
    {
        if (set == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableSortedSet for null");
        }
        return new UnmodifiableSortedSet<E>(SortedSetAdapter.adapt(set));
    }

    protected MutableSortedSet<T> getSortedSet()
    {
        return (MutableSortedSet<T>) this.getMutableCollection();
    }

    @Override
    public MutableSortedSet<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableSortedSet<T> asSynchronized()
    {
        return SynchronizedSortedSet.of(this);
    }

    @Override
    public ImmutableSortedSet<T> toImmutable()
    {
        return SortedSets.immutable.ofSortedSet(this.getSortedSet());
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getSortedSet().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getSortedSet().hashCode();
    }

    @Override
    public UnmodifiableSortedSet<T> clone()
    {
        return this;
    }

    @Override
    public MutableSortedSet<T> newEmpty()
    {
        return this.getSortedSet().newEmpty();
    }

    @Override
    public MutableSortedSet<T> filter(Predicate<? super T> predicate)
    {
        return this.getSortedSet().filter(predicate);
    }

    @Override
    public <P> MutableSortedSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getSortedSet().filterWith(predicate, parameter);
    }

    @Override
    public MutableSortedSet<T> filterNot(Predicate<? super T> predicate)
    {
        return this.getSortedSet().filterNot(predicate);
    }

    @Override
    public <P> MutableSortedSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getSortedSet().filterNotWith(predicate, parameter);
    }

    @Override
    public PartitionMutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        return this.getSortedSet().partition(predicate);
    }

    @Override
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return this.getSortedSet().transform(function);
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getSortedSet().flatTransform(function);
    }

    @Override
    public <P, A> MutableList<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getSortedSet().transformWith(function, parameter);
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getSortedSet().transformIf(predicate, function);
    }

    @Override
    public <V> MutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getSortedSet().groupBy(function);
    }

    @Override
    public <V> MutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getSortedSet().groupByEach(function);
    }

    @Override
    public <S> MutableSortedSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getSortedSet().zip(that);
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getSortedSet().zip(that, target);
    }

    @Override
    public MutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.getSortedSet().zipWithIndex();
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.getSortedSet().zipWithIndex(target);
    }

    public Comparator<? super T> comparator()
    {
        return this.getSortedSet().comparator();
    }

    public MutableSortedSet<T> union(SetIterable<? extends T> set)
    {
        return this.getSortedSet().union(set);
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getSortedSet().unionInto(set, targetSet);
    }

    public MutableSortedSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.getSortedSet().intersect(set);
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getSortedSet().intersectInto(set, targetSet);
    }

    public MutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.getSortedSet().difference(subtrahendSet);
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return this.getSortedSet().differenceInto(subtrahendSet, targetSet);
    }

    public MutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.getSortedSet().symmetricDifference(setB);
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getSortedSet().symmetricDifferenceInto(set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getSortedSet().isSubsetOf(candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getSortedSet().isProperSubsetOf(candidateSuperset);
    }

    public MutableSortedSet<SortedSetIterable<T>> powerSet()
    {
        return this.getSortedSet().powerSet();
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return this.getSortedSet().cartesianProduct(set);
    }

    public MutableSortedSet<T> subSet(T fromElement, T toElement)
    {
        return this.getSortedSet().subSet(fromElement, toElement).asUnmodifiable();
    }

    public MutableSortedSet<T> headSet(T toElement)
    {
        return this.getSortedSet().headSet(toElement).asUnmodifiable();
    }

    public MutableSortedSet<T> tailSet(T fromElement)
    {
        return this.getSortedSet().tailSet(fromElement).asUnmodifiable();
    }

    public T first()
    {
        return this.getSortedSet().first();
    }

    public T last()
    {
        return this.getSortedSet().last();
    }

    public int compareTo(SortedSetIterable<T> o)
    {
        return this.getSortedSet().compareTo(o);
    }

    @Override
    public MutableSortedSet<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedSet<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedSet<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSortedSet<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }
}
