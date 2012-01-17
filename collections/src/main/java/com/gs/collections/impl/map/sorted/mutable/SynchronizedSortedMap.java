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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.SynchronizedMutableCollection;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.map.SynchronizedMapIterable;
import com.gs.collections.impl.set.mutable.SynchronizedMutableSet;
import com.gs.collections.impl.tuple.AbstractImmutableEntry;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * A synchronized view of a SortedMap.
 *
 * @see #SynchronizedSortedMap(MutableSortedMap)
 */
public class SynchronizedSortedMap<K, V>
        extends SynchronizedMapIterable<K, V> implements MutableSortedMap<K, V>
{
    private static final long serialVersionUID = 1L;

    protected SynchronizedSortedMap(MutableSortedMap<K, V> newMap)
    {
        super(newMap);
    }

    protected SynchronizedSortedMap(MutableSortedMap<K, V> newMap, Object lock)
    {
        super(newMap, lock);
    }

    /**
     * This method will take a MutableSortedMap and wrap it directly in a SynchronizedSortedMap.  It will
     * take any other non-GS-SortedMap and first adapt it will a SortedMapAdapter, and then return a
     * SynchronizedSortedMap that wraps the adapter.
     */
    public static <K, V, M extends SortedMap<K, V>> SynchronizedSortedMap<K, V> of(M map)
    {
        return new SynchronizedSortedMap<K, V>(SortedMapAdapter.adapt(map));
    }

    public static <K, V, M extends SortedMap<K, V>> SynchronizedSortedMap<K, V> of(M map, Object lock)
    {
        return new SynchronizedSortedMap<K, V>(SortedMapAdapter.adapt(map), lock);
    }

    protected MutableSortedMap<K, V> getSortedMap()
    {
        return (MutableSortedMap<K, V>) this.getMap();
    }

    public MutableSortedMap<K, V> newEmpty()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().newEmpty();
        }
    }

    public <E> MutableSortedMap<K, V> transformKeysAndValues(Collection<E> collection, Function<? super E, ? extends K> keyFunction, Function<? super E, ? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().transformKeysAndValues(collection, keyFunction, function);
        }
    }

    public V removeKey(K key)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().removeKey(key);
        }
    }

    public V getIfAbsentPut(K key, Function0<? extends V> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().getIfAbsentPut(key, function);
        }
    }

    public <P> V getIfAbsentPutWith(K key, Function<? super P, ? extends V> function, P parameter)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().getIfAbsentPutWith(key, function, parameter);
        }
    }

    public MutableSortedMap<K, V> with(Pair<K, V>... pairs)
    {
        synchronized (this.lock)
        {
            this.getSortedMap().with(pairs);
            return this;
        }
    }

    public MutableSortedMap<K, V> asUnmodifiable()
    {
        synchronized (this.lock)
        {
            return UnmodifiableTreeMap.of(this);
        }
    }

    public ImmutableSortedMap<K, V> toImmutable()
    {
        synchronized (this.lock)
        {
            return SortedMaps.immutable.ofSortedMap(this);
        }
    }

    public MutableSortedMap<K, V> asSynchronized()
    {
        return this;
    }

    public V put(K key, V value)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().put(key, value);
        }
    }

    public V remove(Object key)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().remove(key);
        }
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        synchronized (this.lock)
        {
            this.getSortedMap().putAll(map);
        }
    }

    public void clear()
    {
        synchronized (this.lock)
        {
            this.getSortedMap().clear();
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

    public RichIterable<Pair<K, V>> keyValuesView()
    {
        return LazyIterate.adapt(this.entrySet()).transform(AbstractImmutableEntry.<K, V>getPairFunction());
    }

    public MutableSet<Entry<K, V>> entrySet()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableSet.of(this.getSortedMap().entrySet(), this.lock);
        }
    }

    public MutableSet<K> keySet()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableSet.of(this.getSortedMap().keySet(), this.lock);
        }
    }

    public MutableSortedMap<K, V> headMap(K toKey)
    {
        synchronized (this.lock)
        {
            return of(this.getSortedMap().headMap(toKey), this.lock);
        }
    }

    public MutableSortedMap<K, V> tailMap(K fromKey)
    {
        synchronized (this.lock)
        {
            return of(this.getSortedMap().tailMap(fromKey), this.lock);
        }
    }

    public MutableSortedMap<K, V> subMap(K fromKey, K toKey)
    {
        synchronized (this.lock)
        {
            return of(this.getSortedMap().subMap(fromKey, toKey), this.lock);
        }
    }

    public MutableCollection<V> values()
    {
        synchronized (this.lock)
        {
            return SynchronizedMutableCollection.of(this.getSortedMap().values(), this.lock);
        }
    }

    public K firstKey()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().firstKey();
        }
    }

    public K lastKey()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().lastKey();
        }
    }

    @Override
    public int hashCode()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().hashCode();
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().equals(obj);
        }
    }

    @Override
    public MutableSortedMap<K, V> clone()
    {
        synchronized (this.lock)
        {
            return of(this.getSortedMap().clone());
        }
    }

    @Override
    public String toString()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().toString();
        }
    }

    @Override
    public String makeString()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().makeString();
        }
    }

    @Override
    public String makeString(String separator)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().makeString(separator);
        }
    }

    @Override
    public String makeString(String start, String separator, String end)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().makeString(start, separator, end);
        }
    }

    public Comparator<? super K> comparator()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().comparator();
        }
    }

    public <R> MutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().transformValues(function);
        }
    }

    public MutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().filter(predicate);
        }
    }

    public MutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().filterNot(predicate);
        }
    }

    public PartitionMutableList<V> partition(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().partition(predicate);
        }
    }

    public <K2, V2> MutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().transform(function);
        }
    }

    public <R> MutableList<R> transform(Function<? super V, ? extends R> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().transform(function);
        }
    }

    @Override
    public <R> MutableList<R> transformIf(
            Predicate<? super V> predicate,
            Function<? super V, ? extends R> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().transformIf(predicate, function);
        }
    }

    public <R> MutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().flatTransform(function);
        }
    }

    public MutableList<V> filterNot(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().filterNot(predicate);
        }
    }

    public MutableList<V> filter(Predicate<? super V> predicate)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().filter(predicate);
        }
    }

    public <S> MutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().zip(that);
        }
    }

    public MutableList<Pair<V, Integer>> zipWithIndex()
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().zipWithIndex();
        }
    }

    public <KK> MutableListMultimap<KK, V> groupBy(Function<? super V, ? extends KK> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().groupBy(function);
        }
    }

    public <KK> MutableListMultimap<KK, V> groupByEach(Function<? super V, ? extends Iterable<KK>> function)
    {
        synchronized (this.lock)
        {
            return this.getSortedMap().groupByEach(function);
        }
    }
}
