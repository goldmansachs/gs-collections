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

package com.gs.collections.impl.multimap.set;

import java.io.Externalizable;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractSynchronizedPutMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.ArrayIterate;

/**
 * A Multimap that is optimized for parallel writes, but is not protected for concurrent reads.
 */
public final class SynchronizedPutUnifiedSetMultimap<K, V>
        extends AbstractSynchronizedPutMultimap<K, V, MutableSet<V>> implements MutableSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 42L;

    public SynchronizedPutUnifiedSetMultimap()
    {
    }

    public SynchronizedPutUnifiedSetMultimap(int initialCapacity, float loadFactor, int concurrencyLevel)
    {
        super(ConcurrentHashMap.<K, MutableSet<V>>newMap(initialCapacity));
    }

    public SynchronizedPutUnifiedSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        this.putAll(multimap);
    }

    public SynchronizedPutUnifiedSetMultimap(Pair<K, V>... pairs)
    {
        this();
        ArrayIterate.forEach(pairs, new Procedure<Pair<K, V>>()
        {
            public void value(Pair<K, V> pair)
            {
                SynchronizedPutUnifiedSetMultimap.this.put(pair.getOne(), pair.getTwo());
            }
        });
    }

    @Override
    protected MutableSet<V> createCollection()
    {
        return UnifiedSet.newSet(1);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>();
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(int initiaCapacity, float loadFactor, int concurrencyLevel)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(initiaCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(multimap);
    }

    public static <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(pairs);
    }

    public SynchronizedPutUnifiedSetMultimap<K, V> newEmpty()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>();
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new SynchronizedPutUnifiedSetMultimap<K, V>(this);
    }

    public ImmutableSetMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableSet<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableSet<V>>()
        {
            public void value(K key, MutableSet<V> set)
            {
                map.put(key, set.toImmutable());
            }
        });

        return new ImmutableSetMultimapImpl<K, V>(map);
    }
}
