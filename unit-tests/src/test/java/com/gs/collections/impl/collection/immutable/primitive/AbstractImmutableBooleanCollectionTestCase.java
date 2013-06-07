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

package com.gs.collections.impl.collection.immutable.primitive;

import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.collection.mutable.primitive.AbstractBooleanIterableTestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link ImmutableBooleanCollection}s.
 */
public abstract class AbstractImmutableBooleanCollectionTestCase extends AbstractBooleanIterableTestCase
{
    @Override
    protected abstract ImmutableBooleanCollection classUnderTest();

    @Override
    protected abstract ImmutableBooleanCollection newWith(boolean... elements);

    @Override
    protected abstract MutableBooleanCollection newMutableCollectionWith(boolean... elements);

    @Test
    public void testNewWith()
    {
        ImmutableBooleanCollection booleanCollection = this.classUnderTest();
        MutableBooleanList list = booleanCollection.toList();
        ImmutableBooleanCollection collection = booleanCollection.newWith(true);
        ImmutableBooleanCollection collection0 = booleanCollection.newWith(true).newWith(false);
        ImmutableBooleanCollection collection1 = booleanCollection.newWith(true).newWith(false).newWith(true);
        ImmutableBooleanCollection collection2 = booleanCollection.newWith(true).newWith(false).newWith(true).newWith(false);
        ImmutableBooleanCollection collection3 = booleanCollection.newWith(true).newWith(false).newWith(true).newWith(false).newWith(true);
        ImmutableBooleanCollection collection4 = collection3.newWith(true).newWith(false).newWith(true).newWith(false).newWith(true);
        Assert.assertEquals(list, booleanCollection);
        Assert.assertEquals(list.with(true), collection);
        Assert.assertEquals(list.with(false), collection0);
        Assert.assertEquals(list.with(true), collection1);
        Assert.assertEquals(list.with(false), collection2);
        Assert.assertEquals(list.with(true), collection3);
        list.addAll(true, false, true, false, true);
        Assert.assertEquals(list, collection4);
    }

    @Test
    public void newWithAll()
    {
        ImmutableBooleanCollection booleanCollection = this.classUnderTest();
        MutableBooleanList list = booleanCollection.toList();
        ImmutableBooleanCollection collection = booleanCollection.newWithAll(this.newMutableCollectionWith(true));
        ImmutableBooleanCollection collection0 = booleanCollection.newWithAll(this.newMutableCollectionWith(true, false));
        ImmutableBooleanCollection collection1 = booleanCollection.newWithAll(this.newMutableCollectionWith(true, false, true));
        ImmutableBooleanCollection collection2 = booleanCollection.newWithAll(this.newMutableCollectionWith(true, false, true, false));
        ImmutableBooleanCollection collection3 = booleanCollection.newWithAll(this.newMutableCollectionWith(true, false, true, false, true));
        ImmutableBooleanCollection collection4 = collection3.newWithAll(this.newMutableCollectionWith(true, false, true, false, true));
        Assert.assertEquals(list, booleanCollection);
        Assert.assertEquals(list.with(true), collection);
        Assert.assertEquals(list.with(false), collection0);
        Assert.assertEquals(list.with(true), collection1);
        Assert.assertEquals(list.with(false), collection2);
        Assert.assertEquals(list.with(true), collection3);
        list.addAll(true, false, true, false, true);
        Assert.assertEquals(list, collection4);
    }

    @Test
    public void newWithout()
    {
        ImmutableBooleanCollection trueCollection = this.getTrueCollection(this.classUnderTest()).toImmutable();
        Assert.assertSame(trueCollection, trueCollection.newWithout(false));
        Assert.assertNotSame(trueCollection, trueCollection.newWithout(true));

        ImmutableBooleanCollection collection = this.classUnderTest();
        MutableBooleanList list = collection.toList();
        Assert.assertEquals(list.without(true), collection.newWithout(true));
        MutableBooleanList list1 = collection.toList();
        Assert.assertEquals(list1.without(false), collection.newWithout(false));
        Assert.assertEquals(this.classUnderTest(), collection);
    }

    @Test
    public void newWithoutAll()
    {
        ImmutableBooleanCollection immutableBooleanCollection = this.classUnderTest();
        MutableBooleanCollection mutableTrueCollection = this.getTrueCollection(immutableBooleanCollection);
        ImmutableBooleanCollection trueCollection = mutableTrueCollection.toImmutable();
        Assert.assertEquals(this.newMutableCollectionWith(), trueCollection.newWithoutAll(this.newMutableCollectionWith(true, false)));
        Assert.assertEquals(mutableTrueCollection, trueCollection);

        MutableBooleanList list = immutableBooleanCollection.toList();
        list.removeAll(true);
        Assert.assertEquals(list, immutableBooleanCollection.newWithoutAll(this.newMutableCollectionWith(true)));
        Assert.assertEquals(this.newMutableCollectionWith(), immutableBooleanCollection.newWithoutAll(this.newMutableCollectionWith(true, false)));

        ImmutableBooleanCollection collection = this.newWith(true, false, true, false, true);
        Assert.assertEquals(this.newMutableCollectionWith(false, false), collection.newWithoutAll(this.newMutableCollectionWith(true, true)));
        Assert.assertEquals(this.newMutableCollectionWith(), collection.newWithoutAll(this.newMutableCollectionWith(true, false)));
    }

    private MutableBooleanCollection getTrueCollection(ImmutableBooleanCollection collection)
    {
        MutableBooleanCollection trueCollection = this.newMutableCollectionWith();
        for (int i = 0; i < collection.size(); i++)
        {
            trueCollection.add(true);
        }
        return trueCollection;
    }
}
