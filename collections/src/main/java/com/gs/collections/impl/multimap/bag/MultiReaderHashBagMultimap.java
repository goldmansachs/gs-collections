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

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.MultiReaderHashBag;
import com.gs.collections.impl.map.mutable.ConcurrentHashMap;
import com.gs.collections.impl.utility.Iterate;

public final class MultiReaderHashBagMultimap<K, V>
        extends AbstractMutableBagMultimap<K, V> implements Externalizable
{
    private static final long serialVersionUID = 2L;

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

    public MutableBagMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    public <V2> MultiReaderHashBagMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, MultiReaderHashBagMultimap.<K, V2>newMultimap());
    }

    public MultiReaderHashBagMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    public MultiReaderHashBagMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    public MultiReaderHashBagMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    public MultiReaderHashBagMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super Iterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }
}
