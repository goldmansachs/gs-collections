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

package com.gs.collections.impl.map.immutable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.ImmutableByteBag;
import com.gs.collections.api.bag.primitive.ImmutableCharBag;
import com.gs.collections.api.bag.primitive.ImmutableDoubleBag;
import com.gs.collections.api.bag.primitive.ImmutableFloatBag;
import com.gs.collections.api.bag.primitive.ImmutableIntBag;
import com.gs.collections.api.bag.primitive.ImmutableLongBag;
import com.gs.collections.api.bag.primitive.ImmutableShortBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.bag.PartitionImmutableBag;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.PartitionPredicate2Procedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.block.procedure.SelectInstancesOfProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.map.AbstractMapIterable;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.partition.bag.PartitionHashBag;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

@Immutable
public abstract class AbstractImmutableMap<K, V>
        extends AbstractMapIterable<K, V>
        implements ImmutableMap<K, V>, Map<K, V>
{
    public Map<K, V> castToMap()
    {
        return this;
    }

    public MutableMap<K, V> toMap()
    {
        return UnifiedMap.newMap(this);
    }

    public ImmutableMap<K, V> toImmutable()
    {
        return this;
    }

    public Iterator<V> iterator()
    {
        return this.valuesView().iterator();
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("Cannot call putAll() on " + this.getClass().getSimpleName());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot call clear() on " + this.getClass().getSimpleName());
    }

    public ImmutableSetMultimap<V, K> flip()
    {
        return MapIterate.flip(this).toImmutable();
    }

    public Set<Entry<K, V>> entrySet()
    {
        final MutableSet<Entry<K, V>> set = UnifiedSet.newSet(this.size());
        this.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K key, V value)
            {
                set.add(ImmutableEntry.of(key, value));
            }
        });
        return set.toImmutable().castToSet();
    }

    public ImmutableMap<K, V> newWithKeyValue(K key, V value)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        map.put(key, value);
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValues)
        {
            map.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValuePairs)
        {
            map.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithoutKey(K key)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        map.removeKey(key);
        return map.toImmutable();
    }

    public ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys)
    {
        UnifiedMap<K, V> map = UnifiedMap.newMap(this);
        for (K key : keys)
        {
            map.removeKey(key);
        }
        return map.toImmutable();
    }

    public V put(K key, V value)
    {
        throw new UnsupportedOperationException("Cannot call put() on " + this.getClass().getSimpleName());
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public ImmutableMap<V, K> flipUniqueValues()
    {
        return MapIterate.flipUniqueValues(this).toImmutable();
    }

    public <K2, V2> ImmutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        UnifiedMap<K2, V2> result = MapIterate.collect(this, function, UnifiedMap.<K2, V2>newMap());
        return result.toImmutable();
    }

    public <R> ImmutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        UnifiedMap<K, R> result = MapIterate.collectValues(this, function, UnifiedMap.<K, R>newMap(this.size()));
        return result.toImmutable();
    }

    public ImmutableMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        UnifiedMap<K, V> result = MapIterate.selectMapOnEntry(this, predicate, UnifiedMap.<K, V>newMap());
        return result.toImmutable();
    }

    public ImmutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        UnifiedMap<K, V> result = MapIterate.rejectMapOnEntry(this, predicate, UnifiedMap.<K, V>newMap());
        return result.toImmutable();
    }

    public Pair<K, V> detect(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.detect(this, predicate);
    }

    public <R> ImmutableBag<R> collect(Function<? super V, ? extends R> function)
    {
        return this.collect(function, new HashBag<R>()).toImmutable();
    }

    public <P, VV> ImmutableBag<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public ImmutableBooleanBag collectBoolean(BooleanFunction<? super V> booleanFunction)
    {
        MutableBooleanBag result = new BooleanHashBag();
        this.forEach(new CollectBooleanProcedure<V>(booleanFunction, result));
        return result.toImmutable();
    }

    public ImmutableByteBag collectByte(ByteFunction<? super V> byteFunction)
    {
        MutableByteBag result = new ByteHashBag();
        this.forEach(new CollectByteProcedure<V>(byteFunction, result));
        return result.toImmutable();
    }

    public ImmutableCharBag collectChar(CharFunction<? super V> charFunction)
    {
        MutableCharBag result = new CharHashBag();
        this.forEach(new CollectCharProcedure<V>(charFunction, result));
        return result.toImmutable();
    }

    public ImmutableDoubleBag collectDouble(DoubleFunction<? super V> doubleFunction)
    {
        MutableDoubleBag result = new DoubleHashBag();
        this.forEach(new CollectDoubleProcedure<V>(doubleFunction, result));
        return result.toImmutable();
    }

    public ImmutableFloatBag collectFloat(FloatFunction<? super V> floatFunction)
    {
        MutableFloatBag result = new FloatHashBag();
        this.forEach(new CollectFloatProcedure<V>(floatFunction, result));
        return result.toImmutable();
    }

    public ImmutableIntBag collectInt(IntFunction<? super V> intFunction)
    {
        MutableIntBag result = new IntHashBag();
        this.forEach(new CollectIntProcedure<V>(intFunction, result));
        return result.toImmutable();
    }

    public ImmutableLongBag collectLong(LongFunction<? super V> longFunction)
    {
        MutableLongBag result = new LongHashBag();
        this.forEach(new CollectLongProcedure<V>(longFunction, result));
        return result.toImmutable();
    }

    public ImmutableShortBag collectShort(ShortFunction<? super V> shortFunction)
    {
        MutableShortBag result = new ShortHashBag();
        this.forEach(new CollectShortProcedure<V>(shortFunction, result));
        return result.toImmutable();
    }

    public <R> ImmutableBag<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.collectIf(predicate, function, new HashBag<R>()).toImmutable();
    }

    public <R> ImmutableBag<R> flatCollect(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatCollect(function, new HashBag<R>()).toImmutable();
    }

    public ImmutableBag<V> select(Predicate<? super V> predicate)
    {
        return this.select(predicate, new HashBag<V>()).toImmutable();
    }

    public <P> ImmutableBag<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public ImmutableMap<K, V> tap(Procedure<? super V> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public ImmutableBag<V> reject(Predicate<? super V> predicate)
    {
        return this.reject(predicate, new HashBag<V>()).toImmutable();
    }

    public <P> ImmutableBag<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableBag<V> partition(Predicate<? super V> predicate)
    {
        PartitionMutableBag<V> partitionMutableBag = new PartitionHashBag<V>();
        this.forEach(new PartitionProcedure<V>(predicate, partitionMutableBag));
        return partitionMutableBag.toImmutable();
    }

    public <P> PartitionImmutableBag<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        PartitionMutableBag<V> partitionMutableBag = new PartitionHashBag<V>();
        this.forEach(new PartitionPredicate2Procedure<V, P>(predicate, parameter, partitionMutableBag));
        return partitionMutableBag.toImmutable();
    }

    public <S> ImmutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        MutableBag<S> result = HashBag.newBag();
        this.forEach(new SelectInstancesOfProcedure<S>(clazz, result));
        return result.toImmutable();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zip(Iterable)} instead.
     */
    @Deprecated
    public <S> ImmutableBag<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, HashBag.<Pair<V, S>>newBag(this.size())).toImmutable();
    }

    /**
     * @deprecated in 6.0. Use {@link OrderedIterable#zipWithIndex()} instead.
     */
    @Deprecated
    public ImmutableSet<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(UnifiedSet.<Pair<V, Integer>>newSet(this.size())).toImmutable();
    }

    public <VV> ImmutableBagMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.groupBy(function, HashBagMultimap.<VV, V>newMultimap()).toImmutable();
    }

    public <VV> ImmutableBagMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.groupByEach(function, HashBagMultimap.<VV, V>newMultimap()).toImmutable();
    }

    public <V1> ImmutableMap<V1, V> groupByUniqueKey(Function<? super V, ? extends V1> function)
    {
        return this.groupByUniqueKey(function, UnifiedMap.<V1, V>newMap()).toImmutable();
    }

    public <K2, V2> ImmutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator)
    {
        MutableMap<K2, V2> map = UnifiedMap.newMap();
        this.forEach(new MutatingAggregationProcedure<V, K2, V2>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map.toImmutable();
    }

    public <K2, V2> ImmutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator)
    {
        MutableMap<K2, V2> map = UnifiedMap.newMap();
        this.forEach(new NonMutatingAggregationProcedure<V, K2, V2>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map.toImmutable();
    }
}
