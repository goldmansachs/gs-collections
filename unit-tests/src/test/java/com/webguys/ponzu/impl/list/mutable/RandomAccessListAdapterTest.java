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

package com.webguys.ponzu.impl.list.mutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.test.SerializeTestHelper;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link RandomAccessListAdapter}.
 */
public class RandomAccessListAdapterTest extends AbstractListTestCase
{
    @Override
    protected <T> RandomAccessListAdapter<T> classUnderTest()
    {
        return new RandomAccessListAdapter<T>(Collections.<T>synchronizedList(new ArrayList<T>()));
    }

    @Test
    public void testAsSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, RandomAccessListAdapter.adapt(Collections.singletonList("1")).asSynchronized());
    }

    @Override
    @Test
    public void testClone()
    {
        MutableList<Integer> list = this.<Integer>classUnderTest().with(1, 2, 3);
        MutableList<Integer> list2 = list.clone();
        Verify.assertListsEqual(list, list2);
    }

    @Test
    public void testEqualsAndHashCode()
    {
        MutableList<Integer> list1 = this.<Integer>classUnderTest().with(1, 2, 3);
        MutableList<Integer> list2 = this.<Integer>classUnderTest().with(1, 2, 3);
        MutableList<Integer> list3 = this.<Integer>classUnderTest().with(2, 3, 4);
        Verify.assertNotEquals(list1, null);
        Verify.assertEqualsAndHashCode(list1, list1);
        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertNotEquals(list2, list3);
    }

    @Test
    @Override
    public void subList()
    {
        MutableList<String> list = this.newWith("A", "B", "C", "D");
        MutableList<String> sublist = list.subList(1, 3);
        Verify.assertEqualsAndHashCode(sublist, sublist);
        Verify.assertSize(2, sublist);
        Verify.assertContainsAll(sublist, "B", "C");
        sublist.add("X");
        Verify.assertSize(3, sublist);
        Verify.assertContainsAll(sublist, "B", "C", "X");
        Verify.assertSize(5, list);
        Verify.assertContainsAll(list, "A", "B", "C", "X", "D");
        sublist.remove("X");
        Verify.assertContainsAll(sublist, "B", "C");
        Verify.assertContainsAll(list, "A", "B", "C", "D");
        Assert.assertEquals("C", sublist.set(1, "R"));
        Verify.assertContainsAll(sublist, "B", "R");
        Verify.assertContainsAll(list, "A", "B", "R", "D");
        sublist.addAll(Arrays.asList("W", "G"));
        Verify.assertContainsAll(sublist, "B", "R", "W", "G");
        Verify.assertContainsAll(list, "A", "B", "R", "W", "G", "D");
        sublist.clear();
        Verify.assertEmpty(sublist);
        Verify.assertContainsAll(list, "A", "D");
    }

    @Test
    public void testNewListWithSize()
    {
        MutableList<Integer> collection = this.<Integer>classUnderTest().with(1, 2, 3);
        Verify.assertContainsAll(collection, 1, 2, 3);
    }

    @Test
    public void testSerialization()
    {
        MutableList<Integer> collection = this.<Integer>classUnderTest().with(1, 2, 3, 4, 5);
        MutableList<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertContainsAll(deserializedCollection, 1, 2, 3, 4, 5);
        Assert.assertEquals(collection, deserializedCollection);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = this.<Integer>classUnderTest().with(1, 2, 3, 4);
        collection.forEach(2, 3, CollectionAddProcedure.on(result));
        Verify.assertSize(2, result);
        Verify.assertContainsAll(result, 3, 4);
    }

    @Override
    @Test
    public void remove()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3, null);
        objects.removeIf(Predicates.isNull());
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
    }

    @Test
    public void testRemoveIndex()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        objects.remove(2);
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);
    }

    @Test
    public void testIndexOf()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Test
    public void testLastIndexOf()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        Assert.assertEquals(1, objects.lastIndexOf(2));
    }

    @Test
    public void testSet()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), objects.set(1, 4));
        Verify.assertItemAtIndex(4, 1, objects);
    }

    @Test
    public void testAddAtIndex()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        objects.add(0, 0);
        Verify.assertSize(4, objects);
        Verify.assertItemAtIndex(0, 0, objects);
    }

    @Test
    public void testAddAllAtIndex()
    {
        MutableList<Integer> objects = this.<Integer>classUnderTest().with(1, 2, 3);
        objects.addAll(0, Lists.fixedSize.of(0));
        Verify.assertSize(4, objects);
        Verify.assertItemAtIndex(0, 0, objects);
    }

    @Test
    public void testWithMethods()
    {
        Verify.assertContainsAll(this.classUnderTest().with(1), 1);
        Verify.assertContainsAll(this.classUnderTest().with(1, 2), 1, 2);
        Verify.assertContainsAll(this.classUnderTest().with(1, 2, 3), 1, 2, 3);
        Verify.assertContainsAll(this.classUnderTest().with(1, 2, 3, 4), 1, 2, 3, 4);
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(MutableList.class, this.classUnderTest().newEmpty());
    }

    @Test
    public void testForEachWithIndexWithFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        RandomAccessListAdapter<Integer> integers = this.classUnderTest();
        integers.with(1, 2, 3).forEachWithIndex(1, 2, new AddToList(result));
        Assert.assertEquals(FastList.newListWith(2, 3), result);
    }

    @Test
    public void adaptNull()
    {
        Verify.assertThrows(
                NullPointerException.class,
                new Runnable()
                {
                    public void run()
                    {
                        new RandomAccessListAdapter<Object>(null);
                    }
                });

        Verify.assertThrows(
                NullPointerException.class,
                new Runnable()
                {
                    public void run()
                    {
                        RandomAccessListAdapter.adapt(null);
                    }
                });
    }
}
