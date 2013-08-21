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

package com.gs.collections.impl.map.strategy.immutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollection;
import com.gs.collections.impl.map.immutable.AbstractImmutableMap;
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.mutable.UnmodifiableMutableSet;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import com.gs.collections.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

@Immutable
/**
 * @see ImmutableMap
 */
public class ImmutableUnifiedMapWithHashingStrategy<K, V>
        extends AbstractImmutableMap<K, V> implements BatchIterable<V>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final UnifiedMapWithHashingStrategy<K, V> delegate;

    public ImmutableUnifiedMapWithHashingStrategy(UnifiedMapWithHashingStrategy<K, V> delegate)
    {
        this.delegate = UnifiedMapWithHashingStrategy.newMap(delegate);
    }

    public ImmutableUnifiedMapWithHashingStrategy(
            HashingStrategy<? super K> hashingStrategy,
            Pair<K, V>... pairs)
    {
        this.delegate = UnifiedMapWithHashingStrategy.newMapWith(hashingStrategy, pairs);
    }

    @Override
    public boolean equals(Object o)
    {
        return this.delegate.equals(o);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    public int size()
    {
        return this.delegate.size();
    }

    public boolean containsKey(Object key)
    {
        return this.delegate.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return this.delegate.containsValue(value);
    }

    public V get(Object key)
    {
        return this.delegate.get(key);
    }

    public int getBatchCount(int batchSize)
    {
        return this.delegate.getBatchCount(batchSize);
    }

    public void batchForEach(Procedure<? super V> procedure, int sectionIndex, int sectionCount)
    {
        this.delegate.batchForEach(procedure, sectionIndex, sectionCount);
    }

    @Override
    public void forEachValue(Procedure<? super V> procedure)
    {
        this.delegate.forEachValue(procedure);
    }

    @Override
    public void forEachKey(Procedure<? super K> procedure)
    {
        this.delegate.forEachKey(procedure);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        this.delegate.forEachKeyValue(procedure);
    }

    @Override
    public Set<Entry<K, V>> entrySet()
    {
        final UnifiedSetWithHashingStrategy<Entry<K, V>> result = UnifiedSetWithHashingStrategy.newSet(
                HashingStrategies.<Entry<K, V>>defaultStrategy(), this.delegate.size());
        final HashingStrategy<? super K> hashingStrategy = this.delegate.hashingStrategy();
        this.forEachKeyValue(new Procedure2<K, V>()
        {
            public void value(K argument1, V argument2)
            {
                result.put(ImmutableEntryWithHashingStrategy.of(argument1, argument2, hashingStrategy));
            }
        });
        return result.toImmutable().castToSet();
    }

    public Set<K> keySet()
    {
        return UnmodifiableMutableSet.of(this.delegate.keySet());
    }

    public Collection<V> values()
    {
        return UnmodifiableMutableCollection.of(this.delegate.values());
    }

    public RichIterable<K> keysView()
    {
        return this.delegate.keysView();
    }

    public RichIterable<V> valuesView()
    {
        return this.delegate.valuesView();
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return this.delegate.keyValuesView();
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super V> objectIntProcedure)
    {
        this.delegate.forEachWithIndex(objectIntProcedure);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super V, ? super P> procedure, P parameter)
    {
        this.delegate.forEachWith(procedure, parameter);
    }

    @Override
    public ImmutableMap<K, V> newWithKeyValue(K key, V value)
    {
        UnifiedMapWithHashingStrategy<K, V> result = UnifiedMapWithHashingStrategy.newMap(this.delegate);
        result.put(key, value);
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        UnifiedMapWithHashingStrategy<K, V> result = UnifiedMapWithHashingStrategy.newMap(this.delegate);
        for (Pair<? extends K, ? extends V> pair : keyValues)
        {
            result.put(pair.getOne(), pair.getTwo());
        }
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        UnifiedMapWithHashingStrategy<K, V> result = UnifiedMapWithHashingStrategy.newMap(this.delegate);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValuePairs)
        {
            result.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> newWithoutKey(K key)
    {
        UnifiedMapWithHashingStrategy<K, V> result = UnifiedMapWithHashingStrategy.newMap(this.delegate);
        result.remove(key);
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys)
    {
        UnifiedMapWithHashingStrategy<K, V> result = UnifiedMapWithHashingStrategy.newMap(this.delegate);
        for (K key : keys)
        {
            result.remove(key);
        }
        return result.toImmutable();
    }

    @Override
    public <R> ImmutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        MutableMap<K, R> result = MapIterate.collectValues(this, function,
                UnifiedMapWithHashingStrategy.<K, R>newMap(this.delegate.hashingStrategy(), this.delegate.size()));
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        MutableMap<K, V> result = MapIterate.selectMapOnEntry(this, predicate, this.delegate.newEmpty());
        return result.toImmutable();
    }

    @Override
    public ImmutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        MutableMap<K, V> result = MapIterate.rejectMapOnEntry(this, predicate, this.delegate.newEmpty());
        return result.toImmutable();
    }

    protected Object writeReplace()
    {
        return new ImmutableMapWithHashingStrategySerializationProxy<K, V>(this, this.delegate.hashingStrategy());
    }
}
