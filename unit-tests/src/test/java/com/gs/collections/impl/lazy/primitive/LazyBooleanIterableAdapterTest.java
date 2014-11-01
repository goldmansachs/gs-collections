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

package com.gs.collections.impl.lazy.primitive;

import java.util.Arrays;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link LazyByteIterableAdapter}.
 */
public class LazyBooleanIterableAdapterTest
{
    private final LazyBooleanIterableAdapter iterable =
            new LazyBooleanIterableAdapter(BooleanArrayList.newListWith(true, false, true));

    @Test
    public void booleanIterator()
    {
        int sum = 0;
        for (BooleanIterator iterator = this.iterable.booleanIterator(); iterator.hasNext(); )
        {
            sum += iterator.next() ? 1 : 0;
        }
        Assert.assertEquals(2, sum);
    }

    @Test
    public void forEach()
    {
        int[] sum = new int[1];
        this.iterable.forEach(each -> sum[0] += each ? 1 : 0);
        Assert.assertEquals(2, sum[0]);
    }

    @Test
    public void size()
    {
        Verify.assertSize(3, this.iterable);
    }

    @Test
    public void empty()
    {
        Assert.assertFalse(this.iterable.isEmpty());
        Assert.assertTrue(this.iterable.notEmpty());
        Verify.assertNotEmpty(this.iterable);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2, this.iterable.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(1, this.iterable.count(BooleanPredicates.isFalse()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.iterable.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertTrue(this.iterable.anySatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.iterable.allSatisfy(value -> true));
        Assert.assertFalse(this.iterable.allSatisfy(BooleanPredicates.isFalse()));
        Assert.assertFalse(this.iterable.allSatisfy(BooleanPredicates.isTrue()));
    }

    @Test
    public void select()
    {
        Verify.assertSize(2, this.iterable.select(BooleanPredicates.isTrue()));
        Verify.assertSize(1, this.iterable.select(BooleanPredicates.isFalse()));
    }

    @Test
    public void reject()
    {
        Verify.assertSize(1, this.iterable.reject(BooleanPredicates.isTrue()));
        Verify.assertSize(2, this.iterable.reject(BooleanPredicates.isFalse()));
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertTrue(this.iterable.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(this.iterable.detectIfNone(BooleanPredicates.isFalse(), true));
        Assert.assertFalse(this.iterable.detectIfNone(value -> false, false));
    }

    @Test
    public void collect()
    {
        RichIterable<String> collect = this.iterable.collect(String::valueOf);
        Verify.assertIterableSize(3, collect);
        Assert.assertEquals("truefalsetrue", collect.makeString(""));
    }

    @Test
    public void toArray()
    {
        Assert.assertTrue(Arrays.equals(new boolean[]{true, false, true}, this.iterable.toArray()));
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(this.iterable.contains(true));
        Assert.assertTrue(this.iterable.contains(false));
    }

    @Test
    public void containsAllArray()
    {
        Assert.assertTrue(this.iterable.containsAll(true, false));
        Assert.assertTrue(this.iterable.containsAll(false, true));
        Assert.assertTrue(this.iterable.containsAll());
    }

    @Test
    public void containsAllIterable()
    {
        Assert.assertTrue(this.iterable.containsAll(BooleanArrayList.newListWith(true)));
        Assert.assertTrue(this.iterable.containsAll(BooleanArrayList.newListWith(false)));
        Assert.assertTrue(this.iterable.containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(this.iterable.containsAll(BooleanArrayList.newListWith(false, true)));
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(true, false, true), this.iterable.toList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), this.iterable.toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(false, true, true), this.iterable.toBag());
    }
}
