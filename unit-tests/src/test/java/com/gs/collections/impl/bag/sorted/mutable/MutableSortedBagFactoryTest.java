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

package com.gs.collections.impl.bag.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class MutableSortedBagFactoryTest
{
    @Test
    public void ofEmpty()
    {
        Assert.assertEquals(TreeBag.newBag(), SortedBags.mutable.of());
        Assert.assertEquals(TreeBag.newBag(Comparators.reverseNaturalOrder()), SortedBags.mutable.of(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void withEmpty()
    {
        Assert.assertEquals(TreeBag.newBag(), SortedBags.mutable.with());
        Assert.assertEquals(TreeBag.newBag(Comparators.reverseNaturalOrder()), SortedBags.mutable.with(Comparators.reverseNaturalOrder()));
    }

    @Test
    public void ofElements()
    {
        Assert.assertEquals(TreeBag.newBagWith(1, 1, 2), SortedBags.mutable.of(1, 1, 2));
        Assert.assertEquals(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2), SortedBags.mutable.of(Comparators.reverseNaturalOrder(), 1, 1, 2));
    }

    @Test
    public void withElements()
    {
        Assert.assertEquals(TreeBag.newBagWith(1, 1, 2), SortedBags.mutable.with(1, 1, 2));
        Assert.assertEquals(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 1, 2), SortedBags.mutable.with(Comparators.reverseNaturalOrder(), 1, 1, 2));
    }

    @Test
    public void ofAll()
    {
        LazyIterable<Integer> list = FastList.newListWith(1, 2, 2).asLazy();
        Assert.assertEquals(TreeBag.newBagWith(1, 2, 2), SortedBags.mutable.ofAll(list));
    }

    @Test
    public void withAll()
    {
        LazyIterable<Integer> list = FastList.newListWith(1, 2, 2).asLazy();
        Assert.assertEquals(TreeBag.newBagWith(1, 2, 2), SortedBags.mutable.withAll(list));
    }

    @Test
    public void ofAllComparator()
    {
        LazyIterable<Integer> list = FastList.newListWith(1, 2, 2).asLazy();
        Assert.assertEquals(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 2, 2), SortedBags.mutable.ofAll(Comparators.reverseNaturalOrder(), list));
    }

    @Test
    public void withAllComparator()
    {
        LazyIterable<Integer> list = FastList.newListWith(1, 2, 2).asLazy();
        Assert.assertEquals(TreeBag.newBagWith(Comparators.reverseNaturalOrder(), 1, 2, 2), SortedBags.mutable.withAll(Comparators.reverseNaturalOrder(), list));
    }

    @Test
    public void empty()
    {
        Assert.assertEquals(TreeBag.newBag(), SortedBags.mutable.empty());
        Assert.assertEquals(TreeBag.newBag(Comparator.reverseOrder()), SortedBags.mutable.empty(Comparator.reverseOrder()));
    }
}
