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

package com.webguys.ponzu.impl.multimap.bag;

import java.io.Externalizable;

import com.webguys.ponzu.api.bag.ImmutableBag;
import com.webguys.ponzu.api.bag.MutableBag;
import com.webguys.ponzu.api.block.procedure.Procedure;
import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.multimap.bag.ImmutableBagMultimap;
import com.webguys.ponzu.api.multimap.bag.MutableBagMultimap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.bag.mutable.HashBag;
import com.webguys.ponzu.impl.map.mutable.ConcurrentMutableHashMap;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractSynchronizedPutMultimap;
import com.webguys.ponzu.impl.utility.ArrayIterate;

/**
 * A Multimap that is optimized for parallel writes, but is not protected for concurrent reads.
 */
public final class SynchronizedPutHashBagMultimap<K, V>
        extends AbstractSynchronizedPutMultimap<K, V, MutableBag<V>> implements MutableBagMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 42L;

    public SynchronizedPutHashBagMultimap()
    {
    }

    public SynchronizedPutHashBagMultimap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        super(ConcurrentMutableHashMap.<K, MutableBag<V>>newMap(initialCapacity, loadFactor, concurrencyLevel));
    }

    public SynchronizedPutHashBagMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this.putAll(multimap);
    }

    public SynchronizedPutHashBagMultimap(Pair<K, V>... pairs)
    {
        ArrayIterate.forEach(pairs, new Procedure<Pair<K, V>>()
        {
            public void value(Pair<K, V> pair)
            {
                put(pair.getOne(), pair.getTwo());
            }
        });
    }

    @Override
    protected MutableBag<V> createCollection()
    {
        return HashBag.newBag(1);
    }

    public static <K, V> SynchronizedPutHashBagMultimap<K, V> newMultimap()
    {
        return new SynchronizedPutHashBagMultimap<K, V>();
    }

    public static <K, V> SynchronizedPutHashBagMultimap<K, V> newMultimap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        return new SynchronizedPutHashBagMultimap<K, V>(initialCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> SynchronizedPutHashBagMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new SynchronizedPutHashBagMultimap<K, V>(multimap);
    }

    public static <K, V> SynchronizedPutHashBagMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new SynchronizedPutHashBagMultimap<K, V>(pairs);
    }

    public SynchronizedPutHashBagMultimap<K, V> newEmpty()
    {
        return new SynchronizedPutHashBagMultimap<K, V>();
    }

    public MutableBagMultimap<K, V> toMutable()
    {
        return new SynchronizedPutHashBagMultimap<K, V>(this);
    }

    public ImmutableBagMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableBag<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableBag<V>>()
        {
            public void value(K key, MutableBag<V> bag)
            {
                map.put(key, bag.toImmutable());
            }
        });

        return new ImmutableBagMultimapImpl<K, V>(map);
    }
}
