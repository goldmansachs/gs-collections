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

package com.gs.collections.impl.multimap.set.sorted;

import java.util.Collections;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSortedSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected ImmutableSortedSetMultimap<String, String> classUnderTest()
    {
        return TreeSortedSetMultimap.<String, String>newMultimap().toImmutable();
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
        ImmutableSortedSetMultimapImpl<Integer, Integer> immutableMap = new ImmutableSortedSetMultimapImpl<Integer, Integer>(map, null);
        Assert.assertEquals(FastList.newListWith(1), immutableMap.get(1).toList());
        Assert.assertNull(immutableMap.comparator());
        Verify.assertSize(1, immutableMap);
    }

    @Test
    public void testNewEmpty()
    {
        ImmutableSortedSetMultimapImpl<Integer, Integer> map = new ImmutableSortedSetMultimapImpl<Integer, Integer>(UnifiedMap.<Integer, ImmutableSortedSet<Integer>>newMap(), Collections.<Integer>reverseOrder());
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
        multimap.forEachKeyMultiValue((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 2, 1)), Tuples.pair("Three", TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 3, 3))), collection);
    }
}
