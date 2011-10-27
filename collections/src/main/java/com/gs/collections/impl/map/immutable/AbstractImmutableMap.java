/*
 * Copyright 2011 Goldman Sachs & Co.
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

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.AbstractMapIterable;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.partition.list.PartitionFastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.utility.MapIterate;
import net.jcip.annotations.Immutable;

@Immutable
public abstract class AbstractImmutableMap<K, V>
        extends AbstractMapIterable<K, V>
        implements ImmutableMap<K, V>, Map<K, V>
{
    /**
     * Returns a string representation of this map.  The string representation consists of a list of key-value mappings
     * in the order returned by the map's <tt>entrySet</tt> view's iterator, enclosed in braces (<tt>"{}"</tt>).
     * Adjacent mappings are separated by the characters <tt>", "</tt> (comma and space).  Each key-value mapping is
     * rendered as the key followed by an equals sign (<tt>"="</tt>) followed by the associated value.  Keys and values
     * are converted to strings as by <tt>String.valueOf(Object)</tt>.<p>
     * <p/>
     * This implementation creates an empty string buffer, appends a left brace, and iterates over the map's
     * <tt>entrySet</tt> view, appending the string representation of each <tt>map.entry</tt> in turn.  After appending
     * each entry except the last, the string <tt>", "</tt> is appended.  Finally a right brace is appended.  A string
     * is obtained from the stringbuffer, and returned.
     *
     * @return a String representation of this map.
     */
    public Map<K, V> castToMap()
    {
        return this;
    }

    public MutableMap<K, V> toMap()
    {
        return UnifiedMap.newMap(this);
    }

    public Iterator<V> iterator()
    {
        return this.valuesView().iterator();
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public void clear()
    {
        throw new UnsupportedOperationException("ImmutableMap");
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
        throw new UnsupportedOperationException("ImmutableMap");
    }

    public V remove(Object key)
    {
        throw new UnsupportedOperationException("ImmutableMap");
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

    @Override
    public <R> ImmutableCollection<R> collect(Function<? super V, ? extends R> function)
    {
        return this.collect(function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public <R> ImmutableCollection<R> collectIf(Predicate<? super V> predicate, Function<? super V, ? extends R> function)
    {
        return this.collectIf(predicate, function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public <R> ImmutableCollection<R> flatCollect(Function<? super V, ? extends Iterable<R>> function)
    {
        return this.flatCollect(function, FastList.<R>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<V> reject(Predicate<? super V> predicate)
    {
        return this.reject(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<V> select(Predicate<? super V> predicate)
    {
        return this.select(predicate, FastList.<V>newList(this.size())).toImmutable();
    }

    public PartitionImmutableCollection<V> partition(Predicate<? super V> predicate)
    {
        return PartitionFastList.of(this, predicate).toImmutable();
    }

    @Override
    public <S> ImmutableCollection<Pair<V, S>> zip(Iterable<S> that)
    {
        return this.zip(that, FastList.<Pair<V, S>>newList(this.size())).toImmutable();
    }

    @Override
    public ImmutableCollection<Pair<V, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(FastList.<Pair<V, Integer>>newList(this.size())).toImmutable();
    }

    public <VV> ImmutableMultimap<VV, V> groupBy(Function<? super V, ? extends VV> function)
    {
        return this.groupBy(function, FastListMultimap.<VV, V>newMultimap()).toImmutable();
    }

    public <VV> ImmutableMultimap<VV, V> groupByEach(Function<? super V, ? extends Iterable<VV>> function)
    {
        return this.groupByEach(function, FastListMultimap.<VV, V>newMultimap()).toImmutable();
    }
}
