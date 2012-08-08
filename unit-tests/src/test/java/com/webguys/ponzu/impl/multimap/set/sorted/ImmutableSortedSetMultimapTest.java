/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.multimap.set.sorted;

import java.util.Collections;

import com.webguys.ponzu.api.collection.MutableCollection;
import com.webguys.ponzu.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.webguys.ponzu.api.set.sorted.ImmutableSortedSet;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.AbstractImmutableMultimapTestCase;
import com.webguys.ponzu.impl.set.sorted.mutable.TreeSortedSet;
import com.webguys.ponzu.impl.test.SerializeTestHelper;
import com.webguys.ponzu.impl.test.Verify;
import junit.framework.Assert;
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
        Verify.assertListsEqual(FastList.<Integer>newListWith(10, 9, 8),
                deserialized.newWithAll(3, FastList.newListWith(8, 9, 10)).get(3).toList());
    }

    @Override
    public void allowDuplicates()
    {
        // Sets do not allow duplicates
    }
}
