/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.list.fixed;

import java.io.Serializable;
import java.util.NoSuchElementException;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class EmptyListTest
{
    @Test
    public void size()
    {
        Verify.assertSize(0, new EmptyList<Object>());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(new EmptyList<Object>().isEmpty());
        Assert.assertFalse(new EmptyList<Object>().notEmpty());
        Assert.assertTrue(Lists.fixedSize.of().isEmpty());
        Assert.assertFalse(Lists.fixedSize.of().notEmpty());
    }

    @Test
    public void getFirstLast()
    {
        Assert.assertNull(new EmptyList<Object>().getFirst());
        Assert.assertNull(new EmptyList<Object>().getLast());
    }

    @Test
    public void readResolve()
    {
        Verify.assertInstanceOf(EmptyList.class, Lists.fixedSize.of());
        Verify.assertPostSerializedIdentity((Serializable) Lists.fixedSize.of());
    }

    @Test
    public void testClone()
    {
        Assert.assertSame(Lists.fixedSize.of().clone(), Lists.fixedSize.of());
    }

    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        Lists.fixedSize.of().min(Comparators.naturalOrder());
    }

    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        Lists.fixedSize.of().max(Comparators.naturalOrder());
    }

    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        Lists.fixedSize.of().min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        Lists.fixedSize.of().max();
    }

    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        Lists.fixedSize.of().minBy(Functions.getToString());
    }

    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        Lists.fixedSize.of().maxBy(Functions.getToString());
    }

    @Test
    public void zip()
    {
        Assert.assertEquals(
                Lists.fixedSize.of(),
                Lists.fixedSize.of().zip(FastList.newListWith(1, 2, 3)));
    }

    @Test
    public void zipWithIndex()
    {
        Assert.assertEquals(
                Lists.fixedSize.of(),
                Lists.fixedSize.of().zipWithIndex());
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(Lists.fixedSize.of(), Lists.fixedSize.of().chunk(10));
    }

    @Test
    public void sortThis()
    {
        MutableList<Object> expected = Lists.fixedSize.of();
        MutableList<Object> list = Lists.fixedSize.of();
        MutableList<Object> sortedList = list.sortThis();
        Assert.assertEquals(expected, sortedList);
        Assert.assertSame(sortedList, list);
    }

    @Test
    public void sortThisBy()
    {
        MutableList<Object> expected = Lists.fixedSize.of();
        MutableList<Object> list = Lists.fixedSize.of();
        MutableList<Object> sortedList = list.sortThisBy(Functions.getToString());
        Assert.assertEquals(expected, sortedList);
        Assert.assertSame(sortedList, list);
    }

    @Test
    public void with()
    {
        MutableList<Integer> list = new EmptyList<Integer>().with(1);
        Verify.assertListsEqual(FastList.newListWith(1), list);
        Verify.assertInstanceOf(SingletonList.class, list);
    }

    @Test
    public void withAll()
    {
        MutableList<Integer> list = new EmptyList<Integer>().withAll(FastList.newListWith(1, 2));
        Verify.assertListsEqual(FastList.newListWith(1, 2), list);
        Verify.assertInstanceOf(DoubletonList.class, list);
    }

    @Test
    public void without()
    {
        MutableList<Integer> list = new EmptyList<Integer>();
        Assert.assertSame(list, list.without(2));
    }

    @Test
    public void withoutAll()
    {
        MutableList<Integer> list = new EmptyList<Integer>();
        Assert.assertEquals(list, list.withoutAll(FastList.newListWith(1, 2)));
    }
}
