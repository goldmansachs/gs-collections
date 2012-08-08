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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.partition.set.sorted.PartitionMutableSortedSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.set.sorted.ImmutableSortedSet;
import com.webguys.ponzu.api.set.sorted.MutableSortedSet;
import com.webguys.ponzu.api.set.sorted.SortedSetIterable;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.collection.mutable.AbstractCollectionAdapter;
import com.webguys.ponzu.impl.factory.SortedSets;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.multimap.set.sorted.TreeSortedSetMultimap;
import com.webguys.ponzu.impl.partition.set.sorted.PartitionTreeSortedSet;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.Iterate;
import com.webguys.ponzu.impl.utility.internal.SetIterables;
import com.webguys.ponzu.impl.utility.internal.SetIterate;
import com.webguys.ponzu.impl.utility.internal.SortedSetIterables;

/**
 * This class provides a MutableSortedSet wrapper around a JDK Collections SortedSet interface instance.  All of the MutableSortedSet
 * interface methods are supported in addition to the JDK SortedSet interface methods.
 * <p/>
 * To create a new wrapper around an existing SortedSet instance, use the {@link #adapt(SortedSet)} factory method.
 */
public final class SortedSetAdapter<T>
        extends AbstractCollectionAdapter<T>
        implements Serializable, MutableSortedSet<T>
{
    private static final long serialVersionUID = 1L;
    private final SortedSet<T> delegate;

    SortedSetAdapter(SortedSet<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("SortedSetAdapter may not wrap null");
        }
        this.delegate = newDelegate;
    }

    @Override
    protected SortedSet<T> getDelegate()
    {
        return this.delegate;
    }

    public MutableSortedSet<T> asUnmodifiable()
    {
        return UnmodifiableSortedSet.of(this);
    }

    public MutableSortedSet<T> asSynchronized()
    {
        return SynchronizedSortedSet.of(this);
    }

    public ImmutableSortedSet<T> toImmutable()
    {
        return SortedSets.immutable.ofSortedSet(this.delegate);
    }

    public static <T> MutableSortedSet<T> adapt(SortedSet<T> set)
    {
        if (set instanceof MutableSortedSet<?>)
        {
            return (MutableSortedSet<T>) set;
        }
        return new SortedSetAdapter<T>(set);
    }

    @Override
    public MutableSortedSet<T> clone()
    {
        return TreeSortedSet.newSet(this.delegate);
    }

    @Override
    public boolean contains(Object o)
    {
        return this.delegate.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return this.delegate.containsAll(collection);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.delegate.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    public SortedSetAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public SortedSetAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public SortedSetAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public SortedSetAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public SortedSetAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public SortedSetAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public SortedSetAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    /**
     * @deprecated use {@link TreeSortedSet#newSet()} instead (inlineable)
     */
    @Deprecated
    public MutableSortedSet<T> newEmpty()
    {
        return TreeSortedSet.newSet(this.comparator());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return SetIterate.removeAllIterable(this, iterable);
    }

    @Override
    public MutableSortedSet<T> filter(Predicate<? super T> predicate)
    {
        return Iterate.filter(this.delegate, predicate, TreeSortedSet.<T>newSet(this.comparator()));
    }

    @Override
    public MutableSortedSet<T> filterNot(Predicate<? super T> predicate)
    {
        return Iterate.filterNot(this.delegate, predicate, TreeSortedSet.<T>newSet(this.comparator()));
    }

    @Override
    public PartitionMutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionTreeSortedSet.of(this, predicate);
    }

    @Override
    public <V> MutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return Iterate.transform(this.delegate, function, FastList.<V>newList());
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return Iterate.tranformIf(this.delegate, predicate, function, FastList.<V>newList());
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.flatTransform(this.delegate, function, FastList.<V>newList());
    }

    @Override
    public <V> TreeSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return Iterate.groupBy(this.delegate, function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator()));
    }

    @Override
    public <V> TreeSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.groupByEach(this.delegate, function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator()));
    }

    @Override
    public <P> MutableSortedSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.filterWith(this.delegate, predicate, parameter, TreeSortedSet.<T>newSet(this.comparator()));
    }

    @Override
    public <P> MutableSortedSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.filterNotWith(this.delegate, predicate, parameter, TreeSortedSet.<T>newSet(this.comparator()));
    }

    @Override
    public <P, V> MutableList<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return Iterate.transformWith(this.delegate, function, parameter, FastList.<V>newList());
    }

    @Override
    public <S> MutableSortedSet<Pair<T, S>> zip(Iterable<S> that)
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, S>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, S>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zip(this.delegate, that, pairs);
        }
        return Iterate.zip(this.delegate, that, TreeSortedSet.<Pair<T, S>>newSet(Comparators.<T>byFirstOfPair(comparator)));
    }

    @Override
    public MutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, Integer>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, Integer>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zipWithIndex(this.delegate, pairs);
        }
        return Iterate.zipWithIndex(this.delegate, TreeSortedSet.<Pair<T, Integer>>newSet(Comparators.<T>byFirstOfPair(comparator)));
    }

    public MutableSortedSet<T> union(SetIterable<? extends T> set)
    {
        return this.unionInto(set, TreeSortedSet.<T>newSet(this.comparator()));
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSortedSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.intersectInto(set, TreeSortedSet.<T>newSet(this.comparator()));
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.differenceInto(subtrahendSet, TreeSortedSet.<T>newSet(this.comparator()));
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.symmetricDifferenceInto(setB, TreeSortedSet.<T>newSet(this.comparator()));
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    public Comparator<? super T> comparator()
    {
        return this.delegate.comparator();
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    public MutableSortedSet<SortedSetIterable<T>> powerSet()
    {
        return (MutableSortedSet<SortedSetIterable<T>>) (MutableSortedSet<?>) SortedSetIterables.powerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    public MutableSortedSet<T> subSet(T fromElement, T toElement)
    {
        return SortedSetAdapter.adapt(this.delegate.subSet(fromElement, toElement));
    }

    public MutableSortedSet<T> headSet(T toElement)
    {
        return SortedSetAdapter.adapt(this.delegate.headSet(toElement));
    }

    public MutableSortedSet<T> tailSet(T fromElement)
    {
        return SortedSetAdapter.adapt(this.delegate.tailSet(fromElement));
    }

    public T first()
    {
        if (this.delegate.size() == 0)
        {
            throw new NoSuchElementException();
        }
        return this.delegate.first();
    }

    public T last()
    {
        if (this.delegate.size() == 0)
        {
            throw new NoSuchElementException();
        }
        return this.delegate.last();
    }

    @Override
    public T getFirst()
    {
        return this.first();
    }

    @Override
    public T getLast()
    {
        return this.last();
    }

    public int compareTo(SortedSetIterable<T> o)
    {
        return SortedSetIterables.compare(this, o);
    }
}
