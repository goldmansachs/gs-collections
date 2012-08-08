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

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.webguys.ponzu.api.LazyIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.partition.set.PartitionMutableSet;
import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.set.SetIterable;
import com.webguys.ponzu.api.set.UnsortedSetIterable;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.collection.mutable.AbstractCollectionAdapter;
import com.webguys.ponzu.impl.factory.Sets;
import com.webguys.ponzu.impl.multimap.set.UnifiedSetMultimap;
import com.webguys.ponzu.impl.partition.set.PartitionUnifiedSet;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.Iterate;
import com.webguys.ponzu.impl.utility.internal.SetIterables;
import com.webguys.ponzu.impl.utility.internal.SetIterate;

/**
 * This class provides a MutableSet wrapper around a JDK Collections Set interface instance.  All of the MutableSet
 * interface methods are supported in addition to the JDK Set interface methods.
 * <p/>
 * To create a new wrapper around an existing Set instance, use the {@link #adapt(Set)} factory method.
 */
public final class SetAdapter<T>
        extends AbstractCollectionAdapter<T>
        implements Serializable, MutableSet<T>
{
    private static final long serialVersionUID = 1L;
    private final Set<T> delegate;

    SetAdapter(Set<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("SetAdapter may not wrap null");
        }
        this.delegate = newDelegate;
    }

    @Override
    protected Set<T> getDelegate()
    {
        return this.delegate;
    }

    public MutableSet<T> asUnmodifiable()
    {
        return UnmodifiableMutableSet.of(this);
    }

    public MutableSet<T> asSynchronized()
    {
        return SynchronizedMutableSet.of(this);
    }

    public ImmutableSet<T> toImmutable()
    {
        return Sets.immutable.ofAll(this.delegate);
    }

    public static <E> MutableSet<E> adapt(Set<E> set)
    {
        if (set instanceof MutableSet)
        {
            return (MutableSet<E>) set;
        }
        return new SetAdapter<E>(set);
    }

    @Override
    public MutableSet<T> clone()
    {
        return UnifiedSet.newSet(this.delegate);
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

    public SetAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    public SetAdapter<T> with(T element1, T element2)
    {
        this.add(element1);
        this.add(element2);
        return this;
    }

    public SetAdapter<T> with(T element1, T element2, T element3)
    {
        this.add(element1);
        this.add(element2);
        this.add(element3);
        return this;
    }

    public SetAdapter<T> with(T... elements)
    {
        ArrayIterate.forEach(elements, CollectionAddProcedure.on(this.delegate));
        return this;
    }

    public SetAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    public SetAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    public SetAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    /**
     * @deprecated use {@link UnifiedSet#newSet()} instead (inlineable)
     */
    @Deprecated
    public MutableSet<T> newEmpty()
    {
        return UnifiedSet.newSet();
    }

    @Override
    public MutableSet<T> filter(Predicate<? super T> predicate)
    {
        return Iterate.filter(this.delegate, predicate, UnifiedSet.<T>newSet());
    }

    @Override
    public MutableSet<T> filterNot(Predicate<? super T> predicate)
    {
        return Iterate.filterNot(this.delegate, predicate, UnifiedSet.<T>newSet());
    }

    @Override
    public PartitionMutableSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionUnifiedSet.of(this, predicate);
    }

    @Override
    public <V> MutableSet<V> transform(Function<? super T, ? extends V> function)
    {
        return Iterate.transform(this.delegate, function, UnifiedSet.<V>newSet());
    }

    @Override
    public <V> MutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.flatTransform(this.delegate, function, UnifiedSet.<V>newSet());
    }

    @Override
    public <V> MutableSet<V> transformIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return Iterate.tranformIf(this.delegate, predicate, function, UnifiedSet.<V>newSet());
    }

    @Override
    public <V> UnifiedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return Iterate.groupBy(this.delegate, function, UnifiedSetMultimap.<V, T>newMultimap());
    }

    @Override
    public <V> UnifiedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.groupByEach(this.delegate, function, UnifiedSetMultimap.<V, T>newMultimap());
    }

    @Override
    public <P> MutableSet<T> filterWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.filterWith(this.delegate, predicate, parameter, UnifiedSet.<T>newSet());
    }

    @Override
    public <P> MutableSet<T> filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.filterNotWith(this.delegate, predicate, parameter, UnifiedSet.<T>newSet());
    }

    @Override
    public <P, V> MutableSet<V> transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return Iterate.transformWith(this.delegate, function, parameter, UnifiedSet.<V>newSet());
    }

    @Override
    public <S> MutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return Iterate.zip(this, that, UnifiedSet.<Pair<T, S>>newSet());
    }

    @Override
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return Iterate.zipWithIndex(this, UnifiedSet.<Pair<T, Integer>>newSet());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return SetIterate.removeAllIterable(this, iterable);
    }

    public MutableSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.union(this, set);
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersect(this, set);
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.difference(this, subtrahendSet);
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifference(this, setB);
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

    public MutableSet<UnsortedSetIterable<T>> powerSet()
    {
        return (MutableSet<UnsortedSetIterable<T>>) (MutableSet<?>) SetIterables.powerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }
}
