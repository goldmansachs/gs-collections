/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.list.immutable;

import java.util.ListIterator;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.impl.factory.Lists;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableSubListTest extends AbstractImmutableListTestCase
{
    @Override
    protected ImmutableList<Integer> classUnderTest()
    {
        return Lists.immutable.of(0, 1, 2, 3, 4, 5, 6, 7).subList(1, 5);
    }

    @Test
    public void testSubListListIterator()
    {
        ImmutableList<Integer> subList = this.classUnderTest();
        ListIterator<Integer> iterator = subList.listIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.hasPrevious());
        Assert.assertEquals(Integer.valueOf(1), iterator.next());
        Assert.assertEquals(Integer.valueOf(2), iterator.next());
        Assert.assertEquals(Integer.valueOf(3), iterator.next());
        Assert.assertTrue(iterator.hasPrevious());
        Assert.assertEquals(Integer.valueOf(3), iterator.previous());
        Assert.assertEquals(Integer.valueOf(2), iterator.previous());
        Assert.assertEquals(Integer.valueOf(1), iterator.previous());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubListListIteratorSet_throws()
    {
        ImmutableList<Integer> subList = this.classUnderTest();
        ListIterator<Integer> iterator = subList.listIterator();
        iterator.set(4);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubListListIteratorRemove_throws()
    {
        ImmutableList<Integer> subList = this.classUnderTest();
        ListIterator<Integer> iterator = subList.listIterator();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubListListIteratorAdd_throws()
    {
        ImmutableList<Integer> subList = this.classUnderTest();
        ListIterator<Integer> iterator = subList.listIterator();
        iterator.add(4);
    }
}
