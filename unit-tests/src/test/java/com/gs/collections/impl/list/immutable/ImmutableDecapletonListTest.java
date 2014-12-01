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

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class ImmutableDecapletonListTest extends AbstractImmutableListTestCase
{
    @Override
    protected ImmutableList<Integer> classUnderTest()
    {
        return new ImmutableDecapletonList<>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Override
    @Test
    public void toSortedSetBy()
    {
        MutableList<Integer> expected = TreeSortedSet.newSetWith("1", "2", "3", "4", "5", "6", "7", "8", "9", "10").collect(Integer::valueOf);
        MutableList<Integer> sortedList = this.classUnderTest().toSortedSetBy(String::valueOf).toList();
        Verify.assertListsEqual(expected, sortedList);
    }

    @Test
    public void selectInstanceOf()
    {
        ImmutableList<Number> numbers = new ImmutableDecapletonList<>(1, 2.0, 3, 4.0, 5, 6.0, 7, 8.0, 9, 10.0);
        Assert.assertEquals(
                iList(1, 3, 5, 7, 9),
                numbers.selectInstancesOf(Integer.class));
    }
}
