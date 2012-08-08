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

package com.webguys.ponzu.impl.map.sorted.immutable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.function.Function2;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.predicate.Predicate2;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.list.ImmutableList;
import com.webguys.ponzu.api.map.ImmutableMap;
import com.webguys.ponzu.api.map.sorted.ImmutableSortedMap;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.multimap.list.ImmutableListMultimap;
import com.webguys.ponzu.api.partition.list.PartitionImmutableList;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.AbstractMapIterable;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.map.sorted.mutable.TreeSortedMap;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.partition.list.PartitionFastList;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;
import com.webguys.ponzu.impl.tuple.ImmutableEntry;
import com.webguys.ponzu.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

@Immutable
public abstract class AbstractImmutableSortedMap<K, V>
        extends AbstractMapIterable<K, V>
        implements ImmutableSortedMap<K, V>, SortedMap<K, V>
{
    public SortedMap<K, V> castToSortedMap()
    {
        return this;
    }

    public MutableSortedMap<K, V> toSortedMap()
    {
        return TreeSortedMap.newMap(this);
    }

    public Iterator<V> iterator()
    {
        return this.valuesView().iterator();
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("ImmutableSortedMap");
    }

    public void clear()
    {
        throw new UnsupportedOperationException("ImmutableSortedMap");
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

    public ImmutableSortedMap<K, V> newWithKeyValue(K key, V value)
    {
        TreeSortedMap<K, V> sortedMap = TreeSortedMap.newMap(this);
        sortedMap.put(key, value);
        return sortedMap.toImmutable();
    }

    public ImmutableSortedMap<K, V> newWithAllKeyValues(Iterable<? extends Pair<? extends K, ? extends V>> keyValues)
    {
        TreeSortedMap<K, V> sortedMap = TreeSortedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValues)
        {
            sortedMap.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return sortedMap.toImmutable();
    }

    public ImmutableSortedMap<K, V> newWithAllKeyValueArguments(Pair<? extends K, ? extends V>... keyValuePairs)
    {
        TreeSortedMap<K, V> sortedMap = TreeSortedMap.newMap(this);
        for (Pair<? extends K, ? extends V> keyValuePair : keyValuePairs)
        {
            sortedMap.put(keyValuePair.getOne(), keyValuePair.getTwo());
        }
        return sortedMap.toImmutable();
    }

    public ImmutableSortedMap<K, V> newWithoutKey(K key)
    {
        TreeSortedMap<K, V> sortedMap = TreeSortedMap.newMap(this);
        sortedMap.removeKey(key);
        return sortedMap.toImmutable();
    }

    public ImmutableSortedMap<K, V> newWithoutAllKeys(Iterable<? extends K> keys)
    {
        TreeSortedMap<K, V> sortedMap = TreeSortedMap.newMap(this);
        for (K key : keys)
        {
            sortedMap.removeKey(key);
        }
        return sortedMap.toImmutable();
    }

    public V put(K key, V value)
    {
        throw new UnsupportedOperationException("ImmutableSortedMap");
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("ImmutableSortedMap");
    }

    @Override
    public ImmutableList<V> filter(Predicate<? super V> predicate)
    {
        return this.filter(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    public ImmutableSortedMap<K, V> filter(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterMapOnEntry(this, predicate, TreeSortedMap.<K, V>newMap(this.comparator())).toImmutable();
    }

    @Override
    public ImmutableList<V> filterNot(Predicate<? super V> predicate)
    {
        return this.filterNot(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    public ImmutableSortedMap<K, V> filterNot(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.filterNotMapOnEntry(this, predicate, TreeSortedMap.<K, V>newMap(this.comparator())).toImmutable();
    }

    public PartitionImmutableList<V> partition(Predicate<? super V> predicate)
    {
        return PartitionFastList.of(this, predicate).toImmutable();
    }

    @Override
    public <R> ImmutableList<R> transform(Function<? super V, ? extends R> function)
    {
        return this.transform(function, FastList.<R>newList(this.size())).toImmutable();
    }

    public <K2, V2> ImmutableMap<K2, V2> transform(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return MapIterate.transform(this, function, UnifiedMap.<K2, V2>newMap()).toImmutable();
    }

    @Override
    public <R> ImmutableList<R> transformIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.transformIf(predicate, function, FastList.<R>newList(this.size())).toImmutable();
    }

    public <R> ImmutableSortedMap<K, R> transformValues(Function2<? super K, ? super V, ? extends R> function)
    {
        return MapIterate.transformValues(this, function, TreeSortedMap.<K, R>newMap(this.comparator())).toImmutable();
    }

    public Pair<K, V> find(Predicate2<? super K, ? super V> predicate)
    {
        return MapIterate.find(this, predicate);
    }

    @Override
    public <R> ImmutableList<R> flatTransform(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatTransform(function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public <S> ImmutableList<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableList<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList(this.size())).toImmutable();
    }

    public SortedMap<K, V> subMap(K fromKey, K toKey)
    {
        throw new UnsupportedOperationException();
    }

    public SortedMap<K, V> headMap(K toKey)
    {
        throw new UnsupportedOperationException();
    }

    public SortedMap<K, V> tailMap(K fromKey)
    {
        throw new UnsupportedOperationException();
    }

    public <R> ImmutableListMultimap<R, V> groupBy(Function<? super V, ? extends R> function)
    {
        return this.groupBy(function, FastListMultimap.<R, V>newMultimap()).toImmutable();
    }

    public <R> ImmutableListMultimap<R, V> groupByEach(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.groupByEach(function, FastListMultimap.<R, V>newMultimap()).toImmutable();
    }
}
