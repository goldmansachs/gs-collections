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

package com.gs.collections.impl.list.immutable;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableEmptyListTest extends AbstractImmutableListTestCase
{
    @Override
    protected ImmutableList<Integer> classUnderTest()
    {
        return Lists.immutable.of();
    }

    @Override
    @Test
    public void indexOf()
    {
        Assert.assertEquals(-1, this.classUnderTest().indexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.indexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(Integer.MAX_VALUE));
    }

    @Override
    @Test
    public void lastIndexOf()
    {
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(Integer.MAX_VALUE));
    }

    @Test
    public void newWithout()
    {
        Assert.assertSame(Lists.immutable.of(), Lists.immutable.of().newWithout(1));
        Assert.assertSame(Lists.immutable.of(), Lists.immutable.of().newWithoutAll(Interval.oneTo(3)));
    }

    @Override
    @Test
    public void reverseForEach()
    {
        ImmutableList<Integer> list = Lists.immutable.of();
        MutableList<Integer> result = Lists.mutable.of();
        list.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(list, result);
    }

    @Override
    @Test
    public void forEachFromTo()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        final MutableList<Integer> reverseResult = Lists.mutable.of();
        final ImmutableList<Integer> list = this.classUnderTest();
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEach(0, list.size() - 1, CollectionAddProcedure.on(result));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEach(list.size() - 1, 0, CollectionAddProcedure.on(reverseResult));
            }
        });
    }

    @Override
    @Test
    public void forEachWithIndexFromTo()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        final MutableList<Integer> reverseResult = Lists.mutable.of();
        final ImmutableList<Integer> list = this.classUnderTest();
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEachWithIndex(0, list.size() - 1, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(result)));
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                list.forEachWithIndex(list.size() - 1, 0, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResult)));
            }
        });
    }

    @Override
    @Test
    public void detect()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.detect(Predicates.equal(1)));
    }

    @Override
    @Test
    public void detectWith()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.detectWith(Predicates2.equal(), Integer.valueOf(1)));
    }

    @Override
    @Test
    public void distinct()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertNotNull(integers.distinct());
        Assert.assertTrue(integers.isEmpty());
    }

    @Override
    @Test
    public void countWith()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertEquals(0, integers.countWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    @Test
    public void allSatisfy()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void allSatisfyWith()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    public void noneSatisfy()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void noneSatisfyWith()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.noneSatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    @Test
    public void anySatisfy()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Override
    public void anySatisfyWith()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfyWith(Predicates2.instanceOf(), Integer.class));
    }

    @Override
    @Test
    public void getFirst()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getFirst());
    }

    @Override
    @Test
    public void getLast()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertNull(integers.getLast());
    }

    @Override
    @Test
    public void isEmpty()
    {
        ImmutableList<Integer> list = this.classUnderTest();
        Assert.assertTrue(list.isEmpty());
        Assert.assertFalse(list.notEmpty());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min()
    {
        this.classUnderTest().min(Comparators.naturalOrder());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max()
    {
        this.classUnderTest().max(Comparators.naturalOrder());
    }

    @Test
    @Override
    public void min_null_throws()
    {
        // Not applicable for empty collections
        super.min_null_throws();
    }

    @Test
    @Override
    public void max_null_throws()
    {
        // Not applicable for empty collections
        super.max_null_throws();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void min_without_comparator()
    {
        this.classUnderTest().min();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void max_without_comparator()
    {
        this.classUnderTest().max();
    }

    @Test
    @Override
    public void min_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.min_null_throws_without_comparator();
    }

    @Test
    @Override
    public void max_null_throws_without_comparator()
    {
        // Not applicable for empty collections
        super.max_null_throws_without_comparator();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void minBy()
    {
        this.classUnderTest().minBy(Functions.getToString());
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    public void maxBy()
    {
        this.classUnderTest().maxBy(Functions.getToString());
    }

    @Override
    @Test
    public void zip()
    {
        ImmutableList<Integer> immutableList = this.classUnderTest();
        List<Object> nulls = Collections.nCopies(immutableList.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableList.size() + 1, null);

        ImmutableList<Pair<Integer, Object>> pairs = immutableList.zip(nulls);
        Assert.assertEquals(immutableList, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairs.collect(Functions.secondOfPair()));

        ImmutableList<Pair<Integer, Object>> pairsPlusOne = immutableList.zip(nullsPlusOne);
        Assert.assertEquals(immutableList, pairsPlusOne.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.secondOfPair()));

        Assert.assertEquals(immutableList.zip(nulls), immutableList.zip(nulls, FastList.<Pair<Integer, Object>>newList()));
    }

    @Override
    @Test
    public void zipWithIndex()
    {
        ImmutableList<Integer> immutableList = this.classUnderTest();
        ImmutableList<Pair<Integer, Integer>> pairs = immutableList.zipWithIndex();

        Assert.assertEquals(immutableList, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(FastList.<Integer>newList(), pairs.collect(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableList.zipWithIndex(), immutableList.zipWithIndex(FastList.<Pair<Integer, Integer>>newList()));
    }

    @Test
    public void chunk()
    {
        Assert.assertEquals(Lists.mutable.of(), this.classUnderTest().chunk(2));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.classUnderTest().chunk(0);
    }

    @Override
    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().chunk(10));
        Verify.assertInstanceOf(ImmutableList.class, this.classUnderTest().chunk(10));
    }

    @Override
    @Test
    public void equalsAndHashCode()
    {
        ImmutableList<Integer> immutable = this.classUnderTest();
        MutableList<Integer> mutable = FastList.newList(immutable);
        Verify.assertEqualsAndHashCode(immutable, mutable);
        Verify.assertPostSerializedIdentity(immutable);
        Assert.assertNotEquals(immutable, UnifiedSet.newSet(mutable));
    }

    @Override
    @Test
    public void takeWhile()
    {
        Assert.assertEquals(Lists.immutable.of(), this.classUnderTest().takeWhile(Predicates.alwaysTrue()));
        Assert.assertEquals(Lists.immutable.of(), this.classUnderTest().takeWhile(Predicates.alwaysFalse()));
    }

    @Override
    @Test
    public void dropWhile()
    {
        super.dropWhile();

        Assert.assertEquals(Lists.immutable.of(), this.classUnderTest().dropWhile(Predicates.alwaysTrue()));
        Assert.assertEquals(Lists.immutable.of(), this.classUnderTest().dropWhile(Predicates.alwaysFalse()));
    }

    @Override
    @Test
    public void partitionWhile()
    {
        super.partitionWhile();

        PartitionImmutableList<Integer> partition1 = this.classUnderTest().partitionWhile(Predicates.alwaysTrue());
        Assert.assertEquals(Lists.immutable.of(), partition1.getSelected());
        Assert.assertEquals(Lists.immutable.of(), partition1.getRejected());

        PartitionImmutableList<Integer> partiton2 = this.classUnderTest().partitionWhile(Predicates.alwaysFalse());
        Assert.assertEquals(Lists.immutable.of(), partiton2.getSelected());
        Assert.assertEquals(Lists.immutable.of(), partiton2.getRejected());
    }

    @Override
    @Test
    public void listIterator()
    {
        final ListIterator<Integer> it = this.classUnderTest().listIterator();
        Assert.assertFalse(it.hasPrevious());
        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(0, it.nextIndex());

        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                it.next();
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.remove();
            }
        });

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.add(null);
            }
        });
    }
}
