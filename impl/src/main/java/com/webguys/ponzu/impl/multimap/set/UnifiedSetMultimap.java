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

package com.webguys.ponzu.impl.multimap.set;

import java.io.Externalizable;

import com.webguys.ponzu.api.block.procedure.Procedure2;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.multimap.set.ImmutableSetMultimap;
import com.webguys.ponzu.api.multimap.set.MutableSetMultimap;
import com.webguys.ponzu.api.set.ImmutableSet;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractMutableMultimap;
import com.webguys.ponzu.impl.set.mutable.UnifiedSet;

public final class UnifiedSetMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableSet<V>>
        implements MutableSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    public UnifiedSetMultimap()
    {
    }

    public UnifiedSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.sizeDistinct() * 2, 16));
        this.putAll(multimap);
    }

    public UnifiedSetMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMap()
    {
        return UnifiedMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    public static <K, V> UnifiedSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new UnifiedSetMultimap<K, V>(multimap);
    }

    public static <K, V> UnifiedSetMultimap<K, V> newMultimap()
    {
        return new UnifiedSetMultimap<K, V>();
    }

    public static <K, V> UnifiedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new UnifiedSetMultimap<K, V>(pairs);
    }

    @Override
    protected MutableSet<V> createCollection()
    {
        return new UnifiedSet<V>();
    }

    public UnifiedSetMultimap<K, V> newEmpty()
    {
        return new UnifiedSetMultimap<K, V>();
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new UnifiedSetMultimap<K, V>(this);
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
