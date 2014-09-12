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
import com.gs.collections.api.multimap.list.ImmutableListMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
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
}
