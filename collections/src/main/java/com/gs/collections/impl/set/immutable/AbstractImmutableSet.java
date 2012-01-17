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

package com.gs.collections.impl.set.immutable;

import java.util.Iterator;
import java.util.Set;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.FilterNotProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.partition.set.PartitionUnifiedSet;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.internal.SetIterables;
import net.jcip.annotations.Immutable;

/**
 * This class is the parent class for all ImmutableLists.  All implementations of ImmutableList must implement the List
 * interface so anArrayList.equals(anImmutableList) can return true when the contents and order are the same.
 */
@Immutable
public abstract class AbstractImmutableSet<T> extends AbstractImmutableCollection<T>
        implements ImmutableSet<T>, Set<T>
{
    public Set<T> castToSet()
    {
        return this;
    }

    protected int nullSafeHashCode(Object element)
    {
        return element == null ? 0 : element.hashCode();
    }

    public ImmutableSet<T> newWith(T element)
    {
        if (!this.contains(element))
        {
            UnifiedSet<T> result = UnifiedSet.newSet(this);
            result.add(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            UnifiedSet<T> result = UnifiedSet.newSet(this);
            result.remove(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSet<T> newWithAll(Iterable<? extends T> elements)
    {
        UnifiedSet<T> result = UnifiedSet.newSet(elements);
        result.addAll(this);
        return result.toImmutable();
    }

    public ImmutableSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        UnifiedSet<T> result = UnifiedSet.newSet(this);
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public abstract T getFirst();

    public abstract T getLast();

    public ImmutableSet<T> filter(Predicate<? super T> predicate)
    {
        UnifiedSet<T> result = UnifiedSet.newSet();
        this.forEach(new SelectProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public ImmutableSet<T> filterNot(Predicate<? super T> predicate)
    {
        UnifiedSet<T> result = UnifiedSet.newSet();
        this.forEach(new FilterNotProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public PartitionImmutableSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionUnifiedSet.of(this, predicate).toImmutable();
    }

    public <V> ImmutableSet<V> transform(Function<? super T, ? extends V> function)
    {
        UnifiedSet<V> result = UnifiedSet.newSet();
        this.forEach(new CollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <V> ImmutableSet<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        UnifiedSet<V> result = UnifiedSet.newSet();
        this.forEach(new CollectIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    public <V> ImmutableSet<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        UnifiedSet<V> result = UnifiedSet.newSet();
        this.forEach(new FlatCollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public abstract Iterator<T> iterator();

    protected abstract class ImmutableSetIterator
            implements Iterator<T>
    {
        private int next;    // next entry to return, defaults to 0

        protected abstract T getElement(int i);

        public boolean hasNext()
        {
            return this.next < AbstractImmutableSet.this.size();
        }

        public T next()
        {
            return this.getElement(this.next++);
        }

        public void remove()
        {
            throw new UnsupportedOperationException("Cannot remove from an ImmutableSet");
        }
    }

    public <V> ImmutableSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, UnifiedSetMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    public <V> ImmutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, UnifiedSetMultimap.<V, T>newMultimap()).toImmutable();
    }

    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    public <S> ImmutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, UnifiedSet.<Pair<T, S>>newSet()).toImmutable();
    }

    public ImmutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<T, Integer>>newSet()).toImmutable();
    }

    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return UnifiedSet.newSet(size);
    }

    public ImmutableSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.union(this, set).toImmutable();
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public ImmutableSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersect(this, set).toImmutable();
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public ImmutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.difference(this, subtrahendSet).toImmutable();
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public ImmutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifference(this, setB).toImmutable();
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

    public ImmutableSet<UnsortedSetIterable<T>> powerSet()
    {
        return (ImmutableSet<UnsortedSetIterable<T>>) (ImmutableSet<?>) SetIterables.immutablePowerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }
}
