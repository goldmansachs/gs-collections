/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.factory;

import com.gs.collections.api.factory.set.sorted.ImmutableSortedSetFactory;
import com.gs.collections.api.factory.set.sorted.MutableSortedSetFactory;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class SortedSetsTest
{
    @Test
    public void immutables()
    {
        ImmutableSortedSetFactory factory = SortedSets.immutable;
        Assert.assertEquals(UnifiedSet.newSet(), factory.of());
        Verify.assertInstanceOf(ImmutableSortedSet.class, factory.of());
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2), factory.of(1, 2, 2));
        Verify.assertInstanceOf(ImmutableSortedSet.class, factory.of(1, 2));
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4), factory.of(1, 2, 3, 4));
        Verify.assertInstanceOf(ImmutableSortedSet.class, factory.of(1, 2, 3, 4));
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4, 5, 6), factory.of(1, 2, 3, 4, 5, 6));
        Verify.assertInstanceOf(ImmutableSortedSet.class, factory.of(1, 2, 3, 4, 5, 6));
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8), factory.of(1, 2, 3, 4, 5, 6, 7, 8));
        Verify.assertInstanceOf(ImmutableSortedSet.class, factory.of(1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    public void mutables()
    {
        MutableSortedSetFactory factory = SortedSets.mutable;
        Assert.assertEquals(TreeSortedSet.newSet(), factory.of());
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of());
        Assert.assertEquals(TreeSortedSet.newSetWith(1, 2), factory.of(1, 2, 2));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of(1, 2));
        Assert.assertEquals(TreeSortedSet.newSetWith(1, 2, 3, 4), factory.of(1, 2, 3, 4));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of(1, 2, 3, 4));
        Assert.assertEquals(TreeSortedSet.newSetWith(1, 2, 3, 4, 5, 6), factory.of(1, 2, 3, 4, 5, 6));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of(1, 2, 3, 4, 5, 6));
        Assert.assertEquals(TreeSortedSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8), factory.of(1, 2, 3, 4, 5, 6, 7, 8));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of(1, 2, 3, 4, 5, 6, 7, 8));
        Assert.assertEquals(TreeSortedSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8), factory.ofAll(FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8)));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.ofAll(FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8)));
        Assert.assertEquals(TreeSortedSet.newSet(Comparators.naturalOrder()), factory.of(Comparators.naturalOrder()));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.of(Comparators.naturalOrder()));
        Assert.assertEquals(TreeSortedSet.newSetWith(8, 7, 6, 5, 4, 3, 2, 1), factory.ofAll(Comparators.reverseNaturalOrder(), FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8)));
        Verify.assertInstanceOf(MutableSortedSet.class, factory.ofAll(Comparators.reverseNaturalOrder(), FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8)));
    }

    @Test
    public void classIsNonInstantiable()
    {
        Verify.assertClassNonInstantiable(SortedSets.class);
    }
}
