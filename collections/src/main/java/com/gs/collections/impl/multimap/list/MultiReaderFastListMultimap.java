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

package com.gs.collections.impl.multimap.list;

import java.io.Externalizable;

import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.MultiReaderFastList;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.utility.Iterate;

public final class MultiReaderFastListMultimap<K, V>
        extends AbstractMutableListMultimap<K, V> implements Externalizable
{
    private static final long serialVersionUID = 1L;

    // Default from FastList
    private static final int DEFAULT_CAPACITY = 1;

    private int initialListCapacity;

    public MultiReaderFastListMultimap()
    {
        this.initialListCapacity = DEFAULT_CAPACITY;
    }

    public MultiReaderFastListMultimap(int distinctKeys, int valuesPerKey)
    {
        super(Math.max(distinctKeys * 2, 16));
        if (distinctKeys < 0 || valuesPerKey < 0)
        {
            throw new IllegalArgumentException("Both arguments must be positive.");
        }
        this.initialListCapacity = valuesPerKey;
    }

    public MultiReaderFastListMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this(
                multimap.keysView().size(),
                multimap instanceof MultiReaderFastListMultimap
                        ? ((MultiReaderFastListMultimap<?, ?>) multimap).initialListCapacity
                        : DEFAULT_CAPACITY);
        this.putAll(multimap);
    }

    public MultiReaderFastListMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public MultiReaderFastListMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V> MultiReaderFastListMultimap<K, V> newMultimap()
    {
        return new MultiReaderFastListMultimap<K, V>();
    }

    public static <K, V> MultiReaderFastListMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new MultiReaderFastListMultimap<K, V>(multimap);
    }

    public static <K, V> MultiReaderFastListMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new MultiReaderFastListMultimap<K, V>(pairs);
    }

    public static <K, V> MultiReaderFastListMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new MultiReaderFastListMultimap<K, V>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableList<V>> createMap()
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableList<V>> createMapWithKeyCount(int keyCount)
    {
        return ConcurrentHashMap.newMap(keyCount);
    }

    @Override
    protected MutableList<V> createCollection()
    {
        return MultiReaderFastList.newList(this.initialListCapacity);
    }

    public MultiReaderFastListMultimap<K, V> newEmpty()
    {
        return new MultiReaderFastListMultimap<K, V>();
    }

    public MutableBagMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    public FastListMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, FastListMultimap.<K, V>newMultimap());
    }

    public FastListMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, FastListMultimap.<K, V>newMultimap());
    }

    public FastListMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, FastListMultimap.<K, V>newMultimap());
    }

    public FastListMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, FastListMultimap.<K, V>newMultimap());
    }
}
