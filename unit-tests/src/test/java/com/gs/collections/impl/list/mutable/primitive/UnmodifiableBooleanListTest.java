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
import com.gs.collections.impl.collection.mutable.primitive.AbstractUnmodifiableBooleanCollectionTestCase;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableBooleanList}.
 */
public class UnmodifiableBooleanListTest extends AbstractUnmodifiableBooleanCollectionTestCase
{
    @Override
    protected final MutableBooleanList classUnderTest()
    {
        return new UnmodifiableBooleanList(BooleanArrayList.newListWith(true, false, true));
    }

    @Override
    protected MutableBooleanList getEmptyCollection()
    {
        return new UnmodifiableBooleanList(new BooleanArrayList());
    }

    @Override
    protected MutableBooleanList getEmptyModifiableCollection()
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
        return new UnmodifiableBooleanList(BooleanArrayList.newListWith(elements));
    }

    @Override
    protected MutableBooleanList newModifiableCollectionWith(boolean... elements)
    {
        return BooleanArrayList.newListWith(elements);
    }

    @Override
    protected MutableCollection<Object> newObjectCollectionWith(Object... elements)
    {
        return FastList.newListWith(elements);
    }

    @Override
    protected MutableBooleanList newSynchronizedCollectionWith(boolean... elements)
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(elements));
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
        MutableBooleanList singleItemList = new UnmodifiableBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getFirst());
        Assert.assertTrue(this.list.getFirst());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFirst_emptyList_throws()
    {
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        emptyList.getFirst();
    }

    @Test
    public void getLast()
    {
        MutableBooleanList singleItemList = new UnmodifiableBooleanList(BooleanArrayList.newListWith(false));
        Assert.assertFalse(singleItemList.getLast());
        Assert.assertTrue(this.list.getLast());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getLast_emptyList_throws()
    {
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        emptyList.getLast();
    }

    @Test
    public void indexOf()
    {
        MutableBooleanList arrayList = new UnmodifiableBooleanList(BooleanArrayList.newListWith(false, true, false));
        Assert.assertEquals(0L, arrayList.indexOf(false));
        Assert.assertEquals(1L, arrayList.indexOf(true));
    }

    @Test
    public void lastIndexOf()
    {
        MutableBooleanList arrayList = new UnmodifiableBooleanList(BooleanArrayList.newListWith(true, false, true));
        Assert.assertEquals(2L, arrayList.lastIndexOf(true));
        Assert.assertEquals(1L, arrayList.lastIndexOf(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAtIndex()
    {
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(0, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        emptyList.addAtIndex(1, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAtIndex_throws_index_negative()
    {
        this.list.addAtIndex(-1, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAll_throws_index_negative()
    {
        this.list.addAllAtIndex(-1, true, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAll_throws_index_greater_than_size()
    {
        this.list.addAllAtIndex(5, true, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable_throws_index_negative()
    {
        this.list.addAllAtIndex(-1, BooleanArrayList.newListWith(true, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable_throws_index_greater_than_size()
    {
        this.list.addAllAtIndex(5, BooleanArrayList.newListWith(true, true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAtIndex()
    {
        this.list.removeAtIndex(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAtIndex_throws_index_greater_than_size()
    {
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        emptyList.removeAtIndex(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAtIndex_throws_index_negative()
    {
        this.list.removeAtIndex(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void set()
    {
        this.list.set(1, true);
    }

    @Override
    @Test
    public void iterator()
    {
        BooleanIterator iterator = this.list.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void reverseThis()
    {
        new UnmodifiableBooleanList(new BooleanArrayList()).reverseThis();
    }

    @Test
    public void toReversed()
    {
        Assert.assertEquals(new BooleanArrayList(), new UnmodifiableBooleanList(new BooleanArrayList()).toReversed());
        MutableBooleanList emptyList = new UnmodifiableBooleanList(new BooleanArrayList());
        Assert.assertNotSame(emptyList, emptyList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false),
                new UnmodifiableBooleanList(BooleanArrayList.newListWith(false, false, true, true)).toReversed());
        MutableBooleanList evenList = new UnmodifiableBooleanList(BooleanArrayList.newListWith(true, false, true, false));
        Assert.assertNotSame(evenList, evenList.toReversed());
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false, false),
                new UnmodifiableBooleanList(BooleanArrayList.newListWith(false, false, true, false, true)).toReversed());
    }

    @Override
    @Test
    public void toArray()
    {
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
        Assert.assertEquals("[true, false, true]", this.list.toString());
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
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), this.classUnderTest().toList());
    }
}
