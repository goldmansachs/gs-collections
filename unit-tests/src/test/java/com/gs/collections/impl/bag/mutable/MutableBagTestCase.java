/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.mutable.AbstractCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public abstract class MutableBagTestCase extends AbstractCollectionTestCase
{
    @Override
    protected abstract <T> MutableBag<T> classUnderTest();

    @Override
    protected <T> MutableBag<T> newWith(T... littleElements)
    {
        return (MutableBag<T>) super.newWith(littleElements);
    }

    @Override
    protected <T> MutableBag<T> newWith(T one)
    {
        return (MutableBag<T>) super.newWith(one);
    }

    @Override
    protected <T> MutableBag<T> newWith(T one, T two)
    {
        return (MutableBag<T>) super.newWith(one, two);
    }

    @Override
    protected <T> MutableBag<T> newWith(T one, T two, T three)
    {
        return (MutableBag<T>) super.newWith(one, two, three);
    }

    @Test
    @Override
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        Verify.assertNotEquals(this.newWith(1, 1, 2, 3), this.newWith(1, 2, 2, 3));
        Verify.assertEqualsAndHashCode(this.newWith(null, null, 2, 3), this.newWith(null, 2, null, 3));
    }

    @Test
    public void toStringOfItemToCount()
    {
        Assert.assertEquals("{}", this.newWith().toStringOfItemToCount());
        Assert.assertEquals("{1=3}", this.newWith(1, 1, 1).toStringOfItemToCount());
        String actual = this.newWith(1, 2, 2).toStringOfItemToCount();
        Assert.assertTrue("{1=1, 2=2}".equals(actual) || "{2=2, 1=1}".equals(actual));
    }

    @Test
    public void toMapOfItemToCount()
    {
        MutableBag<Integer> bag = this.newWith(1, 2, 2, 3, 3, 3);
        Assert.assertEquals(UnifiedMap.newWithKeysValues(1, 1, 2, 2, 3, 3), bag.toMapOfItemToCount());
    }

    @Test
    public void add()
    {
        MutableBag<Integer> bag = this.classUnderTest();
        bag.add(1);
        bag.add(1);
        Verify.assertSize(2, bag);
        bag.add(1);
        Verify.assertSize(3, bag);
    }

    @Override
    @Test
    public void iterator()
    {
        Bag<Integer> bag = this.newWith(1, 1, 2);
        MutableList<Integer> validate = Lists.mutable.of();
        for (Integer each : bag)
        {
            validate.add(each);
        }
        Assert.assertEquals(HashBag.newBagWith(1, 1, 2), HashBag.newBag(validate));
    }

    @Override
    @Test
    public void forEach()
    {
        MutableBag<Integer> bag = this.newWith(1, 1, 2);
        MutableList<Integer> validate = Lists.mutable.of();
        bag.forEach(CollectionAddProcedure.on(validate));
        Assert.assertEquals(HashBag.newBagWith(1, 1, 2), HashBag.newBag(validate));
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableBag<Integer> bag = this.classUnderTest();
        bag.addOccurrences(1, 3);
        bag.addOccurrences(2, 2);
        bag.addOccurrences(3, 1);
        final IntegerSum sum = new IntegerSum(0);
        bag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                sum.add(each * index);
            }
        });
        Assert.assertEquals(10, sum.getIntSum());
        bag.removeOccurrences(2, 1);
        final IntegerSum sum2 = new IntegerSum(0);
        bag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                sum2.add(each * index);
            }
        });
        Assert.assertEquals(8, sum2.getIntSum());
        bag.removeOccurrences(1, 3);
        final IntegerSum sum3 = new IntegerSum(0);
        bag.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int index)
            {
                sum3.add(each * index);
            }
        });
        Assert.assertEquals(5, sum3.getIntSum());
    }

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();
        Verify.assertInstanceOf(MutableBag.class, this.classUnderTest());
        Verify.assertInstanceOf(ImmutableBag.class, this.classUnderTest().toImmutable());
        Assert.assertFalse(this.classUnderTest().toImmutable() instanceof MutableBag);
    }

    @Test
    @Override
    public void getLast()
    {
        Assert.assertEquals(Integer.valueOf(1), this.newWith(1).getLast());
        Assert.assertEquals(Integer.valueOf(3), this.newWith(3).getLast());
    }

    @Test
    public void occurrencesOf()
    {
        MutableBag<Integer> bag = this.newWith(1, 1, 2);
        Assert.assertEquals(2, bag.occurrencesOf(1));
        Assert.assertEquals(1, bag.occurrencesOf(2));
    }

    @Test
    public void addOccurrences()
    {
        MutableBag<Object> bag = this.classUnderTest();
        bag.addOccurrences(new Object(), 0);
        assertBagsEqual(HashBag.<Object>newBag(), bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOccurrences_throws()
    {
        this.classUnderTest().addOccurrences(new Object(), -1);
    }

    @Test
    public void removeOccurrences()
    {
        MutableBag<String> bag = this.newWith("betamax-tape", "betamax-tape");
        MutableBag<String> expected = HashBag.newBag(bag);

        Assert.assertFalse(bag.removeOccurrences("dvd", 2));
        assertBagsEqual(expected, bag);

        Assert.assertFalse(bag.removeOccurrences("dvd", 0));
        assertBagsEqual(expected, bag);

        Assert.assertFalse(bag.removeOccurrences("betamax-tape", 0));
        assertBagsEqual(expected, bag);

        Assert.assertTrue(bag.removeOccurrences("betamax-tape", 1));
        assertBagsEqual(HashBag.newBagWith("betamax-tape"), bag);

        Assert.assertTrue(bag.removeOccurrences("betamax-tape", 10));
        assertBagsEqual(HashBag.<String>newBag(), bag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeOccurrences_throws()
    {
        this.classUnderTest().removeOccurrences(new Object(), -1);
    }

    protected static void assertBagsEqual(Bag<?> expected, Bag<?> actual)
    {
        Assert.assertEquals(expected.toMapOfItemToCount(), actual.toMapOfItemToCount());
        Assert.assertEquals(expected.sizeDistinct(), actual.sizeDistinct());
        Assert.assertEquals(expected.size(), actual.size());
        Verify.assertEqualsAndHashCode(expected, actual);
    }

    @Test
    public void toSortedListWith()
    {
        Assert.assertEquals(
                FastList.<Integer>newListWith(1, 2, 2, 3, 3, 3),
                this.newWith(3, 3, 3, 2, 2, 1).toSortedList());
    }

    @Override
    @Test
    public void toSet()
    {
        super.toSet();
        MutableBag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        Assert.assertEquals(UnifiedSet.newSetWith(1, 2, 3), bag.toSet());
    }

    @Override
    @Test
    public void toList()
    {
        super.toList();
        MutableBag<Integer> bag = this.newWith(1, 1, 1);
        Assert.assertEquals(FastList.newListWith(1, 1, 1), bag.toList());
    }

    @Override
    @Test
    public void remove()
    {
        super.remove();
        MutableBag<String> bag = this.newWith("dakimakura", "dakimakura");
        Assert.assertFalse(bag.remove("Mr. T"));
        Assert.assertTrue(bag.remove("dakimakura"));
        Assert.assertTrue(bag.remove("dakimakura"));
        Assert.assertFalse(bag.remove("dakimakura"));
        assertBagsEqual(Bags.mutable.of(), bag);
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedBag.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableBag.class, this.classUnderTest().asUnmodifiable());
    }

    @Test
    public void serialization()
    {
        MutableBag<String> bag = this.newWith("One", "Two", "Two", "Three", "Three", "Three");
        Verify.assertPostSerializedEqualsAndHashCode(bag);
    }

    @Override
    @Test
    public void partition()
    {
        super.partition();

        MutableBag<Integer> integers = this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        PartitionMutableCollection<Integer> result = integers.partition(IntegerPredicates.isEven());
        Assert.assertEquals(iBag(2, 2, 4, 4, 4, 4), result.getSelected());
        Assert.assertEquals(iBag(1, 3, 3, 3), result.getRejected());
    }

    @Test
    public void selectByOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        Assert.assertEquals(iBag(1, 1, 1, 1, 3, 3), integers.selectByOccurrences(IntPredicates.isEven()));
    }
}
