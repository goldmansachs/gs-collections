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

import com.gs.collections.api.LazyBooleanIterable;
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
 * Abstract JUnit test for {@link MutableBooleanCollection}s.
 */
public abstract class AbstractBooleanCollectionTestCase
{
    protected abstract MutableBooleanCollection classUnderTest();

    protected abstract MutableBooleanCollection getEmptyCollection();

    protected abstract MutableBooleanCollection getEmptyMutableCollection();

    protected abstract MutableCollection<Boolean> getEmptyObjectCollection();

    protected abstract MutableBooleanCollection newWith(boolean... elements);

    protected abstract MutableBooleanCollection newMutableCollectionWith(boolean... elements);

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
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true), this.classUnderTest());
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.getEmptyCollection());
        Verify.assertNotEmpty(this.classUnderTest());
        Verify.assertNotEmpty(this.newWith(false));
        Verify.assertNotEmpty(this.newWith(true));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(this.getEmptyCollection().notEmpty());
        Assert.assertTrue(this.classUnderTest().notEmpty());
        Assert.assertTrue(this.newWith(false).notEmpty());
        Assert.assertTrue(this.newWith(true).notEmpty());
    }

    @Test
    public void clear()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        collection.clear();
        Verify.assertSize(0, collection);
        Verify.assertEmpty(collection);
        Assert.assertFalse(collection.contains(true));
        Assert.assertFalse(collection.contains(false));

        MutableBooleanCollection collection0 = this.getEmptyCollection();
        MutableBooleanCollection collection1 = this.newWith(false);
        MutableBooleanCollection collection2 = this.newWith(true);
        MutableBooleanCollection collection3 = this.newWith(true, false);
        MutableBooleanCollection collection4 = this.newWith(true, false, true, false, true);
        collection0.clear();
        collection1.clear();
        collection2.clear();
        collection3.clear();
        collection4.clear();
        Verify.assertEmpty(collection0);
        Verify.assertEmpty(collection1);
        Verify.assertEmpty(collection2);
        Verify.assertEmpty(collection3);
        Verify.assertEmpty(collection4);
        Verify.assertSize(0, collection0);
        Verify.assertSize(0, collection1);
        Verify.assertSize(0, collection2);
        Verify.assertSize(0, collection3);
        Verify.assertSize(0, collection4);
        Assert.assertFalse(collection1.contains(false));
        Assert.assertFalse(collection2.contains(true));
        Assert.assertFalse(collection3.contains(true));
        Assert.assertFalse(collection3.contains(false));
        Assert.assertFalse(collection4.contains(false));
    }

    @Test
    public void contains()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.contains(true));
        Assert.assertFalse(emptyCollection.contains(false));
        Assert.assertTrue(this.classUnderTest().contains(true));
        Assert.assertTrue(this.classUnderTest().contains(false));
        Assert.assertFalse(this.newWith(true, true, true).contains(false));
        Assert.assertFalse(this.newWith(false, false, false).contains(true));
    }

    @Test
    public void containsAllArray()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.containsAll(true));
        Assert.assertTrue(collection.containsAll(true, false, true));
        Assert.assertTrue(collection.containsAll(true, false));
        Assert.assertTrue(collection.containsAll(true, true));
        Assert.assertTrue(collection.containsAll(false, false));
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertFalse(emptyCollection.containsAll(true));
        Assert.assertFalse(emptyCollection.containsAll(false));
        Assert.assertFalse(emptyCollection.containsAll(false, true, false));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(true));
        Assert.assertTrue(emptyCollection.containsAll(false));

        MutableBooleanCollection trueCollection = this.newWith(true, true, true, true);
        Assert.assertFalse(trueCollection.containsAll(true, false));
        MutableBooleanCollection falseCollection = this.newWith(false, false, false, false);
        Assert.assertFalse(falseCollection.containsAll(true, false));
    }

    @Test
    public void containsAllIterable()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.containsAll(new BooleanArrayList()));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(false)));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(emptyCollection.containsAll(BooleanArrayList.newListWith(false)));
        MutableBooleanCollection collection = this.newWith(true, true, false, false, false);
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(false)));
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(collection.containsAll(BooleanArrayList.newListWith(true, false, true)));

        MutableBooleanCollection trueCollection = this.newWith(true, true, true, true);
        Assert.assertFalse(trueCollection.containsAll(BooleanArrayList.newListWith(true, false)));
        MutableBooleanCollection falseCollection = this.newWith(false, false, false, false);
        Assert.assertFalse(falseCollection.containsAll(BooleanArrayList.newListWith(true, false)));
    }

    @Test
    public void add()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        Assert.assertTrue(emptyCollection.add(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), emptyCollection);
        Assert.assertTrue(emptyCollection.add(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false), emptyCollection);
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.add(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false), collection);
    }

    @Test
    public void addAllArray()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.addAll());
        Assert.assertTrue(collection.addAll(false, true, false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true, false), collection);
    }

    @Test
    public void addAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.addAll(this.getEmptyMutableCollection()));
        Assert.assertTrue(collection.addAll(this.newMutableCollectionWith(false, true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true, false), collection);
    }

    @Test
    public void remove()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertTrue(collection.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, true), collection);
        Assert.assertFalse(collection.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, true), collection);
        Assert.assertTrue(collection.remove(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), collection);

        MutableBooleanCollection collection1 = this.getEmptyCollection();
        Assert.assertFalse(collection1.remove(false));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection1);
        Assert.assertTrue(collection1.add(false));
        Assert.assertTrue(collection1.add(false));
        Assert.assertTrue(collection1.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection1);
        Assert.assertTrue(collection1.remove(false));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection1);

        MutableBooleanCollection collection2 = this.getEmptyCollection();
        Assert.assertFalse(collection2.remove(true));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection2);
        Assert.assertTrue(collection2.add(true));
        Assert.assertTrue(collection2.add(true));
        Assert.assertTrue(collection2.remove(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), collection2);
        Assert.assertTrue(collection2.remove(true));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection2);
    }

    @Test
    public void removeAll()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll());
        Assert.assertTrue(collection.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection);
        Assert.assertFalse(collection.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection);
        Assert.assertTrue(collection.removeAll(false, true));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection);
    }

    @Test
    public void removeAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll(this.getEmptyMutableCollection()));
        Assert.assertTrue(collection.removeAll(this.newMutableCollectionWith(false)));
        Assert.assertEquals(this.newMutableCollectionWith(true, true), collection);
        Assert.assertTrue(collection.removeAll(this.newMutableCollectionWith(true, true)));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection);
    }

    @Test
    public void with()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        MutableBooleanCollection collection = emptyCollection.with(true);
        MutableBooleanCollection collection0 = this.getEmptyCollection().with(true).with(false);
        MutableBooleanCollection collection1 = this.getEmptyCollection().with(true).with(false).with(true);
        MutableBooleanCollection collection2 = this.getEmptyCollection().with(true).with(false).with(true).with(false);
        MutableBooleanCollection collection3 = this.getEmptyCollection().with(true).with(false).with(true).with(false).with(true);
        Assert.assertSame(emptyCollection, collection);
        Assert.assertEquals(this.newMutableCollectionWith(true), collection);
        Assert.assertEquals(this.newMutableCollectionWith(true, false), collection0);
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true), collection1);
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false), collection2);
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true), collection3);
    }

    @Test
    public void withAll()
    {
        MutableBooleanCollection emptyCollection = this.getEmptyCollection();
        MutableBooleanCollection collection = emptyCollection.withAll(this.newMutableCollectionWith(true));
        MutableBooleanCollection collection0 = this.getEmptyCollection().withAll(this.newMutableCollectionWith(true, false));
        MutableBooleanCollection collection1 = this.getEmptyCollection().withAll(this.newMutableCollectionWith(true, false, true));
        MutableBooleanCollection collection2 = this.getEmptyCollection().withAll(this.newMutableCollectionWith(true, false, true, false));
        MutableBooleanCollection collection3 = this.getEmptyCollection().withAll(this.newMutableCollectionWith(true, false, true, false, true));
        Assert.assertSame(emptyCollection, collection);
        Assert.assertEquals(this.newMutableCollectionWith(true), collection);
        Assert.assertEquals(this.newMutableCollectionWith(true, false), collection0);
        Assert.assertEquals(this.classUnderTest(), collection1);
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false), collection2);
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true), collection3);
    }

    @Test
    public void without()
    {
        MutableBooleanCollection collection = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newMutableCollectionWith(true, true, false, true), collection.without(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true), collection.without(true));
        Assert.assertEquals(this.newMutableCollectionWith(true, true), collection.without(false));
        Assert.assertEquals(this.newMutableCollectionWith(true), collection.without(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), collection.without(false));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection.without(true));
        Assert.assertEquals(this.getEmptyMutableCollection(), collection.without(false));

        MutableBooleanCollection collection1 = this.newWith(true, false, true, false, true);
        Assert.assertSame(collection1, collection1.without(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, true, false, true), collection1);
    }

    @Test
    public void withoutAll()
    {
        MutableBooleanCollection mainArrayList = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newMutableCollectionWith(true, true, true), mainArrayList.withoutAll(this.newMutableCollectionWith(false, false)));
    }

    @Test
    public abstract void booleanIterator();

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

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_emptyList()
    {
        this.getEmptyCollection().booleanIterator().next();
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
        Verify.assertSize(1, this.newWith(true));
        Verify.assertSize(1, this.newWith(false));
        Verify.assertSize(2, this.newWith(true, false));
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.newWith(true, false, true).count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.getEmptyCollection().count(BooleanPredicates.isFalse()));

        MutableBooleanCollection collection = this.newWith(true, false, false, true, true, true);
        Assert.assertEquals(4L, collection.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(2L, collection.count(BooleanPredicates.isFalse()));
        Assert.assertEquals(6L, collection.count(BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse())));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.classUnderTest().anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.newWith(true, true).anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.getEmptyCollection().anySatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(true).anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.newWith(false).anySatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(this.classUnderTest().allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(false, false).allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.newWith(true, false).allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(true, true, true).allSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(this.classUnderTest().noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.getEmptyCollection().noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertTrue(this.newWith(false, false).noneSatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.newWith(true, true).noneSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.newWith(true, true).noneSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void select()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(2, collection.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, collection.select(BooleanPredicates.isFalse()));

        MutableBooleanCollection collection1 = this.newWith(false, true, false, false, true, true, true);
        Assert.assertEquals(this.newWith(true, true, true, true), collection1.select(BooleanPredicates.isTrue()));
        Assert.assertEquals(this.newWith(false, false, false), collection1.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(1, collection.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(2, collection.reject(BooleanPredicates.isFalse()));

        MutableBooleanCollection collection1 = this.newWith(false, true, false, false, true, true, true);
        Assert.assertEquals(this.newWith(false, false, false), collection1.reject(BooleanPredicates.isTrue()));
        Assert.assertEquals(this.newWith(true, true, true, true), collection1.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(collection.detectIfNone(BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()), true));

        MutableBooleanCollection collection1 = this.newWith(true, true, true);
        Assert.assertFalse(collection1.detectIfNone(BooleanPredicates.isFalse(), false));
        Assert.assertTrue(collection1.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertTrue(collection1.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertTrue(collection1.detectIfNone(BooleanPredicates.isTrue(), true));

        MutableBooleanCollection collection2 = this.newWith(false, false, false);
        Assert.assertTrue(collection2.detectIfNone(BooleanPredicates.isTrue(), true));
        Assert.assertFalse(collection2.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(collection2.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(collection2.detectIfNone(BooleanPredicates.isFalse(), false));
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

        BooleanToObjectFunction<Boolean> booleanToObjectFunction = new BooleanToObjectFunction<Boolean>()
        {
            public Boolean valueOf(boolean parameter)
            {
                return !parameter;
            }
        };
        Assert.assertEquals(this.newObjectCollectionWith(false, true, false), this.classUnderTest().collect(booleanToObjectFunction));
        Assert.assertEquals(this.getEmptyObjectCollection(), this.getEmptyCollection().collect(booleanToObjectFunction));
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(0L, this.getEmptyCollection().toArray().length);
        Assert.assertTrue(Arrays.equals(new boolean[]{false, true}, this.newWith(true, false).toArray())
                || Arrays.equals(new boolean[]{true, false}, this.newWith(true, false).toArray()));
    }

    @Test
    public void testEquals()
    {
        MutableBooleanCollection collection1 = this.newWith(true, false, true, false);
        MutableBooleanCollection collection2 = this.newWith(true, false, true, false);
        MutableBooleanCollection collection3 = this.newWith(false, false, false, true);
        MutableBooleanCollection collection4 = this.newWith(true, true, true);

        Verify.assertEqualsAndHashCode(collection1, collection2);
        Verify.assertEqualsAndHashCode(this.getEmptyCollection(), this.getEmptyCollection());
        Verify.assertPostSerializedEqualsAndHashCode(collection1);
        Verify.assertPostSerializedEqualsAndHashCode(this.getEmptyCollection());
        Assert.assertNotEquals(collection1, collection3);
        Assert.assertNotEquals(collection1, collection4);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(this.getEmptyObjectCollection().hashCode(), this.getEmptyCollection().hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(true, false, true).hashCode(),
                this.newWith(true, false, true).hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(true).hashCode(),
                this.newWith(true).hashCode());
        Assert.assertEquals(this.newObjectCollectionWith(false).hashCode(),
                this.newWith(false).hashCode());
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
        Assert.assertEquals("true", this.newWith(true).makeString("/"));
        Assert.assertEquals("", this.getEmptyCollection().makeString());
        MutableBooleanCollection collection = this.newWith(true, false);
        Assert.assertTrue("true, false".equals(collection.makeString())
                || "false, true".equals(collection.makeString()));
        Assert.assertTrue(collection.makeString("/"), "true/false".equals(collection.makeString("/"))
                || "false/true".equals(collection.makeString("/")));
        Assert.assertTrue(collection.makeString("[", "/", "]"), "[true/false]".equals(collection.makeString("[", "/", "]"))
                || "[false/true]".equals(collection.makeString("[", "/", "]")));
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
        Assert.assertEquals(collection.toBag(), collection.asLazy().toBag());
        Verify.assertInstanceOf(LazyBooleanIterable.class, collection.asLazy());
    }

    @Test
    public void asSynchronized()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertInstanceOf(this.newWith(true, false, true).asSynchronized().getClass(), collection.asSynchronized());
        Assert.assertEquals(this.newWith(true, false, true).asSynchronized(), collection.asSynchronized());
        Assert.assertEquals(collection, collection.asSynchronized());
    }

    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(this.newWith(true, false, true).asUnmodifiable().getClass(), this.classUnderTest().asUnmodifiable());
        Assert.assertEquals(this.newWith(true, false, true).asUnmodifiable(), this.classUnderTest().asUnmodifiable());
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().asSynchronized());
    }

}
