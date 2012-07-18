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

package com.gs.collections.impl.set.mutable;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link SynchronizedMutableSet}.
 */
public class SynchronizedMutableSet2Test extends AbstractMutableSetTestCase
{
    @Override
    protected <T> MutableSet<T> classUnderTest()
    {
        return new SynchronizedMutableSet<T>(SetAdapter.adapt(new TreeSet<T>()));
    }

    @Test(expected = NoSuchElementException.class)
    public void min_empty_throws_without_comparator()
    {
        this.newWith().min();
    }

    @Test(expected = NoSuchElementException.class)
    public void max_empty_throws_without_comparator()
    {
        this.newWith().max();
    }

    @Override
    @Test
    public void testToString()
    {
        MutableSet<Integer> integer = this.newWith(1);
        Assert.assertEquals("[1]", integer.toString());
    }

    @Override
    @Test
    public void makeString()
    {
        MutableSet<Integer> integer = this.newWith(1);
        Assert.assertEquals("{1}", integer.makeString("{", ",", "}"));
    }

    @Override
    @Test
    public void appendString()
    {
        Appendable stringBuilder = new StringBuilder();
        MutableSet<Integer> integer = this.newWith(1);
        integer.appendString(stringBuilder, "{", ",", "}");
        Assert.assertEquals("{1}", stringBuilder.toString());
    }

    @Override
    @Test
    public void remove()
    {
        MutableSet<Integer> integers = this.newWith(1, 2, 3, 4);
        integers.remove(3);
        Verify.assertSetsEqual(UnifiedSet.newSetWith(1, 2, 4), integers);
    }

    @Override
    public void selectInstancesOf()
    {
        MutableSet<Number> numbers = new SynchronizedMutableSet<Number>(SetAdapter.adapt(new TreeSet<Number>(new Comparator<Number>()
        {
            public int compare(Number o1, Number o2)
            {
                return Double.compare(o1.doubleValue(), o2.doubleValue());
            }
        }))).withAll(FastList.newListWith(1, 2.0, 3, 4.0, 5));
        MutableSet<Integer> integers = numbers.selectInstancesOf(Integer.class);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 3, 5), integers);
        Assert.assertEquals(FastList.newListWith(1, 3, 5), integers.toList());
    }
}
