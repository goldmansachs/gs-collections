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

import java.util.Collections;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.set.UnsortedSetMultimap;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSortedSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected <K, V> ImmutableSortedSetMultimap<K, V> classUnderTest()
    {
        return TreeSortedSetMultimap.<K, V>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return TreeSortedSet.newSet();
    }

    @Test
    public void testConstructor()
    {
        UnifiedMap<Integer, ImmutableSortedSet<Integer>> map = UnifiedMap.newWithKeysValues(1, TreeSortedSet.newSetWith(1).toImmutable());
        ImmutableSortedSetMultimap<Integer, Integer> immutableMap = new ImmutableSortedSetMultimapImpl<>(map, null);
        Assert.assertEquals(FastList.newListWith(1), immutableMap.get(1).toList());
        Assert.assertNull(immutableMap.comparator());
        Verify.assertSize(1, immutableMap);
    }

    @Test
    public void testNewEmpty()
    {
        ImmutableSortedSetMultimap<Integer, Integer> map = new ImmutableSortedSetMultimapImpl<>(UnifiedMap.<Integer, ImmutableSortedSet<Integer>>newMap(), Collections.<Integer>reverseOrder());
        Assert.assertEquals(Collections.<Integer>reverseOrder(), map.newEmpty().comparator());
        Verify.assertEmpty(map.newEmpty());
    }

    @Test
    public void serialization()
    {
        TreeSortedSetMultimap<Integer, Integer> map = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        map.putAll(1, FastList.newListWith(1, 2, 3, 4));
        map.putAll(2, FastList.newListWith(2, 3, 4, 5));
        ImmutableSortedSetMultimap<Integer, Integer> immutableMap = map.toImmutable();
        Verify.assertPostSerializedEqualsAndHashCode(immutableMap);

        ImmutableSortedSetMultimap<Integer, Integer> deserialized = SerializeTestHelper.serializeDeserialize(immutableMap);
        Verify.assertSortedSetsEqual(TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3, 4),
                deserialized.get(1).castToSortedSet());
        Verify.assertListsEqual(FastList.newListWith(10, 9, 8),
                deserialized.newWithAll(3, FastList.newListWith(8, 9, 10)).get(3).toList());
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
        TreeSortedSetMultimap<String, Integer> multimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        multimap.toImmutable().forEachKeyMultiValues((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 2, 1)), Tuples.pair("Three", TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 3, 3))), collection);
    }

    @Override
    @Test
    public void flip()
    {
        ImmutableSortedSetMultimap<String, Integer> multimap = this.<String, Integer>classUnderTest()
                .newWith("Less than 2", 1)
                .newWith("Less than 3", 1)
                .newWith("Less than 3", 2)
                .newWith("Less than 3", 2);
        UnsortedSetMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Sets.immutable.with("Less than 3"), flipped.get(2));
        Assert.assertEquals(Sets.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSortedSetMultimap<String, Integer> selectedMultimap = immutableMap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        TreeSortedSetMultimap<String, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("Two", FastList.newListWith(4, 2));
        ImmutableSortedSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get("Two"), selectedMultimap.get("Two"));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSortedSetMultimap<String, Integer> rejectedMultimap = immutableMap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        TreeSortedSetMultimap<String, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("One", FastList.newListWith(3, 1));
        ImmutableSortedSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, rejectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get("One"), rejectedMultimap.get("One"));
        Assert.assertEquals(expectedMultimap.comparator(), rejectedMultimap.comparator());
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        TreeSortedSetMultimap<Integer, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll(1, FastList.newListWith(4, 3, 1));
        mutableMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        mutableMultimap.putAll(3, FastList.newListWith(5, 4, 3, 2, 2));
        mutableMultimap.putAll(4, FastList.newListWith(4, 3, 1));
        ImmutableSortedSetMultimap<Integer, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSortedSetMultimap<Integer, Integer> selectedMultimap = immutableMap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        TreeSortedSetMultimap<Integer, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedSetMultimap<Integer, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get(2), selectedMultimap.get(2));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        TreeSortedSetMultimap<Integer, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll(1, FastList.newListWith(5, 4, 3, 2, 1));
        mutableMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        mutableMultimap.putAll(3, FastList.newListWith(5, 4, 2, 2));
        mutableMultimap.putAll(4, FastList.newListWith(4, 3, 1));
        ImmutableSortedSetMultimap<Integer, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableSortedSetMultimap<Integer, Integer> selectedMultimap = immutableMap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        TreeSortedSetMultimap<Integer, Integer> expectedMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(3, FastList.newListWith(5, 4, 2, 2));
        ImmutableSortedSetMultimap<Integer, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, selectedMultimap);
        Verify.assertIterablesEqual(expectedImmutableMultimap.get(3), selectedMultimap.get(3));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        TreeSortedSetMultimap<String, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<Integer, String> collectedMultimap1 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap1 = expectedMultimap1.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap1, collectedMultimap1);

        ImmutableBagMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        TreeSortedSetMultimap<String, Integer> mutableMultimap = TreeSortedSetMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<String, String> collectedMultimap = immutableMap.collectValues(value -> value + "Value");
        FastListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        ImmutableListMultimap<String, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);
    }
}
