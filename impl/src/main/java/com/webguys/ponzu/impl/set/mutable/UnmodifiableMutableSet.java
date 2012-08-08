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

package com.webguys.ponzu.impl.set.mutable;

import java.util.Collection;
import java.util.Set;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.multimap.set.MutableSetMultimap;
import com.webguys.ponzu.api.partition.set.PartitionMutableSet;
import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.set.UnsortedSetIterable;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.collection.mutable.UnmodifiableMutableCollection;
import com.webguys.ponzu.impl.factory.Sets;

/**
 * An unmodifiable view of a list.
 *
 * @see MutableSet#asUnmodifiable()
 */
public class UnmodifiableMutableSet<T>
        extends UnmodifiableMutableCollection<T>
        implements MutableSet<T>
{
    private static final long serialVersionUID = 1L;

    protected UnmodifiableMutableSet(MutableSet<? extends T> mutableSet)
    {
        super(mutableSet);
    }

    /**
     * This method will take a MutableSet and wrap it directly in a UnmodifiableMutableSet.  It will
     * take any other non-GS-set and first adapt it will a SetAdapter, and then return a
     * UnmodifiableMutableSet that wraps the adapter.
     */
    public static <E, S extends Set<E>> UnmodifiableMutableSet<E> of(S set)
    {
        if (set == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableMutableSet for null");
        }
        return new UnmodifiableMutableSet<E>(SetAdapter.adapt(set));
    }

    protected MutableSet<T> getMutableSet()
    {
        return (MutableSet<T>) this.getMutableCollection();
    }

    @Override
    public MutableSet<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableSet<T> asSynchronized()
    {
        return SynchronizedMutableSet.of(this);
    }

    @Override
    public ImmutableSet<T> toImmutable()
    {
        return Sets.immutable.ofAll(this.getMutableSet());
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getMutableSet().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getMutableSet().hashCode();
    }

    @Override
    public UnmodifiableMutableSet<T> clone()
    {
        return this;
    }

    @Override
    public MutableSet<T> newEmpty()
    {
        return this.getMutableSet().newEmpty();
    }

    @Override
    public MutableSet<T> filter(Predicate<? super T> predicate)
    {
        return this.getMutableSet().filter(predicate);
    }

    @Override
    public <P> MutableSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableSet().filterWith(predicate, parameter);
    }

    @Override
    public MutableSet<T> filterNot(Predicate<? super T> predicate)
    {
        return this.getMutableSet().filterNot(predicate);
    }

    @Override
    public <P> MutableSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableSet().filterNotWith(predicate, parameter);
    }

    @Override
    public PartitionMutableSet<T> partition(Predicate<? super T> predicate)
    {
        return this.getMutableSet().partition(predicate);
    }

    @Override
    public <V> MutableSet<V> transform(Function<? super T, ? extends V> function)
    {
        return this.getMutableSet().transform(function);
    }

    @Override
    public <V> MutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableSet().flatTransform(function);
    }

    @Override
    public <P, A> MutableSet<A> transformWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getMutableSet().transformWith(function, parameter);
    }

    @Override
    public <V> MutableSet<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getMutableSet().transformIf(predicate, function);
    }

    @Override
    public <V> MutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableSet().groupBy(function);
    }

    @Override
    public <V> MutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableSet().groupByEach(function);
    }

    @Override
    public <S> MutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getMutableSet().zip(that);
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getMutableSet().zip(that, target);
    }

    @Override
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.getMutableSet().zipWithIndex();
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.getMutableSet().zipWithIndex(target);
    }

    public MutableSet<T> union(SetIterable<? extends T> set)
    {
        return this.getMutableSet().union(set);
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getMutableSet().unionInto(set, targetSet);
    }

    public MutableSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.getMutableSet().intersect(set);
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getMutableSet().intersectInto(set, targetSet);
    }

    public MutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.getMutableSet().difference(subtrahendSet);
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return this.getMutableSet().differenceInto(subtrahendSet, targetSet);
    }

    public MutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.getMutableSet().symmetricDifference(setB);
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getMutableSet().symmetricDifferenceInto(set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getMutableSet().isSubsetOf(candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getMutableSet().isProperSubsetOf(candidateSuperset);
    }

    public MutableSet<UnsortedSetIterable<T>> powerSet()
    {
        return this.getMutableSet().powerSet();
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return this.getMutableSet().cartesianProduct(set);
    }

    @Override
    public MutableSet<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSet<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSet<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableSet<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }
}
