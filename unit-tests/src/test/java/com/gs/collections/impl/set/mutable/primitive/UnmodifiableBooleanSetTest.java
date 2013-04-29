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

package com.gs.collections.impl.set.mutable.primitive;

import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.collection.mutable.primitive.AbstractUnmodifiableBooleanCollectionTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link UnmodifiableBooleanSet}.
 */
public class UnmodifiableBooleanSetTest extends AbstractUnmodifiableBooleanCollectionTestCase
{
    @Override
    protected final MutableBooleanSet classUnderTest()
    {
        return new UnmodifiableBooleanSet(BooleanHashSet.newSetWith(true, false, true));
    }

    @Override
    protected MutableBooleanSet getEmptyCollection()
    {
        return new UnmodifiableBooleanSet(new BooleanHashSet());
    }

    @Override
    protected MutableBooleanSet getEmptyModifiableCollection()
    {
        return new BooleanHashSet();
    }

    @Override
    protected MutableCollection<Boolean> getEmptyObjectCollection()
    {
        return UnifiedSet.newSet();
    }

    @Override
    protected MutableBooleanSet newWith(boolean... elements)
    {
        return new UnmodifiableBooleanSet(BooleanHashSet.newSetWith(elements));
    }

    @Override
    protected BooleanHashSet newModifiableCollectionWith(boolean... elements)
    {
        return BooleanHashSet.newSetWith(elements);
    }

    @Override
    protected MutableCollection<Object> newObjectCollectionWith(Object... elements)
    {
        return UnifiedSet.newSetWith(elements);
    }

    @Override
    protected MutableBooleanCollection newSynchronizedCollectionWith(boolean... elements)
    {
        return BooleanHashSet.newSetWith(elements).asSynchronized();
    }

    private final MutableBooleanSet set = this.classUnderTest();

    @Override
    @Test
    public void newCollectionWith()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(2, collection);
        Assert.assertTrue(collection.containsAll(true, false, true));
    }

    @Override
    @Test
    public void iterator()
    {
        BooleanIterator iterator = this.newWith(true, false).booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        MutableBooleanCollection actual = this.getEmptyModifiableCollection();
        actual.add(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        actual.add(iterator.next());
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(this.newModifiableCollectionWith(true, false), actual);
    }

    @Override
    @Test
    public void select()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(1, collection.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, collection.select(BooleanPredicates.isFalse()));
    }

    @Override
    @Test
    public void reject()
    {
        MutableBooleanCollection collection = this.classUnderTest();
        Verify.assertSize(1, collection.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(1, collection.reject(BooleanPredicates.isFalse()));
    }

    @Override
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

        Assert.assertEquals(1L, sum[0]);
    }

    @Override
    @Test
    public void size()
    {
        Verify.assertSize(0, this.getEmptyCollection());
        Verify.assertSize(2, this.classUnderTest());
    }

    @Override
    @Test
    public void count()
    {
        Assert.assertEquals(1L, this.newWith(true, false, true).count(BooleanPredicates.isTrue()));
    }

    @Override
    @Test
    public void testEquals()
    {
        Assert.assertNotEquals(this.getEmptyCollection(), this.set);
        Assert.assertNotEquals(this.newWith(true), this.set);
        Assert.assertNotEquals(this.newWith(false), this.set);
        Verify.assertEqualsAndHashCode(this.newModifiableCollectionWith(false, true), this.set);
        Verify.assertEqualsAndHashCode(this.newModifiableCollectionWith(true, false), this.set);

        Verify.assertPostSerializedEqualsAndHashCode(this.getEmptyCollection());
        Verify.assertPostSerializedEqualsAndHashCode(this.newModifiableCollectionWith(true));
        Verify.assertPostSerializedEqualsAndHashCode(this.set);
    }

    @Override
    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true), this.set.toBag());
    }

}
