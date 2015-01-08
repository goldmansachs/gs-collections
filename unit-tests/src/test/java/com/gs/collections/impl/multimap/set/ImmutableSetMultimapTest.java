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

package com.gs.collections.impl.multimap.set;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.multimap.set.MutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected <K, V> ImmutableSetMultimap<K, V> classUnderTest()
    {
        return UnifiedSetMultimap.<K, V>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return UnifiedSet.newSet();
    }

    @Override
    public void allowDuplicates()
    {
        // Sets do not allow duplicates
    }

    @Test
    public void forEachKeyMultiValue()
    {
        MutableSet<Pair<String, Iterable<Integer>>> collection = UnifiedSet.newSet();
        MutableSetMultimap<String, Integer> multimap = UnifiedSetMultimap.newMultimap();
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        ImmutableSetMultimap<String, Integer> immutableMultimap = multimap.toImmutable();
        immutableMultimap.forEachKeyMultiValues((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", UnifiedSet.newSetWith(2, 1)), Tuples.pair("Three", UnifiedSet.newSetWith(3, 3))), collection);
    }

    @Override
    @Test
    public void flip()
    {
        ImmutableSetMultimap<String, Integer> multimap = this.<String, Integer>classUnderTest()
                .newWith("Less than 2", 1)
                .newWith("Less than 3", 1)
                .newWith("Less than 3", 2)
                .newWith("Less than 3", 2);
        ImmutableSetMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Sets.immutable.with("Less than 3"), flipped.get(2));
        Assert.assertEquals(Sets.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        MutableSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<String, Integer> selectedMultimap = immutableMap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        MutableSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4));
        ImmutableSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        MutableSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<String, Integer> rejectedMultimap = immutableMap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        MutableSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3));
        ImmutableSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        MutableSetMultimap<Integer, String> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "3", "4"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4"));
        ImmutableSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<Integer, String> selectedMultimap = immutableMap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        MutableSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        MutableSetMultimap<Integer, String> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "5", "1"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        ImmutableSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<Integer, String> rejectedMultimap = immutableMap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        MutableSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        MutableSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<Integer, String> collectedMultimap = immutableMap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap = HashBagMultimap.newMultimap();
        expectedMultimap.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);

        ImmutableBagMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        MutableSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(1, 2, 3, 4));
        mutableMultimap.putAll("2", FastList.newListWith(2, 3, 4, 5));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<String, String> collectedMultimap = immutableMap.collectValues(value -> value + "Value");
        MutableBagMultimap<String, String> expectedMultimap = HashBagMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap.putAll("2", FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<String, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);
    }
}
