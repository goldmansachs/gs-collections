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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.procedure.CollectionAddProcedure;
import com.webguys.ponzu.impl.collection.mutable.AbstractCollectionAdapter;
import com.webguys.ponzu.impl.collection.mutable.AbstractCollectionTestCase;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.fixed.ArrayAdapter;
import com.webguys.ponzu.impl.test.SerializeTestHelper;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JUnit test for {@link ArrayListAdapter}.
 */
public class ArrayListAdapterTest extends AbstractCollectionTestCase
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayListAdapterTest.class);

    @Override
    public <T> AbstractCollectionAdapter<T> classUnderTest()
    {
        return ArrayListAdapter.newList();
    }

    @Test
    public void testAsSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, ArrayListAdapter.newList().asSynchronized());
    }

    @Test
    public void testClone()
    {
        MutableList<Integer> list = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        MutableList<Integer> list2 = list.clone();
        Verify.assertListsEqual(list, list2);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = ArrayListAdapter.<Integer>newList(4).with(1, 2, 3, 4);
        collection.forEach(2, 3, CollectionAddProcedure.<Integer>on(result));
        Verify.assertSize(2, result);
        Verify.assertContainsAll(result, 3, 4);
    }

    @Test
    public void testNewListWithSize()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList(4).with(1, 2, 3);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Override
    @Test
    public void remove()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3, null);
        objects.removeIf(Predicates.isNull());
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableList.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void testRemoveIndex()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        objects.remove(2);
        Verify.assertSize(2, objects);
        Verify.assertContainsAll(objects, 1, 2);
    }

    @Test
    public void testIndexOf()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Test
    public void testLastIndexOf()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        Assert.assertEquals(1, objects.lastIndexOf(2));
    }

    @Test
    public void testSet()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), objects.set(1, 4));
        Verify.assertItemAtIndex(4, 1, objects);
    }

    @Test
    public void testAddAtIndex()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        objects.add(0, 0);
        Verify.assertSize(4, objects);
        Verify.assertItemAtIndex(0, 0, objects);
    }

    @Test
    public void testAddAllAtIndex()
    {
        MutableList<Integer> objects = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        objects.addAll(0, Lists.fixedSize.of(0));
        Verify.assertSize(4, objects);
        Verify.assertItemAtIndex(0, 0, objects);
    }

    @Test
    public void testEqualsAndHashCode()
    {
        MutableList<Integer> list1 = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4);
        MutableList<Integer> list2 = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4);
        MutableList<Integer> list3 = ArrayListAdapter.<Integer>newList().with(2, 3, 4);
        Verify.assertNotEquals(list1, null);
        Verify.assertEqualsAndHashCode(list1, list1);
        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertNotEquals(list2, list3);
        Verify.assertEqualsAndHashCode(list1, new ArrayList<Integer>(list1));
        Verify.assertEqualsAndHashCode(list1, new LinkedList<Integer>(list1));
        Verify.assertEqualsAndHashCode(list1, ArrayAdapter.<Integer>newArrayWith(1, 2, 3, 4));
        Verify.assertEqualsAndHashCode(list1, FastList.<Integer>newListWith(1, 2, 3, 4));
    }

    @Test
    public void testSerialization()
    {
        MutableList<Integer> collection = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4, 5);
        MutableList<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertStartsWith(deserializedCollection, 1, 2, 3, 4, 5);
        Verify.assertListsEqual(collection, deserializedCollection);
    }

    @Test
    public void testBAOSSize()
    {
        MutableList<Integer> mutableArrayList = ArrayListAdapter.newList();

        ByteArrayOutputStream stream1 = SerializeTestHelper.getByteArrayOutputStream(mutableArrayList);
        LOGGER.info("ArrayListAdapter size: " + stream1.size());
        LOGGER.info(stream1.toString());
        Assert.assertTrue(stream1.size() > 0);

        List<Integer> arrayList = new ArrayList<Integer>();
        ByteArrayOutputStream stream2 = SerializeTestHelper.getByteArrayOutputStream(arrayList);
        LOGGER.info("ArrayList size: " + stream2.size());
        LOGGER.info(stream2.toString());
        Assert.assertTrue(stream2.size() > 0);
    }

    @Test
    public void testWithMethods()
    {
        Verify.assertContainsAll(ArrayListAdapter.newList().with(1), 1);
        Verify.assertContainsAll(ArrayListAdapter.newList().with(1, 2), 1, 2);
        Verify.assertContainsAll(ArrayListAdapter.newList().with(1, 2, 3), 1, 2, 3);
        Verify.assertContainsAll(ArrayListAdapter.newList().with(1, 2, 3, 4), 1, 2, 3, 4);
    }

    @Test
    public void sortThis_small()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), actual);
    }

    @Test
    public void sortThis()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), actual);
    }

    @Test
    public void sortThis_large()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(Interval.oneTo(1000).toArray());
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(Interval.oneTo(1000), actual);
    }

    @Test
    public void sortThis_with_comparator_small()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(1, 2, 3);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), actual);
    }

    @Test
    public void sortThis_with_comparator()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(10, 9, 8, 7, 6, 5, 4, 3, 2, 1), actual);
    }

    @Test
    public void sortThis_with_comparator_large()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(Interval.oneTo(1000).toArray());
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(Interval.fromToBy(1000, 1, -1), actual);
    }

    @Test
    public void sortThisBy()
    {
        MutableList<Integer> actual = ArrayListAdapter.<Integer>newList().with(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThisBy(Functions.getToString());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 10, 2, 3, 4, 5, 6, 7, 8, 9), actual);
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(ArrayListAdapter.class, ArrayListAdapter.newList().newEmpty());
    }

    @Test
    public void testForEachWithFromTo()
    {
        MutableList<Integer> result1 = Lists.mutable.of();
        ArrayListAdapter.<Integer>newList().with(1, 2, 3).forEach(1, 2, CollectionAddProcedure.<Integer>on(result1));
        Assert.assertEquals(FastList.newListWith(2, 3), result1);
    }

    @Test
    public void testForEachWithIndexWithFromTo()
    {
        MutableList<Integer> result1 = Lists.mutable.of();
        ArrayListAdapter.<Integer>newList().with(1, 2, 3).forEachWithIndex(1, 2, new AddToList(result1));
        Assert.assertEquals(FastList.newListWith(2, 3), result1);
    }

    @Test
    public void testForEachWithFromToWithCommandoPatternOptimization()
    {
        MutableList<Integer> result2 = Lists.mutable.of();
        // Requires list of 100+ elements to engage commando pattern optimization
        ArrayListAdapter.<Integer>adapt(new ArrayList<Integer>(Interval.oneTo(200))).forEach(99, 199, CollectionAddProcedure.<Integer>on(result2));
        Verify.assertSize(101, result2);
    }

    @Test
    public void testForEachWithIndexWithFromToWithCommandoPatternOptimization()
    {
        MutableList<Integer> result2 = Lists.mutable.of();
        // Requires list of 100+ elements to engage commando pattern optimization
        ArrayListAdapter.<Integer>adapt(new ArrayList<Integer>(Interval.oneTo(200))).forEachWithIndex(99, 199, new AddToList(result2)
        );
        Verify.assertSize(101, result2);
    }
}
