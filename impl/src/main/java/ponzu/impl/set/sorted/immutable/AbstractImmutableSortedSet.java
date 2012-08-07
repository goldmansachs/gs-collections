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

package ponzu.impl.set.sorted.immutable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import net.jcip.annotations.Immutable;
import ponzu.api.LazyIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.ImmutableList;
import ponzu.api.list.MutableList;
import ponzu.api.multimap.MutableMultimap;
import ponzu.api.multimap.sortedset.ImmutableSortedSetMultimap;
import ponzu.api.partition.set.sorted.PartitionImmutableSortedSet;
import ponzu.api.set.SetIterable;
import ponzu.api.set.sorted.ImmutableSortedSet;
import ponzu.api.set.sorted.SortedSetIterable;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.Comparators;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.block.procedure.FilterNotProcedure;
import ponzu.impl.block.procedure.FilterProcedure;
import ponzu.impl.block.procedure.FlatTransformProcedure;
import ponzu.impl.block.procedure.MultimapEachPutProcedure;
import ponzu.impl.block.procedure.MultimapPutProcedure;
import ponzu.impl.block.procedure.TransformIfProcedure;
import ponzu.impl.block.procedure.TransformProcedure;
import ponzu.impl.collection.immutable.AbstractImmutableCollection;
import ponzu.impl.factory.Lists;
import ponzu.impl.multimap.set.sorted.TreeSortedSetMultimap;
import ponzu.impl.partition.set.sorted.PartitionTreeSortedSet;
import ponzu.impl.set.sorted.mutable.TreeSortedSet;
import ponzu.impl.utility.Iterate;
import ponzu.impl.utility.internal.SetIterables;
import ponzu.impl.utility.internal.SortedSetIterables;

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

    public ImmutableSortedSet<T> filter(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new FilterProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public ImmutableSortedSet<T> filterNot(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new FilterNotProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public PartitionImmutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        return PartitionTreeSortedSet.of(this, predicate).toImmutable();
    }

    public <V> ImmutableList<V> transform(Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new TransformProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new TransformIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        MutableList<V> result = Lists.mutable.of();
        this.forEach(new FlatTransformProcedure<T, V>(function, result));
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
