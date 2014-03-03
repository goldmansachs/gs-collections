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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.partition.PartitionMutableCollection;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.collection.mutable.AbstractSynchronizedCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link SynchronizedBag}.
 */
public class SynchronizedBagTest extends AbstractSynchronizedCollectionTestCase
{
    @Override
    protected <T> MutableBag<T> newWith(T... littleElements)
    {
        return new SynchronizedBag<T>(HashBag.newBagWith(littleElements));
    }

    @Override
    @Test
    public void newEmpty()
    {
        super.newEmpty();

        Verify.assertInstanceOf(HashBag.class, this.newWith().newEmpty());
    }

    @Override
    @Test
    public void getFirst()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getFirst());
        Assert.assertNull(this.newWith().getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.newWith().getLast());
    }

    @Override
    @Test
    public void groupBy()
    {
        RichIterable<Integer> list = this.newWith(1, 2, 3, 4, 5, 6, 7);
        Multimap<Boolean, Integer> multimap =
                list.groupBy(new Function<Integer, Boolean>()
                {
                    public Boolean valueOf(Integer object)
                    {
                        return IntegerPredicates.isOdd().accept(object);
                    }
                });

        Assert.assertEquals(Bags.mutable.of(1, 3, 5, 7), multimap.get(Boolean.TRUE));
        Assert.assertEquals(Bags.mutable.of(2, 4, 6), multimap.get(Boolean.FALSE));
    }

    @Override
    @Test
    public void asSynchronized()
    {
        Verify.assertInstanceOf(SynchronizedBag.class, this.newWith().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableBag.class, this.newWith().asUnmodifiable());
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        super.equalsAndHashCode();
        Verify.assertPostSerializedEqualsAndHashCode(this.newWith(1, 1, 1, 2, 2, 3));
        Verify.assertInstanceOf(SynchronizedBag.class, SerializeTestHelper.serializeDeserialize(this.newWith(1, 1, 1, 2, 2, 3)));
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

    @Override
    @Test
    public void partitionWith()
    {
        super.partitionWith();

        MutableBag<Integer> integers = this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        PartitionMutableCollection<Integer> result = integers.partitionWith(Predicates2.in(), integers.select(IntegerPredicates.isEven()));
        Assert.assertEquals(iBag(2, 2, 4, 4, 4, 4), result.getSelected());
        Assert.assertEquals(iBag(1, 3, 3, 3), result.getRejected());
    }

    @Test
    public void selectByOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        Assert.assertEquals(iBag(1, 1, 1, 1, 3, 3), integers.selectByOccurrences(IntPredicates.isEven()));
    }

    @Test
    public void addOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        Assert.assertEquals(0, integers.occurrencesOf(5));
        integers.addOccurrences(5, 5);
        Assert.assertEquals(5, integers.occurrencesOf(5));
    }

    @Test
    public void removeOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        Assert.assertEquals(1, integers.occurrencesOf(4));
        Assert.assertEquals(3, integers.occurrencesOf(2));
        integers.removeOccurrences(4, 1);
        integers.removeOccurrences(2, 2);
        Assert.assertEquals(0, integers.occurrencesOf(4));
        Assert.assertEquals(1, integers.occurrencesOf(2));
    }

    @Test
    public void setOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        Assert.assertEquals(0, integers.occurrencesOf(5));
        Assert.assertEquals(3, integers.occurrencesOf(2));
        integers.setOccurrences(5, 5);
        integers.setOccurrences(2, 2);
        Assert.assertEquals(5, integers.occurrencesOf(5));
        Assert.assertEquals(2, integers.occurrencesOf(2));
    }

    @Test
    public void toMapOfItemWithCount()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        MapIterable<Integer, Integer> result = integers.toMapOfItemToCount();
        Assert.assertEquals(Maps.mutable.with(1, 4, 2, 3, 3, 2, 4, 1), result);
    }

    @Test
    public void toStringOfItemWithCount()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1);
        String result = integers.toStringOfItemToCount();
        Assert.assertEquals(Maps.mutable.with(1, 4).toString(), result);
    }

    @Test
    public void forEachWithOccurrences()
    {
        MutableBag<Integer> integers = this.newWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4);
        final MutableBag<Integer> result = HashBag.newBag();
        integers.forEachWithOccurrences(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer each, int parameter)
            {
                result.setOccurrences(each, parameter);
            }
        });
        Assert.assertEquals(integers, result);
    }
}
