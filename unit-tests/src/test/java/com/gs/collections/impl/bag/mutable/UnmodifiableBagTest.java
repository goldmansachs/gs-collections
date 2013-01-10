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

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.primitive.IntPredicates;
import com.gs.collections.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * Abstract JUnit test for {@link UnmodifiableBag}.
 */
public class UnmodifiableBagTest
        extends UnmodifiableMutableCollectionTestCase<String>
{
    @Override
    protected MutableBag<String> getCollection()
    {
        return Bags.mutable.of("").asUnmodifiable();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addOccurrences()
    {
        this.getCollection().addOccurrences(null, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeOccurrences()
    {
        this.getCollection().removeOccurrences(null, 1);
    }

    @Test
    public void asUnmodifiable()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(UnmodifiableBag.class, bag.asUnmodifiable());
    }

    @Test
    public void asSynchronized()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(SynchronizedBag.class, bag.asSynchronized());
    }

    @Test
    public void toImmutable()
    {
        MutableBag<?> bag = this.getCollection();
        Verify.assertInstanceOf(ImmutableBag.class, bag.toImmutable());
    }

    @Test
    public void equalsAndHashCode()
    {
        Verify.assertEqualsAndHashCode(this.getCollection(), Bags.mutable.of(""));
    }

    @Test
    public void forEachWithOccurrences()
    {
        final MutableList<Pair<Object, Integer>> list = Lists.mutable.of();
        this.getCollection().forEachWithOccurrences(new ObjectIntProcedure<Object>()
        {
            public void value(Object each, int index)
            {
                list.add(Tuples.pair(each, index));
            }
        });
        Assert.assertEquals(FastList.newListWith(Tuples.pair("", 1)), list);
    }

    @Test
    public void toMapOfItemToCount()
    {
        Assert.assertEquals(UnifiedMap.newWithKeysValues("", 1), this.getCollection().toMapOfItemToCount());
    }

    @Test
    public void selectInstancesOf()
    {
        MutableBag<Number> numbers = UnmodifiableBag.of(HashBag.<Number>newBagWith(1, 2.0, 3, 4.0, 5));
        Assert.assertEquals(iBag(1, 3, 5), numbers.selectInstancesOf(Integer.class));
        Assert.assertEquals(iBag(1, 2.0, 3, 4.0, 5), numbers.selectInstancesOf(Number.class));
    }

    @Test
    public void selectByOccurrences()
    {
        MutableBag<Integer> integers = UnmodifiableBag.of(HashBag.newBagWith(1, 1, 1, 1, 2, 2, 2, 3, 3, 4));
        Assert.assertEquals(iBag(1, 1, 1, 1, 3, 3), integers.selectByOccurrences(IntPredicates.isEven()));
    }

}
