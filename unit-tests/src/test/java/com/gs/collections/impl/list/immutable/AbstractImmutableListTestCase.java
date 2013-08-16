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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.list.PartitionImmutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollectionTestCase;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.ListIterate;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public abstract class AbstractImmutableListTestCase extends AbstractImmutableCollectionTestCase
{
    @Override
    protected abstract ImmutableList<Integer> classUnderTest();

    @Override
    protected <T> MutableList<T> newMutable()
    {
        return FastList.newList();
    }

    @Test
    public void testEqualsAndHashCode()
    {
        ImmutableList<Integer> immutable = this.classUnderTest();
        MutableList<Integer> mutable1 = FastList.newList(immutable);
        ImmutableList<Integer> immutable1 = mutable1.toImmutable();
        List<Integer> mutable2 = new LinkedList<Integer>(mutable1);
        List<Integer> mutable3 = new ArrayList<Integer>(mutable1);
        Verify.assertEqualsAndHashCode(mutable1, immutable);
        Verify.assertEqualsAndHashCode(immutable1, immutable);
        Verify.assertEqualsAndHashCode(mutable2, immutable);
        Verify.assertEqualsAndHashCode(mutable3, immutable);
        Verify.assertPostSerializedEqualsAndHashCode(immutable);
        Assert.assertNotEquals(immutable, UnifiedSet.newSet(mutable1));
        mutable1.add(null);
        mutable2.add(null);
        mutable3.add(null);
        Assert.assertNotEquals(mutable1, immutable);
        Assert.assertNotEquals(mutable2, immutable);
        Assert.assertNotEquals(mutable3, immutable);
        mutable1.remove(null);
        mutable2.remove(null);
        mutable3.remove(null);
        Verify.assertEqualsAndHashCode(mutable1, immutable);
        Verify.assertEqualsAndHashCode(mutable2, immutable);
        Verify.assertEqualsAndHashCode(mutable3, immutable);
        if (immutable.size() > 2)
        {
            mutable1.set(2, null);
            mutable2.set(2, null);
            mutable3.set(2, null);
            Assert.assertNotEquals(mutable1, immutable);
            Assert.assertNotEquals(mutable2, immutable);
            Assert.assertNotEquals(mutable3, immutable);
            mutable1.remove(2);
            mutable2.remove(2);
            mutable3.remove(2);
            Assert.assertNotEquals(mutable1, immutable);
            Assert.assertNotEquals(mutable2, immutable);
            Assert.assertNotEquals(mutable3, immutable);
        }
    }

    @Test
    public void testContains()
    {
        ImmutableList<Integer> list = this.classUnderTest();
        for (int i = 1; i <= list.size(); i++)
        {
            Assert.assertTrue(list.contains(i));
        }
        Assert.assertFalse(list.contains(list.size() + 1));
    }

    @Test
    public void testContainsAllArray()
    {
        Assert.assertTrue(this.classUnderTest().containsAllArguments(this.classUnderTest().toArray()));
    }

    @Test
    public void testContainsAllIterable()
    {
        Assert.assertTrue(this.classUnderTest().containsAllIterable(this.classUnderTest()));
    }

    @Test
    public void testIndexOf()
    {
        Assert.assertEquals(0, this.classUnderTest().indexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.indexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().indexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testLastIndexOf()
    {
        Assert.assertEquals(0, this.classUnderTest().lastIndexOf(1));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(null));
        ImmutableList<Integer> immutableList = this.classUnderTest().newWith(null);
        Assert.assertEquals(immutableList.size() - 1, immutableList.lastIndexOf(null));
        Assert.assertEquals(-1, this.classUnderTest().lastIndexOf(Integer.MAX_VALUE));
    }

    @Test
    public void testGet()
    {
        final ImmutableList<Integer> list = this.classUnderTest();
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                list.get(list.size() + 1);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                list.get(-1);
            }
        });
    }

    @Test
    public void testForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> collection = this.classUnderTest();
        collection.forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(collection, result);
    }

    @Test
    public void testReverseForEach()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.reverseForEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), result);
    }

    @Test
    public void testForEachFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> reverseResult = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.forEach(0, list.size() - 1, CollectionAddProcedure.on(result));
        Assert.assertEquals(list, result);
        list.forEach(list.size() - 1, 0, CollectionAddProcedure.on(reverseResult));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), reverseResult);
    }

    @Test
    public void testForEachWithIndexFromTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        MutableList<Integer> reverseResult = Lists.mutable.of();
        ImmutableList<Integer> list = this.classUnderTest();
        list.forEachWithIndex(0, list.size() - 1, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(result)));
        Assert.assertEquals(list, result);
        list.forEachWithIndex(list.size() - 1, 0, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResult)));
        Assert.assertEquals(ListIterate.reverseThis(FastList.newList(list)), reverseResult);
    }

    @Test
    public void testForEachWith()
    {
        final MutableCollection<Integer> result = Lists.mutable.of();
        this.classUnderTest().forEachWith(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                result.add(argument1 + argument2);
            }
        }, 0);
        Assert.assertEquals(this.classUnderTest(), result);
    }

    @Test
    public void testForEachWithIndex()
    {
        final MutableList<Integer> result = Lists.mutable.of();
        this.classUnderTest().forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                result.add(object + index);
            }
        });
        result.forEachWithIndex(new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(object, result.set(index, object - index));
            }
        });
        Assert.assertEquals(this.classUnderTest(), result);
    }

    @Test
    public void testSelectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.select(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Verify.assertEmpty(integers.select(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testRejectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Verify.assertEmpty(integers.reject(Predicates.lessThan(integers.size() + 1), FastList.<Integer>newList()));
        Assert.assertEquals(integers, integers.reject(Predicates.greaterThan(integers.size()), FastList.<Integer>newList()));
    }

    @Test
    public void testCollectWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collect(Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void flatCollectWithTarget()
    {
        MutableCollection<String> actual = this.classUnderTest().flatCollect(new Function<Integer, MutableList<String>>()
        {
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        }, FastList.<String>newList());

        ImmutableCollection<String> expected = this.classUnderTest().collect(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDistinct()
    {
        ImmutableList<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.newWith(1).distinct());
    }

    @Test
    public void zip()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        List<Object> nulls = Collections.nCopies(immutableCollection.size(), null);
        List<Object> nullsPlusOne = Collections.nCopies(immutableCollection.size() + 1, null);
        List<Object> nullsMinusOne = Collections.nCopies(immutableCollection.size() - 1, null);

        ImmutableCollection<Pair<Integer, Object>> pairs = immutableCollection.zip(nulls);
        Assert.assertEquals(immutableCollection, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairs.collect(Functions.<Object>secondOfPair()));

        ImmutableCollection<Pair<Integer, Object>> pairsPlusOne = immutableCollection.zip(nullsPlusOne);
        Assert.assertEquals(immutableCollection, pairsPlusOne.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(nulls, pairsPlusOne.collect(Functions.<Object>secondOfPair()));

        ImmutableCollection<Pair<Integer, Object>> pairsMinusOne = immutableCollection.zip(nullsMinusOne);
        Assert.assertEquals(immutableCollection.size() - 1, pairsMinusOne.size());
        Assert.assertTrue(immutableCollection.containsAllIterable(pairsMinusOne.collect(Functions.<Integer>firstOfPair())));

        Assert.assertEquals(immutableCollection.zip(nulls), immutableCollection.zip(nulls, FastList.<Pair<Integer, Object>>newList()));
    }

    @Test
    public void zipWithIndex()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        ImmutableCollection<Pair<Integer, Integer>> pairs = immutableCollection.zipWithIndex();

        Assert.assertEquals(immutableCollection, pairs.collect(Functions.<Integer>firstOfPair()));
        Assert.assertEquals(Interval.zeroTo(immutableCollection.size() - 1), pairs.collect(Functions.<Integer>secondOfPair()));

        Assert.assertEquals(immutableCollection.zipWithIndex(), immutableCollection.zipWithIndex(FastList.<Pair<Integer, Integer>>newList()));
    }

    @Test
    public void chunk_large_size()
    {
        Assert.assertEquals(this.classUnderTest(), this.classUnderTest().chunk(10).getFirst());
        Verify.assertInstanceOf(ImmutableList.class, this.classUnderTest().chunk(10).getFirst());
    }

    @Test
    public void testCollectIfWithTarget()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collectIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru(), FastList.<Integer>newList()));
    }

    @Test
    public void toList()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> list = integers.toList();
        Verify.assertEqualsAndHashCode(integers, list);
        Assert.assertNotSame(integers, list);
    }

    @Test
    public void toSortedListBy()
    {
        MutableList<Integer> mutableList = this.classUnderTest().toList();
        Collections.shuffle(mutableList);
        ImmutableList<Integer> immutableList = mutableList.toImmutable();
        MutableList<Integer> sortedList = immutableList.toSortedListBy(Functions.getIntegerPassThru());
        Assert.assertEquals(this.classUnderTest(), sortedList);
    }

    @Test
    public void testRemoveAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().remove(1);
            }
        });
    }

    @Test
    public void testSet()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().set(0, 1);
            }
        });
    }

    @Test
    public void testAddAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().add(0, 1);
            }
        });
    }

    @Test
    public void testAddAllAtIndex()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().addAll(0, Lists.fixedSize.<Integer>of());
            }
        });
    }

    @Test
    public void testSubList()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableListTestCase.this.classUnderTest().castToList().subList(0, 1);
            }
        });
    }

    @Test
    public void listIterator()
    {
        final ListIterator<Integer> it = this.classUnderTest().listIterator();
        Assert.assertFalse(it.hasPrevious());

        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                it.previous();
            }
        });

        Assert.assertEquals(-1, it.previousIndex());
        Assert.assertEquals(0, it.nextIndex());
        it.next();
        Assert.assertEquals(1, it.nextIndex());

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

        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                it.set(null);
            }
        });
    }

    @Test
    public void toStack()
    {
        MutableStack<Integer> stack = this.classUnderTest().toStack();
        Assert.assertEquals(stack.toSortedList().toReversed(), stack.toList());
    }

    @Test
    public void takeWhile()
    {
        Assert.assertEquals(
                iList(1),
                this.classUnderTest().takeWhile(Predicates.lessThan(2)));
    }

    @Test
    public void dropWhile()
    {
        Assert.assertEquals(
                this.classUnderTest(),
                this.classUnderTest().dropWhile(Predicates.lessThan(0)));
        Assert.assertEquals(
                Lists.immutable.of(),
                this.classUnderTest().dropWhile(Predicates.greaterThan(0)));
    }

    @Test
    public void partitionWhile()
    {
        PartitionImmutableList<Integer> partitionAll = this.classUnderTest().partitionWhile(Predicates.greaterThan(0));
        Assert.assertEquals(this.classUnderTest(), partitionAll.getSelected());
        Assert.assertEquals(Lists.immutable.of(), partitionAll.getRejected());

        PartitionImmutableList<Integer> partitionNone = this.classUnderTest().partitionWhile(Predicates.lessThan(0));
        Assert.assertEquals(Lists.immutable.of(), partitionNone.getSelected());
        Assert.assertEquals(this.classUnderTest(), partitionNone.getRejected());
    }

    @Override
    @Test
    public void collectBoolean()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        ImmutableBooleanCollection immutableCollection = integers.collectBoolean(PrimitiveFunctions.integerIsPositive());
        Verify.assertSize(integers.size(), immutableCollection);
    }
}
