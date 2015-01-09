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

package com.gs.collections.impl.set.immutable;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.set.PartitionImmutableSet;
import com.gs.collections.api.partition.set.PartitionMutableSet;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.set.primitive.ImmutableBooleanSet;
import com.gs.collections.api.set.primitive.ImmutableByteSet;
import com.gs.collections.api.set.primitive.ImmutableCharSet;
import com.gs.collections.api.set.primitive.ImmutableDoubleSet;
import com.gs.collections.api.set.primitive.ImmutableFloatSet;
import com.gs.collections.api.set.primitive.ImmutableIntSet;
import com.gs.collections.api.set.primitive.ImmutableLongSet;
import com.gs.collections.api.set.primitive.ImmutableShortSet;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.MultimapEachPutProcedure;
import com.gs.collections.impl.block.procedure.MultimapPutProcedure;
import com.gs.collections.impl.block.procedure.PartitionPredicate2Procedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.block.procedure.RejectProcedure;
import com.gs.collections.impl.block.procedure.SelectInstancesOfProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.partition.set.PartitionUnifiedSet;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import com.gs.collections.impl.utility.internal.SetIterables;
import net.jcip.annotations.Immutable;

/**
 * This class is the parent class for all ImmutableSets.  All implementations of ImmutableSet must implement the Set
 * interface so anArraySet.equals(anImmutableSet) can return true when the contents and order are the same.
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
            MutableSet<T> result = UnifiedSet.newSet(this);
            result.add(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            MutableSet<T> result = UnifiedSet.newSet(this);
            result.remove(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSet<T> newWithAll(Iterable<? extends T> elements)
    {
        MutableSet<T> result = UnifiedSet.newSet(elements);
        result.addAll(this);
        return result.toImmutable();
    }

    public ImmutableSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        MutableSet<T> result = UnifiedSet.newSet(this);
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public ImmutableSet<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public ImmutableSet<T> select(Predicate<? super T> predicate)
    {
        MutableList<T> intermediateResult = FastList.newList();
        this.forEach(new SelectProcedure<T>(predicate, intermediateResult));
        return Sets.immutable.withAll(intermediateResult);
    }

    public <P> ImmutableSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public ImmutableSet<T> reject(Predicate<? super T> predicate)
    {
        MutableList<T> intermediateResult = FastList.newList();
        this.forEach(new RejectProcedure<T>(predicate, intermediateResult));
        return Sets.immutable.withAll(intermediateResult);
    }

    public <P> ImmutableSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableSet<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableSet<T> partitionUnifiedSet = new PartitionUnifiedSet<T>();
        this.forEach(new PartitionProcedure<T>(predicate, partitionUnifiedSet));
        return partitionUnifiedSet.toImmutable();
    }

    public <P> PartitionImmutableSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableSet<T> partitionUnifiedSet = new PartitionUnifiedSet<T>();
        this.forEach(new PartitionPredicate2Procedure<T, P>(predicate, parameter, partitionUnifiedSet));
        return partitionUnifiedSet.toImmutable();
    }

    public <S> ImmutableSet<S> selectInstancesOf(Class<S> clazz)
    {
        MutableSet<S> result = UnifiedSet.newSet(this.size());
        this.forEach(new SelectInstancesOfProcedure<S>(clazz, result));
        return result.toImmutable();
    }

    public <V> ImmutableSet<V> collect(Function<? super T, ? extends V> function)
    {
        MutableSet<V> result = UnifiedSet.newSet();
        this.forEach(new CollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public ImmutableBooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        MutableBooleanSet result = new BooleanHashSet();
        this.forEach(new CollectBooleanProcedure<T>(booleanFunction, result));
        return result.toImmutable();
    }

    public ImmutableByteSet collectByte(ByteFunction<? super T> byteFunction)
    {
        MutableByteSet result = new ByteHashSet();
        this.forEach(new CollectByteProcedure<T>(byteFunction, result));
        return result.toImmutable();
    }

    public ImmutableCharSet collectChar(CharFunction<? super T> charFunction)
    {
        MutableCharSet result = new CharHashSet(this.size());
        this.forEach(new CollectCharProcedure<T>(charFunction, result));
        return result.toImmutable();
    }

    public ImmutableDoubleSet collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        MutableDoubleSet result = new DoubleHashSet(this.size());
        this.forEach(new CollectDoubleProcedure<T>(doubleFunction, result));
        return result.toImmutable();
    }

    public ImmutableFloatSet collectFloat(FloatFunction<? super T> floatFunction)
    {
        MutableFloatSet result = new FloatHashSet(this.size());
        this.forEach(new CollectFloatProcedure<T>(floatFunction, result));
        return result.toImmutable();
    }

    public ImmutableIntSet collectInt(IntFunction<? super T> intFunction)
    {
        MutableIntSet result = new IntHashSet(this.size());
        this.forEach(new CollectIntProcedure<T>(intFunction, result));
        return result.toImmutable();
    }

    public ImmutableLongSet collectLong(LongFunction<? super T> longFunction)
    {
        MutableLongSet result = new LongHashSet(this.size());
        this.forEach(new CollectLongProcedure<T>(longFunction, result));
        return result.toImmutable();
    }

    public ImmutableShortSet collectShort(ShortFunction<? super T> shortFunction)
    {
        MutableShortSet result = new ShortHashSet(this.size());
        this.forEach(new CollectShortProcedure<T>(shortFunction, result));
        return result.toImmutable();
    }

    public <P, V> ImmutableSet<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public <V> ImmutableSet<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        MutableSet<V> result = UnifiedSet.newSet();
        this.forEach(new CollectIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    public <V> ImmutableSet<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        MutableSet<V> result = UnifiedSet.newSet();
        this.forEach(new FlatCollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public ImmutableSet<T> toImmutable()
    {
        return this;
    }

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

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupBy(Function<? super T, ? extends V> function, R target)
    {
        this.forEach(MultimapPutProcedure.on(target, function));
        return target;
    }

    public <V> ImmutableSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, UnifiedSetMultimap.<V, T>newMultimap()).toImmutable();
    }

    @Override
    public <V, R extends MutableMultimap<V, T>> R groupByEach(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        this.forEach(MultimapEachPutProcedure.on(target, function));
        return target;
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> ImmutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, UnifiedSet.<Pair<T, S>>newSet()).toImmutable();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
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

    public ParallelUnsortedSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return this.toSet().asParallel(executorService, batchSize);
    }
}
