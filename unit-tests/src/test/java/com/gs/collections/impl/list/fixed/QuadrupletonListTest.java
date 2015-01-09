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
 * JUnit test for {@link QuadrupletonList}.
 */
public class QuadrupletonListTest extends AbstractMemoryEfficientMutableListTestCase
{
    @Override
    protected int getSize()
    {
        return 4;
    }

    @Override
    protected Class<?> getListType()
    {
        return QuadrupletonList.class;
    }

    @Test
    public void testClone()
    {
        MutableList<String> growableList = this.list.clone();
        Verify.assertEqualsAndHashCode(this.list, growableList);
        Verify.assertInstanceOf(QuadrupletonList.class, growableList);
    }

    @Test
    public void testContains()
    {
        Verify.assertContains("1", this.list);
        Verify.assertContains("2", this.list);
        Verify.assertContains("3", this.list);
        Verify.assertContains("4", this.list);
        Verify.assertNotContains("5", this.list);
    }

    @Test
    public void testEqualsAndHashCode()
    {
        MutableList<String> one = Lists.fixedSize.of("1", "2", "3", "4");
        List<String> oneA = new ArrayList<>(one);
        Verify.assertEqualsAndHashCode(one, oneA);
        Verify.assertPostSerializedEqualsAndHashCode(one);
    }

    @Test
    public void testRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.remove(0));
        this.assertUnchanged();
    }

    @Test
    public void testAddAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.add(0, "1"));
        this.assertUnchanged();
    }

    @Test
    public void testAdd()
    {
        Verify.assertThrows(UnsupportedOperationException.class, () -> this.list.add("1"));
        this.assertUnchanged();
    }

    @Test
    public void testAddingAllToOtherList()
    {
        List<String> newList = new ArrayList<>(this.list);
        newList.add("5");
        Verify.assertStartsWith(newList, "1", "2", "3", "4", "5");
    }

    @Test
    public void testGet()
    {
        Verify.assertStartsWith(this.list, "1", "2", "3", "4");
        Verify.assertThrows(IndexOutOfBoundsException.class, () -> this.list.get(4));
    }

    @Test
    public void testSet()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "2", "3", "4");
        Assert.assertEquals("1", list.set(0, "4"));
        Assert.assertEquals("2", list.set(1, "3"));
        Assert.assertEquals("3", list.set(2, "2"));
        Assert.assertEquals("4", list.set(3, "1"));
        Assert.assertEquals(FastList.newListWith("4", "3", "2", "1"), list);
        Verify.assertThrows(IndexOutOfBoundsException.class, () -> list.set(4, "0"));
    }

    private void assertUnchanged()
    {
        Verify.assertSize(4, this.list);
        Verify.assertNotContains("5", this.list);
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), this.list);
    }

    @Test
    public void testSerializableEqualsAndHashCode()
    {
        Verify.assertPostSerializedEqualsAndHashCode(this.list);
        MutableList<String> copyOfList = SerializeTestHelper.serializeDeserialize(this.list);
        Assert.assertNotSame(this.list, copyOfList);
    }

    @Test
    public void testGetFirstGetLast()
    {
        MutableList<String> list4 = Lists.fixedSize.of("1", "2", "3", "4");
        Assert.assertEquals("1", list4.getFirst());
        Assert.assertEquals("4", list4.getLast());
    }

    @Test
    public void testForEach()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableList<String> source = Lists.fixedSize.of("1", "2", "3", "4");
        source.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
    }

    @Test
    public void testForEachWithIndex()
    {
        int[] indexSum = new int[1];
        MutableList<String> result = Lists.mutable.of();
        MutableList<String> source = Lists.fixedSize.of("1", "2", "3", "4");
        source.forEachWithIndex((each, index) -> {
            result.add(each);
            indexSum[0] += index;
        });
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
        Assert.assertEquals(6, indexSum[0]);
    }

    @Test
    public void testForEachWith()
    {
        MutableList<String> result = Lists.mutable.of();
        MutableList<String> source = Lists.fixedSize.of("1", "2", "3", "4");
        source.forEachWith(Procedures2.fromProcedure(result::add), null);
        Assert.assertEquals(FastList.newListWith("1", "2", "3", "4"), result);
    }

    @Test
    public void testForLoop()
    {
        MutableList<String> list = Lists.fixedSize.of("one", "two", "three", "four");
        MutableList<String> upperList = Lists.fixedSize.of("ONE", "TWO", "THREE", "FOUR");
        for (String each : list)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
    }

    @Test
    public void testSubList()
    {
        MutableList<String> list = Lists.fixedSize.of("one", "two", "three", "four");
        MutableList<String> subList = list.subList(0, 3);
        MutableList<String> upperList = Lists.fixedSize.of("ONE", "TWO", "THREE", "FOUR");
        for (String each : subList)
        {
            Verify.assertContains(each.toUpperCase(), upperList);
        }
    }

    @Test
    public void without()
    {
        MutableList<Integer> list = new QuadrupletonList<>(1, 2, 3, 2);
        Assert.assertSame(list, list.without(9));
        list = list.without(2);
        Verify.assertListsEqual(FastList.newListWith(1, 3, 2), list);
        Verify.assertInstanceOf(TripletonList.class, list);
    }
}
