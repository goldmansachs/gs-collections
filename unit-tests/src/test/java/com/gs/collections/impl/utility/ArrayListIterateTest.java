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

package com.gs.collections.impl.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.list.PartitionMutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

/**
 * JUnit test for {@link ArrayListIterate}.
 */
public class ArrayListIterateTest
{
    private static final class ThisIsNotAnArrayList<T>
            extends ArrayList<T>
    {
        private static final long serialVersionUID = 1L;

        private ThisIsNotAnArrayList(Collection<? extends T> collection)
        {
            super(collection);
        }
    }

    @Test
    public void testThisIsNotAnArrayList()
    {
        ThisIsNotAnArrayList<Integer> undertest = new ThisIsNotAnArrayList<Integer>(FastList.newListWith(1, 2, 3));
        Assert.assertNotSame(undertest.getClass(), ArrayList.class);
    }

    @Test
    public void testSortOnListWithLessThan10Elements()
    {
        ArrayList<Integer> integers = this.newArrayList(2, 3, 4, 1, 5, 7, 6, 9, 8);
        Verify.assertStartsWith(ArrayListIterate.sortThis(integers), 1, 2, 3, 4, 5, 6, 7, 8, 9);

        ArrayList<Integer> integers2 = this.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Verify.assertStartsWith(
                ArrayListIterate.sortThis(integers2, Collections.<Integer>reverseOrder()),
                9, 8, 7, 6, 5, 4, 3, 2, 1);

        ArrayList<Integer> integers3 = this.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Verify.assertStartsWith(ArrayListIterate.sortThis(integers3), 1, 2, 3, 4, 5, 6, 7, 8, 9);

        ArrayList<Integer> integers4 = this.newArrayList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        Verify.assertStartsWith(ArrayListIterate.sortThis(integers4), 1, 2, 3, 4, 5, 6, 7, 8, 9);

        ThisIsNotAnArrayList<Integer> arrayListThatIsnt = new ThisIsNotAnArrayList<Integer>(FastList.newListWith(9, 8, 7, 6, 5, 4, 3, 2, 1));
        Verify.assertStartsWith(ArrayListIterate.sortThis(arrayListThatIsnt), 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    public void testSortingWitoutAccessToInternalArray()
    {
        ThisIsNotAnArrayList<Integer> arrayListThatIsnt = new ThisIsNotAnArrayList<Integer>(FastList.newListWith(5, 3, 4, 1, 2));
        Verify.assertStartsWith(ArrayListIterate.sortThis(arrayListThatIsnt, Comparators.naturalOrder()), 1, 2, 3, 4, 5);
    }

    @Test
    public void testCopyToArray()
    {
        ThisIsNotAnArrayList<Integer> notAnArrayList = this.newNotAnArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer[] target1 = {1, 2, null, null};
        ArrayListIterate.toArray(notAnArrayList, target1, 2, 2);
        Assert.assertArrayEquals(target1, new Integer[]{1, 2, 1, 2});

        ArrayList<Integer> arrayList = this.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Integer[] target2 = {1, 2, null, null};
        ArrayListIterate.toArray(arrayList, target2, 2, 2);
        Assert.assertArrayEquals(target2, new Integer[]{1, 2, 1, 2});
    }

    @Test
    public void testSortOnListWithMoreThan10Elements()
    {
        ArrayList<Integer> integers = this.newArrayList(2, 3, 4, 1, 5, 7, 6, 8, 10, 9);
        Verify.assertStartsWith(ArrayListIterate.sortThis(integers), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        ArrayList<Integer> integers2 = this.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        Verify.assertStartsWith(
                ArrayListIterate.sortThis(integers2, Collections.<Integer>reverseOrder()),
                8, 7, 6, 5, 4, 3, 2, 1);

        ArrayList<Integer> integers3 = this.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Verify.assertStartsWith(ArrayListIterate.sortThis(integers3), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    public void testForEachUsingFromTo()
    {
        final ArrayList<Integer> integers = Interval.oneTo(5).addAllTo(new ArrayList<Integer>());
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayListIterate.forEach(integers, 0, 4, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);
        ArrayListIterate.forEach(integers, 4, 0, procedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(integers, 4, -1, procedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(integers, -1, 4, procedure);
            }
        });
    }

    @Test
    public void testForEachUsingFromToWithOptimisable()
    {
        ArrayList<Integer> expected = Interval.oneTo(5).addAllTo(new ArrayList<Integer>());
        final ArrayList<Integer> optimisableList = Interval.oneTo(105).addAllTo(new ArrayList<Integer>());
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayListIterate.forEach(optimisableList, 0, 4, CollectionAddProcedure.on(results));
        Assert.assertEquals(expected, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);
        ArrayListIterate.forEach(optimisableList, 4, 0, procedure);
        Assert.assertEquals(ListIterate.reverseThis(expected), reverseResults);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(optimisableList, 104, -1, procedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEach(optimisableList, -1, 4, procedure);
            }
        });
    }

    @Test
    public void testForEachWithIndexUsingFromTo()
    {
        final ArrayList<Integer> integers = Interval.oneTo(5).addAllTo(new ArrayList<Integer>());
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayListIterate.forEachWithIndex(integers, 0, 4, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(results)));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        ArrayListIterate.forEachWithIndex(integers, 4, 0, objectIntProcedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEachWithIndex(integers, 4, -1, objectIntProcedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEachWithIndex(integers, -1, 4, objectIntProcedure);
            }
        });
    }

    @Test
    public void testForEachWithIndexUsingFromToWithOptimisableList()
    {
        ArrayList<Integer> optimisableList = Interval.oneTo(105).addAllTo(new ArrayList<Integer>());
        final ArrayList<Integer> expected = Interval.oneTo(105).addAllTo(new ArrayList<Integer>());
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayListIterate.forEachWithIndex(optimisableList, 0, 104, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(results)));
        Assert.assertEquals(expected, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        ArrayListIterate.forEachWithIndex(expected, 104, 0, objectIntProcedure);
        Assert.assertEquals(ListIterate.reverseThis(expected), reverseResults);
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEachWithIndex(expected, 104, -1, objectIntProcedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ArrayListIterate.forEachWithIndex(expected, -1, 104, objectIntProcedure);
            }
        });
    }

    @Test
    public void testReverseForEach()
    {
        ArrayList<Integer> integers = Interval.oneTo(5).addAllTo(new ArrayList<Integer>());
        MutableList<Integer> reverseResults = Lists.mutable.of();
        ArrayListIterate.reverseForEach(integers, CollectionAddProcedure.on(reverseResults));
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void testReverseForEach_emptyList()
    {
        ArrayList<Integer> integers = new ArrayList<Integer>();
        MutableList<Integer> results = Lists.mutable.of();
        ArrayListIterate.reverseForEach(integers, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
    }

    @Test
    public void testInjectInto()
    {
        ArrayList<Integer> list = this.newArrayList(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(1 + 1 + 2 + 3),
                ArrayListIterate.injectInto(1, list, AddFunction.INTEGER));
    }

    @Test
    public void testInjectIntoOver100()
    {
        ArrayList<Integer> list = this.oneHundredAndOneOnes();
        Assert.assertEquals(Integer.valueOf(102), ArrayListIterate.injectInto(1, list, AddFunction.INTEGER));
    }

    @Test
    public void testInjectIntoDoubleOver100()
    {
        ArrayList<Integer> list = this.oneHundredAndOneOnes();
        Assert.assertEquals(102.0, ArrayListIterate.injectInto(1.0d, list, AddFunction.INTEGER_TO_DOUBLE), 0.0001);
    }

    private ArrayList<Integer> oneHundredAndOneOnes()
    {
        return new ArrayList<Integer>(Collections.nCopies(101, 1));
    }

    @Test
    public void testInjectIntoIntegerOver100()
    {
        ArrayList<Integer> list = this.oneHundredAndOneOnes();
        Assert.assertEquals(102, ArrayListIterate.injectInto(1, list, AddFunction.INTEGER_TO_INT));
    }

    @Test
    public void testInjectIntoLongOver100()
    {
        ArrayList<Integer> list = this.oneHundredAndOneOnes();
        Assert.assertEquals(102, ArrayListIterate.injectInto(1L, list, AddFunction.INTEGER_TO_LONG));
    }

    @Test
    public void testInjectIntoDouble()
    {
        ArrayList<Double> list = new ArrayList<Double>();
        list.add(1.0);
        list.add(2.0);
        list.add(3.0);
        Assert.assertEquals(new Double(7.0),
                ArrayListIterate.injectInto(1.0, list, AddFunction.DOUBLE));
    }

    @Test
    public void testInjectIntoString()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        Assert.assertEquals("0123", ArrayListIterate.injectInto("0", list, AddFunction.STRING));
    }

    @Test
    public void testInjectIntoMaxString()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("12");
        list.add("123");
        Assert.assertEquals(Integer.valueOf(3), ArrayListIterate.injectInto(Integer.MIN_VALUE, list, MaxSizeFunction.STRING));
    }

    @Test
    public void testInjectIntoMinString()
    {
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("12");
        list.add("123");
        Assert.assertEquals(Integer.valueOf(1), ArrayListIterate.injectInto(Integer.MAX_VALUE, list, MinSizeFunction.STRING));
    }

    @Test
    public void testCollect()
    {
        ArrayList<Boolean> list = new ArrayList<Boolean>();
        list.add(Boolean.TRUE);
        list.add(Boolean.FALSE);
        list.add(Boolean.TRUE);
        list.add(Boolean.TRUE);
        list.add(Boolean.FALSE);
        list.add(null);
        list.add(null);
        list.add(Boolean.FALSE);
        list.add(Boolean.TRUE);
        list.add(null);
        ArrayList<String> newCollection = ArrayListIterate.collect(list, Functions.getToString());
        //List<String> newCollection = ArrayListIterate.collect(list, ArrayListIterateTest.TO_STRING_FUNCTION);
        Verify.assertSize(10, newCollection);
        Verify.assertContainsAll(newCollection, "null", "false", "true");
    }

    @Test
    public void testCollectOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<Class<?>> newCollection = ArrayListIterate.collect(list, Functions.getToClass());
        Verify.assertSize(101, newCollection);
        Verify.assertContains(Integer.class, newCollection);
    }

    private ArrayList<Integer> getIntegerList()
    {
        return new ArrayList<Integer>(Interval.toReverseList(1, 5));
    }

    private ArrayList<Integer> getOver100IntegerList()
    {
        return new ArrayList<Integer>(Interval.toReverseList(1, 105));
    }

    @Test
    public void testForEachWithIndex()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Iterate.sortThis(list);
        ArrayListIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(index, object - 1);
            }
        });
    }

    @Test
    public void testForEachWithIndexOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Iterate.sortThis(list);
        ArrayListIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(index, object - 1);
            }
        });
    }

    @Test
    public void testForEach()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Iterate.sortThis(list);
        MutableList<Integer> result = Lists.mutable.of();
        ArrayListIterate.forEach(list, CollectionAddProcedure.<Integer>on(result));
        Verify.assertListsEqual(list, result);
    }

    @Test
    public void testForEachOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Iterate.sortThis(list);
        FastList<Integer> result = FastList.newList(101);
        ArrayListIterate.forEach(list, CollectionAddProcedure.<Integer>on(result));
        Verify.assertListsEqual(list, result);
    }

    @Test
    public void testForEachWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Iterate.sortThis(list);
        MutableList<Integer> result = Lists.mutable.of();
        ArrayListIterate.forEachWith(list,
                Procedures2.fromProcedure(CollectionAddProcedure.<Integer>on(result)),
                null);
        Verify.assertListsEqual(list, result);
    }

    @Test
    public void testForEachWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Iterate.sortThis(list);
        MutableList<Integer> result = FastList.newList(101);
        ArrayListIterate.forEachWith(list,
                Procedures2.fromProcedure(CollectionAddProcedure.<Integer>on(result)),
                null);
        Verify.assertListsEqual(list, result);
    }

    @Test
    public void testForEachInBoth()
    {
        final MutableList<Pair<String, String>> list = Lists.mutable.of();
        ArrayList<String> list1 = new ArrayList<String>(mList("1", "2"));
        ArrayList<String> list2 = new ArrayList<String>(mList("a", "b"));
        ArrayListIterate.forEachInBoth(list1, list2,
                new Procedure2<String, String>()
                {
                    public void value(String argument1, String argument2)
                    {
                        list.add(Tuples.twin(argument1, argument2));
                    }
                });

        Assert.assertEquals(FastList.newListWith(Tuples.twin("1", "a"), Tuples.twin("2", "b")), list);
    }

    @Test
    public void testDetect()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertEquals(Integer.valueOf(1), ArrayListIterate.detect(list, Predicates.equal(1)));
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        ArrayList<Integer> list2 =
                this.newArrayList(1, new Integer(2), 2);  // test relies on having a unique instance of "2"
        Assert.assertSame(list2.get(1), ArrayListIterate.detect(list2, Predicates.equal(2)));
    }

    @Test
    public void testDetectOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertEquals(Integer.valueOf(1), ArrayListIterate.detect(list, Predicates.equal(1)));
    }

    @Test
    public void testDetectWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertEquals(Integer.valueOf(1), ArrayListIterate.detectWith(list, Predicates2.equal(), 1));
        //noinspection CachedNumberConstructorCall,UnnecessaryBoxing
        ArrayList<Integer> list2 =
                this.newArrayList(1, new Integer(2), 2);  // test relies on having a unique instance of "2"
        Assert.assertSame(list2.get(1), ArrayListIterate.detectWith(list2, Predicates2.equal(), 2));
    }

    @Test
    public void testDetectWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertEquals(Integer.valueOf(1), ArrayListIterate.detectWith(list, Predicates2.equal(), 1));
    }

    @Test
    public void testDetectIfNone()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertEquals(Integer.valueOf(7), ArrayListIterate.detectIfNone(list, Predicates.equal(6), 7));
        Assert.assertEquals(Integer.valueOf(2), ArrayListIterate.detectIfNone(list, Predicates.equal(2), 7));
    }

    @Test
    public void testDetectIfNoneOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertNull(ArrayListIterate.detectIfNone(list, Predicates.equal(102), null));
    }

    @Test
    public void testDetectWithIfNone()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertEquals(Integer.valueOf(7), ArrayListIterate.detectWithIfNone(list, Predicates2.equal(), 6, 7));
        Assert.assertEquals(Integer.valueOf(2), ArrayListIterate.detectWithIfNone(list, Predicates2.equal(), 2, 7));
    }

    @Test
    public void testDetectWithIfNoneOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertNull(ArrayListIterate.detectWithIfNone(list, Predicates2.equal(), 102, null));
    }

    @Test
    public void testSelect()
    {
        ArrayList<Integer> list = this.getIntegerList();
        ArrayList<Integer> results = ArrayListIterate.select(list, Predicates.instanceOf(Integer.class));
        Verify.assertSize(5, results);
    }

    @Test
    public void testSelectOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<Integer> results = ArrayListIterate.select(list, Predicates.instanceOf(Integer.class));
        Verify.assertSize(101, results);
    }

    @Test
    public void testSelectWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        ArrayList<Integer> results = ArrayListIterate.selectWith(list, Predicates2.instanceOf(), Integer.class);
        Verify.assertSize(5, results);
    }

    @Test
    public void testSelectWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<Integer> results = ArrayListIterate.selectWith(list, Predicates2.instanceOf(), Integer.class);
        Verify.assertSize(101, results);
    }

    @Test
    public void testReject()
    {
        ArrayList<Integer> list = this.getIntegerList();
        ArrayList<Integer> results = ArrayListIterate.reject(list, Predicates.instanceOf(Integer.class));
        Verify.assertEmpty(results);
    }

    @Test
    public void testRejectOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        List<Integer> results = ArrayListIterate.reject(list, Predicates.instanceOf(Integer.class));
        Verify.assertEmpty(results);
    }

    @Test
    public void testSelectInstancesOfOver100()
    {
        ArrayList<Number> list = new ArrayList<Number>(Interval.oneTo(101));
        list.add(102.0);
        MutableList<Double> results = ArrayListIterate.selectInstancesOf(list, Double.class);
        Assert.assertEquals(iList(102.0), results);
    }

    public static final class CollectionCreator
    {
        private final int data;

        private CollectionCreator(int data)
        {
            this.data = data;
        }

        public Collection<Integer> getCollection()
        {
            return FastList.newListWith(this.data, this.data);
        }
    }

    @Test
    public void flatCollect()
    {
        ArrayList<CollectionCreator> list = new ArrayList<CollectionCreator>();
        list.add(new CollectionCreator(1));
        list.add(new CollectionCreator(2));

        Function<CollectionCreator, Collection<Integer>> flatteningFunction = new Function<CollectionCreator, Collection<Integer>>()
        {
            public Collection<Integer> valueOf(CollectionCreator object)
            {
                return object.getCollection();
            }
        };
        List<Integer> results1 = ArrayListIterate.flatCollect(list, flatteningFunction);
        Verify.assertListsEqual(FastList.newListWith(1, 1, 2, 2), results1);

        MutableList<Integer> target1 = Lists.mutable.of();
        MutableList<Integer> results2 = ArrayListIterate.flatCollect(list, flatteningFunction, target1);
        Assert.assertSame(results2, target1);

        Verify.assertListsEqual(FastList.newListWith(1, 1, 2, 2), results2);
    }

    @Test
    public void testRejectWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        ArrayList<Integer> results = ArrayListIterate.rejectWith(list, Predicates2.instanceOf(), Integer.class);
        Verify.assertEmpty(results);
    }

    @Test
    public void testRejectWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<Integer> results = ArrayListIterate.rejectWith(list, Predicates2.instanceOf(), Integer.class);
        Verify.assertEmpty(results);
    }

    @Test
    public void testSelectAndRejectWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Twin<MutableList<Integer>> result =
                ArrayListIterate.selectAndRejectWith(list, Predicates2.in(), Lists.immutable.of(1));
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(4, result.getTwo());
    }

    @Test
    public void testSelectAndRejectWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Twin<MutableList<Integer>> result =
                ArrayListIterate.selectAndRejectWith(list, Predicates2.in(), Lists.immutable.of(1));
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(100, result.getTwo());
    }

    @Test
    public void testPartitionOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        PartitionMutableList<Integer> result =
                ArrayListIterate.partition(list, Predicates.in(Lists.immutable.of(1)));
        Verify.assertSize(1, result.getSelected());
        Verify.assertSize(100, result.getRejected());
    }

    @Test
    public void testAnySatisfyWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertTrue(ArrayListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(ArrayListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void testAnySatisfy()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertTrue(ArrayListIterate.anySatisfy(list, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayListIterate.anySatisfy(list, Predicates.instanceOf(Double.class)));
    }

    @Test
    public void testAnySatisfyWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertTrue(ArrayListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(ArrayListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void testAnySatisfyOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertTrue(ArrayListIterate.anySatisfy(list, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayListIterate.anySatisfy(list, Predicates.instanceOf(Double.class)));
    }

    @Test
    public void testAllSatisfyWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertTrue(ArrayListIterate.allSatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Predicate2<Integer, Integer> greaterThanPredicate = Predicates2.greaterThan();
        Assert.assertFalse(ArrayListIterate.allSatisfyWith(list, greaterThanPredicate, 2));
    }

    @Test
    public void testAllSatisfy()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertTrue(ArrayListIterate.allSatisfy(list, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayListIterate.allSatisfy(list, Predicates.greaterThan(2)));
    }

    @Test
    public void testAllSatisfyWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertTrue(ArrayListIterate.allSatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Predicate2<Integer, Integer> greaterThanPredicate = Predicates2.greaterThan();
        Assert.assertFalse(ArrayListIterate.allSatisfyWith(list, greaterThanPredicate, 2));
    }

    @Test
    public void testAllSatisfyOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertTrue(ArrayListIterate.allSatisfy(list, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayListIterate.allSatisfy(list, Predicates.greaterThan(2)));
    }

    @Test
    public void testCountWith()
    {
        ArrayList<Integer> list = this.getIntegerList();
        Assert.assertEquals(5, ArrayListIterate.countWith(list, Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, ArrayListIterate.countWith(list, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void testCountWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        Assert.assertEquals(101, ArrayListIterate.countWith(list, Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, ArrayListIterate.countWith(list, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void testCollectIfOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<Class<?>> result = ArrayListIterate.collectIf(list, Predicates.equal(101), Functions.getToClass());
        Assert.assertEquals(FastList.newListWith(Integer.class), result);
    }

    @Test
    public void testCollectWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.oneTo(101));
        ArrayList<String> result = ArrayListIterate.collectWith(list, new Function2<Integer, Integer, String>()
        {
            public String value(Integer argument1, Integer argument2)
            {
                return argument1.equals(argument2) ? "101" : null;
            }
        }, 101);
        Verify.assertSize(101, result);
        Verify.assertContainsAll(result, null, "101");
        Assert.assertEquals(100, Iterate.count(result, Predicates.isNull()));
    }

    @Test
    public void testDetectIndexOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 101));
        Assert.assertEquals(100, ArrayListIterate.detectIndex(list, Predicates.equal(1)));
        Assert.assertEquals(0, Iterate.detectIndex(list, Predicates.equal(101)));
        Assert.assertEquals(-1, Iterate.detectIndex(list, Predicates.equal(200)));
    }

    @Test
    public void testDetectIndexSmallList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 5));
        Assert.assertEquals(4, ArrayListIterate.detectIndex(list, Predicates.equal(1)));
        Assert.assertEquals(0, Iterate.detectIndex(list, Predicates.equal(5)));
        Assert.assertEquals(-1, Iterate.detectIndex(list, Predicates.equal(10)));
    }

    @Test
    public void testDetectIndexWithOver100()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 101));
        Assert.assertEquals(100, Iterate.detectIndexWith(list, Predicates2.equal(), 1));
        Assert.assertEquals(0, Iterate.detectIndexWith(list, Predicates2.equal(), 101));
        Assert.assertEquals(-1, Iterate.detectIndexWith(list, Predicates2.equal(), 200));
    }

    @Test
    public void testDetectIndexWithSmallList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 5));
        Assert.assertEquals(4, Iterate.detectIndexWith(list, Predicates2.equal(), 1));
        Assert.assertEquals(0, Iterate.detectIndexWith(list, Predicates2.equal(), 5));
        Assert.assertEquals(-1, Iterate.detectIndexWith(list, Predicates2.equal(), 10));
    }

    @Test
    public void testInjectIntoWithOver100()
    {
        Sum result = new IntegerSum(0);
        Integer parameter = 2;
        Function3<Sum, Integer, Integer, Sum> function = new Function3<Sum, Integer, Integer, Sum>()
        {
            public Sum value(Sum sum, Integer element, Integer withValue)
            {
                return sum.add((element.intValue() - element.intValue()) * withValue.intValue());
            }
        };
        Sum sumOfDoubledValues = ArrayListIterate.injectIntoWith(result, this.getOver100IntegerList(), function, parameter);
        Assert.assertEquals(0, sumOfDoubledValues.getValue().intValue());
    }

    @Test
    public void removeIf()
    {
        ArrayList<Integer> objects = this.newArrayList(1, 2, 3, null);
        ArrayListIterate.removeIf(objects, Predicates.isNull());
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);

        ArrayList<Integer> objects5 = this.newArrayList(null, 1, 2, 3);
        ArrayListIterate.removeIf(objects5, Predicates.isNull());
        Verify.assertSize(3, objects5);
        Verify.assertContainsAll(objects5, 1, 2, 3);

        ArrayList<Integer> objects4 = this.newArrayList(1, null, 2, 3);
        ArrayListIterate.removeIf(objects4, Predicates.isNull());
        Verify.assertSize(3, objects4);
        Verify.assertContainsAll(objects4, 1, 2, 3);

        ArrayList<Integer> objects3 = this.newArrayList(null, null, null, null);
        ArrayListIterate.removeIf(objects3, Predicates.isNull());
        Verify.assertEmpty(objects3);

        ArrayList<Integer> objects2 = this.newArrayList(null, 1, 2, 3, null);
        ArrayListIterate.removeIf(objects2, Predicates.isNull());
        Verify.assertSize(3, objects2);
        Verify.assertContainsAll(objects2, 1, 2, 3);

        ArrayList<Integer> objects1 = this.newArrayList(1, 2, 3);
        ArrayListIterate.removeIf(objects1, Predicates.isNull());
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);

        ThisIsNotAnArrayList<Integer> objects6 = this.newNotAnArrayList(1, 2, 3);
        ArrayListIterate.removeIf(objects6, Predicates.isNull());
        Verify.assertSize(3, objects6);
        Verify.assertContainsAll(objects6, 1, 2, 3);
    }

    @Test
    public void removeIfWith()
    {
        ArrayList<Integer> objects = this.newArrayList(1, 2, 3, null);
        ArrayListIterate.removeIfWith(objects, Predicates2.isNull(), null);
        Verify.assertSize(3, objects);
        Verify.assertContainsAll(objects, 1, 2, 3);

        ArrayList<Integer> objects5 = this.newArrayList(null, 1, 2, 3);
        ArrayListIterate.removeIfWith(objects5, Predicates2.isNull(), null);
        Verify.assertSize(3, objects5);
        Verify.assertContainsAll(objects5, 1, 2, 3);

        ArrayList<Integer> objects4 = this.newArrayList(1, null, 2, 3);
        ArrayListIterate.removeIfWith(objects4, Predicates2.isNull(), null);
        Verify.assertSize(3, objects4);
        Verify.assertContainsAll(objects4, 1, 2, 3);

        ArrayList<Integer> objects3 = this.newArrayList(null, null, null, null);
        ArrayListIterate.removeIfWith(objects3, Predicates2.isNull(), null);
        Verify.assertEmpty(objects3);

        ArrayList<Integer> objects2 = this.newArrayList(null, 1, 2, 3, null);
        ArrayListIterate.removeIfWith(objects2, Predicates2.isNull(), null);
        Verify.assertSize(3, objects2);
        Verify.assertContainsAll(objects2, 1, 2, 3);

        ArrayList<Integer> objects1 = this.newArrayList(1, 2, 3);
        ArrayListIterate.removeIfWith(objects1, Predicates2.isNull(), null);
        Verify.assertSize(3, objects1);
        Verify.assertContainsAll(objects1, 1, 2, 3);

        ThisIsNotAnArrayList<Integer> objects6 = this.newNotAnArrayList(1, 2, 3);
        ArrayListIterate.removeIfWith(objects6, Predicates2.isNull(), null);
        Verify.assertSize(3, objects6);
        Verify.assertContainsAll(objects6, 1, 2, 3);
    }

    @Test
    public void testTake()
    {
        ArrayList<Integer> list = this.getIntegerList();

        Assert.assertEquals(FastList.newListWith(5, 4), ArrayListIterate.take(list, 2));

        Verify.assertSize(0, ArrayListIterate.take(list, 0));
        Verify.assertSize(5, ArrayListIterate.take(list, 5));

        Verify.assertSize(0, ArrayListIterate.take(new ArrayList<Integer>(), 2));

        ArrayList<Integer> list1 = new ArrayList<Integer>(130);
        list1.addAll(Interval.oneTo(120));
        Verify.assertListsEqual(Interval.oneTo(120), ArrayListIterate.take(list1, 125));
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_throws()
    {
        ArrayListIterate.take(this.getIntegerList(), -1);
    }

    @Test
    public void testDrop()
    {
        ArrayList<Integer> list = this.getIntegerList();
        ArrayList<Integer> results = ArrayListIterate.drop(list, 2);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), results);

        Verify.assertSize(0, ArrayListIterate.drop(list, 5));
        Verify.assertSize(0, ArrayListIterate.drop(list, 6));
        Verify.assertSize(5, ArrayListIterate.drop(list, 0));

        Verify.assertSize(0, ArrayListIterate.drop(new ArrayList<Integer>(), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_throws()
    {
        ArrayListIterate.drop(this.getIntegerList(), -1);
    }

    private ArrayList<Integer> newArrayList(Integer... items)
    {
        return new ArrayList<Integer>(mList(items));
    }

    private ThisIsNotAnArrayList<Integer> newNotAnArrayList(Integer... items)
    {
        return new ThisIsNotAnArrayList<Integer>(mList(items));
    }

    @Test
    public void testGroupByWithOptimisedList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 105));
        MutableMultimap<String, Integer> target = new FastListMultimap<String, Integer>();
        MutableMultimap<String, Integer> result = ArrayListIterate.groupBy(list, Functions.getToString(), target);
        Assert.assertEquals(result.get("105"), FastList.newListWith(105));
    }

    @Test
    public void testGroupByEachWithOptimisedList()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 105));
        Function<Integer, Iterable<String>> function = new Function<Integer, Iterable<String>>()
        {
            public Iterable<String> valueOf(Integer object)
            {
                return FastList.newListWith(object.toString(), object.toString() + '*');
            }
        };
        MutableMultimap<String, Integer> target = new FastListMultimap<String, Integer>();
        MutableMultimap<String, Integer> result = ArrayListIterate.groupByEach(list, function, target);
        Assert.assertEquals(result.get("105"), FastList.newListWith(105));
        Assert.assertEquals(result.get("105*"), FastList.newListWith(105));
    }

    @Test
    public void testFlattenWithOptimisedArrays()
    {
        ArrayList<Integer> list = new ArrayList<Integer>(Interval.toReverseList(1, 105));

        ArrayList<Integer> result = ArrayListIterate.flatCollect(list, new CollectionWrappingFunction<Integer>(),
                new ArrayList<Integer>());
        Assert.assertEquals(105, result.get(0).intValue());
    }

    private static class CollectionWrappingFunction<T> implements Function<T, Collection<T>>
    {
        private static final long serialVersionUID = 1L;

        public Collection<T> valueOf(T value)
        {
            return FastList.newListWith(value);
        }
    }
}
