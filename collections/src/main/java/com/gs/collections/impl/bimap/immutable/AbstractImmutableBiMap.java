/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.bimap.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.bimap.ImmutableBiMap;
import com.gs.collections.api.bimap.MutableBiMap;
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
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.ImmutableByteCollection;
import com.gs.collections.api.collection.primitive.ImmutableCharCollection;
import com.gs.collections.api.collection.primitive.ImmutableDoubleCollection;
import com.gs.collections.api.collection.primitive.ImmutableFloatCollection;
import com.gs.collections.api.collection.primitive.ImmutableIntCollection;
import com.gs.collections.api.collection.primitive.ImmutableLongCollection;
import com.gs.collections.api.collection.primitive.ImmutableShortCollection;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bimap.AbstractBiMap;
import com.gs.collections.impl.bimap.mutable.HashBiMap;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.utility.MapIterate;

public abstract class AbstractImmutableBiMap<K, V> extends AbstractBiMap<K, V> implements ImmutableBiMap<K, V>, Map<K, V>
{
    private final ImmutableMap<K, V> delegate;
    private final AbstractImmutableBiMap<V, K> inverse;

    private AbstractImmutableBiMap(ImmutableMap<K, V> delegate, AbstractImmutableBiMap<V, K> valuesToKeys)
    {
        this.delegate = delegate;
        this.inverse = valuesToKeys;
    }

    AbstractImmutableBiMap(ImmutableMap<K, V> map)
    {
        this.delegate = map;
        this.inverse = new Inverse<V, K>(map.flipUniqueValues(), this);
    }

    AbstractImmutableBiMap(MutableBiMap<K, V> biMap)
    {
        this.delegate = Maps.immutable.ofAll(biMap);
        this.inverse = new Inverse<V, K>(Maps.immutable.ofAll(biMap.inverse()), this);
    }

    @Override
    protected ImmutableMap<K, V> getDelegate()
    {
        return this.delegate;
    }

    @Override
    protected ImmutableMap<V, K> getInverse()
    {
        return this.inverse.delegate;
    }

    public ImmutableBiMap<K, V> newWithKeyValue(K key, V value)
    {
        HashBiMap<K, V> map = new HashBiMap<K, V>(this.delegate.castToMap());
        map.put(key, value);
        return map.toImmutable();
    }

    public ImmutableBiMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        HashBiMap<K, V> map = new HashBiMap<K, V>(this.delegate.castToMap());
        for (Pair<? extends K, ? extends V> keyValuePair : keyValues)
        {
            map.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return map.toImmutable();
    }

    public ImmutableBiMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        return this.newWithAllKeyValues(ArrayAdapter.adapt(keyValuePairs));
    }

    public ImmutableBiMap<K, V> newWithoutKey(K key)
    {
        HashBiMap<K, V> map = new HashBiMap<K, V>(this.delegate.castToMap());
        map.removeKey(key);
        return map.toImmutable();
    }

    public ImmutableBiMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys)
    {
        HashBiMap<K, V> map = new HashBiMap<K, V>(this.delegate.castToMap());
        for (K key : keys)
        {
            map.removeKey(key);
        }
        return map.toImmutable();
    }

    public ImmutableBiMap<V, K> inverse()
    {
        return this.inverse;
    }

    public ImmutableSetMultimap<V, K> flip()
    {
        // TODO: We could optimize this since we know the values are unique
        return MapIterate.flip(this).toImmutable();
    }

    public ImmutableBiMap<V, K> flipUniqueValues()
    {
        return this.inverse();
    }

    public V put(K key, V value)
    {
        throw new UnsupportedOperationException("Cannot call put() on " + this.getClass().getSimpleName());
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("Cannot call putAll() on " + this.getClass().getSimpleName());
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot call clear() on " + this.getClass().getSimpleName());
    }

    public Set<K> keySet()
    {
        return this.delegate.castToMap().keySet();
    }

    public Collection<V> values()
    {
        return this.delegate.castToMap().values();
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.delegate.castToMap().entrySet();
    }

    public Iterator<V> iterator()
    {
        return this.delegate.iterator();
    }

    public ImmutableBiMap<K, V> toImmutable()
    {
        return this;
    }

    public Map<K, V> castToMap()
    {
        return this;
    }

    public MutableMap<K, V> toMap()
    {
        return this.getDelegate().toMap();
    }

    public <K2, V2> ImmutableBiMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        ImmutableMap<K2, V2> result = this.delegate.collect(function);
        return new ImmutableHashBiMap<K2, V2>(result);
    }

    public <VV> ImmutableCollection<VV> collect(Function<? super V, ? extends VV> function)
    {
        return this.delegate.collect(function);
    }

    public <R> ImmutableBiMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        ImmutableMap<K, R> result = this.delegate.collectValues(function);
        return new ImmutableHashBiMap<K, R>(result);
    }

    public ImmutableBooleanCollection collectBoolean(BooleanFunction<? super V> booleanFunction)
    {
        return this.delegate.collectBoolean(booleanFunction);
    }

    public ImmutableByteCollection collectByte(ByteFunction<? super V> byteFunction)
    {
        return this.delegate.collectByte(byteFunction);
    }

    public ImmutableCharCollection collectChar(CharFunction<? super V> charFunction)
    {
        return this.delegate.collectChar(charFunction);
    }

    public ImmutableDoubleCollection collectDouble(DoubleFunction<? super V> doubleFunction)
    {
        return this.delegate.collectDouble(doubleFunction);
    }

    public ImmutableFloatCollection collectFloat(FloatFunction<? super V> floatFunction)
    {
        return this.delegate.collectFloat(floatFunction);
    }

    public ImmutableIntCollection collectInt(IntFunction<? super V> intFunction)
    {
        return this.delegate.collectInt(intFunction);
    }

    public ImmutableLongCollection collectLong(LongFunction<? super V> longFunction)
    {
        return this.delegate.collectLong(longFunction);
    }

    public ImmutableShortCollection collectShort(ShortFunction<? super V> shortFunction)
    {
        return this.delegate.collectShort(shortFunction);
    }

    public <P, VV> ImmutableCollection<VV> collectWith(Function2<? super V, ? super P, ? extends VV> function, P parameter)
    {
        return this.delegate.collectWith(function, parameter);
    }

    public <VV> ImmutableCollection<VV> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends VV> function)
    {
        return this.delegate.collectIf(predicate, function);
    }

    public <VV> ImmutableCollection<VV> flatCollect(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.delegate.flatCollect(function);
    }

    public ImmutableBiMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.selectMapOnEntry(this, predicate, HashBiMap.<K, V>newMap()).toImmutable();
    }

    public ImmutableCollection<V> select(Predicate<? super V> predicate)
    {
        return this.delegate.select(predicate);
    }

    public <P> ImmutableCollection<V> selectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.selectWith(predicate, parameter);
    }

    public ImmutableBiMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.rejectMapOnEntry(this, predicate, HashBiMap.<K, V>newMap()).toImmutable();
    }

    public ImmutableCollection<V> reject(Predicate<? super V> predicate)
    {
        return this.delegate.reject(predicate);
    }

    public <P> ImmutableCollection<V> rejectWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.rejectWith(predicate, parameter);
    }

    public PartitionImmutableCollection<V> partition(Predicate<? super V> predicate)
    {
        return this.delegate.partition(predicate);
    }

    public <P> PartitionImmutableCollection<V> partitionWith(Predicate2<? super V, ? super P> predicate, P parameter)
    {
        return this.delegate.partitionWith(predicate, parameter);
    }

    public <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.delegate.zip(that);
    }

    public ImmutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        return this.delegate.zipWithIndex();
    }

    public <VV> ImmutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.delegate.groupBy(function);
    }

    public <VV> ImmutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.delegate.groupByEach(function);
    }

    public <VV> ImmutableBiMap<VV, V> groupByUniqueKey(Function<? super V, ? extends VV> function)
    {
        return new ImmutableHashBiMap<VV, V>(this.delegate.groupByUniqueKey(function));
    }

    public <K2, V2> ImmutableMap<K2, V2> aggregateBy(Function<? super V, ? extends K2> groupBy, Function0<? extends V2> zeroValueFactory, Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator)
    {
        return this.delegate.aggregateBy(groupBy, zeroValueFactory, nonMutatingAggregator);
    }

    public <K2, V2> ImmutableMap<K2, V2> aggregateInPlaceBy(Function<? super V, ? extends K2> groupBy, Function0<? extends V2> zeroValueFactory, Procedure2<? super V2, ? super V> mutatingAggregator)
    {
        return this.delegate.aggregateInPlaceBy(groupBy, zeroValueFactory, mutatingAggregator);
    }

    public <S> ImmutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        return this.delegate.selectInstancesOf(clazz);
    }

    private static class Inverse<K, V> extends AbstractImmutableBiMap<K, V> implements Serializable
    {
        Inverse(ImmutableMap<K, V> delegate, AbstractImmutableBiMap<V, K> inverse)
        {
            super(delegate, inverse);
        }

        protected Object writeReplace()
        {
            return new ImmutableBiMapSerializationProxy<K, V>(this);
        }
    }
}
