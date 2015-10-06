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

package com.gs.collections.impl.map.sorted.mutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.collection.mutable.CollectionAdapter;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.MapIterate;

/**
 * This class provides a MutableSortedMap wrapper around a JDK Collections SortedMap interface instance.  All of the MutableSortedMap
 * interface methods are supported in addition to the JDK SortedMap interface methods.
 * <p>
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

    @Override
    public MutableSortedMap<K, V> with(Pair<K, V>... pairs)
    {
        ArrayIterate.forEach(pairs, new MapCollectProcedure<Pair<K, V>, K, V>(this, Functions.<K>firstOfPair(), Functions.<V>secondOfPair()));
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

    public MutableSortedMap<K, V> toReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".toReversed() not implemented yet");
    }

    public MutableSortedMap<K, V> take(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        MutableSortedMap<K, V> output = SortedMaps.mutable.of(this.comparator());
        Iterator<Entry<K, V>> iterator = this.delegate.entrySet().iterator();
        int countCopy = count;
        while (iterator.hasNext() && countCopy-- > 0)
        {
            Entry<K, V> next = iterator.next();
            output.put(next.getKey(), next.getValue());
        }
        return output;
    }

    public MutableSortedMap<K, V> takeWhile(Predicate<? super V> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".takeWhile() not implemented yet");
    }

    public MutableSortedMap<K, V> drop(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }

        MutableSortedMap<K, V> output = SortedMaps.mutable.of(this.comparator());
        Iterator<Entry<K, V>> iterator = this.delegate.entrySet().iterator();
        int start = Math.min(count, this.size());
        if (start == this.size())
        {
            return output;
        }
        int i = 0;
        while (iterator.hasNext())
        {
            if (i >= start)
            {
                Entry<K, V> next = iterator.next();
                output.put(next.getKey(), next.getValue());
            }
            else
            {
                iterator.next();
            }
            i++;
        }
        return output;
    }

    public MutableSortedMap<K, V> dropWhile(Predicate<? super V> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".dropWhile() not implemented yet");
    }

    public PartitionMutableList<V> partitionWhile(Predicate<? super V> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".partitionWhile() not implemented yet");
    }

    public MutableList<V> distinct()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".distinct() not implemented yet");
    }
}
