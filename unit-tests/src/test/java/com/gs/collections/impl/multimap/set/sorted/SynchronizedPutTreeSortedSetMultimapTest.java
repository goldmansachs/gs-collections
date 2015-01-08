/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.multimap.set.sorted;

import java.util.Comparator;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.sortedset.MutableSortedSetMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link SynchronizedPutTreeSortedSetMultimap}.
 */
public class SynchronizedPutTreeSortedSetMultimapTest extends AbstractMutableSortedSetMultimapTestCase
{
    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimap()
    {
        return SynchronizedPutTreeSortedSetMultimap.newMultimap(Comparators.reverseNaturalOrder());
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimap(Comparator<? super V> comparator)
    {
        return SynchronizedPutTreeSortedSetMultimap.newMultimap(comparator);
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        MutableSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        MutableSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        MutableSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        MutableSortedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @SafeVarargs
    @Override
    protected final <K, V> MutableSortedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        MutableSortedSetMultimap<K, V> result = this.newMultimap();
        for (Pair<K, V> pair : pairs)
        {
            result.add(pair);
        }
        return result;
    }

    @Override
    protected <K, V> MutableSortedSetMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
    {
        MutableSortedSetMultimap<K, V> result = this.newMultimap();
        for (Pair<K, V> pair : inputIterable)
        {
            result.add(pair);
        }
        return result;
    }

    @SafeVarargs
    @Override
    protected final <V> TreeSortedSet<V> createCollection(V... args)
    {
        return TreeSortedSet.newSetWith(Comparators.reverseNaturalOrder(), args);
    }

    @Test
    @Override
    public void testToString()
    {
        super.testToString();

        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "One", 2);
        String toString = multimap.toString();
        Assert.assertTrue("{One=[1, 2]}".equals(toString) || "{One=[2, 1]}".equals(toString));
    }
}
