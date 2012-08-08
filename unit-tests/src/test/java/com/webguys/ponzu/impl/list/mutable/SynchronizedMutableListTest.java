/*
 * Copyright 2011 Goldman Sachs.
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

package com.webguys.ponzu.impl.list.mutable;

import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedMutableList}.
 */
public class SynchronizedMutableListTest extends AbstractListTestCase
{
    @Override
    protected <T> MutableList<T> classUnderTest()
    {
        return new SynchronizedMutableList<T>(FastList.<T>newList());
    }

    @Override
    @Test
    public void newEmpty()
    {
        super.newEmpty();

        Verify.assertInstanceOf(FastList.class, classUnderTest().newEmpty());
    }

    @Override
    @Test
    public void testToString()
    {
        MutableList<Object> list = this.<Object>newWith(1, 2, 3);
        Assert.assertEquals("[1, 2, 3]", list.toString());
    }
}
