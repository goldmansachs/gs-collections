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

package com.gs.collections.impl.set.sorted.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.SortedSets;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A synchronized view of a SortedSet.
 *
 * @see MutableSortedSet#asSynchronized()
 */
@ThreadSafe
public class SynchronizedSortedSet<E>
        extends SynchronizedMutableCollection<E>
        implements MutableSortedSet<E>
{
    private static final long serialVersionUID = 1L;

    public SynchronizedSortedSet(MutableSortedSet<E> set)
    {
        super(set);
    }

    public SynchronizedSortedSet(MutableSortedSet<E> set, Object newLock)
    {
        super(set, newLock);
    }

    /**
     * This method will take a MutableSortedSet and wrap it directly in a SynchronizedSortedSet.  It will
     * take any other non-GS-collection and first adapt it will a SortedSetAdapter, and then return a
     * SynchronizedSortedSet that wraps the adapter.
     */
    public static <E, S extends SortedSet<E>> SynchronizedSortedSet<E> of(S set)
    {
        return new SynchronizedSortedSet<E>(SortedSetAdapter.adapt(set));
    }

    /**
     * This method will take a MutableSortedSet and wrap it directly in a SynchronizedSortedSet.  It will
     * take any other non-GS-collection and first adapt it will a SortedSetAdapter, and then return a
     * SynchronizedSortedSet that wraps the adapter.  Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, S extends SortedSet<E>> MutableSortedSet<E> of(S set, Object lock)
    {
        return new SynchronizedSortedSet<E>(SortedSetAdapter.adapt(set), lock);
    }

    @GuardedBy("getLock()")
    private MutableSortedSet<E> getSortedSet()
    {
        return (MutableSortedSet<E>) this.getCollection();
    }

    @Override
    public MutableSortedSet<E> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableSortedSet.of(this);
        }
    }

    @Override
    public ImmutableSortedSet<E> toImmutable()
    {
        synchronized (this.getLock())
        {
            return SortedSets.immutable.ofSortedSet(this.getSortedSet());
        }
    }

    @Override
    public MutableSortedSet<E> asSynchronized()
    {
        return this;
    }

    @Override
    public MutableSortedSet<E> clone()
    {
        synchronized (this.getLock())
        {
            return of(this.getSortedSet().clone());
        }
    }

    @Override
    public <V> MutableList<V> transform(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().transform(function);
        }
    }

    @Override
    public <V> MutableList<V> flatTransform(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().flatTransform(function);
        }
    }

    @Override
    public <V> MutableList<V> transformIf(
            Predicate<? super E> predicate,
            Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().transformIf(predicate, function);
        }
    }

    @Override
    public <P, V> MutableList<V> transformWith(Function2<? super E, ? super P, ? extends V> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().transformWith(function, parameter);
        }
    }

    @Override
    public <V> MutableSortedSetMultimap<V, E> groupBy(Function<? super E, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().groupBy(function);
        }
    }

    @Override
    public <V> MutableSortedSetMultimap<V, E> groupByEach(Function<? super E, ? extends Iterable<V>> function)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().groupByEach(function);
        }
    }

    @Override
    public MutableSortedSet<E> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().newEmpty();
        }
    }

    @Override
    public MutableSortedSet<E> filterNot(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().filterNot(predicate);
        }
    }

    @Override
    public <P> MutableSortedSet<E> filterNotWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().filterNotWith(predicate, parameter);
        }
    }

    @Override
    public MutableSortedSet<E> filter(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().filter(predicate);
        }
    }

    @Override
    public <P> MutableSortedSet<E> filterWith(Predicate2<? super E, ? super P> predicate, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().filterWith(predicate, parameter);
        }
    }

    @Override
    public PartitionMutableSortedSet<E> partition(Predicate<? super E> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().partition(predicate);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().equals(obj);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().hashCode();
        }
    }

    @Override
    public <S> MutableSortedSet<Pair<E, S>> zip(Iterable<S> that)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().zip(that);
        }
    }

    @Override
    public <S, R extends Collection<Pair<E, S>>> R zip(Iterable<S> that, R target)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().zip(that, target);
        }
    }

    @Override
    public MutableSortedSet<Pair<E, Integer>> zipWithIndex()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().zipWithIndex();
        }
    }

    @Override
    public <R extends Collection<Pair<E, Integer>>> R zipWithIndex(R target)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().zipWithIndex(target);
        }
    }

    public Comparator<? super E> comparator()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().comparator();
        }
    }

    public MutableSortedSet<E> union(SetIterable<? extends E> set)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().union(set);
        }
    }

    public <R extends Set<E>> R unionInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().unionInto(set, targetSet);
        }
    }

    public MutableSortedSet<E> intersect(SetIterable<? extends E> set)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().intersect(set);
        }
    }

    public <R extends Set<E>> R intersectInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().intersectInto(set, targetSet);
        }
    }

    public MutableSortedSet<E> difference(SetIterable<? extends E> subtrahendSet)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().difference(subtrahendSet);
        }
    }

    public <R extends Set<E>> R differenceInto(SetIterable<? extends E> subtrahendSet, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().differenceInto(subtrahendSet, targetSet);
        }
    }

    public MutableSortedSet<E> symmetricDifference(SetIterable<? extends E> setB)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().symmetricDifference(setB);
        }
    }

    public <R extends Set<E>> R symmetricDifferenceInto(SetIterable<? extends E> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().symmetricDifferenceInto(set, targetSet);
        }
    }

    public boolean isSubsetOf(SetIterable<? extends E> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().isSubsetOf(candidateSuperset);
        }
    }

    public boolean isProperSubsetOf(SetIterable<? extends E> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().isProperSubsetOf(candidateSuperset);
        }
    }

    public MutableSortedSet<SortedSetIterable<E>> powerSet()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().powerSet();
        }
    }

    public <B> LazyIterable<Pair<E, B>> cartesianProduct(SetIterable<B> set)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().cartesianProduct(set);
        }
    }

    public MutableSortedSet<E> subSet(E fromElement, E toElement)
    {
        synchronized (this.getLock())
        {
            return of(this.getSortedSet().subSet(fromElement, toElement), this.getLock());
        }
    }

    public MutableSortedSet<E> headSet(E toElement)
    {
        synchronized (this.getLock())
        {
            return of(this.getSortedSet().headSet(toElement), this.getLock());
        }
    }

    public MutableSortedSet<E> tailSet(E fromElement)
    {
        synchronized (this.getLock())
        {
            return of(this.getSortedSet().tailSet(fromElement), this.getLock());
        }
    }

    public E first()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().first();
        }
    }

    public E last()
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().last();
        }
    }

    public int compareTo(SortedSetIterable<E> o)
    {
        synchronized (this.getLock())
        {
            return this.getSortedSet().compareTo(o);
        }
    }

    @Override
    public MutableSortedSet<E> with(E element)
    {
        this.add(element);
        return this;
    }

    @Override
    public MutableSortedSet<E> without(E element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public MutableSortedSet<E> withAll(Iterable<? extends E> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public MutableSortedSet<E> withoutAll(Iterable<? extends E> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }
}
