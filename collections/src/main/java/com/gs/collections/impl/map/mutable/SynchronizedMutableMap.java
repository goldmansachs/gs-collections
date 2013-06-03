/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.map.mutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.map.SynchronizedMapIterable;
import com.gs.collections.impl.set.mutable.SynchronizedMutableSet;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * A synchronized view of a {@link MutableMap}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableMap#asSynchronized()
 */
public class SynchronizedMutableMap<K, V>
        extends SynchronizedMapIterable<K, V> implements MutableMap<K, V>
{
    private static final long serialVersionUID = 1L;

    protected SynchronizedMutableMap(MutableMap<K, V> newMap)
    {
        super(newMap);
    }

    protected SynchronizedMutableMap(MutableMap<K, V> newMap, Object newLock)
    {
        super(newMap, newLock);
    }

    /**
     * This method will take a MutableMap and wrap it directly in a SynchronizedMutableMap.  It will
     * take any other non-GS-map and first adapt it will a MapAdapter, and then return a
     * SynchronizedMutableMap that wraps the adapter.
     */
    public static <K, V, M extends Map<K, V>> SynchronizedMutableMap<K, V> of(M map)
    {
        if (map == null)
        {
            throw new IllegalArgumentException("cannot create a SynchronizedMutableMap for null");
        }
        return new SynchronizedMutableMap<K, V>(MapAdapter.adapt(map));
    }

    public MutableMap<K, V> getMutableMap()
    {
        return (MutableMap<K, V>) this.getMap();
    }

    public MutableMap<K, V> select(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().select(predicate);
        }
    }

    public <R> MutableMap<K, R> collectValues(Function2<? super K, ? super V, ? extends R> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().collectValues(function);
        }
    }

    public <K2, V2> MutableMap<K2, V2> collect(Function2<? super K, ? super V, Pair<K2, V2>> pairFunction)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().collect(pairFunction);
        }
    }

    public MutableMap<K, V> reject(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().reject(predicate);
        }
    }

    public MutableCollection<V> select(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().select(predicate);
        }
    }

    public MutableCollection<V> reject(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().reject(predicate);
        }
    }

    public PartitionMutableCollection<V> partition(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().partition(predicate);
        }
    }

    public <S> MutableCollection<S> selectInstancesOf(Class<S> clazz)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().selectInstancesOf(clazz);
        }
    }

    public <S> MutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().zip(that);
        }
    }

    public MutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().zipWithIndex();
        }
    }

    public <A> MutableCollection<A> flatCollect(Function<? super V, ? extends Iterable<A>> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().flatCollect(function);
        }
    }

    public <A> MutableCollection<A> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().collectIf(predicate, function);
        }
    }

    public <A> MutableCollection<A> collect(Function<? super V, ? extends A> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().collect(function);
        }
    }

    public <KK> MutableMultimap<KK, V> groupBy(Function<? super V, ? extends KK> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().groupBy(function);
        }
    }

    public <KK> MutableMultimap<KK, V> groupByEach(Function<? super V, ? extends Iterable<KK>> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().groupByEach(function);
        }
    }

    public MutableMap<K, V> newEmpty()
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().newEmpty();
        }
    }

    public <E> MutableMap<K, V> collectKeysAndValues(Collection<E> collection, Function<? super E, ? extends K> keyFunction, Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().collectKeysAndValues(collection, keyFunction, function);
        }
    }

    public V removeKey(K key)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().removeKey(key);
        }
    }

    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().getIfAbsentPut(key, function);
        }
    }

    public V getIfAbsentPutWithKey(K key, Function<? super K, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().getIfAbsentPutWithKey(key, function);
        }
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().getIfAbsentPutWith(key, function, parameter);
        }
    }

    @Override
    public MutableMap<K, V> clone()
    {
        synchronized (this.lock)
        {
            return of(this.getMutableMap().clone());
        }
    }

    @Override
    public boolean equals(Object o)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().equals(o);
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().hashCode();
        }
    }

    @Override
    public String toString()
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().toString();
        }
    }

    public MutableMap<K, V> asUnmodifiable()
    {
        synchronized (this.lock)
        {
            return UnmodifiableMutableMap.of(this);
        }
    }

    public ImmutableMap<K, V> toImmutable()
    {
        synchronized (this.lock)
        {
            return Maps.immutable.ofAll(this);
        }
    }

    public MutableMap<K, V> asSynchronized()
    {
        return this;
    }

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        synchronized (this.lock)
        {
            Set<Entry<K, V>> entries = this.getMutableMap().entrySet();
            Iterable<Pair<K, V>> pairs = Iterate.collect(entries, AbstractImmutableEntry.<K, V>getPairFunction());
            return LazyIterate.adapt(pairs);
        }
    }

    @Override
    public RichIterable<K> keysView()
    {
        return LazyIterate.adapt(this.keySet());
    }

    @Override
    public RichIterable<V> valuesView()
    {
        return LazyIterate.adapt(this.values());
    }

    public V put(K key, V value)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().put(key, value);
        }
    }

    public V remove(Object key)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().remove(key);
        }
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        synchronized (this.lock)
        {
            this.getMutableMap().putAll(map);
        }
    }

    public void clear()
    {
        synchronized (this.lock)
        {
            this.getMutableMap().clear();
        }
    }

    public Set<K> keySet()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableSet.of(this.getMutableMap().keySet(), this.lock);
        }
    }

    public Collection<V> values()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableCollection.of(this.getMutableMap().values(), this.lock);
        }
    }

    public Set<Entry<K, V>> entrySet()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableSet.of(this.getMutableMap().entrySet(), this.lock);
        }
    }

    public MutableMap<K, V> withKeyValue(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    public MutableMap<K, V> withAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        return this.withAllKeyValues(ArrayAdapter.adapt(keyValuePairs));
    }

    public MutableMap<K, V> withAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        synchronized (this.lock)
        {
            for (Pair<? extends K, ? extends V> keyValue : keyValues)
            {
                this.getMutableMap().put(keyValue.getOne(), keyValue.getTwo());
            }
            return this;
        }
    }

    public MutableMap<K, V> withoutKey(K key)
    {
        this.remove(key);
        return this;
    }

    public MutableMap<K, V> withoutAllKeys(Iterable<? extends K> keys)
    {
        synchronized (this.lock)
        {
            for (K key : keys)
            {
                this.getMutableMap().removeKey(key);
            }
            return this;
        }
    }

    public <K2, V2> MutableMap<K2, V2> aggregateInPlaceBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Procedure2<? super V2, ? super V> mutatingAggregator)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().aggregateInPlaceBy(groupBy, zeroValueFactory, mutatingAggregator);
        }
    }

    public <K2, V2> MutableMap<K2, V2> aggregateBy(
            Function<? super V, ? extends K2> groupBy,
            Function0<? extends V2> zeroValueFactory,
            Function2<? super V2, ? super V, ? extends V2> nonMutatingAggregator)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().aggregateBy(groupBy, zeroValueFactory, nonMutatingAggregator);
        }
    }

    public V updateValue(K key, Function0<? extends V> factory, Function<? super V, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().updateValue(key, factory, function);
        }
    }

    public <P> V updateValueWith(
            K key,
            Function0<? extends V> factory,
            Function2<? super V, ? super P, ? extends V> function,
            P parameter)
    {
        synchronized (this.lock)
        {
            return this.getMutableMap().updateValueWith(key, factory, function, parameter);
        }
    }
}
