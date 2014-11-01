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

package com.gs.collections.impl.bag.immutable.primitive;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.ImmutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.collection.immutable.primitive.AbstractImmutableBooleanCollectionTestCase;
import com.gs.collections.impl.factory.primitive.BooleanBags;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Abstract JUnit test for {@link ImmutableBooleanBag}.
 */
public abstract class AbstractImmutableBooleanBagTestCase extends AbstractImmutableBooleanCollectionTestCase
{
    @Override
    protected abstract ImmutableBooleanBag classUnderTest();

    @Override
    protected ImmutableBooleanBag newWith(boolean... elements)
    {
        return BooleanBags.immutable.with(elements);
    }

    @Override
    protected MutableBooleanBag newMutableCollectionWith(boolean... elements)
    {
        return BooleanHashBag.newBagWith(elements);
    }

    @Override
    protected MutableBag<Object> newObjectCollectionWith(Object... elements)
    {
        return HashBag.newBagWith(elements);
    }

    @Test
    public void sizeDistinct()
    {
        Assert.assertEquals(0L, this.newWith().sizeDistinct());
        Assert.assertEquals(1L, this.newWith(true).sizeDistinct());
        Assert.assertEquals(1L, this.newWith(true, true, true).sizeDistinct());
        Assert.assertEquals(2L, this.newWith(true, false, true, false, true).sizeDistinct());
    }

    @Test
    public void forEachWithOccurrences()
    {
        StringBuilder stringBuilder = new StringBuilder();
        this.classUnderTest().forEachWithOccurrences((argument1, argument2) -> stringBuilder.append(argument1).append(argument2));
        String string = stringBuilder.toString();
        Assert.assertTrue("true2false1".equals(string)
                || "false1true2".equals(string));
    }

    @Override
    @Test
    public void size()
    {
        super.size();
        Verify.assertSize(3, this.classUnderTest());
    }

    @Override
    @Test
    public void booleanIterator()
    {
        BooleanHashBag bag = BooleanHashBag.newBagWith();
        BooleanIterator iterator = this.classUnderTest().booleanIterator();
        for (int i = 0; i < this.classUnderTest().size(); i++)
        {
            Assert.assertTrue(iterator.hasNext());
            bag.add(iterator.next());
        }
        Assert.assertEquals(bag, this.classUnderTest());
        Assert.assertFalse(iterator.hasNext());
    }

    @Override
    @Test
    public void anySatisfy()
    {
        super.anySatisfy();
        long[] count = {0};
        ImmutableBooleanBag bag = this.newWith(false, true, false);
        Assert.assertTrue(bag.anySatisfy(value -> {
            count[0]++;
            return value;
        }));
        Assert.assertEquals(2L, count[0]);
    }

    @Override
    @Test
    public void allSatisfy()
    {
        super.allSatisfy();
        int[] count = {0};
        ImmutableBooleanBag bag = this.newWith(false, true, false);
        Assert.assertFalse(bag.allSatisfy(value -> {
            count[0]++;
            return !value;
        }));
        Assert.assertEquals(2L, count[0]);
    }

    @Override
    @Test
    public void noneSatisfy()
    {
        super.noneSatisfy();
        ImmutableBooleanBag bag = this.newWith(false, true, false);
        Assert.assertFalse(bag.noneSatisfy(value -> value));
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();
        ImmutableBooleanBag bag = this.newWith(true, false, false, true, true, true);
        BooleanToObjectFunction<String> stringValueOf = parameter -> parameter ? "true" : "false";
        Assert.assertEquals(HashBag.newBagWith("true", "false", "false", "true", "true", "true"), bag.collect(stringValueOf));
        ImmutableBooleanBag bag1 = this.newWith(false, false);
        Assert.assertEquals(HashBag.newBagWith("false", "false"), bag1.collect(stringValueOf));
        ImmutableBooleanBag bag2 = this.newWith(true, true);
        Assert.assertEquals(HashBag.newBagWith("true", "true"), bag2.collect(stringValueOf));
    }

    @Override
    @Test
    public void testEquals()
    {
        super.testEquals();
        ImmutableBooleanCollection collection1 = this.newWith(true, false, true, false);
        ImmutableBooleanCollection collection2 = this.newWith(true, false, false, true);
        ImmutableBooleanCollection collection3 = this.newWith(true, false);
        ImmutableBooleanCollection collection4 = this.newWith(true, true, false);
        Assert.assertEquals(collection1, collection2);
        Verify.assertPostSerializedIdentity(this.newWith());
        Assert.assertNotEquals(collection3, collection4);
        Assert.assertNotEquals(collection3, BooleanArrayList.newListWith(true, false));
        Assert.assertNotEquals(this.newWith(true), BooleanArrayList.newListWith(true));
        Assert.assertNotEquals(this.newWith(), BooleanArrayList.newListWith());
    }

    @Override
    @Test
    public void testHashCode()
    {
        super.testHashCode();
        ImmutableBooleanCollection collection1 = this.newWith(true, false, true, false);
        ImmutableBooleanCollection collection2 = this.newWith(true, false, false, true);
        ImmutableBooleanCollection collection3 = this.newWith(true, false);
        ImmutableBooleanCollection collection4 = this.newWith(true, true, false);
        Verify.assertEqualsAndHashCode(collection1, collection2);
        Assert.assertNotEquals(collection3.hashCode(), collection4.hashCode());
    }

    @Override
    @Test
    public void testToString()
    {
        super.testToString();
        Assert.assertEquals("[true, true, true]", BooleanHashBag.newBagWith(true, true, true).toString());
    }

    @Override
    @Test
    public void makeString()
    {
        super.makeString();
        Assert.assertEquals("true, true, true", BooleanHashBag.newBagWith(true, true, true).makeString());
    }

    @Override
    @Test
    public void appendString()
    {
        super.appendString();
        StringBuilder appendable1 = new StringBuilder();
        this.newWith(true, true, true).appendString(appendable1);
        Assert.assertEquals("true, true, true", appendable1.toString());

        StringBuilder appendable2 = new StringBuilder();
        ImmutableBooleanBag bag1 = this.newWith(false, false, true);
        bag1.appendString(appendable2);
        Assert.assertTrue(appendable2.toString(), "false, false, true".equals(appendable2.toString())
                || "true, false, false".equals(appendable2.toString())
                || "false, true, false".equals(appendable2.toString()));
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        MutableBooleanList list = this.newWith(false, false, true).toList();
        Assert.assertTrue(list.equals(BooleanArrayList.newListWith(false, false, true))
                || list.equals(BooleanArrayList.newListWith(true, false, false))
                || list.equals(BooleanArrayList.newListWith(false, true, false)));
    }

    @Test
    public void toImmutable()
    {
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().toImmutable());
        ImmutableBooleanBag expected = this.classUnderTest();
        Assert.assertSame(expected, expected.toImmutable());
    }
}
