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

package com.gs.collections.impl.list.mutable.primitive;

import java.lang.reflect.Field;
import java.util.BitSet;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanArrayList}.
 */
public class BooleanArrayListTest
{
    private final BooleanArrayList list = BooleanArrayList.newListWith(true, true, false);

    @Test
    public void testBooleanArrayListWithInitialCapacity() throws Exception
    {
        BooleanArrayList arrayList = new BooleanArrayList(7);
        Assert.assertTrue(arrayList.isEmpty());
        Field items = BooleanArrayList.class.getDeclaredField("items");
        items.setAccessible(true);
        Assert.assertEquals(64L, ((BitSet) items.get(arrayList)).size());
        BooleanArrayList arrayList1 = new BooleanArrayList(64);
        Assert.assertEquals(64L, ((BitSet) items.get(arrayList1)).size());
        BooleanArrayList arrayList2 = new BooleanArrayList(65);
        Assert.assertEquals(128L, ((BitSet) items.get(arrayList2)).size());
    }

    @Test
    public void newListWith()
    {
        Assert.assertEquals(3L, this.list.size());
        Assert.assertTrue(this.list.containsAll(true, true, false));
    }

    @Test
    public void newList()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false), BooleanArrayList.newList(this.list));
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(new BooleanArrayList().isEmpty());
        Assert.assertFalse(BooleanArrayList.newListWith(false).isEmpty());
        Assert.assertFalse(this.list.isEmpty());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(new BooleanArrayList().notEmpty());
        Assert.assertTrue(BooleanArrayList.newListWith(false).notEmpty());
        Assert.assertTrue(this.list.notEmpty());
    }

    @Test
    public void clear()
    {
        this.list.clear();
        Assert.assertEquals(0L, this.list.size());
        Assert.assertFalse(this.list.contains(true));
        Assert.assertFalse(this.list.contains(false));
        BooleanArrayList emptyArrayList = new BooleanArrayList();
        emptyArrayList.clear();
        Assert.assertEquals(0L, emptyArrayList.size());
        Assert.assertFalse(emptyArrayList.contains(true));
        Assert.assertFalse(emptyArrayList.contains(false));
    }

    @Test
    public void contains()
    {
        BooleanArrayList emptyArrayList = new BooleanArrayList();
        Assert.assertFalse(emptyArrayList.contains(true));
        Assert.assertFalse(emptyArrayList.contains(false));
        Assert.assertTrue(this.list.contains(true));
        Assert.assertTrue(this.list.contains(false));
        Assert.assertFalse(BooleanArrayList.newListWith(true, true, true).contains(false));
        Assert.assertFalse(BooleanArrayList.newListWith(false, false, false).contains(true));
    }

    @Test
    public void containsAll()
    {
        BooleanArrayList emptyArrayList = new BooleanArrayList();
        Assert.assertFalse(emptyArrayList.containsAll(true));
        Assert.assertFalse(emptyArrayList.containsAll(false));
        Assert.assertTrue(this.list.containsAll(true));
        Assert.assertTrue(this.list.containsAll(false));
        Assert.assertTrue(this.list.containsAll(true, false));
        BooleanArrayList trueArrayList = BooleanArrayList.newListWith(true, true, true, true);
        Assert.assertFalse(trueArrayList.containsAll(true, false));
        BooleanArrayList falseArrayList = BooleanArrayList.newListWith(false, false, false, false);
        Assert.assertFalse(falseArrayList.containsAll(true, false));
    }

    @Test
    public void get()
    {
        Assert.assertTrue(this.list.get(0));
        Assert.assertTrue(this.list.get(1));
        Assert.assertFalse(this.list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_greater_than_size()
    {
        this.list.get(3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_empty_list()
    {
        BooleanArrayList.newListWith().get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throws_index_negative()
    {
        this.list.get(-1);
    }

    @Test
    public void getFirst()
    {
        BooleanArrayList singleItemList = BooleanArrayList.newListWith(true);
        Assert.assertTrue(singleItemList.getFirst());
        Assert.assertTrue(this.list.getFirst());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFirst_emptyList_throws()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        emptyList.getFirst();
    }

    @Test
    public void getLast()
    {
        BooleanArrayList singleItemList = BooleanArrayList.newListWith(true);
        Assert.assertTrue(singleItemList.getLast());
        Assert.assertFalse(this.list.getLast());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getLast_emptyList_throws()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        emptyList.getLast();
    }

    @Test
    public void indexOf()
    {
        BooleanArrayList arrayList = BooleanArrayList.newListWith(true, false, true);
        Assert.assertEquals(0L, arrayList.indexOf(true));
        Assert.assertEquals(1L, arrayList.indexOf(false));
        Assert.assertEquals(-1L, BooleanArrayList.newListWith(false, false).indexOf(true));
        BooleanArrayList emptyList = new BooleanArrayList();
        Assert.assertEquals(-1L, emptyList.indexOf(true));
        Assert.assertEquals(-1L, emptyList.indexOf(false));
    }

    @Test
    public void lastIndexOf()
    {
        BooleanArrayList arrayList = BooleanArrayList.newListWith(true, false, true);
        Assert.assertEquals(2L, arrayList.lastIndexOf(true));
        Assert.assertEquals(1L, arrayList.lastIndexOf(false));
        Assert.assertEquals(-1L, BooleanArrayList.newListWith(false, false).lastIndexOf(true));
        BooleanArrayList emptyList = new BooleanArrayList();
        Assert.assertEquals(-1L, emptyList.lastIndexOf(true));
        Assert.assertEquals(-1L, emptyList.lastIndexOf(false));
    }

    @Test
    public void add()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        Assert.assertTrue(emptyList.add(true));
        Assert.assertEquals(BooleanArrayList.newListWith(true), emptyList);
        BooleanArrayList arrayList = this.list;
        Assert.assertTrue(arrayList.add(false));
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false), arrayList);
    }

    @Test
    public void addAtIndex()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        emptyList.addAtIndex(0, false);
        Assert.assertEquals(BooleanArrayList.newListWith(false), emptyList);
        BooleanArrayList arrayList = this.list;
        arrayList.addAtIndex(3, true);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true), arrayList);
        arrayList.addAtIndex(2, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, false, true), arrayList);
    }

    @Test
    public void addAtIndexAtCapacity() throws Exception
    {
        BooleanArrayList listWithCapacity = new BooleanArrayList(64);
        for (int i = 0; i < 64; i++)
        {
            listWithCapacity.add((i & 1) == 0);
        }
        listWithCapacity.addAtIndex(64, true);
        Field items = BooleanArrayList.class.getDeclaredField("items");
        items.setAccessible(true);
        Assert.assertEquals(128L, ((BitSet) items.get(listWithCapacity)).size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_greater_than_size()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        emptyList.addAtIndex(1, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAtIndex_throws_index_negative()
    {
        this.list.addAtIndex(-1, true);
    }

    @Test
    public void addAll()
    {
        Assert.assertFalse(this.list.addAll());
        Assert.assertFalse(this.list.addAllAtIndex(1));
        Assert.assertTrue(this.list.addAll(true, false, true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true, false, true), this.list);
        Assert.assertTrue(this.list.addAllAtIndex(4, false, true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true, false, true, false, true), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_negative()
    {
        this.list.addAllAtIndex(-1, false, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_greater_than_size()
    {
        this.list.addAllAtIndex(5, false, true);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAll_throws_index_greater_than_size_empty_list()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
        emptyList.addAllAtIndex(1, false);
    }

    @Test
    public void remove()
    {
        Assert.assertFalse(BooleanArrayList.newListWith(true, true).remove(false));
        Assert.assertTrue(this.list.remove(true));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), this.list);
    }

    @Test
    public void removeAll()
    {
        Assert.assertFalse(this.list.removeAll());
        BooleanArrayList booleanArrayList = BooleanArrayList.newListWith(false, false);
        Assert.assertFalse(booleanArrayList.removeAll(true));
        Assert.assertEquals(BooleanArrayList.newListWith(false, false), booleanArrayList);
        Assert.assertTrue(this.list.removeAll(true, false));
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.list);
        Assert.assertTrue(this.list.removeAll(true));
        Assert.assertEquals(new BooleanArrayList(), this.list);
    }

    @Test
    public void removeAllIterable()
    {
        Assert.assertFalse(this.list.removeAll(new BooleanArrayList()));
        BooleanArrayList booleanArrayList = BooleanArrayList.newListWith(false, false);
        Assert.assertFalse(booleanArrayList.removeAll(new BooleanArrayList(true)));
        Assert.assertEquals(BooleanArrayList.newListWith(false, false), booleanArrayList);
        Assert.assertTrue(this.list.removeAll(new BooleanArrayList(true)));
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), this.list);
        Assert.assertTrue(this.list.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanArrayList(), this.list);
        Assert.assertFalse(this.list.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanArrayList(), this.list);
    }

    @Test
    public void removeAtIndex()
    {
        this.list.removeAtIndex(1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), this.list);
        this.list.removeAtIndex(1);
        Assert.assertEquals(BooleanArrayList.newListWith(true), this.list);
        this.list.removeAtIndex(0);
        Assert.assertEquals(BooleanArrayList.newListWith(), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeAtIndex_throws_index_greater_than_size()
    {
        BooleanArrayList emptyList = new BooleanArrayList();
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
        this.list.set(1, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, false), this.list);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void set_throws_index_greater_than_size()
    {
        new BooleanArrayList().set(1, false);
    }

    @Test
    public void with()
    {
        BooleanArrayList arrayList = new BooleanArrayList().with(true);
        BooleanArrayList arrayList0 = new BooleanArrayList().with(false, false);
        BooleanArrayList arrayList1 = new BooleanArrayList().with(true, true, false);
        BooleanArrayList arrayList2 = new BooleanArrayList().with(true, true, false, true);
        BooleanArrayList arrayList3 = new BooleanArrayList().with(true, true, false, true, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true), arrayList);
        Assert.assertEquals(BooleanArrayList.newListWith(false, false), arrayList0);
        Assert.assertEquals(this.list, arrayList1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true), arrayList2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, false, true, false), arrayList3);
    }

    @Test
    public void withAll()
    {
        BooleanArrayList arrayList = new BooleanArrayList().withAll(BooleanArrayList.newListWith(true));
        BooleanArrayList arrayList0 = new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false));
        BooleanArrayList arrayList1 = new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, true, false));
        BooleanArrayList arrayList2 = new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false, true, false));
        BooleanArrayList arrayList3 = new BooleanArrayList().withAll(BooleanArrayList.newListWith(true, false, false, true, false));
        Assert.assertEquals(BooleanArrayList.newListWith(true), arrayList);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), arrayList0);
        Assert.assertEquals(this.list, arrayList1);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true, false), arrayList2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, false, true, false), arrayList3);
    }

    @Test
    public void without()
    {
        BooleanArrayList mainArrayList = new BooleanArrayList(true, false, false);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, true, true, true), BooleanArrayList.newListWith(true, true, true, true, true).without(false));
        Assert.assertEquals(BooleanArrayList.newListWith(false, false), mainArrayList.without(true));
        Assert.assertEquals(new BooleanArrayList(false), mainArrayList.without(false));
        Assert.assertEquals(new BooleanArrayList(), mainArrayList.without(false));
        Assert.assertEquals(new BooleanArrayList(), mainArrayList.without(true));
    }

    @Test
    public void withoutAll()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, true, true), BooleanArrayList.newListWith(true, true, true).withoutAll(BooleanArrayList.newListWith(false)));
        BooleanArrayList mainArrayList = new BooleanArrayList(true, false, false, true, false);
        Assert.assertEquals(BooleanArrayList.newListWith(false, true, false), mainArrayList.withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanArrayList(false), mainArrayList.withoutAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(new BooleanArrayList(), mainArrayList.withoutAll(BooleanArrayList.newListWith(false)));
        Assert.assertEquals(new BooleanArrayList(), mainArrayList.withoutAll(BooleanArrayList.newListWith(true)));
    }

    @Test
    public void iterator()
    {
        BooleanIterator iterator = this.list.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanIterator iterator = this.list.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }

        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_emptyList()
    {
        BooleanIterator iterator = BooleanArrayList.newListWith().booleanIterator();
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_non_empty_list()
    {
        BooleanArrayList arrayList = new BooleanArrayList();
        arrayList.add(true);
        arrayList.add(true);
        arrayList.add(false);
        BooleanIterator iterator = arrayList.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }
        iterator.next();
    }

    @Test
    public void forEach()
    {
        final String[] sum = new String[2];
        sum[0] = "";
        sum[1] = "";
        this.list.forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each + " ";
            }
        });
        new BooleanArrayList().forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[1] += each;
            }
        });
        Assert.assertEquals("true true false ", sum[0]);
        Assert.assertEquals("", sum[1]);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(0L, new BooleanArrayList().size());
        Assert.assertEquals(1L, BooleanArrayList.newListWith(false).size());
        Assert.assertEquals(3L, this.list.size());
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.list.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, new BooleanArrayList().count(BooleanPredicates.isFalse()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.list.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(BooleanArrayList.newListWith(true, true).anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(new BooleanArrayList().anySatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.list.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(new BooleanArrayList().allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(new BooleanArrayList().allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(BooleanArrayList.newListWith(false, false).allSatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(2L, this.list.select(BooleanPredicates.isTrue()).size());
        Assert.assertEquals(1L, this.list.select(BooleanPredicates.isFalse()).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(1L, this.list.reject(BooleanPredicates.isTrue()).size());
        Assert.assertEquals(2L, this.list.reject(BooleanPredicates.isFalse()).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertTrue(this.list.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(this.list.detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));
    }

    @Test
    public void collect()
    {
        BooleanToObjectFunction<Boolean> booleanToObjectFunction = new BooleanToObjectFunction<Boolean>()
        {
            public Boolean valueOf(boolean parameter)
            {
                return !parameter;
            }
        };
        Assert.assertEquals(FastList.newListWith(false, false, true), this.list.collect(booleanToObjectFunction));
        Assert.assertEquals(FastList.newListWith(), new BooleanArrayList().collect(booleanToObjectFunction));
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(0L, new BooleanArrayList().toArray().length);
        Assert.assertEquals(3L, this.list.toArray().length);
        Assert.assertTrue(this.list.toArray()[0]);
        Assert.assertTrue(this.list.toArray()[1]);
        Assert.assertFalse(this.list.toArray()[2]);
    }

    @Test
    public void reverseThis()
    {
        Verify.assertEquals(BooleanArrayList.newListWith(true, true, false, false), BooleanArrayList.newListWith(false, false, true, true).reverseThis());
        BooleanArrayList originalList = BooleanArrayList.newListWith(true, true, false, false);
        Verify.assertSame(originalList, originalList.reverseThis());
    }

    @Test
    public void toReversed()
    {
        Verify.assertEquals(BooleanArrayList.newListWith(true, true, false, false), BooleanArrayList.newListWith(false, false, true, true).toReversed());
        BooleanArrayList originalList = BooleanArrayList.newListWith(true, true, false, false);
        Verify.assertNotSame(originalList, originalList.toReversed());
    }

    @Test
    public void testEquals()
    {
        BooleanArrayList list1 = BooleanArrayList.newListWith(true, false, true, true);
        BooleanArrayList list2 = BooleanArrayList.newListWith(true, false, true, true);
        BooleanArrayList list3 = BooleanArrayList.newListWith(true, true, false, true);
        BooleanArrayList list4 = BooleanArrayList.newListWith(false, false, false, true);
        BooleanArrayList list5 = BooleanArrayList.newListWith(false, false, false);

        Verify.assertEqualsAndHashCode(list1, list2);
        Verify.assertEqualsAndHashCode(new BooleanArrayList(), new BooleanArrayList());
        Verify.assertPostSerializedEqualsAndHashCode(list1);
        Verify.assertPostSerializedEqualsAndHashCode(new BooleanArrayList());
        Verify.assertNotEquals(list1, list3);
        Verify.assertNotEquals(list1, list4);
        Verify.assertNotEquals(list1, list5);
    }

    @Test
    public void testHashCode()
    {
        BitSet bitSet = new BitSet();
        bitSet.set(0, 2);
        Assert.assertEquals(bitSet.hashCode(), BooleanArrayList.newListWith(true, true, false).hashCode());
        Assert.assertEquals(new BitSet().hashCode(), BooleanArrayList.newListWith().hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[true, true, false]", this.list.toString());
        Assert.assertEquals("[]", new BooleanArrayList().toString());
    }

    @Test
    public void makeString()
    {
        Assert.assertEquals("true, true, false", this.list.makeString());
        Assert.assertEquals("true", BooleanArrayList.newListWith(true).makeString("/"));
        Assert.assertEquals("true/true/false", this.list.makeString("/"));
        Assert.assertEquals(this.list.toString(), this.list.makeString("[", ", ", "]"));
        Assert.assertEquals("", new BooleanArrayList().makeString());
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        new BooleanArrayList().appendString(appendable);
        Assert.assertEquals("", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        this.list.appendString(appendable2);
        Assert.assertEquals("true, true, false", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        this.list.appendString(appendable3, "/");
        Assert.assertEquals("true/true/false", appendable3.toString());
        StringBuilder appendable4 = new StringBuilder();
        this.list.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(this.list.toString(), appendable4.toString());
    }
}
