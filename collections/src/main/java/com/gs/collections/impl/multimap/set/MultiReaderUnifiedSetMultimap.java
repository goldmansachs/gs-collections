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

package com.gs.collections.impl.multimap.set;

import java.io.Externalizable;

import com.gs.collections.api.block.predicate.Predicate2;
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
import com.gs.collections.impl.multimap.AbstractMutableMultimap;
import com.gs.collections.impl.set.mutable.MultiReaderUnifiedSet;

public final class MultiReaderUnifiedSetMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableSet<V>>
        implements MutableSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    public MultiReaderUnifiedSetMultimap()
    {
    }

    public MultiReaderUnifiedSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.sizeDistinct() * 2, 16));
        this.putAll(multimap);
    }

    public MultiReaderUnifiedSetMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public MultiReaderUnifiedSetMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V> MultiReaderUnifiedSetMultimap<K, V> newMultimap()
    {
        return new MultiReaderUnifiedSetMultimap<K, V>();
    }

    public static <K, V> MultiReaderUnifiedSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new MultiReaderUnifiedSetMultimap<K, V>(multimap);
    }

    public static <K, V> MultiReaderUnifiedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new MultiReaderUnifiedSetMultimap<K, V>(pairs);
    }

    public static <K, V> MultiReaderUnifiedSetMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new MultiReaderUnifiedSetMultimap<K, V>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMap()
    {
        return ConcurrentHashMap.newMap();
    }

    @Override
    protected MutableMap<K, MutableSet<V>> createMapWithKeyCount(int keyCount)
    {
        return ConcurrentHashMap.newMap(keyCount);
    }

    @Override
    protected MutableSet<V> createCollection()
    {
        return MultiReaderUnifiedSet.newSet();
    }

    public MultiReaderUnifiedSetMultimap<K, V> newEmpty()
    {
        return new MultiReaderUnifiedSetMultimap<K, V>();
    }

    public MutableSetMultimap<K, V> toMutable()
    {
        return new MultiReaderUnifiedSetMultimap<K, V>(this);
    }

    public ImmutableSetMultimap<K, V> toImmutable()
    {
        final MutableMap<K, ImmutableSet<V>> map = UnifiedMap.newMap();

        this.map.forEachKeyValue(new Procedure2<K, MutableSet<V>>()
        {
            public void value(K key, MutableSet<V> list)
            {
                map.put(key, list.toImmutable());
            }
        });

        return new ImmutableSetMultimapImpl<K, V>(map);
    }

    public UnifiedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, UnifiedSetMultimap.<K, V>newMultimap());
    }

    public UnifiedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, UnifiedSetMultimap.<K, V>newMultimap());
    }

    public UnifiedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, UnifiedSetMultimap.<K, V>newMultimap());
    }

    public UnifiedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, UnifiedSetMultimap.<K, V>newMultimap());
    }
}
