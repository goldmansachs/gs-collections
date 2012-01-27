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

package ponzu.impl.bag.mutable;

import ponzu.api.RichIterable;
import ponzu.api.bag.MutableBag;
import ponzu.api.block.function.Function;
import ponzu.api.multimap.Multimap;
import ponzu.api.partition.PartitionMutableCollection;
import ponzu.impl.block.factory.IntegerPredicates;
import ponzu.impl.collection.mutable.AbstractSynchronizedCollectionTestCase;
import ponzu.impl.factory.Bags;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static ponzu.impl.factory.Iterables.*;

/**
 * JUnit test for {@link SynchronizedBag}.
 */
public class SynchronizedBagTest extends AbstractSynchronizedCollectionTestCase
{
    @Override
    protected <T> MutableBag<T> classUnderTest()
    {
        return new SynchronizedBag<T>(HashBag.<T>newBag());
    }

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

    @Override
    @Test
    public void newEmpty()
    {
        super.newEmpty();

        Verify.assertInstanceOf(HashBag.class, this.classUnderTest().newEmpty());
    }

    @Override
    @Test
    public void getFirst()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getFirst());
        Assert.assertNull(this.classUnderTest().getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        Assert.assertNotNull(this.newWith(1, 2, 3).getLast());
        Assert.assertNull(this.classUnderTest().getLast());
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
        Verify.assertInstanceOf(SynchronizedBag.class, this.classUnderTest().asSynchronized());
    }

    @Override
    @Test
    public void asUnmodifiable()
    {
        Verify.assertInstanceOf(UnmodifiableBag.class, this.classUnderTest().asUnmodifiable());
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
}
