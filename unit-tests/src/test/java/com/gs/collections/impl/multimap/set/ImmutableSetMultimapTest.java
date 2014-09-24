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

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected ImmutableSetMultimap<String, String> classUnderTest()
    {
        return UnifiedSetMultimap.<String, String>newMultimap().toImmutable();
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
        UnifiedSetMultimap<String, Integer> multimap = UnifiedSetMultimap.newMultimap();
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        ImmutableSetMultimap<String, Integer> immutableMultimap = multimap.toImmutable();
        immutableMultimap.forEachKeyMultiValue((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", UnifiedSet.newSetWith(2, 1)), Tuples.pair("Three", UnifiedSet.newSetWith(3, 3))), collection);
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        UnifiedSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<String, Integer> selectedMultimap = immutableMap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        UnifiedSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4));
        ImmutableSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        UnifiedSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        mutableMultimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<String, Integer> rejectedMultimap = immutableMap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        UnifiedSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3));
        ImmutableSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        UnifiedSetMultimap<Integer, String> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "3", "4"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4"));
        ImmutableSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<Integer, String> selectedMultimap = immutableMap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        UnifiedSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        UnifiedSetMultimap<Integer, String> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "5", "1"));
        mutableMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        mutableMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        ImmutableSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<Integer, String> rejectedMultimap = immutableMap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        UnifiedSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        UnifiedSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 1));
        mutableMultimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 2));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<Integer, String> collectedMultimap = immutableMap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value.toString() + "Value"));
        UnifiedSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "1Value"));
        expectedMultimap.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "2Value"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);

        ImmutableSetMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(1, value.toString() + "Value"));
        UnifiedSetMultimap<Integer, String> expectedMultimap2 = UnifiedSetMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        ImmutableSetMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        UnifiedSetMultimap<String, Integer> mutableMultimap = UnifiedSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 1));
        mutableMultimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 2));
        ImmutableSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSetMultimap<String, String> collectedMultimap = immutableMap.collectValues(value -> value.toString() + "Value");
        UnifiedSetMultimap<String, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("1Value", "2Value", "3Value", "4Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("2Value", "3Value", "4Value", "5Value", "2Value"));
        ImmutableSetMultimap<String, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);
    }
}
