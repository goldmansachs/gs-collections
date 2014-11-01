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

import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.math.MutableInteger;
import org.junit.Assert;
import org.junit.Test;

public class SelectBooleanIterableTest
{
    private final SelectBooleanIterable iterable = new SelectBooleanIterable(BooleanArrayList.newListWith(true, false, false, true), BooleanPredicates.isTrue());

    @Test
    public void booleanIterator()
    {
        StringBuilder concat = new StringBuilder();
        for (BooleanIterator iterator = this.iterable.booleanIterator(); iterator.hasNext(); )
        {
            concat.append(iterator.next());
        }
        Assert.assertEquals("truetrue", concat.toString());
    }

    @Test
    public void forEach()
    {
        String[] concat = new String[1];
        concat[0] = "";
        this.iterable.forEach(each -> concat[0] += each);
        Assert.assertEquals("truetrue", concat[0]);
    }

    @Test
    public void injectInto()
    {
        MutableInteger result = this.iterable.injectInto(new MutableInteger(0), (object, value) -> object.add(value ? 1 : 0));
        Assert.assertEquals(new MutableInteger(2), result);
    }

    @Test
    public void size()
    {
        Assert.assertEquals(2L, this.iterable.size());
    }

    @Test
    public void empty()
    {
        Assert.assertTrue(this.iterable.notEmpty());
        Assert.assertFalse(this.iterable.isEmpty());
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, this.iterable.count(BooleanPredicates.isTrue()));
        Assert.assertEquals(0L, this.iterable.count(BooleanPredicates.isFalse()));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(this.iterable.anySatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.iterable.anySatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(this.iterable.allSatisfy(BooleanPredicates.isTrue()));
        Assert.assertFalse(this.iterable.allSatisfy(BooleanPredicates.isFalse()));
    }

    @Test
    public void select()
    {
        Assert.assertEquals(0L, this.iterable.select(BooleanPredicates.isFalse()).size());
        Assert.assertEquals(2L, this.iterable.select(BooleanPredicates.equal(true)).size());
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(2L, this.iterable.reject(BooleanPredicates.isFalse()).size());
        Assert.assertEquals(0L, this.iterable.reject(BooleanPredicates.equal(true)).size());
    }

    @Test
    public void detectIfNone()
    {
        Assert.assertTrue(this.iterable.detectIfNone(BooleanPredicates.isTrue(), false));
        Assert.assertFalse(this.iterable.detectIfNone(BooleanPredicates.isFalse(), false));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(2L, this.iterable.collect(String::valueOf).size());
    }

    @Test
    public void toArray()
    {
        Assert.assertEquals(2L, this.iterable.toArray().length);
        Assert.assertTrue(this.iterable.toArray()[0]);
        Assert.assertTrue(this.iterable.toArray()[1]);
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(this.iterable.contains(true));
        Assert.assertFalse(this.iterable.contains(false));
    }

    @Test
    public void containsAll()
    {
        Assert.assertTrue(this.iterable.containsAll(true, true));
        Assert.assertFalse(this.iterable.containsAll(false, true));
        Assert.assertFalse(this.iterable.containsAll(false, false));
    }
}
