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

package com.gs.collections.impl.utility.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.DoNothingProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class RandomAccessListIterateTest
{
    private static final Function<Number, Integer> INT_VALUE = new Function<Number, Integer>()
    {
        public Integer valueOf(Number number)
        {
            return number.intValue();
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void forEachWithNegativeFroms()
    {
        RandomAccessListIterate.forEach(FastList.<Object>newList(), -1, 1, DoNothingProcedure.DO_NOTHING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachWithNegativeTos()
    {
        RandomAccessListIterate.forEach(FastList.<Object>newList(), 1, -1, DoNothingProcedure.DO_NOTHING);
    }

    @Test
    public void forEachInBothWithNull()
    {
        RandomAccessListIterate.forEachInBoth(null, FastList.newListWith(1, 2, 3), new FailProcedure2());
        RandomAccessListIterate.forEachInBoth(FastList.newListWith(1, 2, 3), null, new FailProcedure2());
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachInBothThrowsOnMisMatchedLists()
    {
        RandomAccessListIterate.forEachInBoth(FastList.<Object>newListWith("1", 2), FastList.<Object>newListWith(1, 2, 3),
                Procedures2.fromProcedure(DoNothingProcedure.DO_NOTHING));
    }

    @Test
    public void removeIf()
    {
        List<Integer> result = RandomAccessListIterate.removeIf(FastList.newListWith(1, 2, 3), Predicates.greaterThan(1));
        Verify.assertSize(1, result);
    }

    @Test
    public void dropWithLargeCount()
    {
        Verify.assertEmpty(RandomAccessListIterate.drop(FastList.newListWith(1, 2, 3), 5));
    }

    @Test
    public void injectInto()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        Assert.assertEquals(Integer.valueOf(7), RandomAccessListIterate.injectInto(1, list, AddFunction.INTEGER));
    }

    @Test
    public void injectIntoInt()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        Assert.assertEquals(7, RandomAccessListIterate.injectInto(1, list, AddFunction.INTEGER_TO_INT));
    }

    @Test
    public void injectIntoLong()
    {
        MutableList<Integer> list = Lists.fixedSize.of(1, 2, 3);
        Assert.assertEquals(7, RandomAccessListIterate.injectInto(1, list, AddFunction.INTEGER_TO_LONG));
    }

    @Test
    public void injectIntoDouble()
    {
        MutableList<Double> list = Lists.fixedSize.of(1.0, 2.0, 3.0);
        Assert.assertEquals(7.0d, RandomAccessListIterate.injectInto(1.0, list, AddFunction.DOUBLE), 0.001);
    }

    @Test
    public void injectIntoString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "2", "3");
        Assert.assertEquals("0123", RandomAccessListIterate.injectInto("0", list, AddFunction.STRING));
    }

    @Test
    public void injectIntoMaxString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "12", "123");
        Function2<Integer, String, Integer> function = MaxSizeFunction.STRING;
        Assert.assertEquals(Integer.valueOf(3), RandomAccessListIterate.injectInto(Integer.MIN_VALUE, list, function));
    }

    @Test
    public void injectIntoMinString()
    {
        MutableList<String> list = Lists.fixedSize.of("1", "12", "123");
        Function2<Integer, String, Integer> function = MinSizeFunction.STRING;
        Assert.assertEquals(Integer.valueOf(1), RandomAccessListIterate.injectInto(Integer.MAX_VALUE, list, function));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals(
                iList("true", "false", "null"),
                RandomAccessListIterate.collect(mList(true, false, null), Functions.getToString()));
    }

    @Test
    public void collectReflective()
    {
        Assert.assertEquals(
                iList("true", "false", "null"),
                RandomAccessListIterate.collect(mList(true, false, null), Functions.getToString()));

        Assert.assertEquals(
                iList("true", "false", "null"),
                RandomAccessListIterate.collect(mList(true, false, null), Functions.getToString(), new ArrayList<String>()));
    }

    @Test
    public void flattenReflective()
    {
        MutableList<MutableList<Boolean>> list = Lists.fixedSize.<MutableList<Boolean>>of(
                Lists.fixedSize.of(true, false),
                Lists.fixedSize.of(true, null));
        MutableList<Boolean> newList = RandomAccessListIterate.flatCollect(list, new Function<MutableList<Boolean>, MutableList<Boolean>>()
        {
            public MutableList<Boolean> valueOf(MutableList<Boolean> mutableList)
            {
                return mutableList.toList();
            }
        });
        Verify.assertListsEqual(
                FastList.newListWith(true, false, true, null),
                newList);

        MutableSet<Boolean> newSet = RandomAccessListIterate.flatCollect(list, new Function<MutableList<Boolean>, MutableSet<Boolean>>()
        {
            public MutableSet<Boolean> valueOf(MutableList<Boolean> mutableList)
            {
                return mutableList.toSet();
            }
        }, UnifiedSet.<Boolean>newSet());
        Verify.assertSetsEqual(
                UnifiedSet.newSetWith(true, false, null),
                newSet);
    }

    @Test
    public void getLast()
    {
        MutableList<Boolean> list = Lists.fixedSize.of(true, null, false);
        Assert.assertEquals(Boolean.FALSE, RandomAccessListIterate.getLast(list));
    }

    @Test
    public void getLastOnEmpty()
    {
        List<?> list = new ArrayList<Object>();
        Assert.assertNull(RandomAccessListIterate.getLast(list));
    }

    @Test
    public void count()
    {
        MutableList<Integer> list = this.getIntegerList();
        int result = RandomAccessListIterate.count(list, Predicates.attributeEqual(INT_VALUE, 3));
        Assert.assertEquals(1, result);
        int result2 = RandomAccessListIterate.count(list, Predicates.attributeEqual(INT_VALUE, 6));
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
        Iterate.sortThis(list);
        RandomAccessListIterate.forEachWithIndex(list, new ObjectIntProcedure<Integer>()
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
    }

    private void assertForEachUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        RandomAccessListIterate.forEach(integers, 0, 4, CollectionAddProcedure.on(results));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final CollectionAddProcedure<Integer> procedure = CollectionAddProcedure.on(reverseResults);

        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                RandomAccessListIterate.forEach(integers, 4, -1, procedure);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                RandomAccessListIterate.forEach(integers, -1, 4, procedure);
            }
        });
    }

    private void assertReverseForEachUsingFromTo(List<Integer> integers, MutableList<Integer> reverseResults, CollectionAddProcedure<Integer> procedure)
    {
        RandomAccessListIterate.forEach(integers, 4, 0, procedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void forEachWithIndexUsingFromTo()
    {
        MutableList<Integer> integers = Interval.oneTo(5).toList();
        this.assertForEachWithIndexUsingFromTo(integers);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        this.assertReverseForEachIndexUsingFromTo(integers, reverseResults, objectIntProcedure);
    }

    private void assertForEachWithIndexUsingFromTo(final List<Integer> integers)
    {
        MutableList<Integer> results = Lists.mutable.of();
        RandomAccessListIterate.forEachWithIndex(integers, 0, 4, ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(results)));
        Assert.assertEquals(integers, results);
        MutableList<Integer> reverseResults = Lists.mutable.of();
        final ObjectIntProcedure<Integer> objectIntProcedure = ObjectIntProcedures.fromProcedure(CollectionAddProcedure.on(reverseResults));
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                RandomAccessListIterate.forEachWithIndex(integers, 4, -1, objectIntProcedure);
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                RandomAccessListIterate.forEachWithIndex(integers, -1, 4, objectIntProcedure);
            }
        });
    }

    private void assertReverseForEachIndexUsingFromTo(MutableList<Integer> integers, MutableList<Integer> reverseResults, ObjectIntProcedure<Integer> objectIntProcedure)
    {
        RandomAccessListIterate.forEachWithIndex(integers, 4, 0, objectIntProcedure);
        Assert.assertEquals(ListIterate.reverseThis(integers), reverseResults);
    }

    @Test
    public void forEachInBoth()
    {
        MutableList<String> list1 = Lists.fixedSize.of("1", "2");
        MutableList<String> list2 = Lists.fixedSize.of("a", "b");
        final List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        RandomAccessListIterate.forEachInBoth(list1, list2,
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
        Assert.assertEquals(Integer.valueOf(1), RandomAccessListIterate.detectWith(list, Predicates2.equal(), 1));
        MutableList<Integer> list2 = Lists.fixedSize.of(1, 2, 2);
        Assert.assertSame(list2.get(1), RandomAccessListIterate.detectWith(list2, Predicates2.equal(), 2));
    }

    @Test
    public void selectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Verify.assertSize(5, RandomAccessListIterate.selectWith(list, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void rejectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Verify.assertEmpty(RandomAccessListIterate.rejectWith(list, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void distinct()
    {
        MutableList<Integer> list = FastList.newListWith(5, 2, 6, 2, 3, 5, 2);
        MutableList<Integer> actualList = FastList.newList();
        RandomAccessListIterate.distinct(list, actualList);
        Verify.assertListsEqual(FastList.newListWith(5, 2, 6, 3), actualList);
        Verify.assertSize(7, list);
    }

    @Test
    public void selectAndRejectWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Twin<MutableList<Integer>> result = RandomAccessListIterate.selectAndRejectWith(list, Predicates2.in(), Lists.fixedSize.of(1));
        Verify.assertSize(1, result.getOne());
        Verify.assertSize(4, result.getTwo());
    }

    @Test
    public void anySatisfyWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Assert.assertTrue(RandomAccessListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Assert.assertFalse(RandomAccessListIterate.anySatisfyWith(list, Predicates2.instanceOf(), Double.class));
    }

    @Test
    public void allSatisfyWith()
    {
        MutableList<Integer> list = this.getIntegerList();
        Assert.assertTrue(RandomAccessListIterate.allSatisfyWith(list, Predicates2.instanceOf(), Integer.class));
        Predicate2<Integer, Integer> greaterThanPredicate = Predicates2.greaterThan();
        Assert.assertFalse(RandomAccessListIterate.allSatisfyWith(list, greaterThanPredicate, 2));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(5, RandomAccessListIterate.countWith(this.getIntegerList(), Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void collectIf()
    {
        MutableList<Integer> integers = Lists.fixedSize.of(1, 2, 3);
        Verify.assertContainsAll(RandomAccessListIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString()), "1", "2", "3");
        Verify.assertContainsAll(RandomAccessListIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString(), new ArrayList<String>()), "1", "2", "3");
    }

    @Test
    public void take()
    {
        MutableList<Integer> integers = this.getIntegerList();
        Collection<Integer> results = RandomAccessListIterate.take(integers, 2);
        Assert.assertEquals(FastList.newListWith(5, 4), results);

        Verify.assertSize(0, RandomAccessListIterate.take(integers, 0));
        Verify.assertSize(5, RandomAccessListIterate.take(integers, 5));
        Verify.assertSize(5, RandomAccessListIterate.take(integers, 10));

        MutableList<Integer> integers2 = Lists.fixedSize.of();
        Verify.assertSize(0, RandomAccessListIterate.take(integers2, 2));

        Verify.assertSize(0, RandomAccessListIterate.take(Lists.fixedSize.of(), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_throws()
    {
        RandomAccessListIterate.take(this.getIntegerList(), -1);
    }

    @Test
    public void drop()
    {
        MutableList<Integer> integers = this.getIntegerList();
        MutableList<Integer> results = RandomAccessListIterate.drop(integers, 2);
        Assert.assertEquals(FastList.newListWith(3, 2, 1), results);

        Verify.assertSize(0, RandomAccessListIterate.drop(integers, 5));
        Verify.assertSize(0, RandomAccessListIterate.drop(integers, 6));
        Verify.assertSize(5, RandomAccessListIterate.drop(integers, 0));

        MutableList<Integer> integers2 = Lists.fixedSize.of();
        Verify.assertSize(0, RandomAccessListIterate.drop(integers2, 2));

        Verify.assertSize(0, RandomAccessListIterate.drop(Lists.fixedSize.<Integer>of(), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_throws()
    {
        RandomAccessListIterate.drop(this.getIntegerList(), -1);
    }

    private static class FailProcedure2 implements Procedure2<Object, Integer>
    {
        private static final long serialVersionUID = 1L;

        public void value(Object argument1, Integer argument2)
        {
            Assert.fail();
        }
    }
}
