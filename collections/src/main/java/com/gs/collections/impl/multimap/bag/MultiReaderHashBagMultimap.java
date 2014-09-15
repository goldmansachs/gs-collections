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

package com.gs.collections.impl.multimap.bag;

import java.io.Externalizable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.MultiReaderHashBag;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractMutableMultimap;

public final class MultiReaderHashBagMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableBag<V>>
        implements MutableBagMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    public MultiReaderHashBagMultimap()
    {
    }

    public MultiReaderHashBagMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.keysView().size() * 2, 16));
        this.putAll(multimap);
    }

    public MultiReaderHashBagMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public MultiReaderHashBagMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V> MultiReaderHashBagMultimap<K, V> newMultimap()
    {
        return new MultiReaderHashBagMultimap<K, V>();
    }

    public static <K, V> MultiReaderHashBagMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new MultiReaderHashBagMultimap<K, V>(multimap);
    }

    public static <K, V> MultiReaderHashBagMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new MultiReaderHashBagMultimap<K, V>(pairs);
    }

    public static <K, V> MultiReaderHashBagMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new MultiReaderHashBagMultimap<K, V>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableBag<V>> createMap()
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableBag<V>> createMapWithKeyCount(int keyCount)
    {
        return ConcurrentHashMap.newMap(keyCount);
    }

    @Override
    protected MutableBag<V> createCollection()
    {
        return MultiReaderHashBag.newBag();
    }

    public MultiReaderHashBagMultimap<K, V> newEmpty()
    {
        return new MultiReaderHashBagMultimap<K, V>();
    }

    public MutableBagMultimap<K, V> toMutable()
    {
        return new MultiReaderHashBagMultimap<K, V>(this);
    }

    public ImmutableBagMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableBag<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableBag<V>>()
        {
            public void value(K key, MutableBag<V> list)
            {
                map.put(key, list.toImmutable());
            }
        });

        return new ImmutableBagMultimapImpl<K, V>(map);
    }

    public HashBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, HashBagMultimap.<K, V>newMultimap());
    }

    public HashBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, HashBagMultimap.<K, V>newMultimap());
    }

    public HashBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, HashBagMultimap.<K, V>newMultimap());
    }

    public HashBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, HashBagMultimap.<K, V>newMultimap());
    }
}
