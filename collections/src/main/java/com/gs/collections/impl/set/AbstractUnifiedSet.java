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

package com.gs.collections.impl.set;

import java.util.Collection;
import java.util.Set;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.Pool;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.UnsortedSetIterable;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableByteSet;
import com.gs.collections.api.set.primitive.MutableCharSet;
import com.gs.collections.api.set.primitive.MutableDoubleSet;
import com.gs.collections.api.set.primitive.MutableFloatSet;
import com.gs.collections.api.set.primitive.MutableIntSet;
import com.gs.collections.api.set.primitive.MutableLongSet;
import com.gs.collections.api.set.primitive.MutableShortSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.AbstractRichIterable;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.set.UnifiedSetMultimap;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.mutable.SynchronizedMutableSet;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.mutable.UnmodifiableMutableSet;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.set.mutable.primitive.ByteHashSet;
import com.gs.collections.impl.set.mutable.primitive.CharHashSet;
import com.gs.collections.impl.set.mutable.primitive.DoubleHashSet;
import com.gs.collections.impl.set.mutable.primitive.FloatHashSet;
import com.gs.collections.impl.set.mutable.primitive.IntHashSet;
import com.gs.collections.impl.set.mutable.primitive.LongHashSet;
import com.gs.collections.impl.set.mutable.primitive.ShortHashSet;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.MutableCollectionIterate;
import com.gs.collections.impl.utility.internal.SetIterables;

public abstract class AbstractUnifiedSet<T>
        extends AbstractRichIterable<T>
        implements MutableSet<T>, Pool<T>, BatchIterable<T>
{
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected static final int DEFAULT_INITIAL_CAPACITY = 8;

    protected float loadFactor = DEFAULT_LOAD_FACTOR;

    protected int maxSize;

    protected abstract Object[] getTable();

    protected abstract void allocateTable(int sizeToAllocate);

    protected abstract void rehash(int newCapacity);

    protected abstract T detect(Predicate<? super T> predicate, int start, int end);

    @SuppressWarnings("AbstractMethodOverridesAbstractMethod")
    public abstract MutableSet<T> clone();

    protected abstract boolean shortCircuit(
            Predicate<? super T> predicate,
            boolean expected,
            boolean onShortCircuit,
            boolean atEnd,
            int start,
            int end);

    protected abstract <P> boolean shortCircuitWith(
            Predicate2<? super T, ? super P> predicate2,
            P parameter,
            boolean expected,
            boolean onShortCircuit,
            boolean atEnd);

    protected int init(int initialCapacity)
    {
        int capacity = 1;
        while (capacity < initialCapacity)
        {
            capacity <<= 1;
        }

        return this.allocate(capacity);
    }

    protected int allocate(int capacity)
    {
        this.allocateTable(capacity);
        this.computeMaxSize(capacity);

        return capacity;
    }

    protected void computeMaxSize(int capacity)
    {
        // need at least one free slot for open addressing
        this.maxSize = Math.min(capacity - 1, (int) (capacity * this.loadFactor));
    }

    protected void rehash()
    {
        this.rehash(this.getTable().length << 1);
    }

    protected boolean shortCircuit(
            Predicate<? super T> predicate,
            boolean expected,
            boolean onShortCircuit,
            boolean atEnd)
    {
        return this.shortCircuit(predicate, expected, onShortCircuit, atEnd, 0, this.getTable().length);
    }

    public int getBatchCount(int batchSize)
    {
        return Math.max(1, this.getTable().length / batchSize);
    }

    public boolean removeIf(Predicate<? super T> predicate)
    {
        return IterableIterate.removeIf(this, predicate);
    }

    public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return IterableIterate.removeIfWith(this, predicate, parameter);
    }

    public <V> UnifiedSet<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, UnifiedSet.<V>newSet());
    }

    public MutableBooleanSet collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, new BooleanHashSet());
    }

    public MutableByteSet collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, new ByteHashSet());
    }

    public MutableCharSet collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, new CharHashSet());
    }

    public MutableDoubleSet collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, new DoubleHashSet());
    }

    public MutableFloatSet collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, new FloatHashSet());
    }

    public MutableIntSet collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, new IntHashSet());
    }

    public MutableLongSet collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, new LongHashSet());
    }

    public MutableShortSet collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, new ShortHashSet());
    }

    public <V> UnifiedSet<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, UnifiedSet.<V>newSet());
    }

    public <P, A> UnifiedSet<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.collectWith(function, parameter, UnifiedSet.<A>newSet());
    }

    public <V> UnifiedSet<V> collectIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, UnifiedSet.<V>newSet());
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return this.detect(predicate, 0, this.getTable().length);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return this.shortCircuit(predicate, true, true, false);
    }

    @Override
    public <P> boolean anySatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.shortCircuitWith(predicate, parameter, true, true, false);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return this.shortCircuit(predicate, false, false, true);
    }

    @Override
    public <P> boolean allSatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.shortCircuitWith(predicate, parameter, false, false, true);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return this.shortCircuit(predicate, true, false, true);
    }

    @Override
    public <P> boolean noneSatisfyWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return this.shortCircuitWith(predicate, parameter, true, false, true);
    }

    public <IV, P> IV injectIntoWith(
            IV injectValue,
            final Function3<? super IV, ? super T, ? super P, ? extends IV> function,
            final P parameter)
    {
        return this.injectInto(injectValue, new Function2<IV, T, IV>()
        {
            public IV value(IV argument1, T argument2)
            {
                return function.value(argument1, argument2, parameter);
            }
        });
    }

    public MutableSet<T> asUnmodifiable()
    {
        return UnmodifiableMutableSet.of(this);
    }

    public MutableSet<T> asSynchronized()
    {
        return SynchronizedMutableSet.of(this);
    }

    public boolean addAll(Collection<? extends T> collection)
    {
        return this.addAllIterable(collection);
    }

    public boolean removeAll(Collection<?> collection)
    {
        return this.removeAllIterable(collection);
    }

    public boolean removeAllIterable(Iterable<?> iterable)
    {
        boolean changed = false;
        for (Object each : iterable)
        {
            changed |= this.remove(each);
        }
        return changed;
    }

    public boolean retainAll(Collection<?> collection)
    {
        return this.retainAllIterable(collection);
    }

    public <V> UnifiedSetMultimap<V, T> groupByEach(
            Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, UnifiedSetMultimap.<V, T>newMultimap());
    }

    public <V> MutableMap<V, T> groupByUniqueKey(
            Function<? super T, ? extends V> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V, T>newMap());
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> MutableSet<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, UnifiedSet.<Pair<T, S>>newSet());
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<T, Integer>>newSet());
    }

    public RichIterable<RichIterable<T>> chunk(int size)
    {
        return MutableCollectionIterate.chunk(this, size);
    }

    public MutableSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.unionInto(this, set, this.newEmpty());
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public MutableSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersectInto(this, set, this.newEmpty());
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public MutableSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, this.newEmpty());
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public MutableSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifferenceInto(this, setB, this.newEmpty());
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

    public <K2, V> MutableMap<K2, V> aggregateInPlaceBy(
            Function<? super T, ? extends K2> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K2, V> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<T, K2, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public <K2, V> MutableMap<K2, V> aggregateBy(
            Function<? super T, ? extends K2> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K2, V> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<T, K2, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }
}
