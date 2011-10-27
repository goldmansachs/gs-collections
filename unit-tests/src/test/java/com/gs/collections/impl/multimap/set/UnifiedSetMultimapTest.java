/*
 * Copyright 2011 Goldman Sachs & Co.
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

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.multimap.AbstractMutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;

/**
 * Test of {@link UnifiedSetMultimap}.
 */
public class UnifiedSetMultimapTest extends AbstractMutableMultimapTestCase
{
    @Override
    public <K, V> UnifiedSetMultimap<K, V> newMultimap()
    {
        return UnifiedSetMultimap.newMultimap();
    }

    @Override
    public <K, V> UnifiedSetMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        UnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    public <K, V> UnifiedSetMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        UnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    protected <V> MutableCollection<V> createCollection(V... args)
    {
        return UnifiedSet.newSetWith(args);
    }

    @Override
    public <K, V> Multimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return UnifiedSetMultimap.newMultimap(pairs);
    }

    @Override
    public <K, V> UnifiedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        UnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    public <K, V> UnifiedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        UnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }
}
