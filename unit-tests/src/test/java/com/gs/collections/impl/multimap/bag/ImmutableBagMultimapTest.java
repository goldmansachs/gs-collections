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

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.bag.ImmutableBagMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableBagMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected ImmutableBagMultimap<String, String> classUnderTest()
    {
        return HashBagMultimap.<String, String>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return Bags.mutable.of();
    }

    @Override
    public void noDuplicates()
    {
        // Bags allow duplicates
    }

    @Test
    public void forEachKeyMultiValue()
    {
        MutableSet<Pair<String, Iterable<Integer>>> collection = UnifiedSet.newSet();
        HashBagMultimap<String, Integer> multimap = HashBagMultimap.newMultimap();
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        multimap.toImmutable().forEachKeyMultiValue((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair("Two", HashBag.newBagWith(2, 1)), Tuples.pair("Three", HashBag.newBagWith(3, 3))), collection);
    }
}
