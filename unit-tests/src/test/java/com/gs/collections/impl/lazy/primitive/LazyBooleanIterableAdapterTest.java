/*
 * Copyright 2015 Goldman Sachs.
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
import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.factory.primitive.ByteLists;
import com.gs.collections.impl.factory.primitive.CharLists;
import com.gs.collections.impl.factory.primitive.DoubleLists;
import com.gs.collections.impl.factory.primitive.FloatLists;
import com.gs.collections.impl.factory.primitive.IntLists;
import com.gs.collections.impl.factory.primitive.LongLists;
import com.gs.collections.impl.factory.primitive.ShortLists;
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
    public void lazyCollectPrimitives()
    {
        Assert.assertEquals(BooleanLists.immutable.of(false, true, false), this.iterable.collectBoolean(e -> !e).toList());
        Assert.assertEquals(CharLists.immutable.of((char) 1, (char) 0, (char) 1), this.iterable.asLazy().collectChar(e -> e ? (char) 1 : (char) 0).toList());
        Assert.assertEquals(ByteLists.immutable.of((byte) 1, (byte) 0, (byte) 1), this.iterable.asLazy().collectByte(e -> e ? (byte) 1 : (byte) 0).toList());
        Assert.assertEquals(ShortLists.immutable.of((short) 1, (short) 0, (short) 1), this.iterable.asLazy().collectShort(e -> e ? (short) 1 : (short) 0).toList());
        Assert.assertEquals(IntLists.immutable.of(1, 0, 1), this.iterable.asLazy().collectInt(e -> e ? 1 : 0).toList());
        Assert.assertEquals(FloatLists.immutable.of(1.0f, 0.0f, 1.0f), this.iterable.asLazy().collectFloat(e -> e ? 1.0f : 0.0f).toList());
        Assert.assertEquals(LongLists.immutable.of(1L, 0L, 1L), this.iterable.asLazy().collectLong(e -> e ? 1L : 0L).toList());
        Assert.assertEquals(DoubleLists.immutable.of(1.0, 0.0, 1.0), this.iterable.asLazy().collectDouble(e -> e ? 1.0 : 0.0).toList());
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
