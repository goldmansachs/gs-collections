/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.collection.immutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.collection.ImmutableCollection;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.partition.PartitionImmutableCollection;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public abstract class AbstractImmutableCollectionTestCase
{
    protected abstract ImmutableCollection<Integer> classUnderTest();

    protected abstract <T> MutableCollection<T> newMutable();

    @Test
    public void selectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().with(101).withAll(integers).select(IntegerPredicates.isOdd()),
                integers.selectWith(Predicates2.in(), iList(1, 3, 5, 7, 9), this.<Integer>newMutable().with(101)));
    }

    @Test
    public void rejectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(
                this.<Integer>newMutable().with(100).withAll(integers).reject(IntegerPredicates.isOdd()),
                integers.rejectWith(Predicates2.in(), iList(1, 3, 5, 7, 9), this.<Integer>newMutable().with(100)));
    }

    @Test
    public void partition()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        PartitionImmutableCollection<Integer> partition = integers.partition(IntegerPredicates.isOdd());
        Assert.assertEquals(
                integers.select(IntegerPredicates.isOdd()),
                partition.getSelected());
        Assert.assertEquals(
                integers.select(IntegerPredicates.isEven()),
                partition.getRejected());
    }

    @Test
    public void collectWith()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();

        MutableCollection<String> expected = this.<String>newMutable().with("?").withAll(integers.collect(new Function<Integer, String>()
        {
            public String valueOf(Integer object)
            {
                return object + "!";
            }
        }));

        MutableCollection<String> actual = integers.collectWith(new Function2<Integer, String, String>()
        {
            public String value(Integer argument1, String argument2)
            {
                return argument1 + argument2;
            }
        }, "!", this.<String>newMutable().with("?"));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInjectInto()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Integer result = integers.injectInto(0, AddFunction.INTEGER);
        Assert.assertEquals(FastList.newList(integers).injectInto(0, AddFunction.INTEGER_TO_INT), result.intValue());
    }

    @Test
    public void injectIntoInt()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).longValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_INT));
    }

    @Test
    public void injectIntoLong()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).longValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_LONG));
    }

    @Test
    public void injectIntoDouble()
    {
        Assert.assertEquals(
                this.classUnderTest().injectInto(0, AddFunction.INTEGER).doubleValue(),
                this.classUnderTest().injectInto(0, AddFunction.INTEGER_TO_DOUBLE),
                0.0);
    }

    @Test
    public void testMakeString()
    {
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + this.classUnderTest().makeString() + ']');
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + this.classUnderTest().makeString(", ") + ']');
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), this.classUnderTest().makeString("[", ", ", "]"));
    }

    @Test
    public void testAppendString()
    {
        Appendable builder1 = new StringBuilder();
        this.classUnderTest().appendString(builder1);
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + builder1.toString() + ']');

        Appendable builder2 = new StringBuilder();
        this.classUnderTest().appendString(builder2, ", ");
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), '[' + builder2.toString() + ']');

        Appendable builder3 = new StringBuilder();
        this.classUnderTest().appendString(builder3, "[", ", ", "]");
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), builder3.toString());
    }

    @Test
    public void testToString()
    {
        Assert.assertEquals(FastList.newList(this.classUnderTest()).toString(), this.classUnderTest().toString());
    }

    @Test
    public void testSelect()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.select(Predicates.lessThan(integers.size() + 1)));
        Verify.assertIterableEmpty(integers.select(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void testReject()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Verify.assertIterableEmpty(integers.reject(Predicates.lessThan(integers.size() + 1)));
        Assert.assertEquals(integers, integers.reject(Predicates.greaterThan(integers.size())));
    }

    @Test
    public void testCollect()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collect(Functions.getIntegerPassThru()));
    }

    @Test
    public void flatCollect()
    {
        RichIterable<String> actual = this.classUnderTest().flatCollect(new Function<Integer, MutableList<String>>()
        {
            public MutableList<String> valueOf(Integer integer)
            {
                return Lists.fixedSize.of(String.valueOf(integer));
            }
        });

        ImmutableCollection<String> expected = this.classUnderTest().collect(Functions.getToString());

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        this.classUnderTest().chunk(0);
    }

    @Test
    public void testDetect()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(1), integers.detect(Predicates.equal(1)));
        Assert.assertNull(integers.detect(Predicates.equal(integers.size() + 1)));
    }

    @Test
    public void testDetectIfNoneWithBlock()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Function0<Integer> function = new PassThruFunction0<Integer>(integers.size() + 1);
        Assert.assertEquals(Integer.valueOf(1), integers.detectIfNone(Predicates.equal(1), function));
        Assert.assertEquals(Integer.valueOf(integers.size() + 1), integers.detectIfNone(Predicates.equal(integers.size() + 1), function));
    }

    @Test
    public void testAllSatisfy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertTrue(integers.allSatisfy(Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(integers.allSatisfy(Predicates.equal(0)));
    }

    @Test
    public void testAnySatisfy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertFalse(integers.anySatisfy(Predicates.instanceOf(String.class)));
        Assert.assertTrue(integers.anySatisfy(Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void testCount()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers.size(), integers.count(Predicates.instanceOf(Integer.class)));
        Assert.assertEquals(0, integers.count(Predicates.instanceOf(String.class)));
    }

    @Test
    public void testCollectIf()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(integers, integers.collectIf(Predicates.instanceOf(Integer.class),
                Functions.getIntegerPassThru()));
    }

    @Test
    public void testGetFirst()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(1), integers.getFirst());
    }

    @Test
    public void testGetLast()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        Assert.assertEquals(Integer.valueOf(integers.size()), integers.getLast());
    }

    @Test
    public void testIsEmpty()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        Assert.assertFalse(immutableCollection.isEmpty());
        Assert.assertTrue(immutableCollection.notEmpty());
    }

    @Test
    public void testIterator()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        final Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; iterator.hasNext(); i++)
        {
            Integer integer = iterator.next();
            Assert.assertEquals(i + 1, integer.intValue());
        }
        Verify.assertThrows(NoSuchElementException.class, new Runnable()
        {
            public void run()
            {
                iterator.next();
            }
        });
    }

    @Test
    public void testToArray()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> copy = FastList.newList(integers);
        Assert.assertArrayEquals(integers.toArray(), copy.toArray());
        Assert.assertArrayEquals(integers.toArray(new Integer[integers.size()]), copy.toArray(new Integer[integers.size()]));
    }

    @Test
    public void toSortedList()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableList<Integer> copy = FastList.newList(integers);
        MutableList<Integer> list = integers.toSortedList(Collections.<Integer>reverseOrder());
        Assert.assertEquals(copy.sortThis(Collections.<Integer>reverseOrder()), list);
        MutableList<Integer> list2 = integers.toSortedList();
        Assert.assertEquals(copy.sortThis(), list2);
    }

    @Test
    public void toSortedSet()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSet();
        Verify.assertListsEqual(integers.toSortedList(), set.toList());
    }

    @Test
    public void toSortedSetWithComparator()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSet(Comparators.<Integer>reverseNaturalOrder());
        Assert.assertEquals(integers.toSet(), set);
        Assert.assertEquals(integers.toSortedList(Comparators.<Integer>reverseNaturalOrder()), set.toList());
    }

    @Test
    public void toSortedSetBy()
    {
        ImmutableCollection<Integer> integers = this.classUnderTest();
        MutableSortedSet<Integer> set = integers.toSortedSetBy(Functions.getToString());
        Verify.assertSortedSetsEqual(TreeSortedSet.newSet(integers), set);
    }

    @Test
    public void testForLoop()
    {
        ImmutableCollection<Integer> immutableCollection = this.classUnderTest();
        for (Integer each : immutableCollection)
        {
            Assert.assertNotNull(each);
        }
    }

    private ImmutableCollection<Integer> classUnderTestWithNull()
    {
        return this.classUnderTest().reject(Predicates.equal(1)).newWith(null);
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws()
    {
        this.classUnderTestWithNull().min(Comparators.naturalOrder());
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws()
    {
        this.classUnderTestWithNull().max(Comparators.naturalOrder());
    }

    @Test
    public void min()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min(Comparators.naturalOrder()));
    }

    @Test
    public void max()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().max(Comparators.reverse(Comparators.naturalOrder())));
    }

    @Test(expected = NullPointerException.class)
    public void min_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().min();
    }

    @Test(expected = NullPointerException.class)
    public void max_null_throws_without_comparator()
    {
        this.classUnderTestWithNull().max();
    }

    @Test
    public void min_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().min());
    }

    @Test
    public void max_without_comparator()
    {
        Assert.assertEquals(Integer.valueOf(this.classUnderTest().size()), this.classUnderTest().max());
    }

    @Test
    public void minBy()
    {
        Assert.assertEquals(Integer.valueOf(1), this.classUnderTest().minBy(Functions.getToString()));
    }

    @Test
    public void maxBy()
    {
        Assert.assertEquals(Integer.valueOf(this.classUnderTest().size()), this.classUnderTest().maxBy(Functions.getIntegerPassThru()));
    }

    @Test
    public void testIteratorRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                AbstractImmutableCollectionTestCase.this.classUnderTest().iterator().remove();
            }
        });
    }

    @Test
    public void testAdd()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).add(1);
            }
        });
    }

    @Test
    public void testRemove()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).remove(Integer.valueOf(1));
            }
        });
    }

    @Test
    public void testClear()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).clear();
            }
        });
    }

    @Test
    public void testRemoveAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).removeAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void testRetainAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).retainAll(Lists.fixedSize.of());
            }
        });
    }

    @Test
    public void testAddAll()
    {
        Verify.assertThrows(UnsupportedOperationException.class, new Runnable()
        {
            public void run()
            {
                ((Collection<Integer>) AbstractImmutableCollectionTestCase.this.classUnderTest()).addAll(Lists.fixedSize.<Integer>of());
            }
        });
    }
}
