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

package com.gs.collections.impl.list.mutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link MutableList}s.
 */
public abstract class AbstractListTestCase
        extends AbstractCollectionTestCase
{
    @Override
    protected abstract <T> MutableList<T> classUnderTest();

    @Override
    protected <T> MutableList<T> newWith(T one)
    {
        return (MutableList<T>) super.newWith(one);
    }

    @Override
    protected <T> MutableList<T> newWith(T one, T two)
    {
        return (MutableList<T>) super.newWith(one, two);
    }

    @Override
    protected <T> MutableList<T> newWith(T one, T two, T three)
    {
        return (MutableList<T>) super.newWith(one, two, three);
    }

    @Override
    protected <T> MutableList<T> newWith(T... littleElements)
    {
        return (MutableList<T>) super.newWith(littleElements);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedMutableList.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();
        Verify.assertInstanceOf(ImmutableList.class, this.classUnderTest().toImmutable());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableMutableList.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void testClone()
    {
        MutableList<Integer> list = this.newWith(1, 2, 3);
        MutableList<Integer> list2 = list.clone();
        Verify.assertListsEqual(list, list2);
        Verify.assertShallowClone(list);
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        MutableCollection<Integer> list1 = this.newWith(1, 2, 3);
        MutableCollection<Integer> list2 = this.newWith(1, 2, 3);
        MutableCollection<Integer> list3 = this.newWith(2, 3, 4);
        MutableCollection<Integer> list4 = this.newWith(1, 2, 3, 4);
        Verify.assertNotEquals(list1, null);
        Verify.assertEqualsAndHashCode(list1, list1);
        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertEqualsAndHashCode(new LinkedList<Integer>(Arrays.<Integer>asList(1, 2, 3)), list1);
        Verify.assertEqualsAndHashCode(new ArrayList<Integer>(Arrays.<Integer>asList(1, 2, 3)), list1);
        Verify.assertEqualsAndHashCode(ArrayAdapter.newArrayWith(1, 2, 3), list1);
        Verify.assertNotEquals(list2, list3);
        Verify.assertNotEquals(list2, list4);
        Verify.assertNotEquals(new LinkedList<Integer>(Arrays.<Integer>asList(1, 2, 3, 4)), list1);
        Verify.assertNotEquals(new LinkedList<Integer>(Arrays.<Integer>asList(1, 2, null)), list1);
        Verify.assertNotEquals(new LinkedList<Integer>(Arrays.<Integer>asList(1, 2)), list1);
        Verify.assertNotEquals(new ArrayList<Integer>(Arrays.<Integer>asList(1, 2, 3, 4)), list1);
        Verify.assertNotEquals(new ArrayList<Integer>(Arrays.<Integer>asList(1, 2, null)), list1);
        Verify.assertNotEquals(new ArrayList<Integer>(Arrays.<Integer>asList(1, 2)), list1);
        Verify.assertNotEquals(ArrayAdapter.newArrayWith(1, 2, 3, 4), list1);
    }

    @Test
    public void newListWithSize()
    {
        MutableList<Integer> list = this.newWith(1, 2, 3);
        Verify.assertContainsAll(list, 1, 2, 3);
    }

    @Test
    public void serialization()
    {
        MutableList<Integer> collection = this.newWith(1, 2, 3, 4, 5);
        MutableList<Integer> deserializedCollection = SerializeTestHelper.serializeDeserialize(collection);
        Verify.assertSize(5, deserializedCollection);
        Verify.assertContainsAll(deserializedCollection, 1, 2, 3, 4, 5);
        Assert.assertEquals(collection, deserializedCollection);
    }

    @Test
    public void forEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newWith(1, 2, 3, 4).forEach(2, 3, CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith(3, 4), result);
    }

    @Test
    public void forEachFromToInReverse()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newWith(1, 2, 3, 4).forEach(3, 2, CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith(4, 3), result);
    }

    @Test
    public void reverseForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> collection = this.newWith(1, 2, 3, 4);
        collection.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), result);
    }

    @Test
    public void reverseForEach_emptyList()
    {
        MutableList<Integer> integers = Lists.mutable.of();
        MutableList<Integer> results = Lists.mutable.of();
        integers.reverseForEach(CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
    }

    @Test
    public void reverseThis()
    {
        MutableList<Integer> original = this.newWith(1, 2, 3, 4);
        MutableList<Integer> reversed = original.reverseThis();
        Assert.assertEquals(FastList.newListWith(4, 3, 2, 1), reversed);
        Assert.assertSame(original, reversed);
    }

    @Test
    public void toReversed()
    {
        MutableList<Integer> original = this.newWith(1, 2, 3, 4);
        MutableList<Integer> actual = original.toReversed();
        MutableList<Integer> expected = this.newWith(4, 3, 2, 1);
        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(original, actual);
    }

    @Override
    @Test
    public void remove()
    {
        MutableCollection<Integer> objects = this.newWith(1, 2, 3, null);
        objects.removeIf(Predicates.isNull());
        Assert.assertEquals(FastList.newListWith(1, 2, 3), objects);
    }

    @Test
    public void removeIndex()
    {
        MutableList<Integer> objects = this.newWith(1, 2, 3);
        objects.remove(2);
        Assert.assertEquals(FastList.newListWith(1, 2), objects);
    }

    @Test
    public void indexOf()
    {
        MutableList<Integer> objects = this.newWith(1, 2, 2);
        Assert.assertEquals(1, objects.indexOf(2));
    }

    @Test
    public void lastIndexOf()
    {
        MutableList<Integer> objects = this.newWith(2, 2, 3);
        Assert.assertEquals(1, objects.lastIndexOf(2));
    }

    @Test
    public void set()
    {
        MutableList<Integer> objects = this.newWith(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), objects.set(1, 4));
        Assert.assertEquals(FastList.newListWith(1, 4, 3), objects);
    }

    @Test
    public void addAtIndex()
    {
        MutableList<Integer> objects = this.newWith(1, 2, 3);
        objects.add(0, 0);
        Assert.assertEquals(FastList.newListWith(0, 1, 2, 3), objects);
    }

    @Test
    public void addAllAtIndex()
    {
        MutableList<Integer> objects = this.newWith(1, 2, 3);
        objects.addAll(0, Lists.fixedSize.of(0));
        Integer one = -1;
        objects.addAll(0, new ArrayList<Integer>(Lists.fixedSize.of(one)));
        objects.addAll(0, FastList.newListWith(-2));
        objects.addAll(0, UnifiedSet.newSetWith(-3));
        Assert.assertEquals(FastList.newListWith(-3, -2, -1, 0, 1, 2, 3), objects);
    }

    @Test
    public void withMethods()
    {
        Verify.assertContainsAll(this.newWith(1), 1);
        Verify.assertContainsAll(this.newWith(1, 2), 1, 2);
        Verify.assertContainsAll(this.newWith(1, 2, 3), 1, 2, 3);
        Verify.assertContainsAll(this.newWith(1, 2, 3, 4), 1, 2, 3, 4);
    }

    @Test
    public void sortThis_with_null()
    {
        MutableList<Integer> integers = this.newWith(2, null, 3, 4, 1);
        Verify.assertStartsWith(integers.sortThis(Comparators.safeNullsLow(Comparators.<Integer>naturalOrder())), null, 1, 2, 3, 4);
    }

    @Test
    public void sortThis_small()
    {
        MutableList<Integer> actual = this.newWith(1, 2, 3);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), actual);
    }

    @Test
    public void sortThis()
    {
        MutableList<Integer> actual = this.newWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), actual);
    }

    @Test
    public void sortThis_large()
    {
        MutableList<Integer> actual = this.newWith(Interval.oneTo(1000).toArray());
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis();
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(Interval.oneTo(1000).toList(), actual);
    }

    @Test
    public void sortThis_with_comparator_small()
    {
        MutableList<Integer> actual = this.newWith(1, 2, 3);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), actual);
    }

    @Test
    public void sortThis_with_comparator()
    {
        MutableList<Integer> actual = this.newWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(10, 9, 8, 7, 6, 5, 4, 3, 2, 1), actual);
    }

    @Test
    public void sortThis_with_comparator_large()
    {
        MutableList<Integer> actual = this.newWith(Interval.oneTo(1000).toArray());
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThis(Collections.<Integer>reverseOrder());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(Interval.fromToBy(1000, 1, -1).toList(), actual);
    }

    @Test
    public void sortThisBy()
    {
        MutableList<Integer> actual = this.newWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(actual);
        MutableList<Integer> sorted = actual.sortThisBy(Functions.getToString());
        Assert.assertSame(actual, sorted);
        Assert.assertEquals(FastList.newListWith(1, 10, 2, 3, 4, 5, 6, 7, 8, 9), actual);
    }

    @Override
    @Test
    public void newEmpty()
    {
        Verify.assertInstanceOf(MutableList.class, this.classUnderTest().newEmpty());
    }

    @Override
    @Test
    public void testToString()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        list.add(list);
        Assert.assertEquals("[1, 2, 3, (this " + list.getClass().getSimpleName() + ")]", list.toString());
    }

    @Override
    @Test
    public void makeString()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        list.add(list);
        Assert.assertEquals("1, 2, 3, (this " + list.getClass().getSimpleName() + ')', list.makeString());
    }

    @Override
    @Test
    public void makeStringWithSeparator()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals("1/2/3", list.makeString("/"));
    }

    @Override
    @Test
    public void makeStringWithSeparatorAndStartAndEnd()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals("[1/2/3]", list.makeString("[", "/", "]"));
    }

    @Override
    @Test
    public void appendString()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        list.add(list);

        Appendable builder = new StringBuilder();
        list.appendString(builder);
        Assert.assertEquals("1, 2, 3, (this " + list.getClass().getSimpleName() + ')', builder.toString());
    }

    @Override
    @Test
    public void appendStringWithSeparator()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);

        Appendable builder = new StringBuilder();
        list.appendString(builder, "/");
        Assert.assertEquals("1/2/3", builder.toString());
    }

    @Override
    @Test
    public void appendStringWithSeparatorAndStartAndEnd()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);

        Appendable builder = new StringBuilder();
        list.appendString(builder, "[", "/", "]");
        Assert.assertEquals("[1/2/3]", builder.toString());
    }

    @Test
    public void forEachWithIndexWithFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newWith(1, 2, 3).forEachWithIndex(1, 2, new AddToList(result));
        Assert.assertEquals(FastList.newListWith(2, 3), result);
    }

    @Test
    public void forEachWithIndexWithFromToInReverse()
    {
        MutableList<Integer> result = Lists.mutable.of();
        this.newWith(1, 2, 3).forEachWithIndex(2, 1, new AddToList(result));
        Assert.assertEquals(FastList.newListWith(3, 2), result);
    }

    @Test(expected = NullPointerException.class)
    public void sortThisWithNullWithNoComparator()
    {
        MutableList<Integer> integers = this.newWith(2, null, 3, 4, 1);
        integers.sortThis();
    }

    @Test(expected = NullPointerException.class)
    public void sortThisWithNullWithNoComparatorOnListWithMoreThan10Elements()
    {
        MutableList<Integer> integers = this.newWith(2, null, 3, 4, 1, 5, 6, 7, 8, 9, 10, 11);
        integers.sortThis();
    }

    @Test(expected = NullPointerException.class)
    public void toSortedListWithNullWithNoComparator()
    {
        MutableList<Integer> integers = this.newWith(2, null, 3, 4, 1);
        integers.toSortedList();
    }

    @Test(expected = NullPointerException.class)
    public void toSortedListWithNullWithNoComparatorOnListWithMoreThan10Elements()
    {
        MutableList<Integer> integers = this.newWith(2, null, 3, 4, 1, 5, 6, 7, 8, 9, 10, 11);
        integers.toSortedList();
    }

    @Test
    public void forEachOnRange()
    {
        final MutableList<Integer> list = this.classUnderTest();

        list.addAll(FastList.newListWith(0, 1, 2, 3));
        list.addAll(FastList.newListWith(4, 5, 6));
        list.addAll(FastList.<Integer>newList());
        list.addAll(FastList.newListWith(7, 8, 9));

        this.validateForEachOnRange(list, 0, 0, FastList.newListWith(0));
        this.validateForEachOnRange(list, 3, 5, FastList.newListWith(3, 4, 5));
        this.validateForEachOnRange(list, 4, 6, FastList.newListWith(4, 5, 6));
        this.validateForEachOnRange(list, 9, 9, FastList.newListWith(9));
        this.validateForEachOnRange(list, 0, 9, FastList.newListWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                AbstractListTestCase.this.validateForEachOnRange(list, 10, 10, FastList.<Integer>newList());
            }
        });
    }

    protected void validateForEachOnRange(MutableList<Integer> list, int from, int to, List<Integer> expectedOutput)
    {
        final List<Integer> outputList = Lists.mutable.of();
        list.forEach(from, to, new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                outputList.add(each);
            }
        });

        Assert.assertEquals(expectedOutput, outputList);
    }

    @Test
    public void forEachWithIndexOnRange()
    {
        final MutableList<Integer> list = this.classUnderTest();

        list.addAll(FastList.newListWith(0, 1, 2, 3));
        list.addAll(FastList.newListWith(4, 5, 6));
        list.addAll(FastList.<Integer>newList());
        list.addAll(FastList.newListWith(7, 8, 9));

        this.validateForEachWithIndexOnRange(list, 0, 0, FastList.newListWith(0));
        this.validateForEachWithIndexOnRange(list, 3, 5, FastList.newListWith(3, 4, 5));
        this.validateForEachWithIndexOnRange(list, 4, 6, FastList.newListWith(4, 5, 6));
        this.validateForEachWithIndexOnRange(list, 9, 9, FastList.newListWith(9));
        this.validateForEachWithIndexOnRange(list, 0, 9, FastList.newListWith(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                AbstractListTestCase.this.validateForEachWithIndexOnRange(list, 10, 10, FastList.<Integer>newList());
            }
        });
    }

    protected void validateForEachWithIndexOnRange(
            MutableList<Integer> list,
            int from,
            int to,
            List<Integer> expectedOutput)
    {
        final MutableList<Integer> outputList = Lists.mutable.of();
        list.forEachWithIndex(from, to, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                outputList.add(each);
            }
        });

        Assert.assertEquals(expectedOutput, outputList);
    }

    @Test
    public void subList()
    {
        MutableList<String> list = this.newWith("A", "B", "C", "D");
        MutableList<String> sublist = list.subList(1, 3);
        Verify.assertPostSerializedEqualsAndHashCode(sublist);
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

    @Test(expected = IndexOutOfBoundsException.class)
    public void subListFromOutOfBoundsException()
    {
        this.newWith(1).subList(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subListToGreaterThanSizeException()
    {
        this.newWith(1).subList(0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void subListFromGreaterThanToException()
    {
        this.newWith(1).subList(1, 0);
    }

    @Test
    public void testGetWithIndexOutOfBoundsException()
    {
        final Object item = new Object();

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                AbstractListTestCase.this.newWith(item).get(1);
            }
        });
    }

    @Test
    public void testGetWithArrayIndexOutOfBoundsException()
    {
        final Object item = new Object();

        Verify.assertThrows(ArrayIndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                AbstractListTestCase.this.newWith(item).get(-1);
            }
        });
    }

    @Test
    public void listIterator()
    {
        int sum = 0;
        MutableList<Integer> integers = this.newWith(1, 2, 3, 4);
        for (Iterator<Integer> iterator = integers.listIterator(); iterator.hasNext(); )
        {
            Integer each = iterator.next();
            sum += each.intValue();
        }
        for (ListIterator<Integer> iterator = integers.listIterator(4); iterator.hasPrevious(); )
        {
            Integer each = iterator.previous();
            sum += each.intValue();
        }
        Assert.assertEquals(20, sum);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void listIteratorIndexTooSmall()
    {
        this.newWith(1).listIterator(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void listIteratorIndexTooBig()
    {
        this.newWith(1).listIterator(2);
    }

    @Override
    @Test
    public void chunk()
    {
        super.chunk();

        MutableCollection<String> collection = this.newWith("1", "2", "3", "4", "5", "6", "7");
        RichIterable<RichIterable<String>> groups = collection.chunk(2);
        Assert.assertEquals(
                FastList.<RichIterable<String>>newListWith(
                        FastList.newListWith("1", "2"),
                        FastList.newListWith("3", "4"),
                        FastList.newListWith("5", "6"),
                        FastList.newListWith("7")),
                groups);
    }
}
