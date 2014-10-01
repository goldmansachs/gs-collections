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
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.utility.Iterate;

public final class UnifiedSetMultimap<K, V>
        extends AbstractMutableSetMultimap<K, V> implements Externalizable
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

    public UnifiedSetMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
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

    public static <K, V> UnifiedSetMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new UnifiedSetMultimap<K, V>(inputIterable);
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

    @Override
    protected MutableSet<V> createCollection()
    {
        return new UnifiedSet<V>();
    }

    public UnifiedSetMultimap<K, V> newEmpty()
    {
        return new UnifiedSetMultimap<K, V>();
    }

    public MutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    public UnifiedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    public UnifiedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    public UnifiedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    public UnifiedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }
}
