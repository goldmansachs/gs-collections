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

package com.gs.collections.impl.map.sorted.immutable;

import java.util.Comparator;

import com.gs.collections.api.map.sorted.ImmutableSortedMap;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.map.sorted.ImmutableSortedMapTestCase;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableTreeMapTest extends ImmutableSortedMapTestCase
{
    @Override
    protected ImmutableSortedMap<Integer, String> classUnderTest()
    {
        return SortedMaps.immutable.of(1, "1", 2, "2", 3, "3", 4, "4");
    }

    @Override
    protected ImmutableSortedMap<Integer, String> classUnderTest(Comparator<? super Integer> comparator)
    {
        return SortedMaps.immutable.of(comparator, 1, "1", 2, "2", 3, "3", 4, "4");
    }

    @Override
    protected int size()
    {
        return 4;
    }

    @Test
    @Override
    public void testToString()
    {
        Assert.assertEquals("{1=1, 2=2, 3=3, 4=4}", this.classUnderTest().toString());
        Assert.assertEquals("{4=4, 3=3, 2=2, 1=1}", this.classUnderTest(Comparators.<Integer>reverseNaturalOrder()).toString());
    }
}
