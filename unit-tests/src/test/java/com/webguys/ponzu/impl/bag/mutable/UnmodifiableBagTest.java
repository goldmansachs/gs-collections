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

package com.webguys.ponzu.impl.bag.mutable;

import com.webguys.ponzu.api.bag.ImmutableBag;
import com.webguys.ponzu.api.bag.MutableBag;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.impl.collection.mutable.UnmodifiableMutableCollectionTestCase;
import com.webguys.ponzu.impl.factory.Bags;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.test.Verify;
import com.webguys.ponzu.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

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
}
