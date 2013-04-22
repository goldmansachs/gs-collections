/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.list.mutable.primitive;

import java.util.Arrays;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.collection.mutable.primitive.AbstractSynchronizedBooleanCollectionTestCase;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanList}.
 */
public class SynchronizedBooleanListTest extends AbstractSynchronizedBooleanCollectionTestCase
{
    @Override
    protected final MutableBooleanList classUnderTest()
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true));
    }

    @Override
    protected MutableBooleanCollection classUnderTestWithLock()
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true), new Object());
    }

    @Override
    protected MutableBooleanList getEmptyCollection()
    {
        return new SynchronizedBooleanList(new BooleanArrayList());
    }

    @Override
    protected MutableBooleanList getEmptyUnSynchronizedCollection()
    {
        return new BooleanArrayList();
    }

    @Override
    protected MutableCollection<Boolean> getEmptyObjectCollection()
    {
        return FastList.newList();
    }

    @Override
    protected MutableBooleanList newWith(boolean... elements)
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(elements));
    }

    @Override
    protected BooleanArrayList newUnSynchronizedCollectionWith(boolean... elements)
    {
        return BooleanArrayList.newListWith(elements);
    }

    @Override
    protected MutableCollection<Object> newObjectCollectionWith(Object... elements)
    {
        return FastList.newListWith(elements);
    }

    private final MutableBooleanList list = this.classUnderTest();

    @Test
    public void get()
    {
        Assert.assertTrue(this.list.get(0));
        Assert.assertFalse(this.list.get(1));
        Assert.assertTrue(this.list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_greater_than_size()
    {
        this.list.get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_negative()
    {
        this.list.get(-1);
    }

    @Test
    public void getFirst()
    {
        MutableBooleanList singleItemList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getFirst());
        Assert.assertTrue(this.list.getFirst());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFirst_emptyList_throws()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.getFirst();
    }

    @Test
    public void getLast()
    {
        MutableBooleanList singleItemList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getLast());
        Assert.assertTrue(this.list.getLast());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getLast_emptyList_throws()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.getLast();
    }

    @Test
    public void indexOf()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(BooleanArrayList.newListWith(false, true, false));
        Assert.assertEquals(0L, arrayList.indexOf(false));
        Assert.assertEquals(1L, arrayList.indexOf(true));
    }

    @Test
    public void lastIndexOf()
    {
        MutableBooleanList arrayList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true));
        Assert.assertEquals(2L, arrayList.lastIndexOf(true));
        Assert.assertEquals(1L, arrayList.lastIndexOf(false));
    }

    @Test
    public void addAtIndex()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(0, true);
        Assert.assertEquals(BooleanArrayList.newListWith(true), emptyList);
        MutableBooleanList arrayList = this.list;
        arrayList.addAtIndex(3, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false), arrayList);
        arrayList.addAtIndex(2, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, false, true, false), arrayList);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(1, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_negative()
    {
        this.list.addAtIndex(-1, true);
    }

    @Override
    @Test
    public void addAllArray()
    {
        super.addAllArray();
        Assert.assertFalse(this.list.addAllAtIndex(1));
        Assert.assertTrue(this.list.addAll(BooleanArrayList.newListWith(false, true, false)));
        Assert.assertTrue(this.list.addAllAtIndex(4, true, true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, true, true, false), this.list);
    }

    @Override
    @Test
    public void addAllIterable()
    {
        super.addAllIterable();
        Assert.assertFalse(this.list.addAllAtIndex(1));
        Assert.assertTrue(this.list.addAll(BooleanArrayList.newListWith(false, true, false)));
        Assert.assertTrue(this.list.addAllAtIndex(4, BooleanArrayList.newListWith(true, true)));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, true, true, true, false), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_negative()
    {
        this.list.addAllAtIndex(-1, true, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_greater_than_size()
    {
        this.list.addAllAtIndex(5, true, true);
    }

    @Test
    public void removeAtIndex()
    {
        this.list.removeAtIndex(1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        emptyList.removeAtIndex(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeAtIndex_throws_index_negative()
    {
        this.list.removeAtIndex(-1);
    }

    @Test
    public void set()
    {
        this.list.set(1, true);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, true), this.list);
    }

    @Override
    @Test
    public void iterator()
    {
        BooleanIterator iterator = this.classUnderTest().booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void reverseThis()
    {
        Assert.assertEquals(new BooleanArrayList(), new SynchronizedBooleanList(new BooleanArrayList()).reverseThis());
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        Assert.assertSame(emptyList, emptyList.reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true), new SynchronizedBooleanList(BooleanArrayList.newListWith(true)).reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), new SynchronizedBooleanList(BooleanArrayList.newListWith(false, true)).reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false), new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, true)).reverseThis());
        MutableBooleanList sameList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        Assert.assertSame(sameList, sameList.reverseThis());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false, true),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, false, true, true)).reverseThis());
    }

    @Test
    public void toReversed()
    {
        Assert.assertEquals(new BooleanArrayList(), new SynchronizedBooleanList(new BooleanArrayList()).toReversed());
        MutableBooleanList emptyList = new SynchronizedBooleanList(new BooleanArrayList());
        Assert.assertNotSame(emptyList, emptyList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, true)).toReversed());
        MutableBooleanList evenList = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        Assert.assertNotSame(evenList, evenList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, false),
                new SynchronizedBooleanList(BooleanArrayList.newListWith(false, false, true, false, true)).toReversed());
    }

    @Override
    @Test
    public void toArray()
    {
        super.toArray();
        Assert.assertTrue(Arrays.equals(new boolean[]{true, false, true, false},
                this.newWith(true, false, true, false).toArray()));
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        MutableBooleanCollection list1 = this.newWith(true, false, true, false);
        MutableBooleanCollection list2 = this.newWith(false, true, false, true);
        MutableBooleanCollection list3 = this.newWith(false, false, true, true);

        Assert.assertNotEquals(list1, list2);
        Assert.assertNotEquals(list1, list3);
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals("[true, false, true]", this.classUnderTest().toString());
    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();
        Assert.assertEquals(this.list.toString(), this.list.makeString("[", ", ", "]"));
        Assert.assertEquals("true, false, true", this.list.makeString());
        Assert.assertEquals("true/false/true", this.list.makeString("/"));
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        StringBuilder appendable2 = new StringBuilder();
        this.list.appendString(appendable2);
        Assert.assertEquals("true, false, true", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.list.appendString(appendable3, "/");
        Assert.assertEquals("true/false/true", appendable3.toString());
        StringBuilder appendable4 = new StringBuilder();
        this.list.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.list.toString(), appendable4.toString());
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), this.classUnderTest().toList());
    }
}
