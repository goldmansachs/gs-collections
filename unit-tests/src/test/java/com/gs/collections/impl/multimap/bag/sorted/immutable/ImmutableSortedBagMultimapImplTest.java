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

package com.gs.collections.impl.multimap.bag.sorted.immutable;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSortedBagMultimapImplTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected <K, V> ImmutableMultimap<K, V> classUnderTest()
    {
        return new ImmutableSortedBagMultimapImpl<>(UnifiedMap.newMap());
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return SortedBags.mutable.empty();
    }

    @Override
    @Test
    public void flip()
    {
        MutableSortedBagMultimap<String, Integer> mutableMap = TreeBagMultimap.<String, Integer>newMultimap();
        mutableMap.put("Less than 2", 1);
        mutableMap.put("Less than 3", 1);
        mutableMap.put("Less than 3", 2);
        mutableMap.put("Less than 3", 2);
        ImmutableSortedBagMultimap<String, Integer> multimap = mutableMap.toImmutable();
        ImmutableBagMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Bags.immutable.with("Less than 3", "Less than 3"), flipped.get(2));
        Assert.assertEquals(Bags.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void noDuplicates()
    {
        // Bags allow duplicates
    }

    @Override
    @Test
    public void collectValues()
    {
        TreeBagMultimap<String, Integer> mutableMultimap = TreeBagMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableSortedBagMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<String, String> collectedMultimap = immutableMap.collectValues(value -> value + "Value");
        FastListMultimap<String, String> expectedMultimap = FastListMultimap.<String, String>newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value", "2Value"));
        ImmutableListMultimap<String, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Assert.assertEquals(expectedImmutableMultimap, collectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        TreeBagMultimap<Integer, Integer> multimap = TreeBagMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll(1, FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        ImmutableSortedBagMultimap<Integer, Integer> immutableMultimap = multimap.toImmutable();
        ImmutableSortedBagMultimap<Integer, Integer> selectedMultimap = immutableMultimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        TreeBagMultimap<Integer, Integer> expectedMultimap = TreeBagMultimap.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedBagsEqual(expectedMultimap.toImmutable().get(3), selectedMultimap.get(3));
        Assert.assertEquals(expectedMultimap.toImmutable().comparator(), selectedMultimap.comparator());
    }

    @Test
    public void constructors()
    {
        ImmutableSortedBagMultimap<Integer, Integer> map = new ImmutableSortedBagMultimapImpl<>(Maps.immutable.empty());
        ImmutableSortedBagMultimap<Integer, Integer> map2 = new ImmutableSortedBagMultimapImpl<>(Maps.immutable.empty(), Comparators.reverseNaturalOrder());
        Assert.assertEquals(this.classUnderTest(), map);
        Assert.assertEquals(TreeBagMultimap.newMultimap(Comparators.reverseNaturalOrder()), map2);
    }

    @Test
    public void empty()
    {
        ImmutableSortedBagMultimap<Object, Object> multimap = new ImmutableSortedBagMultimapImpl<>(Maps.mutable.empty()).newEmpty();
        Verify.assertEmpty(multimap);
        Verify.assertInstanceOf(ImmutableSortedBagMultimap.class, multimap);
    }
}
