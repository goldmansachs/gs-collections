/*
 * Copyright 2011 Goldman Sachs & Co.
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
import java.util.LinkedList;
import java.util.List;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class ListIterateTest
{
    private static final Function<Number, Integer> INT_VALUE = new Function<Number, Integer>()
    {
        public Integer valueOf(Number number)
        {
            return number.intValue();
        }
    };

    @Test
    public void injectInto()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        List<Integer> linked = new LinkedList<Integer>(list);
        Assert.assertEquals(Integer.valueOf(7), ListIterate.injectInto(1, list, AddFunction.INTEGER));
        Assert.assertEquals(Integer.valueOf(7), ListIterate.injectInto(1, linked, AddFunction.INTEGER));
    }

    @Test
    public void injectIntoInt()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        List<Integer> linked = new LinkedList<Integer>(list);
        Assert.assertEquals(7, ListIterate.injectInto(1, list, AddFunction.INTEGER_TO_INT));
        Assert.assertEquals(7, ListIterate.injectInto(1, linked, AddFunction.INTEGER_TO_INT));
    }

    @Test
    public void injectIntoLong()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        List<Integer> linked = new LinkedList<Integer>(list);
        Assert.assertEquals(7, ListIterate.injectInto(1, list, AddFunction.INTEGER_TO_LONG));
        Assert.assertEquals(7, ListIterate.injectInto(1, linked, AddFunction.INTEGER_TO_LONG));
    }

    @Test
    public void injectIntoDouble()
    {
        MutableList<Double> list = Lists.fixedSize.of(1.0d, 2.0d, 3.0d);
        List<Double> linked = new LinkedList<Double>(list);
        Assert.assertEquals(7.0d, ListIterate.injectInto(1.0, list, AddFunction.DOUBLE), 0.001);
        Assert.assertEquals(7.0d, ListIterate.injectInto(1.0, linked, AddFunction.DOUBLE), 0.001);
    }

    @Test
    public void injectIntoString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "2", "3");
        List<String> linked = new LinkedList<String>(list);
        Assert.assertEquals("0123", ListIterate.injectInto("0", list, AddFunction.STRING));
        Assert.assertEquals("0123", ListIterate.injectInto("0", linked, AddFunction.STRING));
    }

    @Test
    public void injectIntoMaxString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "12", "123");
        List<String> linked = new LinkedList<String>(list);
        Function2<Integer, String, Integer> function = MaxSizeFunction.STRING;
        Assert.assertEquals(Integer.valueOf(3), ListIterate.injectInto(Integer.MIN_VALUE, list, function));
        Assert.assertEquals(Integer.valueOf(3), ListIterate.injectInto(Integer.MIN_VALUE, linked, function));
    }

    @Test
    public void injectIntoMinString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "12", "123");
        List<String> linked = new LinkedList<String>(list);
        Function2<Integer, String, Integer> function = MinSizeFunction.STRING;
        Assert.assertEquals(Integer.valueOf(1), ListIterate.injectInto(Integer.MAX_VALUE, list, function));
        Assert.assertEquals(Integer.valueOf(1), ListIterate.injectInto(Integer.MAX_VALUE, linked, function));
    }

    @Test
    public void collect()
    {
        MutableList<Boolean> list = Lists.fixedSize.of(true, false, null);
        List<Boolean> linked = new LinkedList<Boolean>(list);
        this.assertCollect(list);
        this.assertCollect(linked);
    }

    private void assertCollect(List<Boolean> list)
    {
        MutableList<String> newCollection = ListIterate.collect(list, Functions.getToString());
        Verify.assertListsEqual(newCollection, FastList.newListWith("true", "false", "null"));

        List<String> newCollection2 = ListIterate.collect(list, Functions.getToString(), new ArrayList<String>());
        Verify.assertListsEqual(newCollection2, FastList.newListWith("true", "false", "null"));
    }

    @Test
    public void flatten()
    {
        MutableList<MutableList<Boolean>> list = Lists.fixedSize.<MutableList<Boolean>>of(
                Lists.fixedSize.of(true, false),
                Lists.fixedSize.of(true, null));
        List<MutableList<Boolean>> linked = new LinkedList<MutableList<Boolean>>(list);
        this.assertFlatten(list);
        this.assertFlatten(linked);
    }

    private void assertFlatten(List<MutableList<Boolean>> list)
    {
        MutableList<Boolean> newList = ListIterate.flatCollect(list, new Function<MutableList<Boolean>, MutableList<Boolean>>()
        {
            public MutableList<Boolean> valueOf(MutableList<Boolean> list)
            {
                return list.toList();
            }
        });
        Verify.assertListsEqual(
                FastList.newListWith(true, false, true, null),
                newList);

        MutableSet<Boolean> newSet = ListIterate.flatCollect(list, new Function<MutableList<Boolean>, MutableSet<Boolean>>()
        {
            public MutableSet<Boolean> valueOf(MutableList<Boolean> list)
            {
                return list.toSet();
            }
        }, UnifiedSet.<Boolean>newSet());
        Verify.assertSetsEqual(
                UnifiedSet.newSetWith(true, false, null),
                newSet);
    }

    @Test
    public void getFirstAndLast()
    {
        MutableList<Boolean> list = Lists.fixedSize.of(true, null, false);
        Assert.assertEquals(Boolean.TRUE, ListIterate.getFirst(list));
        Assert.assertEquals(Boolean.FALSE, ListIterate.getLast(list));
        List<Boolean> linked = new LinkedList<Boolean>(list);
        Assert.assertEquals(Boolean.TRUE, ListIterate.getFirst(linked));
        Assert.assertEquals(Boolean.FALSE, ListIterate.getLast(linked));
    }

    @Test
    public void getFirstAndLastOnEmpty()
    {
        List<?> list = new ArrayList();
        Assert.assertNull(ListIterate.getFirst(list));
        Assert.assertNull(ListIterate.getLast(list));

        List<?> linked = new LinkedList();
        Assert.assertNull(ListIterate.getFirst(linked));
        Assert.assertNull(ListIterate.getLast(linked));
    }

    @Test
    public void occurrencesOfAttributeNamedOnList()
    {
        MutableList<Integer> list = this.getIntegerList();
        this.assertOccurrencesOfAttributeNamedOnList(list);
        this.assertOccurrencesOfAttributeNamedOnList(new LinkedList<Integer>(list));
    }

    private void assertOccurrencesOfAttributeNamedOnList(List<Integer> list)
    {
        int result = ListIterate.count(list, Predicates.attributeEqual(INT_VALUE, 3));
        Assert.assertEquals(1, result);
        int result2 = ListIterate.count(list, Predicates.attributeEqual(INT_VALUE, 6));
        Assert.assertEquals(0, result2);
    }

    private MutableList<Integer> getIntegerList()
    {
        return Interval.toReverseList(1, 5);
    }

    @Test
    public void forEachWithIndex()
    {
        MutableList<Integer> list = this.getIntegerList();
        this.assertForEachWithIndex(list);
        this.assertForEachWithIndex(new LinkedList<Integer>(list));
    }

    private void assertForEachWithIndex(List<Integer> list)
    {
        Iterate.sortThis(list);
        ListIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer object, int index)
            {
                Assert.assertEquals(index, object - 1);
            }
        });
    }

    @Test
    public void forEachUsingFromTo()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();

        this.assertForEachUsingFromTo(integers);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);
        this.assertReverseForEachUsingFromTo(integers, reverseResults, procedure);
        this.assertForEachUsingFromTo(new LinkedList<Integer>(integers));
    }

    private void assertForEachUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        ListIterate.forEach(integers, 0, 4, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);

        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(integers, 4, -1, procedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEach(integers, -1, 4, procedure);
            }
        });
    }

    private void assertReverseForEachUsingFromTo(List<Integer> integers, MutableList<Integer> reverseResults, CollectionAddProcedure<Integer> procedure)
    {
        ListIterate.forEach(integers, 4, 0, procedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void forEachWithIndexUsingFromTo()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        this.assertForEachWithIndexUsingFromTo(integers);
        this.assertForEachWithIndexUsingFromTo(new LinkedList<Integer>(integers));
        MutableList<Integer> reverseResults = Lists.mutable.of();
        ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        this.assertReverseForEachIndexUsingFromTo(integers, reverseResults, objectIntProcedure);
    }

    private void assertForEachWithIndexUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        ListIterate.forEachWithIndex(integers, 0, 4, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(results)));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEachWithIndex(integers, 4, -1, objectIntProcedure);
            }
        });
        Verify.assertThrows(IndexOutOfBoundsException.class, new Runnable()
        {
            public void run()
            {
                ListIterate.forEachWithIndex(integers, -1, 4, objectIntProcedure);
            }
        });
    }

    private void assertReverseForEachIndexUsingFromTo(List<Integer> integers, MutableList<Integer> reverseResults, ObjectIntProcedure<Integer> objectIntProcedure)
    {
        ListIterate.forEachWithIndex(integers, 4, 0, objectIntProcedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void reverseForEach()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        MutableList<Integer> reverseResults = Lists.mutable.of();
        ListIterate.reverseForEach(integers, CollectionAddProcedure.on(reverseResults));
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void reverseForEach_emptyList()
    {
        MutableList<Integer> integers = Lists.mutable.of();
        MutableList<Integer> results = Lists.mutable.of();
        ListIterate.reverseForEach(integers, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
    }

    @Test
    public void forEachInBoth()
    {
        MutableList<String> list1 = Lists.fixedSize.of("1", "2");
        MutableList<String> list2 = Lists.fixedSize.of("a", "b");
        this.assertForEachInBoth(list1, list2);
        this.assertForEachInBoth(list1, new LinkedList<String>(list2));
        this.assertForEachInBoth(new LinkedList<String>(list1), list2);
        this.assertForEachInBoth(new LinkedList<String>(list1), new LinkedList<String>(list2));
    }

    private void assertForEachInBoth(List<String> list1, List<String> list2)
    {
        final List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        ListIterate.forEachInBoth(list1, list2,
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
    public void detectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        this.assertDetectWith(list);
        this.assertDetectWith(new LinkedList<Integer>(list));
    }

    private void assertDetectWith(List<Integer> list)
    {
        Assert.assertEquals(Integer.valueOf(1), ListIterate.detectWith(list, Predicates2.equal(), 1));
        MutableList<Integer> list2 = Lists.fixedSize.of(1, 2, 2);
        Assert.assertSame(list2.get(1), ListIterate.detectWith(list2, Predicates2.equal(), 2));
    }

    @Test
    public void detectIfNone()
    {
        MutableList<Integer> list = this.getIntegerList();
        Assert.assertNull(ListIterate.detectIfNone(list, Predicates.equal(6), null));
        Assert.assertNull(ListIterate.detectIfNone(new LinkedList<Integer>(list), Predicates.equal(6), null));
    }

    @Test
    public void detectWithIfNone()
    {
        MutableList<Integer> list = this.getIntegerList();
        Assert.assertNull(ListIterate.detectWithIfNone(list, Predicates2.equal(), 6, null));
        Assert.assertNull(ListIterate.detectWithIfNone(new LinkedList<Integer>(list), Predicates2.equal(), 6, null));
    }

    @Test
    public void selectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Verify.assertSize(5, ListIterate.selectWith(list, Predicates2.instanceOf(), Integer.class));
        Verify.assertSize(
                5,
                ListIterate.selectWith(new LinkedList<Integer>(list), Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void rejectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Verify.assertEmpty(ListIterate.rejectWith(list, Predicates2.instanceOf(), Integer.class));
        Verify.assertEmpty(ListIterate.rejectWith(new LinkedList<Integer>(list), Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void selectAndRejectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        this.assertSelectAndRejectWith(list);
        this.assertSelectAndRejectWith(new LinkedList<Integer>(list));
    }

    private void assertSelectAndRejectWith(List<Integer> list)
    {
        Twin<MutableList<Integer>> result =
                ListIterate.selectAndRejectWith(list, Predicates2.in(), Lists.fixedSize.of(1));
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(4, result.getTwo());
    }

    @Test
    public void anySatisfyWith()
    {
        MutableList<Integer> undertest = this.getIntegerList();
        this.assertAnySatisyWith(undertest);
        this.assertAnySatisyWith(new LinkedList<Integer>(undertest));
    }

    private void assertAnySatisyWith(List<Integer> undertest)
    {
        Assert.assertTrue(ListIterate.anySatisfyWith(undertest, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(ListIterate.anySatisfyWith(undertest, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void allSatisfyWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        this.assertAllSatisfyWith(list);
        this.assertAllSatisfyWith(new LinkedList<Integer>(list));
    }

    private void assertAllSatisfyWith(List<Integer> list)
    {
        Assert.assertTrue(ListIterate.allSatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Predicate2<Integer, Integer> greaterThanPredicate = Predicates2.<Integer>greaterThan();
        Assert.assertFalse(ListIterate.allSatisfyWith(list, greaterThanPredicate, 2));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(5, ListIterate.countWith(this.getIntegerList(), Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(5, ListIterate.countWith(
                new LinkedList<Integer>(this.getIntegerList()),
                Predicates2.instanceOf(),
                Integer.class));
    }

    @Test
    public void collectIf()
    {
        MutableList<Integer> integers = Lists.fixedSize.of(1, 2, 3);
        this.assertCollectIf(integers);
        this.assertCollectIf(new LinkedList<Integer>(integers));
    }

    private void assertCollectIf(List<Integer> integers)
    {
        Verify.assertContainsAll(ListIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString()), "1", "2", "3");
        Verify.assertContainsAll(ListIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString(), new ArrayList<String>()), "1", "2", "3");
    }

    @Test
    public void reverseThis()
    {
        Assert.assertEquals(FastList.newListWith(2, 3, 1), ListIterate.reverseThis(Lists.fixedSize.of(1, 3, 2)));
        Assert.assertEquals(FastList.newListWith(2, 3, 1), ListIterate.reverseThis(new LinkedList<Integer>(Lists.fixedSize.of(1, 3, 2))));
    }

    @Test
    public void take()
    {
        MutableList<Integer> integers = this.getIntegerList();
        this.assertTake(integers);
        this.assertTake(new LinkedList<Integer>(integers));

        integers = Lists.fixedSize.of();
        Verify.assertSize(0, ListIterate.take(integers, 2));
        Verify.assertSize(0, ListIterate.take(new LinkedList<Object>(), 2));
    }

    private void assertTake(List<Integer> integers)
    {
        Collection<Integer> results = ListIterate.take(integers, 2);
        Assert.assertEquals(FastList.newListWith(5, 4), results);

        Verify.assertSize(0, ListIterate.take(integers, 0));
        Verify.assertSize(5, ListIterate.take(integers, 5));
        Verify.assertSize(5, ListIterate.take(integers, 10));

        MutableList<Integer> integers2 = Lists.fixedSize.of();
        Verify.assertSize(0, ListIterate.take(integers2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_throws()
    {
        ListIterate.take(this.getIntegerList(), -1);
    }

    @Test
    public void drop()
    {
        MutableList<Integer> integers = this.getIntegerList();
        this.assertDrop(integers);

        Verify.assertSize(0, ListIterate.drop(Lists.fixedSize.<Integer>of(), 2));
        Verify.assertSize(0, ListIterate.drop(new LinkedList<Integer>(), 2));
    }

    private void assertDrop(MutableList<Integer> integers)
    {
        MutableList<Integer> results = ListIterate.drop(integers, 2);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), results);

        Verify.assertSize(0, ListIterate.drop(integers, 5));
        Verify.assertSize(0, ListIterate.drop(integers, 6));
        Verify.assertSize(5, ListIterate.drop(integers, 0));

        MutableList<Integer> integers2 = Lists.fixedSize.of();
        Verify.assertSize(0, ListIterate.drop(integers2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_throws()
    {
        ListIterate.drop(this.getIntegerList(), -1);
    }

    @Test
    public void chunk()
    {
        MutableList<String> list = FastList.newListWith("1", "2", "3", "4", "5", "6", "7");
        RichIterable<RichIterable<String>> groups = ListIterate.chunk(list, 2);
        RichIterable<Integer> sizes = groups.collect(new Function<RichIterable<String>, Integer>()
        {
            public Integer valueOf(RichIterable<String> richIterable)
            {
                return richIterable.size();
            }
        });
        Assert.assertEquals(FastList.newListWith(2, 2, 2, 1), sizes);
    }

    @Test
    public void removeIf()
    {
        MutableList<Integer> list1 = FastList.newListWith(1, 2, 3, 4, 5);
        MutableList<Integer> resultList1 = Lists.mutable.of();
        List<Integer> list2 = new LinkedList<Integer>(list1);
        MutableList<Integer> resultList2 = Lists.mutable.of();

        ListIterate.removeIf(list1, IntegerPredicates.isEven(), CollectionAddProcedure.on(resultList1));
        Assert.assertEquals(FastList.newListWith(1, 3, 5), list1);
        Assert.assertEquals(FastList.newListWith(2, 4), resultList1);

        ListIterate.removeIf(list2, IntegerPredicates.isEven(), CollectionAddProcedure.on(resultList2));
        Assert.assertEquals(FastList.newListWith(1, 3, 5), list2);
        Assert.assertEquals(FastList.newListWith(2, 4), resultList2);
    }
}
