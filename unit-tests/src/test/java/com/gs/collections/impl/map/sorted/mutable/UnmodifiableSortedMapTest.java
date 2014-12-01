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

package com.gs.collections.impl.map.sorted.mutable;

import java.util.Map;
import java.util.TreeMap;

import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class UnmodifiableSortedMapTest
{
    private final UnmodifiableSortedMap<Integer, String> map = new UnmodifiableSortedMap<>(
            new TreeMap<>(SortedMaps.mutable.of(1, "1", 2, "2", 3, "3", 4, "4")));
    private final UnmodifiableSortedMap<Integer, String> revMap = new UnmodifiableSortedMap<>(
            new TreeMap<>(SortedMaps.mutable.of(Comparators.<Integer>reverseNaturalOrder(),
                    1, "1", 2, "2", 3, "3", 4, "4")));

    @Test
    public void comparator()
    {
        Assert.assertEquals(Comparators.<Integer>reverseNaturalOrder(), this.revMap.comparator());
    }

    @Test
    public void subMap()
    {
        Verify.assertInstanceOf(UnmodifiableSortedMap.class, this.map.subMap(1, 3));
        this.checkMutability(this.map.subMap(1, 3));
    }

    @Test
    public void headMap()
    {
        Verify.assertInstanceOf(UnmodifiableSortedMap.class, this.map.headMap(3));
        this.checkMutability(this.map.headMap(3));
    }

    @Test
    public void tailMap()
    {
        Verify.assertInstanceOf(UnmodifiableSortedMap.class, this.map.tailMap(2));
        this.checkMutability(this.map.tailMap(2));
    }

    @Test
    public void firstKey()
    {
        Assert.assertEquals(1, this.map.firstKey().intValue());
        Assert.assertEquals(4, this.revMap.firstKey().intValue());
    }

    @Test
    public void lasKey()
    {
        Assert.assertEquals(4, this.map.lastKey().intValue());
        Assert.assertEquals(1, this.revMap.lastKey().intValue());
    }

    private void checkMutability(Map<Integer, String> map)
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> map.put(3, "1"));

        Verify.assertThrows(UnsupportedOperationException.class, () -> map.putAll(SortedMaps.mutable.of(1, "1", 2, "2")));

        Verify.assertThrows(UnsupportedOperationException.class, () -> map.remove(2));

        Verify.assertThrows(UnsupportedOperationException.class, map::clear);
    }
}
