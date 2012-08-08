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

package com.webguys.ponzu.impl.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.webguys.ponzu.api.RichIterable;
import com.webguys.ponzu.api.block.function.Function;
import com.webguys.ponzu.api.block.predicate.Predicate;
import com.webguys.ponzu.api.block.procedure.ObjectIntProcedure;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.multimap.Multimap;
import com.webguys.ponzu.api.multimap.MutableMultimap;
import com.webguys.ponzu.api.tuple.Pair;
import com.webguys.ponzu.api.tuple.Twin;
import com.webguys.ponzu.impl.block.factory.Functions;
import com.webguys.ponzu.impl.block.factory.Functions2;
import com.webguys.ponzu.impl.block.factory.IntegerPredicates;
import com.webguys.ponzu.impl.block.factory.Predicates;
import com.webguys.ponzu.impl.block.factory.Predicates2;
import com.webguys.ponzu.impl.block.function.AddFunction;
import com.webguys.ponzu.impl.block.function.MaxSizeFunction;
import com.webguys.ponzu.impl.block.function.MinSizeFunction;
import com.webguys.ponzu.impl.block.function.NegativeIntervalFunction;
import com.webguys.ponzu.impl.block.procedure.MapPutProcedure;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.list.Interval;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.map.mutable.UnifiedMap;
import com.webguys.ponzu.impl.multimap.list.FastListMultimap;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.webguys.ponzu.impl.factory.Iterables.*;

public class ArrayIterateTest
{
    private static final Integer[] INTEGER_ARRAY = {5, 4, 3, 2, 1};

    @Test
    public void injectInto()
    {
        Integer[] objectArray = this.threeIntegerArray2();
        Assert.assertEquals(
                Integer.valueOf(7),
                ArrayIterate.foldLeft(1, objectArray, AddFunction.INTEGER));
    }

    private Integer[] threeIntegerArray2()
    {
        return new Integer[]{1, 2, 3};
    }

    @Test
    public void injectIntoDouble()
    {
        Double[] objectArray = {(double) 1, (double) 2, (double) 3};
        Assert.assertEquals(
                new Double(1 + 1 + 2 + 3),
                ArrayIterate.foldLeft((double) 1, objectArray, AddFunction.DOUBLE));
    }

    @Test
    public void injectIntoString()
    {
        String[] objectArray = {"1", "2", "3"};
        Assert.assertEquals("0123", ArrayIterate.foldLeft("0", objectArray, AddFunction.STRING));
    }

    //todo:review
    @Test
    public void injectIntoMaxString()
    {
        String[] objectArray = this.threeStringArray();
        Assert.assertEquals(Integer.valueOf(3),
                ArrayIterate.foldLeft(Integer.MIN_VALUE, objectArray, MaxSizeFunction.STRING));
    }

    private String[] threeStringArray()
    {
        return new String[]{"1", "12", "123"};
    }

    //todo:review
    @Test
    public void injectIntoMinString()
    {
        String[] objectArray = this.threeStringArray();
        Assert.assertEquals(
                Integer.valueOf(1),
                ArrayIterate.foldLeft(Integer.MAX_VALUE, objectArray, MinSizeFunction.STRING));
    }

    @Test
    public void collect()
    {
        Boolean[] objectArray = {true, false, null};
        Assert.assertEquals(
                iList("true", "false", "null"),
                ArrayIterate.transform(objectArray, Functions.getToString()));
    }

    @Test
    public void collectWith()
    {
        Boolean[] objectArray = {true, false, null};
        Assert.assertEquals(
                iList("true", "false", "null"),
                ArrayIterate.transformWith(objectArray, Functions2.fromFunction(Functions.getToString()), null));
    }

    @Test
    public void addAllTo()
    {
        MutableList<Integer> result = Lists.mutable.of();
        ArrayIterate.addAllTo(new Integer[]{1, 2, 3}, result);
        Assert.assertEquals(FastList.newListWith(1, 2, 3), result);
    }

    @Test
    public void getFirstAndLast()
    {
        List<Boolean> list = new ArrayList<Boolean>();
        list.add(Boolean.TRUE);
        list.add(null);
        list.add(Boolean.FALSE);
        Object[] objectArray = list.toArray();
        Assert.assertEquals(Boolean.TRUE, ArrayIterate.getFirst(objectArray));
        Assert.assertEquals(Boolean.FALSE, ArrayIterate.getLast(objectArray));
    }

    @Test
    public void getFirstAndLastOnEmpty()
    {
        Object[] objectArray = {};
        Assert.assertNull(ArrayIterate.getFirst(objectArray));
        Assert.assertNull(ArrayIterate.getLast(objectArray));
    }

    private void assertContainsAllIntegers(Collection<Integer> collection)
    {
        Assert.assertEquals(FastList.newListWith(5, 4, 3, 2, 1), collection);
    }

    @Test
    public void select()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        Collection<Integer> list = ArrayIterate.filter(objectArray, Predicates.instanceOf(Integer.class));
        this.assertContainsAllIntegers(list);
    }

    @Test
    public void selectWith()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        Collection<Integer> list = ArrayIterate.filterWith(objectArray, Predicates2.instanceOf(), Integer.class);
        this.assertContainsAllIntegers(list);
    }

    @Test
    public void countOnNullOrEmptyArray()
    {
        Assert.assertEquals(0, ArrayIterate.count(null, Predicates.alwaysTrue()));
        Assert.assertEquals(0, ArrayIterate.count(new Object[]{}, Predicates.alwaysTrue()));
    }

    @Test
    public void count()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        int count = ArrayIterate.count(objectArray, Predicates.instanceOf(Integer.class));
        Assert.assertEquals(5, count);
    }

    @Test
    public void countWith()
    {
        Object[] objectArray = INTEGER_ARRAY;
        int count = ArrayIterate.countWith(objectArray, Predicates2.instanceOf(), Integer.class);
        Assert.assertEquals(5, count);
    }

    @Test
    public void selectAndRejectWith()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        Twin<MutableList<Integer>> result =
                ArrayIterate.partitionWith(objectArray, Predicates2.<Integer>lessThan(), 3);
        MutableList<Integer> positive = result.getOne();
        MutableList<Integer> negative = result.getTwo();
        Verify.assertSize(2, positive);
        Verify.assertContains(2, positive);
        Verify.assertNotContains(3, positive);
        Verify.assertSize(3, negative);
        Verify.assertNotContains(2, negative);
        Verify.assertContains(3, negative);
    }

    @Test
    public void selectWithDifferentTargetCollection()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        Collection<Integer> list = ArrayIterate.filter(objectArray, Predicates.instanceOf(Integer.class), new ArrayList<Integer>());
        this.assertContainsAllIntegers(list);
    }

    @Test
    public void rejectDifferentTargetCollection()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        List<Integer> list = ArrayIterate.filterNot(objectArray, Predicates.instanceOf(Integer.class), new ArrayList<Integer>());
        Verify.assertSize(0, list);
    }

    //todo: further review
    @Test
    public void rejectWith()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        Collection<Integer> list =
                ArrayIterate.filterNotWith(objectArray, Predicates2.instanceOf(), Integer.class, new ArrayList<Integer>());
        Verify.assertSize(0, list);
    }

    @Test
    public void asHashMap()
    {
        Integer[] objectArray = INTEGER_ARRAY;
        MutableMap<String, Integer> map = ArrayIterate.toMap(objectArray, Functions.getToString());
        Verify.assertSize(5, map);
        Verify.assertContainsKeyValue("1", 1, map);
        Verify.assertContainsKeyValue("2", 2, map);
        Verify.assertContainsKeyValue("3", 3, map);
        Verify.assertContainsKeyValue("4", 4, map);
        Verify.assertContainsKeyValue("5", 5, map);
    }

    @Test
    public void contains()
    {
        Object[] array = INTEGER_ARRAY;
        Assert.assertTrue(ArrayIterate.contains(array, 5));
        Assert.assertFalse(ArrayIterate.contains(array, 6));
        Assert.assertFalse(ArrayIterate.contains(array, null));
        Assert.assertTrue(ArrayIterate.contains(new Object[]{null}, null));
    }

    @Test
    public void containsInt()
    {
        int[] array = {1, 2, 3, 4, 5};
        Assert.assertTrue(ArrayIterate.contains(array, 5));
        Assert.assertFalse(ArrayIterate.contains(array, 6));
    }

    @Test
    public void containsLong()
    {
        long[] array = {1, 2, 3, 4, 5};
        Assert.assertTrue(ArrayIterate.contains(array, 5));
        Assert.assertFalse(ArrayIterate.contains(array, 6));
    }

    @Test
    public void containsDouble()
    {
        double[] array = {1, 2, 3, 4, 5};
        Assert.assertTrue(ArrayIterate.contains(array, 5));
        Assert.assertFalse(ArrayIterate.contains(array, 6));
    }

    @Test
    public void anySatisfy()
    {
        Assert.assertTrue(ArrayIterate.anySatisfy(INTEGER_ARRAY, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayIterate.anySatisfy(INTEGER_ARRAY, Predicates.isNull()));
    }

    @Test
    public void anySatisfyWith()
    {
        Assert.assertTrue(ArrayIterate.anySatisfyWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(ArrayIterate.allSatisfy(INTEGER_ARRAY, Predicates.instanceOf(Integer.class)));
        Assert.assertFalse(ArrayIterate.allSatisfy(INTEGER_ARRAY, Predicates.isNull()));
    }

    @Test
    public void allSatisfyWith()
    {
        Object[] array = INTEGER_ARRAY;
        Assert.assertTrue(ArrayIterate.allSatisfyWith(array, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void isEmpty()
    {
        Assert.assertFalse(ArrayIterate.isEmpty(INTEGER_ARRAY));
        Assert.assertTrue(ArrayIterate.isEmpty(new Object[]{}));
        Assert.assertTrue(ArrayIterate.isEmpty(null));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertTrue(ArrayIterate.notEmpty(new Integer[]{5, 4, 3, 2, 1}));
        Assert.assertFalse(ArrayIterate.notEmpty(new Object[]{}));
        Assert.assertFalse(ArrayIterate.notEmpty(null));
    }

    @Test
    public void size()
    {
        Assert.assertEquals(5, ArrayIterate.size(new Integer[]{5, 4, 3, 2, 1}));
        Assert.assertEquals(0, ArrayIterate.size(null));
    }

    @Test
    public void get()
    {
        Assert.assertEquals(Integer.valueOf(1), new Integer[]{5, 4, 3, 2, 1}[4]);
    }

    @Test
    public void forEachInBoth()
    {
        MutableMap<String, String> map = UnifiedMap.newMap();
        ArrayIterate.forEachInBoth(
                new String[]{"1", "2", "3"},
                new String[]{"a", "b", "c"},
                new MapPutProcedure<String, String>(map));
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues(
                        "1", "a",
                        "2", "b",
                        "3", "c"),
                map);
    }

    @Test
    public void forEachWithIndex()
    {
        Integer[] objectArray = {1, 2, 3, 4};
        ArrayIterate.forEachWithIndex(objectArray, new ObjectIntProcedure<Integer>()
        {
            public void value(Integer i, int index)
            {
                Assert.assertEquals(index, i - 1);
            }
        });
    }

    private Integer[] createIntegerArray(int size)
    {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++)
        {
            array[i] = 1;
        }
        return array;
    }

    @Test
    public void detect()
    {
        Object[] array = this.createIntegerArray(1);
        Assert.assertEquals(1, ArrayIterate.find(array, new Predicate()
        {
            public boolean accept(Object anObject)
            {
                return (Integer) anObject == 1;
            }
        }));
    }

    @Test
    public void detectWith()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(
                Integer.valueOf(1),
                ArrayIterate.findWith(array, Predicates2.<Integer>lessThan(), 2));
    }

    @Test
    public void detectIfNone()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(Integer.valueOf(7), ArrayIterate.findIfNone(array, new Predicate<Integer>()
        {
            public boolean accept(Integer anObject)
            {
                return anObject == 2;
            }
        }, 7));
    }

    @Test
    public void detectWithIfNone()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(Integer.valueOf(7),
                ArrayIterate.findWithIfNone(array, Predicates2.equal(), 2, 7));
    }

    @Test
    public void indexOf()
    {
        String[] array = {"1", "2", "3", null};
        Assert.assertEquals(0, ArrayIterate.indexOf(array, "1"));
        Assert.assertEquals(1, ArrayIterate.indexOf(array, "2"));
        Assert.assertEquals(2, ArrayIterate.indexOf(array, "3"));
        Assert.assertEquals(3, ArrayIterate.indexOf(array, null));
        Assert.assertEquals(-1, ArrayIterate.indexOf(array, "4"));
    }

    @Test
    public void indexOfPredicates()
    {
        String[] array = {"1", "2", "3", null};
        Assert.assertEquals(0, ArrayIterate.findIndex(array, Predicates.instanceOf(String.class)));
        Assert.assertEquals(3, ArrayIterate.findIndex(array, Predicates.isNull()));
        Assert.assertEquals(0, ArrayIterate.findIndexWith(array, Predicates2.instanceOf(), String.class));
    }

    @Test
    public void take()
    {
        Assert.assertEquals(ListIterate.take(Interval.zeroTo(0), 5), ArrayIterate.take(Interval.zeroTo(0).toArray(), 5));
        Assert.assertEquals(ListIterate.take(Interval.zeroTo(5), 5), ArrayIterate.take(Interval.zeroTo(5).toArray(), 5));
        Assert.assertEquals(ListIterate.take(Interval.zeroTo(10), 5), ArrayIterate.take(Interval.zeroTo(10).toArray(), 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void take_negative_throws()
    {
        ListIterate.take(Interval.zeroTo(0), -1);
    }

    @Test
    public void drop()
    {
        Assert.assertEquals(ListIterate.drop(Interval.zeroTo(0).toList(), 5), ArrayIterate.drop(Interval.zeroTo(0).toList().toArray(), 5));
        Assert.assertEquals(ListIterate.drop(Interval.zeroTo(5), 5), ArrayIterate.drop(Interval.zeroTo(5).toArray(), 5));
        Assert.assertEquals(ListIterate.drop(Interval.zeroTo(10), 5), ArrayIterate.drop(Interval.zeroTo(10).toArray(), 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void drop_negative_throws()
    {
        ListIterate.drop(Interval.zeroTo(0), -1);
    }

    @Test
    public void groupBy()
    {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7};
        Function<Integer, Boolean> isOddFunction = new Function<Integer, Boolean>()
        {
            public Boolean valueOf(Integer object)
            {
                return IntegerPredicates.isOdd().accept(object);
            }
        };

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.<Boolean, RichIterable<Integer>>newWithKeysValues(
                        Boolean.TRUE, FastList.newListWith(1, 3, 5, 7),
                        Boolean.FALSE, FastList.newListWith(2, 4, 6));

        Multimap<Boolean, Integer> multimap = ArrayIterate.groupBy(array, isOddFunction);
        Assert.assertEquals(expected, multimap.toMap());
    }

    @Test
    public void groupByEach()
    {
        MutableMultimap<Integer, Integer> expected = FastListMultimap.newMultimap();
        for (int i = 1; i < 8; i++)
        {
            expected.putAll(-i, Interval.fromTo(i, 7));
        }

        Multimap<Integer, Integer> actual = ArrayIterate.groupByEach(new Integer[]{1, 2, 3, 4, 5, 6, 7}, new NegativeIntervalFunction());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void zip()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        Object[] nulls = Collections.nCopies(array.length, null).toArray();
        Object[] nullsPlusOne = Collections.nCopies(array.length + 1, null).toArray();
        Object[] nullsMinusOne = Collections.nCopies(array.length - 1, null).toArray();

        MutableList<Pair<String, Object>> pairs = ArrayIterate.zip(array, nulls);
        Assert.assertEquals(
                FastList.newListWith(array),
                pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                FastList.newListWith(nulls),
                pairs.transform(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        MutableList<Pair<String, Object>> pairsPlusOne = ArrayIterate.zip(array, nullsPlusOne);
        Assert.assertEquals(
                FastList.newListWith(array),
                pairsPlusOne.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(FastList.newListWith(nulls), pairsPlusOne.transform(Functions.<Object>secondOfPair(), Lists.mutable.of()));

        MutableList<Pair<String, Object>> pairsMinusOne = ArrayIterate.zip(array, nullsMinusOne);
        Assert.assertEquals(array.length - 1, pairsMinusOne.size());
        Assert.assertTrue(FastList.newListWith(array).containsAll(pairsMinusOne.transform(Functions.<String>firstOfPair())));

        Assert.assertEquals(
                ArrayIterate.zip(array, nulls),
                ArrayIterate.zip(array, nulls, FastList.<Pair<String, Object>>newList()));
    }

    @Test
    public void zipWithIndex()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        MutableList<Pair<String, Integer>> pairs = ArrayIterate.zipWithIndex(array);

        Assert.assertEquals(
                FastList.newListWith(array),
                pairs.transform(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                Interval.zeroTo(array.length - 1).toList(),
                pairs.transform(Functions.<Integer>secondOfPair(), FastList.<Integer>newList()));

        Assert.assertEquals(
                ArrayIterate.zipWithIndex(array),
                ArrayIterate.zipWithIndex(array, FastList.<Pair<String, Integer>>newList()));
    }

    @Test
    public void chunk()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        RichIterable<RichIterable<String>> groups = ArrayIterate.chunk(array, 2);
        RichIterable<Integer> sizes = groups.transform(new Function<RichIterable<String>, Integer>()
        {
            public Integer valueOf(RichIterable<String> richIterable)
            {
                return richIterable.size();
            }
        });
        Assert.assertEquals(FastList.newListWith(2, 2, 2, 1), sizes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void chunk_zero_throws()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        ArrayIterate.chunk(array, 0);
    }

    @Test
    public void chunk_large_size()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        Assert.assertEquals(FastList.newListWith(array), ArrayIterate.chunk(array, 10).getFirst());
    }
}
