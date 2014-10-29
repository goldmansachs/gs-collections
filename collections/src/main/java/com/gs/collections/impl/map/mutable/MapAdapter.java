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

package com.gs.collections.impl.map.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.MapIterate;

/**
 * This class provides a MutableMap wrapper around a JDK Collections Map interface instance.  All of the MutableMap
 * interface methods are supported in addition to the JDK Map interface methods.
 * <p>
 * To create a new wrapper around an existing Map instance, use the {@link #adapt(Map)} factory method.
 */
public class MapAdapter<K, V>
        extends AbstractMutableMap<K, V>
        implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected final Map<K, V> delegate;

    protected MapAdapter(Map<K, V> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("MapAdapter may not wrap null");
        }
        this.delegate = newDelegate;
    }

    public static <K, V> MutableMap<K, V> adapt(Map<K, V> map)
    {
        return map instanceof MutableMap<?, ?> ? (MutableMap<K, V>) map : new MapAdapter<K, V>(map);
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    @Override
    public MutableMap<K, V> clone()
    {
        return UnifiedMap.newMap(this.delegate);
    }

    @Override
    public <K, V> MutableMap<K, V> newEmpty(int capacity)
    {
        return UnifiedMap.newMap(capacity);
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

    public Set<K> keySet()
    {
        return this.delegate.keySet();
    }

    public Collection<V> values()
    {
        return this.delegate.values();
    }

    public Set<Entry<K, V>> entrySet()
    {
        return this.delegate.entrySet();
    }

    public void clear()
    {
        this.delegate.clear();
    }

    public MutableMap<K, V> newEmpty()
    {
        return UnifiedMap.newMap();
    }

    public void forEachKeyValue(Procedure2<? super K, ? super V> procedure)
    {
        MapIterate.forEachKeyValue(this.delegate, procedure);
    }

    public V get(Object key)
    {
        return this.delegate.get(key);
    }

    public V put(K key, V value)
    {
        return this.delegate.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> map)
    {
        this.delegate.putAll(map);
    }

    public <E> MutableMap<K, V> collectKeysAndValues(
            Iterable<E> iterable,
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction)
    {
        Iterate.addToMap(iterable, keyFunction, valueFunction, this.delegate);
        return this;
    }

    public V removeKey(K key)
    {
        return this.delegate.remove(key);
    }

    public boolean containsKey(Object key)
    {
        return this.delegate.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return this.delegate.containsValue(value);
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
    public ImmutableMap<K, V> toImmutable()
    {
        return Maps.immutable.ofMap(this);
    }
}
