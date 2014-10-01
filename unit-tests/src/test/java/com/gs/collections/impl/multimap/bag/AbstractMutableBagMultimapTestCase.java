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

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.multimap.AbstractMutableMultimapTestCase;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractMutableBagMultimapTestCase extends AbstractMutableMultimapTestCase
{
    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimap();

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimapWithKeyValue(K key, V value);

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2);

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimap(Pair<K, V>... pairs);

    @Override
    protected abstract <K, V> MutableBagMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable);

    @Override
    protected abstract <V> MutableBag<V> createCollection(V... args);

    @Override
    @Test
    public void flip()
    {
        BagMultimap<String, Integer> multimap = this.newMultimapWithKeysValues("Less than 2", 1, "Less than 3", 1, "Less than 3", 2, "Less than 3", 2);
        UnsortedBagMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Bags.immutable.with("Less than 3", "Less than 3"), flipped.get(2));
        Assert.assertEquals(Bags.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }
}
