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

package com.webguys.ponzu.impl.map.sorted.mutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.map.sorted.MutableSortedMap;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.procedure.MapTransformProcedure;
import com.webguys.ponzu.impl.collection.mutable.CollectionAdapter;
import com.webguys.ponzu.impl.set.mutable.SetAdapter;
import com.webguys.ponzu.impl.utility.ArrayIterate;
import com.webguys.ponzu.impl.utility.MapIterate;

/**
 * This class provides a MutableSortedMap wrapper around a JDK Collections SortedMap interface instance.  All of the MutableSortedMap
 * interface methods are supported in addition to the JDK SortedMap interface methods.
 * <p/>
 * To create a new wrapper around an existing SortedMap instance, use the {@link #adapt(SortedMap)} factory method.
 */
public class SortedMapAdapter<K, V>
        extends AbstractMutableSortedMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final SortedMap<K, V> delegate;

    protected SortedMapAdapter(SortedMap<K, V> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("SortedMapAdapter may not wrap null");
        }
        this.delegate = newDelegate;
    }

    public static <K, V> MutableSortedMap<K, V> adapt(SortedMap<K, V> map)
    {
        return map instanceof MutableSortedMap<?, ?> ? (MutableSortedMap<K, V>) map : new SortedMapAdapter<K, V>(map);
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        MapIterate.forEachKeyValue(this.delegate, procedure);
    }

    /**
     * @deprecated use {@link TreeSortedMap#newEmpty()} instead (inlineable)
     */
    @Deprecated
    public MutableSortedMap<K, V> newEmpty()
    {
        return TreeSortedMap.newMap(this.delegate.comparator());
    }

    public boolean containsKey(Object key)
    {
        return this.delegate.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return this.delegate.containsValue(value);
    }

    public Comparator<? super K> comparator()
    {
        return this.delegate.comparator();
    }

    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    @Override
    public Iterator<V> iterator()
    {
        return this.delegate.values().iterator();
    }

    public V remove(Object key)
    {
        return this.delegate.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        this.delegate.putAll(map);
    }

    public MutableCollection<V> values()
    {
        return CollectionAdapter.adapt(this.delegate.values());
    }

    public MutableSet<Entry<K, V>> entrySet()
    {
        return SetAdapter.adapt(this.delegate.entrySet());
    }

    public MutableSet<K> keySet()
    {
        return SetAdapter.adapt(this.delegate.keySet());
    }

    public K firstKey()
    {
        return this.delegate.firstKey();
    }

    public K lastKey()
    {
        return this.delegate.lastKey();
    }

    public MutableSortedMap<K, V> headMap(K toKey)
    {
        return SortedMapAdapter.adapt(this.delegate.headMap(toKey));
    }

    public MutableSortedMap<K, V> tailMap(K fromKey)
    {
        return SortedMapAdapter.adapt(this.delegate.tailMap(fromKey));
    }

    public MutableSortedMap<K, V> subMap(K fromKey, K toKey)
    {
        return SortedMapAdapter.adapt(this.delegate.subMap(fromKey, toKey));
    }

    public void clear()
    {
        this.delegate.clear();
    }

    public V get(Object key)
    {
        return this.delegate.get(key);
    }

    public V put(K key, V value)
    {
        return this.delegate.put(key, value);
    }

    public V removeKey(K key)
    {
        return this.delegate.remove(key);
    }

    public MutableSortedMap<K, V> with(Pair<K, V>... pairs)
    {
        ArrayIterate.forEach(pairs, new MapTransformProcedure<Pair<K, V>, K, V>(this, Functions.<K>firstOfPair(), Functions.<V>secondOfPair()));
        return this;
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    @Override
    public MutableSortedMap<K, V> clone()
    {
        return TreeSortedMap.newMap(this.delegate);
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
}
