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

package com.gs.collections.impl.multimap.list;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableListMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected ImmutableListMultimap<String, String> classUnderTest()
    {
        return FastListMultimap.<String, String>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return Lists.mutable.of();
    }

    @Override
    public void noDuplicates()
    {
        // Lists allow duplicates
    }

    @Test
    public void forEachKeyMultiValue()
    {
        MutableSet<Pair<String, Iterable<Integer>>> collection = UnifiedSet.newSet();
        FastListMultimap<String, Integer> multimap = FastListMultimap.newMultimap();
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        multimap.toImmutable().forEachKeyMultiValue((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", FastList.newListWith(2, 1)), Tuples.pair("Three", FastList.newListWith(3, 3))), collection);
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        FastListMultimap<String, Integer> mutableMultimap = FastListMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 2));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 3, 4, 5, 2));
        ImmutableListMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<String, Integer> selectedMultimap = immutableMap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        FastListMultimap<String, Integer> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4, 2));
        ImmutableListMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get("Two"), selectedMultimap.get("Two"));
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        FastListMultimap<String, Integer> mutableMultimap = FastListMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 1));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 3, 4, 5, 1));
        ImmutableListMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<String, Integer> rejectedMultimap = immutableMap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        FastListMultimap<String, Integer> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3, 1));
        ImmutableListMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get("One"), rejectedMultimap.get("One"));
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        FastListMultimap<Integer, String> mutableMultimap = FastListMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "3", "4"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4"));
        ImmutableListMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<Integer, String> selectedMultimap = immutableMap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        FastListMultimap<Integer, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        ImmutableListMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get(2), selectedMultimap.get(2));
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        FastListMultimap<Integer, String> mutableMultimap = FastListMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "1"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        ImmutableListMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<Integer, String> rejectedMultimap = immutableMap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        FastListMultimap<Integer, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        ImmutableListMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get(3), rejectedMultimap.get(3));
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        FastListMultimap<String, Integer> mutableMultimap = FastListMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 1));
        mutableMultimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 2));
        ImmutableListMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<Integer, String> collectedMultimap1 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value.toString() + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "2Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap1 = expectedMultimap1.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap1, collectedMultimap1);

        ImmutableBagMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(1, value.toString() + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "2Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap2, collectedMultimap2);
    }
}
