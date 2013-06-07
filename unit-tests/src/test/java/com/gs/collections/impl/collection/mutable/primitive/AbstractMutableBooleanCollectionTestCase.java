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

import java.util.NoSuchElementException;

import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link MutableBooleanCollection}s.
 */
public abstract class AbstractMutableBooleanCollectionTestCase extends AbstractBooleanIterableTestCase
{
    @Override
    protected abstract MutableBooleanCollection classUnderTest();

    @Override
    protected abstract MutableBooleanCollection newWith(boolean... elements);

    @Override
    protected abstract MutableBooleanCollection newMutableCollectionWith(boolean... elements);

    @Test
    public void clear()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        collection.clear();
        Verify.assertSize(0, collection);
        Verify.assertEmpty(collection);
        Assert.assertFalse(collection.contains(true));
        Assert.assertFalse(collection.contains(false));

        MutableBooleanCollection collection0 = this.newWith();
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
        Assert.assertEquals(this.newMutableCollectionWith(), collection0);
        Assert.assertEquals(this.newMutableCollectionWith(), collection1);
        Assert.assertEquals(this.newMutableCollectionWith(), collection2);
        Assert.assertEquals(this.newMutableCollectionWith(), collection3);
        Assert.assertEquals(this.newMutableCollectionWith(), collection4);
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        Verify.assertPostSerializedEqualsAndHashCode(this.newWith());
    }

    @Override
    @Test
    public void containsAllArray()
    {
        super.containsAllArray();
        MutableBooleanCollection emptyCollection = this.newWith();
        Assert.assertFalse(emptyCollection.containsAll(true));
        Assert.assertFalse(emptyCollection.containsAll(false));
        Assert.assertFalse(emptyCollection.containsAll(false, true, false));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(true));
        Assert.assertTrue(emptyCollection.containsAll(false));
    }

    @Override
    @Test
    public void containsAllIterable()
    {
        super.containsAllIterable();
        MutableBooleanCollection emptyCollection = this.newWith();
        Assert.assertTrue(emptyCollection.containsAll(new BooleanArrayList()));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(false)));
        emptyCollection.add(false);
        Assert.assertFalse(emptyCollection.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(emptyCollection.containsAll(BooleanArrayList.newListWith(false)));
    }

    @Test
    public void add()
    {
        MutableBooleanCollection emptyCollection = this.newWith();
        Assert.assertTrue(emptyCollection.add(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), emptyCollection);
        Assert.assertTrue(emptyCollection.add(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false), emptyCollection);
        Assert.assertTrue(emptyCollection.add(true));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true), emptyCollection);
        Assert.assertTrue(emptyCollection.add(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false), emptyCollection);
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
        Assert.assertTrue(collection.addAll(this.newMutableCollectionWith(true, false, true, false, true)));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true, false, true, false, true, false, true), collection);
    }

    @Test
    public void addAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.addAll(this.newMutableCollectionWith()));
        Assert.assertTrue(collection.addAll(this.newMutableCollectionWith(false, true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true, false), collection);
        Assert.assertTrue(collection.addAll(this.newMutableCollectionWith(true, false, true, false, true)));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true, false, true, false, true, false, true), collection);

        MutableBooleanCollection emptyCollection = this.newWith();
        Assert.assertTrue(emptyCollection.addAll(BooleanArrayList.newListWith(true, false, true, false, true)));
        Assert.assertFalse(emptyCollection.addAll(new BooleanArrayList()));
        Assert.assertEquals(this.newMutableCollectionWith(true, false, true, false, true), emptyCollection);
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

        MutableBooleanCollection collection1 = this.newWith();
        Assert.assertFalse(collection1.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(), collection1);
        Assert.assertTrue(collection1.add(false));
        Assert.assertTrue(collection1.add(false));
        Assert.assertTrue(collection1.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection1);
        Assert.assertTrue(collection1.remove(false));
        Assert.assertEquals(this.newMutableCollectionWith(), collection1);

        MutableBooleanCollection collection2 = this.newWith();
        Assert.assertFalse(collection2.remove(true));
        Assert.assertEquals(this.newMutableCollectionWith(), collection2);
        Assert.assertTrue(collection2.add(true));
        Assert.assertTrue(collection2.add(true));
        Assert.assertTrue(collection2.remove(true));
        Assert.assertEquals(this.newMutableCollectionWith(true), collection2);
        Assert.assertTrue(collection2.remove(true));
        Assert.assertEquals(this.newMutableCollectionWith(), collection2);
    }

    @Test
    public void removeAll()
    {
        Assert.assertFalse(this.newWith().removeAll(true));

        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll());
        Assert.assertTrue(collection.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection);
        Assert.assertFalse(collection.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), collection);
        Assert.assertTrue(collection.removeAll(false, true));
        Assert.assertEquals(this.newMutableCollectionWith(), collection);

        MutableBooleanCollection booleanArrayCollection = this.newWith(false, false);
        Assert.assertFalse(booleanArrayCollection.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false, false), booleanArrayCollection);
        Assert.assertTrue(booleanArrayCollection.removeAll(false));
        Assert.assertEquals(this.newMutableCollectionWith(), booleanArrayCollection);
        MutableBooleanCollection collection1 = this.classUnderTest();
        Assert.assertFalse(collection1.removeAll());
        Assert.assertTrue(collection1.removeAll(true, false));
        Assert.assertEquals(this.newMutableCollectionWith(), collection1);

        MutableBooleanCollection trueFalseList = this.newWith(true, false);
        Assert.assertTrue(trueFalseList.removeAll(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), trueFalseList);

        MutableBooleanCollection collection2 = this.newWith(true, false, true, false, true);
        Assert.assertFalse(collection2.removeAll());
        Assert.assertTrue(collection2.removeAll(true, true));
        Assert.assertEquals(this.newMutableCollectionWith(false, false), collection2);

        MutableBooleanCollection collection3 = this.newWith(true, false, true, false, true);
        Assert.assertFalse(collection3.removeAll());
        Assert.assertTrue(collection3.removeAll(true, false));
        Assert.assertEquals(this.newMutableCollectionWith(), collection3);

        MutableBooleanCollection collection4 = this.newWith(true, false, true, false, true);
        Assert.assertFalse(collection4.removeAll());
        Assert.assertTrue(collection4.removeAll(false, false));
        Assert.assertEquals(this.newMutableCollectionWith(true, true, true), collection4);
    }

    @Test
    public void removeAllIterable()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Assert.assertFalse(collection.removeAll(this.newMutableCollectionWith()));
        Assert.assertTrue(collection.removeAll(this.newMutableCollectionWith(false)));
        Assert.assertEquals(this.newMutableCollectionWith(true, true), collection);
        Assert.assertTrue(collection.removeAll(this.newMutableCollectionWith(true, true)));
        Assert.assertEquals(this.newMutableCollectionWith(), collection);

        MutableBooleanCollection list = this.classUnderTest();
        Assert.assertFalse(list.removeAll(new BooleanArrayList()));
        MutableBooleanCollection booleanArrayList = this.newWith(false, false);
        Assert.assertFalse(booleanArrayList.removeAll(new BooleanArrayList(true)));
        Assert.assertEquals(this.newMutableCollectionWith(false, false), booleanArrayList);
        Assert.assertTrue(booleanArrayList.removeAll(new BooleanArrayList(false)));
        Assert.assertEquals(this.newMutableCollectionWith(), booleanArrayList);
        Assert.assertTrue(list.removeAll(new BooleanArrayList(true)));
        Assert.assertEquals(this.newMutableCollectionWith(false), list);
        Assert.assertTrue(list.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(), list);
        Assert.assertFalse(list.removeAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(), list);

        MutableBooleanCollection list1 = this.newWith(true, false, true, true);
        Assert.assertFalse(list1.removeAll(new BooleanArrayList()));
        Assert.assertTrue(list1.removeAll(BooleanArrayList.newListWith(true, true)));
        Verify.assertSize(1, list1);
        Assert.assertFalse(list1.contains(true));
        Assert.assertEquals(this.newMutableCollectionWith(false), list1);
        Assert.assertTrue(list1.removeAll(BooleanArrayList.newListWith(false, false)));
        Assert.assertEquals(this.newMutableCollectionWith(), list1);

        MutableBooleanCollection list2 = this.newWith(true, false, true, false, true);
        Assert.assertTrue(list2.removeAll(BooleanHashBag.newBagWith(true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(), list2);
    }

    @Test
    public void with()
    {
        MutableBooleanCollection emptyCollection = this.newWith();
        MutableBooleanCollection collection = emptyCollection.with(true);
        MutableBooleanCollection collection0 = this.newWith().with(true).with(false);
        MutableBooleanCollection collection1 = this.newWith().with(true).with(false).with(true);
        MutableBooleanCollection collection2 = this.newWith().with(true).with(false).with(true).with(false);
        MutableBooleanCollection collection3 = this.newWith().with(true).with(false).with(true).with(false).with(true);
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
        MutableBooleanCollection emptyCollection = this.newWith();
        MutableBooleanCollection collection = emptyCollection.withAll(this.newMutableCollectionWith(true));
        MutableBooleanCollection collection0 = this.newWith().withAll(this.newMutableCollectionWith(true, false));
        MutableBooleanCollection collection1 = this.newWith().withAll(this.newMutableCollectionWith(true, false, true));
        MutableBooleanCollection collection2 = this.newWith().withAll(this.newMutableCollectionWith(true, false, true, false));
        MutableBooleanCollection collection3 = this.newWith().withAll(this.newMutableCollectionWith(true, false, true, false, true));
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
        Assert.assertEquals(this.newMutableCollectionWith(), collection.without(true));
        Assert.assertEquals(this.newMutableCollectionWith(), collection.without(false));

        MutableBooleanCollection collection1 = this.newWith(true, false, true, false, true);
        Assert.assertSame(collection1, collection1.without(false));
        Assert.assertEquals(this.newMutableCollectionWith(true, true, false, true), collection1);
    }

    @Test
    public void withoutAll()
    {
        MutableBooleanCollection mainCollection = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newMutableCollectionWith(true, true, true), mainCollection.withoutAll(this.newMutableCollectionWith(false, false)));

        MutableBooleanCollection collection = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newMutableCollectionWith(true, true, true), collection.withoutAll(BooleanHashBag.newBagWith(false)));
        Assert.assertEquals(this.newMutableCollectionWith(), collection.withoutAll(BooleanHashBag.newBagWith(true, false)));
        Assert.assertEquals(this.newMutableCollectionWith(), collection.withoutAll(BooleanHashBag.newBagWith(true, false)));

        MutableBooleanCollection trueCollection = this.newWith(true, true, true);
        Assert.assertEquals(this.newMutableCollectionWith(true, true, true), trueCollection.withoutAll(BooleanArrayList.newListWith(false)));
        MutableBooleanCollection mutableBooleanCollection = trueCollection.withoutAll(BooleanArrayList.newListWith(true));
        Assert.assertEquals(this.newMutableCollectionWith(), mutableBooleanCollection);
        Assert.assertSame(trueCollection, mutableBooleanCollection);

    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void iterator_throws_non_empty_collection()
    {
        super.iterator_throws_non_empty_collection();
        MutableBooleanCollection collection = this.newWith();
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
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().asUnmodifiable());
    }

}
