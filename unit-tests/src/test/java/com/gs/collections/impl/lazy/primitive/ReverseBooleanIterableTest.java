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

import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ReverseBooleanIterable}.
 */
public class ReverseBooleanIterableTest
{
    @Test
    public void isEmpty()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Verify.assertEmpty(new BooleanArrayList().asReversed());
        Verify.assertNotEmpty(iterable);
    }

    @Test
    public void contains()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false).asReversed();
        Assert.assertTrue(iterable.contains(false));
        Assert.assertFalse(iterable.contains(true));
    }

    @Test
    public void containsAll()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(true, false, true).asReversed();
        Assert.assertTrue(iterable.containsAll(true));
        Assert.assertTrue(iterable.containsAll(true, false));
        Assert.assertFalse(BooleanArrayList.newListWith(false, false).asReversed().containsAll(true));
        Assert.assertFalse(BooleanArrayList.newListWith(false, false).asReversed().containsAll(BooleanArrayList.newListWith(true, false)));
        Assert.assertTrue(BooleanArrayList.newListWith(false, false, true).asReversed().containsAll(BooleanArrayList.newListWith(true, false)));
    }

    @Test
    public void iterator()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        BooleanIterator iterator = iterable.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        BooleanIterator iterator = iterable.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }
        iterator.next();
    }

    @Test
    public void forEach()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        boolean[] result = {true};
        iterable.forEach(each -> result[0] &= each);

        Assert.assertFalse(result[0]);
    }

    @Test
    public void size()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Verify.assertSize(0, new BooleanArrayList().asReversed());
        Verify.assertSize(3, iterable);
    }

    @Test
    public void empty()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Assert.assertTrue(iterable.notEmpty());
        Verify.assertNotEmpty(iterable);
    }

    @Test
    public void count()
    {
        Assert.assertEquals(2L, BooleanArrayList.newListWith(false, false, true).asReversed().count(BooleanPredicates.equal(false)));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(BooleanArrayList.newListWith(true, false).asReversed().anySatisfy(BooleanPredicates.equal(false)));
        Assert.assertFalse(BooleanArrayList.newListWith(true).asReversed().anySatisfy(BooleanPredicates.equal(false)));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertFalse(BooleanArrayList.newListWith(true, false).asReversed().allSatisfy(BooleanPredicates.equal(false)));
        Assert.assertTrue(BooleanArrayList.newListWith(false, false).asReversed().allSatisfy(BooleanPredicates.equal(false)));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertFalse(BooleanArrayList.newListWith(true, false).asReversed().noneSatisfy(BooleanPredicates.equal(false)));
        Assert.assertTrue(BooleanArrayList.newListWith(false, false).asReversed().noneSatisfy(BooleanPredicates.equal(true)));
    }

    @Test
    public void select()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Verify.assertSize(2, iterable.select(BooleanPredicates.equal(false)));
        Verify.assertSize(1, iterable.select(BooleanPredicates.equal(true)));
    }

    @Test
    public void reject()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Verify.assertSize(1, iterable.reject(BooleanPredicates.equal(false)));
        Verify.assertSize(2, iterable.reject(BooleanPredicates.equal(true)));
    }

    @Test
    public void detectIfNone()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false).asReversed();
        Assert.assertFalse(iterable.detectIfNone(BooleanPredicates.equal(false), true));
        Assert.assertTrue(iterable.detectIfNone(BooleanPredicates.equal(true), true));
    }

    @Test
    public void collect()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Verify.assertIterablesEqual(FastList.newListWith(false, true, true), iterable.collect(parameter -> !parameter));
    }

    @Test
    public void toArray()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Assert.assertTrue(iterable.toArray()[0]);
        Assert.assertFalse(iterable.toArray()[1]);
        Assert.assertFalse(iterable.toArray()[2]);
    }

    @Test
    public void testToString()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Assert.assertEquals("[true, false, false]", iterable.toString());
        Assert.assertEquals("[]", new BooleanArrayList().asReversed().toString());
    }

    @Test
    public void makeString()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        Assert.assertEquals("true, false, false", iterable.makeString());
        Assert.assertEquals("true", BooleanArrayList.newListWith(true).makeString("/"));
        Assert.assertEquals("true/false/false", iterable.makeString("/"));
        Assert.assertEquals(iterable.toString(), iterable.makeString("[", ", ", "]"));
        Assert.assertEquals("", new BooleanArrayList().asReversed().makeString());
    }

    @Test
    public void appendString()
    {
        BooleanIterable iterable = BooleanArrayList.newListWith(false, false, true).asReversed();
        StringBuilder appendable = new StringBuilder();
        new BooleanArrayList().asReversed().appendString(appendable);
        Assert.assertEquals("", appendable.toString());
        StringBuilder appendable2 = new StringBuilder();
        iterable.appendString(appendable2);
        Assert.assertEquals("true, false, false", appendable2.toString());
        StringBuilder appendable3 = new StringBuilder();
        iterable.appendString(appendable3, "/");
        Assert.assertEquals("true/false/false", appendable3.toString());
        StringBuilder appendable4 = new StringBuilder();
        iterable.appendString(appendable4, "[", ", ", "]");
        Assert.assertEquals(iterable.toString(), appendable4.toString());
    }

    @Test
    public void toList()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(false, true), BooleanArrayList.newListWith(true, false).asReversed().toList());
    }

    @Test
    public void toSet()
    {
        Assert.assertEquals(BooleanHashSet.newSetWith(true, false), BooleanArrayList.newListWith(true, false).asReversed().toSet());
    }

    @Test
    public void toBag()
    {
        Assert.assertEquals(BooleanHashBag.newBagWith(true, false), BooleanArrayList.newListWith(true, false).asReversed().toBag());
    }

    @Test
    public void asLazy()
    {
        Assert.assertEquals(BooleanArrayList.newListWith(false, true), BooleanArrayList.newListWith(true, false).asReversed().asLazy().toList());
    }
}
