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

import com.gs.collections.api.list.primitive.MutableBooleanList;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedBooleanList}.
 */
public class SynchronizedBooleanListTest extends AbstractBooleanListTestCase
{
    @Override
    protected SynchronizedBooleanList classUnderTest()
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true));
    }

    @Override
    protected SynchronizedBooleanList newWith(boolean... elements)
    {
        return new SynchronizedBooleanList(BooleanArrayList.newListWith(elements));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        super.asSynchronized();
        SynchronizedBooleanList list = this.classUnderTest();
        MutableBooleanList listWithLockObject = new SynchronizedBooleanList(BooleanArrayList.newListWith(true, false, true), new Object()).asSynchronized();
        Assert.assertEquals(list, listWithLockObject);
        Assert.assertSame(listWithLockObject, listWithLockObject.asSynchronized());
        Assert.assertSame(list, list.asSynchronized());
        Assert.assertEquals(list, list.asSynchronized());
    }
}
