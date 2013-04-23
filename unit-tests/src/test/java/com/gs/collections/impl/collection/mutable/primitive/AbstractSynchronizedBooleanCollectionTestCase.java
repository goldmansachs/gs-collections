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

package com.gs.collections.impl.collection.mutable.primitive;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link AbstractSynchronizedBooleanCollection}s.
 */
public abstract class AbstractSynchronizedBooleanCollectionTestCase
{
    protected abstract MutableBooleanCollection classUnderTest();

    protected abstract MutableBooleanCollection classUnderTestWithLock();

    protected abstract MutableBooleanCollection getEmptyCollection();

    protected abstract MutableBooleanCollection getEmptyUnSynchronizedCollection();

    protected abstract MutableCollection<Boolean> getEmptyObjectCollection();

    protected abstract MutableBooleanCollection newWith(boolean... elements);

    protected abstract MutableBooleanCollection newUnSynchronizedCollectionWith(boolean... elements);

    protected abstract MutableCollection<Object> newObjectCollectionWith(Object... elements);

    @Test
    public void newCollectionWith()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(3, collection);
        Assert.assertTrue(collection.containsAll(true, false, true));
    }

    @Test
    public void newCollection()
    {
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true), this.classUnderTest());
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.getEmptyCollection());
        Verify.assertNotEmpty(this.classUnderTest());
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.getEmptyCollection().notEmpty());
        Assert.assertTrue(this.classUnderTest().notEmpty());
    }

    @Test
    public void clear()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        collection.clear();
        Verify.assertSize(0, collection);
        Assert.assertFalse(collection.contains(true));
        Assert.assertFalse(collection.contains(false));
    }

    @Test
    public void containsAllArray()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.containsAll(true));
        Assert.assertTrue(collection.containsAll(true, false, true));
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.containsAll(true));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(true));
    }

    @Test
    public void containsAllIterable()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.containsAll(new BooleanArrayList()));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(true, false, true)));
    }

    @Test
    public void add()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.add(true));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true), emptyCollection);
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.add(false));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true, false), collection);
    }

    @Test
    public void addAllArray()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.addAll());
        Assert.assertTrue(collection.addAll(false, true, false));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true, false, true, false), collection);
    }

    @Test
    public void addAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.addAll(this.getEmptyUnSynchronizedCollection()));
        Assert.assertTrue(collection.addAll(this.newUnSynchronizedCollectionWith(false, true, false)));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true, false, true, false), collection);
    }

    @Test
    public void remove()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.remove(false));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, true), collection);
        Assert.assertFalse(collection.remove(false));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, true), collection);
        Assert.assertTrue(collection.remove(true));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true), collection);
    }

    @Test
    public void removeAll()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll());
        Assert.assertTrue(collection.removeAll(true));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(false), collection);
        Assert.assertTrue(collection.removeAll(false, true));
        Assert.assertEquals(this.getEmptyUnSynchronizedCollection(), collection);
    }

    @Test
    public void removeAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll(this.getEmptyUnSynchronizedCollection()));
        Assert.assertTrue(collection.removeAll(this.newUnSynchronizedCollectionWith(false)));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, true), collection);
        Assert.assertTrue(collection.removeAll(this.newUnSynchronizedCollectionWith(true, true)));
        Assert.assertEquals(this.getEmptyUnSynchronizedCollection(), collection);
    }

    @Test
    public void with()
    {
        MutableBooleanCollection arrayList = this.getEmptyCollection().with(true);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true), arrayList);
    }

    @Test
    public void withAll()
    {
        MutableBooleanCollection arrayList = this.getEmptyCollection().withAll(this.newUnSynchronizedCollectionWith(true));
        MutableBooleanCollection arrayList0 = this.getEmptyCollection().withAll(this.newUnSynchronizedCollectionWith(true, false));
        MutableBooleanCollection arrayList1 = this.getEmptyCollection().withAll(this.newUnSynchronizedCollectionWith(true, false, true));
        MutableBooleanCollection arrayList2 = this.getEmptyCollection().withAll(this.newUnSynchronizedCollectionWith(true, false, true, false));
        MutableBooleanCollection arrayList3 = this.getEmptyCollection().withAll(this.newUnSynchronizedCollectionWith(true, false, true, false, true));
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true), arrayList);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false), arrayList0);
        Assert.assertEquals(this.classUnderTest(), arrayList1);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true, false), arrayList2);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, false, true, false, true), arrayList3);
    }

    @Test
    public void without()
    {
        MutableBooleanCollection mainArrayList = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(false, true, false, true), mainArrayList.without(true));
    }

    @Test
    public void withoutAll()
    {
        MutableBooleanCollection mainArrayList = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newUnSynchronizedCollectionWith(true, true, true), mainArrayList.withoutAll(this.newUnSynchronizedCollectionWith(false, false)));
    }

    @Test
    public abstract void iterator();

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanIterator iterator = this.classUnderTest().booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }

        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_non_empty_list()
    {
        MutableBooleanCollection collection = this.getEmptyCollection();
        collection.add(true);
        collection.add(true);
        collection.add(true);
        BooleanIterator iterator = collection.booleanIterator();
        while (iterator.hasNext())
        {
            Assert.assertTrue(iterator.next());
        }
        iterator.next();
    }

    @Test
    public void forEach()
    {
        final long[] sum = new long[1];
        this.classUnderTest().forEach(new BooleanProcedure()
        {
            public void value(boolean each)
            {
                sum[0] += each ? 1 : 0;
            }
        });

        Assert.assertEquals(2L, sum[0]);
    }

    @Test
    public void size()
    {
        Verify.assertSize(0, this.getEmptyCollection());
        Verify.assertSize(3, this.classUnderTest());
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.newWith(true, false, true).count(BooleanPredicates.isTrue()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.newWith(true).anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.newWith(false).anySatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.newWith(true, false).allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(true, true, true).allSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(this.newWith(true, true).noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.newWith(true, true).noneSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void select()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(2, collection.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, collection.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(1, collection.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(2, collection.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(collection.detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(this.newObjectCollectionWith(1, 0, 1), this.classUnderTest().collect(new BooleanToObjectFunction<Object>()
        {
            public Object valueOf(boolean value)
            {
                return Integer.valueOf(value ? 1 : 0);
            }
        }));
    }

    @Test
    public void toArray()
    {
        Assert.assertTrue(Arrays.equals(new boolean[]{false, true}, this.newWith(true, false).toArray())
                || Arrays.equals(new boolean[]{true, false}, this.newWith(true, false).toArray()));
    }

    @Test
    public void testEquals()
    {
        MutableBooleanCollection collection1 = this.newWith(true, false, true, false);
        MutableBooleanCollection collection2 = this.newWith(true, false, true, false);
        MutableBooleanCollection collection3 = this.newWith(true, true, true);

        Verify.assertEqualsAndHashCode(collection1, collection2);
        Verify.assertPostSerializedEqualsAndHashCode(collection1);
        Assert.assertNotEquals(collection1, collection3);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(this.newObjectCollectionWith(true, false, true).hashCode(),
                this.newWith(true, false, true).hashCode());
        Assert.assertEquals(this.getEmptyObjectCollection().hashCode(), this.getEmptyCollection().hashCode());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals("[]", this.getEmptyCollection().toString());
        Assert.assertEquals("[true]", this.newWith(true).toString());
        MutableBooleanCollection collection = this.newWith(true, false);
        Assert.assertTrue("[true, false]".equals(collection.toString())
                || "[false, true]".equals(collection.toString()));
    }

    @Test
    public void makeString()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertEquals("true", this.newWith(true).makeString("/"));
        Assert.assertEquals("", this.getEmptyCollection().makeString());
        MutableBooleanCollection collection1 = this.newWith(true, false);
        Assert.assertTrue("true, false".equals(collection1.makeString())
                || "false, true".equals(collection1.makeString()));
        Assert.assertTrue(collection1.makeString("/"), "true/false".equals(collection1.makeString("/"))
                || "false/true".equals(collection1.makeString("/")));
        Assert.assertTrue(collection1.makeString("[", "/", "]"), "[true/false]".equals(collection1.makeString("[", "/", "]"))
                || "[false/true]".equals(collection1.makeString("[", "/", "]")));
    }

    @Test
    public void appendString()
    {
        StringBuilder appendable = new StringBuilder();
        this.getEmptyCollection().appendString(appendable);
        Assert.assertEquals("", appendable.toString());
        StringBuilder appendable1 = new StringBuilder();
        this.newWith(true).appendString(appendable1);
        Assert.assertEquals("true", appendable1.toString());
        StringBuilder appendable2 = new StringBuilder();
        MutableBooleanCollection collection = this.newWith(true, false);
        collection.appendString(appendable2);
        Assert.assertTrue("true, false".equals(appendable2.toString())
                || "false, true".equals(appendable2.toString()));
        StringBuilder appendable3 = new StringBuilder();
        collection.appendString(appendable3, "/");
        Assert.assertTrue("true/false".equals(appendable3.toString())
                || "false/true".equals(appendable3.toString()));
        StringBuilder appendable4 = new StringBuilder();
        collection.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(collection.toString(), appendable4.toString());
    }

    @Test
    public void toList()
    {
        MutableBooleanCollection collection = this.newWith(true, false);
        Assert.assertTrue(BooleanArrayList.newListWith(false, true).equals(collection.toList())
                || BooleanArrayList.newListWith(true, false).equals(collection.toList()));
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false, true), this.classUnderTest().toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false, true), this.classUnderTest().toBag());
    }

    @Test
    public void asLazy()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertEquals(collection.toList(), collection.asLazy().toList());
    }

    @Test
    public void asSynchronized()
    {
        MutableBooleanCollection collection = this.classUnderTestWithLock();
        Assert.assertEquals(collection, collection.asSynchronized());
    }
}
