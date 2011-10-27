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

package com.gs.collections.impl.map.sorted.immutable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollection;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnmodifiableMutableSet;
import net.jcip.annotations.Immutable;

@Immutable
/**
 * @see ImmutableSortedMap
 */
public class ImmutableTreeMap<K, V>
        extends AbstractImmutableSortedMap<K, V>
{
    private final TreeSortedMap<K, V> delegate;

    public ImmutableTreeMap(SortedMap<K, V> sortedMap)
    {
        this.delegate = TreeSortedMap.newMap(sortedMap);
    }

    public static <K, V> ImmutableSortedMap<K, V> newMap(SortedMap<K, V> sortedMap)
    {
        return new ImmutableTreeMap<K, V>(sortedMap);
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

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        this.delegate.forEachKeyValue(procedure);
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

    public Comparator<? super K> comparator()
    {
        return this.delegate.comparator();
    }

    public K firstKey()
    {
        return this.delegate.firstKey();
    }

    public K lastKey()
    {
        return this.delegate.lastKey();
    }

    public Set<K> keySet()
    {
        return UnmodifiableMutableSet.of(this.delegate.keySet());
    }

    public Collection<V> values()
    {
        return UnmodifiableMutableCollection.of(this.delegate.values());
    }
}
