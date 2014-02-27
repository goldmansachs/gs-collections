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

package com.gs.collections.impl.bag.mutable.primitive;

import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanBag}.
 */
public class SynchronizedBooleanBagTest extends AbstractMutableBooleanBagTestCase
{
    @Override
    protected final SynchronizedBooleanBag classUnderTest()
    {
        return new SynchronizedBooleanBag(BooleanHashBag.newBagWith(true, false, true));
    }

    @Override
    protected SynchronizedBooleanBag newWith(boolean... elements)
    {
        return new SynchronizedBooleanBag(BooleanHashBag.newBagWith(elements));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        super.asSynchronized();
        SynchronizedBooleanBag bagWithLockObject = new SynchronizedBooleanBag(BooleanHashBag.newBagWith(true, false, true), new Object());
        Assert.assertSame(bagWithLockObject, bagWithLockObject.asSynchronized());
        Assert.assertEquals(bagWithLockObject, bagWithLockObject.asSynchronized());
        MutableBooleanBag bag = this.classUnderTest();
        Assert.assertSame(bag, bag.asSynchronized());
        Assert.assertEquals(bag, bag.asSynchronized());
    }
}
