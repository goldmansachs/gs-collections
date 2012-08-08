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

import java.util.LinkedList;

import com.webguys.ponzu.impl.test.Verify;
import org.junit.Test;

public class LinkedListAdapterTest extends ListAdapterTest
{
    @Override
    protected <T> ListAdapter<T> classUnderTest()
    {
        return new ListAdapter<T>(new LinkedList<T>());
    }

    @Override
    @Test
    public void testGetWithArrayIndexOutOfBoundsException()
    {
        final Object item = new Object();

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                LinkedListAdapterTest.this.newWith(item).get(-1);
            }
        });
    }
}
