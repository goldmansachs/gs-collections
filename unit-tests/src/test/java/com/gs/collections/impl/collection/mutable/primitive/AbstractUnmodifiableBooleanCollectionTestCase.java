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
 * Abstract JUnit test for {@link AbstractUnmodifiableBooleanCollection}s.
 */
public abstract class AbstractUnmodifiableBooleanCollectionTestCase
{
    protected abstract MutableBooleanCollection classUnderTest();

    protected abstract MutableBooleanCollection getEmptyCollection();

    protected abstract MutableBooleanCollection getEmptyModifiableCollection();

    protected abstract MutableCollection<Boolean> getEmptyObjectCollection();

    protected abstract MutableBooleanCollection newWith(boolean... elements);

    protected abstract MutableBooleanCollection newModifiableCollectionWith(boolean... elements);

    protected abstract MutableCollection<Object> newObjectCollectionWith(Object... elements);

    protected abstract MutableBooleanCollection newSynchronizedCollectionWith(boolean... elements);

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
        Assert.assertEquals(this.newModifiableCollectionWith(true, false, true), this.classUnderTest());
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

    @Test(expected = UnsupportedOperationException.class)
    public void clear()
    {
        this.classUnderTest().clear();
    }

    @Test
    public void contains()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.contains(true));
        MutableBooleanCollection collection = this.newWith(true);
        Assert.assertTrue(collection.contains(true));
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
        MutableBooleanCollection collection1 = this.newWith(false);
        Assert.assertFalse(collection1.containsAll(true));
    }

    @Test
    public void containsAllIterable()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.containsAll(new BooleanArrayList()));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        MutableBooleanCollection collection = this.newWith(false);
        Assert.assertFalse(collection.containsAll(BooleanArrayList.newListWith(true)));
        MutableBooleanCollection collection1 = this.classUnderTest();
        Assert.assertTrue(collection1.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(collection1.containsAll(BooleanArrayList.newListWith(true, false, true)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add()
    {
        this.getEmptyCollection().add(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllArray()
    {
        this.classUnderTest().addAll(true, false, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIterable()
    {
        this.classUnderTest().addAll(this.getEmptyModifiableCollection());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove()
    {
        this.classUnderTest().remove(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAll()
    {
        this.classUnderTest().removeAll(true, false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAllIterable()
    {
        this.classUnderTest().removeAll(this.getEmptyModifiableCollection());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void with()
    {
        this.getEmptyCollection().with(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withAll()
    {
        this.getEmptyCollection().withAll(this.newModifiableCollectionWith(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void without()
    {
        this.newWith(true, false, true, false, true).without(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void withoutAll()
    {
        this.newWith(true, false, true, false, true).withoutAll(this.newModifiableCollectionWith(false, false));
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
        MutableBooleanCollection collection = this.newWith(true, true, true);
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
        Verify.assertInstanceOf(this.newSynchronizedCollectionWith(true, false, true).getClass(), this.classUnderTest().asSynchronized());
        Assert.assertEquals(this.newSynchronizedCollectionWith(true, false, true), this.classUnderTest().asSynchronized());
    }

    @Test
    public void asUnmodifiable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertEquals(collection, collection.asUnmodifiable());
    }
}
