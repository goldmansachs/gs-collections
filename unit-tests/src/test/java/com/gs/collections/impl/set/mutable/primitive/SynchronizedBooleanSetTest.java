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

import com.gs.collections.api.set.primitive.MutableBooleanSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanSet}.
 */
public class SynchronizedBooleanSetTest extends AbstractBooleanSetTestCase
{
    @Override
    protected SynchronizedBooleanSet classUnderTest()
    {
        return new SynchronizedBooleanSet(BooleanHashSet.newSetWith(true, false, true));
    }

    @Override
    protected SynchronizedBooleanSet newWith(boolean... elements)
    {
        return new SynchronizedBooleanSet(BooleanHashSet.newSetWith(elements));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        super.asSynchronized();
        SynchronizedBooleanSet set = this.classUnderTest();
        MutableBooleanSet setWithLockObject = new SynchronizedBooleanSet(BooleanHashSet.newSetWith(true, false, true), new Object()).asSynchronized();
        Assert.assertEquals(set, setWithLockObject);
        Assert.assertSame(setWithLockObject, setWithLockObject.asSynchronized());
        Assert.assertSame(set, set.asSynchronized());
        Assert.assertEquals(set, set.asSynchronized());
    }
}
