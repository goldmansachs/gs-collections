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

package com.gs.collections.impl.list.fixed;

import java.util.ArrayList;
import java.util.List;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link DoubletonList}.
 */
public class DoubletonListTest extends AbstractMemoryEfficientMutableListTestCase
{
    @Override
    protected int getSize()
    {
        return 2;
    }

    @Override
    protected Class<?> getListType()
    {
        return DoubletonList.class;
    }

    @Test
    public void testClone()
    {
        MutableList<String> growableList = this.list.clone();
        Verify.assertEqualsAndHashCode(this.list, growableList);
        Verify.assertInstanceOf(DoubletonList.class, growableList);
    }

    @Test
    public void testContains()
    {
        Assert.assertTrue(this.list.contains("1"));
        Assert.assertTrue(this.list.contains("2"));
        Assert.assertFalse(this.list.contains("3"));
    }

    @Test
    public void testRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.remove(0));
    }

    @Test
    public void testAddAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.add(0, "1"));
    }

    @Test
    public void testAdd()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.add("1"));
    }

    @Test
    public void testAddingAllToOtherList()
    {
        List<String> newList = new ArrayList<>(this.list);
        newList.add("3");
        Verify.assertStartsWith(newList, "1", "2", "3");
    }

    @Test
    public void testGet()
    {
        Verify.assertStartsWith(this.list, "1", "2");
        Verify.assertThrows(IndexOutOfBoundsException.class, () -> this.list.get(2));
    }

    @Test
    public void testSet()
    {
        Assert.assertEquals("1", this.list.set(0, "2"));
        Assert.assertEquals("2", this.list.set(1, "1"));
        Assert.assertEquals(FastList.newListWith("2", "1"), this.list);
        Verify.assertThrows(IndexOutOfBoundsException.class, () -> this.list.set(2, "0"));
    }

    @Test
    public void testSerialization()
    {
        Verify.assertPostSerializedEqualsAndHashCode(this.list);

        MutableList<String> copy = SerializeTestHelper.serializeDeserialize(this.list);
        Verify.assertInstanceOf(DoubletonList.class, copy);
        Verify.assertSize(2, copy);
        Verify.assertContainsAll(copy, "1", "2");
        Verify.assertNotContains("3", copy);
    }

    @Test
    public void testEqualsAndHashCode()
    {
        MutableList<String> one = this.classUnderTest();
        List<String> oneA = new ArrayList<>(one);
        Verify.assertEqualsAndHashCode(one, oneA);
        Verify.assertPostSerializedEqualsAndHashCode(one);
    }

    @Test
    public void testForEach()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableList<String> source = this.classUnderTest();
        source.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
    }

    @Test
    public void testForEachWithIndex()
    {
        int[] indexSum = new int[1];
        MutableList<String> result = Lists.mutable.of();
        MutableList<String> source = this.classUnderTest();
        source.forEachWithIndex((each, index) -> {
            result.add(each);
            indexSum[0] += index;
        });
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
        Assert.assertEquals(1, indexSum[0]);
    }

    @Test
    public void testForEachWith()
    {
        MutableList<String> result = Lists.mutable.of();
        this.list.forEachWith(Procedures2.fromProcedure(result::add), null);
        Assert.assertEquals(FastList.newListWith("1", "2"), result);
    }

    @Test
    public void testGetFirstGetLast()
    {
        Assert.assertEquals("1", this.list.getFirst());
        Assert.assertEquals("2", this.list.getLast());
    }

    @Test
    public void testForLoop()
    {
        MutableList<String> list = Lists.fixedSize.of("one", "two");
        MutableList<String> upperList = Lists.fixedSize.of("ONE", "TWO");
        for (String each : list)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
    }

    @Test
    public void testSubList()
    {
        MutableList<String> list = Lists.fixedSize.of("one", "two");
        MutableList<String> subList = list.subList(0, 2);
        MutableList<String> upperList = Lists.fixedSize.of("ONE", "TWO");
        for (String each : subList)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
        Assert.assertEquals("one", subList.getFirst());
        Assert.assertEquals("two", subList.getLast());
        MutableList<String> subList2 = list.subList(1, 2);
        Assert.assertEquals("two", subList2.getFirst());
        Assert.assertEquals("two", subList2.getLast());
        MutableList<String> subList3 = list.subList(0, 1);
        Assert.assertEquals("one", subList3.getFirst());
        Assert.assertEquals("one", subList3.getLast());
    }

    @Test
    public void without()
    {
        MutableList<Integer> list = new DoubletonList<>(2, 2);
        Assert.assertSame(list, list.without(9));
        list = list.without(2);
        Verify.assertListsEqual(FastList.newListWith(2), list);
        Verify.assertInstanceOf(SingletonList.class, list);
    }
}
