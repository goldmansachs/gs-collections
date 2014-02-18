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

package com.gs.collections.impl.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.MaxSizeFunction;
import com.gs.collections.impl.block.function.MinSizeFunction;
import com.gs.collections.impl.block.function.NegativeIntervalFunction;
import com.gs.collections.impl.block.procedure.MapPutProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.factory.Iterables.*;

public class ArrayIterateTest
{
    private static final Integer[] INTEGER_ARRAY = {5, 4, 3, 2, 1};

    @Test
    public void injectInto()
    {
        Integer[] objectArray = this.threeIntegerArray2();
        Assert.assertEquals(
                Integer.valueOf(7),
                ArrayIterate.injectInto(1, objectArray, AddFunction.INTEGER));
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
                ArrayIterate.injectInto((double) 1, objectArray, AddFunction.DOUBLE));
    }

    @Test
    public void injectIntoPrimitives()
    {
        double doubleActual = ArrayIterate.injectInto(1.0d, new Double[]{1.0d, 2.0d, 3.0d}, new DoubleObjectToDoubleFunction<Double>()
        {
            public double doubleValueOf(double doubleParameter, Double objectParameter)
            {
                return doubleParameter + objectParameter;
            }
        });
        Assert.assertEquals(7.0, doubleActual, 0.000001);
        long longActual = ArrayIterate.injectInto(1L, new Long[]{1L, 2L, 3L}, new LongObjectToLongFunction<Long>()
        {
            public long longValueOf(long longParameter, Long objectParameter)
            {
                return longParameter + objectParameter;
            }
        });
        Assert.assertEquals(7L, longActual);
        int intActual = ArrayIterate.injectInto(1, new Integer[]{1, 2, 3}, new IntObjectToIntFunction<Integer>()
        {
            public int intValueOf(int intParameter, Integer objectParameter)
            {
                return intParameter + objectParameter;
            }
        });
        Assert.assertEquals(7, intActual);
    }

    @Test
    public void injectIntoThrowsOnNullArgument()
    {
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectInto(0, null, new IntObjectToIntFunction<Object>()
                {
                    public int intValueOf(int intParameter, Object objectParameter)
                    {
                        return 0;
                    }
                });
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectInto(0L, null, new LongObjectToLongFunction<Object>()
                {
                    public long longValueOf(long longParameter, Object objectParameter)
                    {
                        return 0;
                    }
                });
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectInto((double) 0, null, new DoubleObjectToDoubleFunction<Object>()
                {
                    public double doubleValueOf(double doubleParameter, Object objectParameter)
                    {
                        return 0.0;
                    }
                });
            }
        });
        Verify.assertThrows(IllegalArgumentException.class, new Runnable()
        {
            public void run()
            {
                ArrayIterate.injectInto(null, null, null);
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void allSatisfyThrowsOnNullArgument()
    {
        ArrayIterate.allSatisfy(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void allSatisfyWithThrowsOnNullArgument()
    {
        ArrayIterate.allSatisfyWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void anySatisfyThrowsOnNullArgument()
    {
        ArrayIterate.anySatisfy(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void anySatisfyWithThrowsOnNullArgument()
    {
        ArrayIterate.anySatisfyWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noneSatisfyThrowsOnNullArgument()
    {
        ArrayIterate.noneSatisfy(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noneSatisfyWithThrowsOnNullArgument()
    {
        ArrayIterate.noneSatisfyWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectThrowsOnNullArgument()
    {
        ArrayIterate.select(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.select(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectWithThrowsOnNullArgument()
    {
        ArrayIterate.selectWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void selectWithWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.selectWith(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectThrowsOnNullArgument()
    {
        ArrayIterate.reject(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.reject(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectWithThrowsOnNullArgument()
    {
        ArrayIterate.rejectWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectWithWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.rejectWith(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectThrowsOnNullArgument()
    {
        ArrayIterate.collect(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectIfThrowsOnNullArgument()
    {
        ArrayIterate.collectIf(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.collect(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectIfWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.collectIf(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectWithThrowsOnNullArgument()
    {
        ArrayIterate.collectWith(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void collectWithWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.collectWith(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void flatCollectThrowsOnNullArgument()
    {
        ArrayIterate.flatCollect(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void flatCollectWithTargetThrowsOnNullArgument()
    {
        ArrayIterate.flatCollect(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachThrowsOnNullArgument()
    {
        ArrayIterate.forEach(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachWithFromToThrowsOnNullArgument()
    {
        ArrayIterate.forEach(null, 0, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachWithIndexThrowsOnNullArgument()
    {
        ArrayIterate.forEachWithIndex(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void forEachWithIndexWithFromToThrowsOnNullArgument()
    {
        ArrayIterate.forEachWithIndex(null, 0, 0, null);
    }

    @Test
    public void partition()
    {
        PartitionIterable<Integer> result =
                ArrayIterate.partition(new Integer[]{1, 2, 3, 4, 5}, Predicates.greaterThan(3));
        Assert.assertEquals(iBag(4, 5), result.getSelected().toBag());
        Assert.assertEquals(iBag(1, 2, 3), result.getRejected().toBag());
    }

    @Test
    public void injectIntoString()
    {
        String[] objectArray = {"1", "2", "3"};
        Assert.assertEquals("0123", ArrayIterate.injectInto("0", objectArray, AddFunction.STRING));
    }

    //todo:review
    @Test
    public void injectIntoMaxString()
    {
        String[] objectArray = this.threeStringArray();
        Assert.assertEquals(Integer.valueOf(3),
                ArrayIterate.injectInto(Integer.MIN_VALUE, objectArray, MaxSizeFunction.STRING));
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
                ArrayIterate.injectInto(Integer.MAX_VALUE, objectArray, MinSizeFunction.STRING));
    }

    @Test
    public void collect()
    {
        Boolean[] objectArray = {true, false, null};
        Assert.assertEquals(
                iList("true", "false", "null"),
                ArrayIterate.collect(objectArray, Functions.getToString()));
    }

    @Test
    public void collectBoolean()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedBooleanResults(),
                ArrayIterate.collectBoolean(objectArray, PrimitiveFunctions.integerIsPositive()));
    }

    @Test
    public void collectBooleanWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        BooleanArrayList target = new BooleanArrayList();
        MutableBooleanList result = ArrayIterate.collectBoolean(objectArray, PrimitiveFunctions.integerIsPositive(), target);
        Assert.assertEquals(this.getExpectedBooleanResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private BooleanArrayList getExpectedBooleanResults()
    {
        return BooleanArrayList.newListWith(false, false, true);
    }

    @Test
    public void collectByte()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedByteResults(),
                ArrayIterate.collectByte(objectArray, PrimitiveFunctions.unboxIntegerToByte()));
    }

    @Test
    public void collectByteWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        ByteArrayList target = new ByteArrayList();
        ByteArrayList result = ArrayIterate.collectByte(objectArray, PrimitiveFunctions.unboxIntegerToByte(), target);
        Assert.assertEquals(this.getExpectedByteResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private ByteArrayList getExpectedByteResults()
    {
        return ByteArrayList.newListWith((byte) -1, (byte) 0, (byte) 42);
    }

    @Test
    public void collectChar()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedCharResults(),
                ArrayIterate.collectChar(objectArray, PrimitiveFunctions.unboxIntegerToChar()));
    }

    @Test
    public void collectCharWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        CharArrayList target = new CharArrayList();
        CharArrayList result = ArrayIterate.collectChar(objectArray, PrimitiveFunctions.unboxIntegerToChar(), target);
        Assert.assertEquals(this.getExpectedCharResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private CharArrayList getExpectedCharResults()
    {
        return CharArrayList.newListWith((char) -1, (char) 0, (char) 42);
    }

    @Test
    public void collectDouble()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedDoubleResults(),
                ArrayIterate.collectDouble(objectArray, PrimitiveFunctions.unboxIntegerToDouble()));
    }

    @Test
    public void collectDoubleWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        DoubleArrayList target = new DoubleArrayList();
        DoubleArrayList result = ArrayIterate.collectDouble(objectArray, PrimitiveFunctions.unboxIntegerToDouble(), target);
        Assert.assertEquals(this.getExpectedDoubleResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private DoubleArrayList getExpectedDoubleResults()
    {
        return DoubleArrayList.newListWith(-1.0d, 0.0d, 42.0d);
    }

    @Test
    public void collectFloat()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedFloatResults(),
                ArrayIterate.collectFloat(objectArray, PrimitiveFunctions.unboxIntegerToFloat()));
    }

    @Test
    public void collectFloatWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        FloatArrayList target = new FloatArrayList();
        FloatArrayList result = ArrayIterate.collectFloat(objectArray, PrimitiveFunctions.unboxIntegerToFloat(), target);
        Assert.assertEquals(this.getExpectedFloatResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private FloatArrayList getExpectedFloatResults()
    {
        return FloatArrayList.newListWith(-1.0f, 0.0f, 42.0f);
    }

    @Test
    public void collectInt()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedIntResults(),
                ArrayIterate.collectInt(objectArray, PrimitiveFunctions.unboxIntegerToInt()));
    }

    @Test
    public void collectIntWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        IntArrayList target = new IntArrayList();
        IntArrayList result = ArrayIterate.collectInt(objectArray, PrimitiveFunctions.unboxIntegerToInt(), target);
        Assert.assertEquals(this.getExpectedIntResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private IntArrayList getExpectedIntResults()
    {
        return IntArrayList.newListWith(-1, 0, 42);
    }

    @Test
    public void collectLong()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedLongResults(),
                ArrayIterate.collectLong(objectArray, PrimitiveFunctions.unboxIntegerToLong()));
    }

    @Test
    public void collectLongWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        LongArrayList target = new LongArrayList();
        LongArrayList result = ArrayIterate.collectLong(objectArray, PrimitiveFunctions.unboxIntegerToLong(), target);
        Assert.assertEquals(this.getExpectedLongResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private LongArrayList getExpectedLongResults()
    {
        return LongArrayList.newListWith(-1L, 0L, 42L);
    }

    @Test
    public void collectShort()
    {
        Integer[] objectArray = {-1, 0, 42};
        Assert.assertEquals(this.getExpectedShortResults(),
                ArrayIterate.collectShort(objectArray, PrimitiveFunctions.unboxIntegerToShort()));
    }

    @Test
    public void collectShortWithTarget()
    {
        Integer[] objectArray = {-1, 0, 42};
        ShortArrayList target = new ShortArrayList();
        ShortArrayList result = ArrayIterate.collectShort(objectArray, PrimitiveFunctions.unboxIntegerToShort(), target);
        Assert.assertEquals(this.getExpectedShortResults(), result);
        Assert.assertSame("Target List not returned as result", target, result);
    }

    private ShortArrayList getExpectedShortResults()
    {
        return ShortArrayList.newListWith((short) -1, (short) 0, (short) 42);
    }

    @Test
    public void collectWith()
    {
        Boolean[] objectArray = {true, false, null};
        Assert.assertEquals(
                iList("true", "false", "null"),
                ArrayIterate.collectWith(objectArray, Functions2.fromFunction(Functions.getToString()), null));
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

    @Test
    public void select()
    {
        Assert.assertEquals(
                FastList.newListWith(5, 4, 3, 2, 1),
                ArrayIterate.select(INTEGER_ARRAY, Predicates.instanceOf(Integer.class)));
    }

    @Test
    public void reject()
    {
        Assert.assertEquals(
                FastList.newListWith(5, 4, 3, 2, 1),
                ArrayIterate.reject(INTEGER_ARRAY, Predicates.instanceOf(String.class)));
    }

    @Test
    public void distinct()
    {
        List<Integer> result = FastList.newList();
        ArrayIterate.distinct(new Integer[]{5, 3, 1, 5, 7, 1}, result);
        Assert.assertEquals(FastList.newListWith(5, 3, 1, 7), result);
        result.clear();
        ArrayIterate.distinct(INTEGER_ARRAY, result);
        Assert.assertEquals(FastList.newListWith(INTEGER_ARRAY), result);
    }

    @Test
    public void selectWith()
    {
        Assert.assertEquals(
                FastList.newListWith(5, 4, 3, 2, 1),
                ArrayIterate.selectWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                iList(1, 3, 5),
                ArrayIterate.selectInstancesOf(new Number[]{1, 2.0, 3, 4.0, 5}, Integer.class));
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
        Assert.assertEquals(3, ArrayIterate.count(INTEGER_ARRAY, Predicates.lessThan(4)));
    }

    @Test
    public void countWith()
    {
        Assert.assertEquals(5, ArrayIterate.countWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(0, ArrayIterate.countWith(new Integer[]{}, Predicates2.instanceOf(), Integer.class));
        Assert.assertEquals(1, ArrayIterate.countWith(new Object[]{"test", null, Integer.valueOf(2)}, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void selectAndRejectWith()
    {
        Twin<MutableList<Integer>> result =
                ArrayIterate.selectAndRejectWith(INTEGER_ARRAY, Predicates2.<Integer>lessThan(), 3);
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
        Assert.assertEquals(
                FastList.newListWith(5, 4, 3, 2, 1),
                ArrayIterate.select(INTEGER_ARRAY, Predicates.instanceOf(Integer.class), new ArrayList<Integer>()));
    }

    @Test
    public void rejectDifferentTargetCollection()
    {
        Verify.assertEmpty(ArrayIterate.reject(INTEGER_ARRAY, Predicates.instanceOf(Integer.class), new ArrayList<Integer>()));
    }

    @Test
    public void rejectWith()
    {
        Verify.assertEmpty(ArrayIterate.rejectWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class));
        Verify.assertEmpty(ArrayIterate.rejectWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class, new ArrayList<Integer>()));
    }

    @Test
    public void collectIf()
    {
        Object[] integers = Lists.fixedSize.of(1, 2, 3).toArray();
        Verify.assertContainsAll(ArrayIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString()), "1", "2", "3");
        Verify.assertContainsAll(ArrayIterate.collectIf(integers, Predicates.instanceOf(Integer.class), Functions.getToString(), FastList.<String>newList()), "1", "2", "3");
    }

    @Test
    public void toMap()
    {
        MutableMap<String, Integer> map = ArrayIterate.toMap(INTEGER_ARRAY, Functions.getToString());
        Verify.assertSize(5, map);
        Verify.assertContainsKeyValue("1", 1, map);
        Verify.assertContainsKeyValue("2", 2, map);
        Verify.assertContainsKeyValue("3", 3, map);
        Verify.assertContainsKeyValue("4", 4, map);
        Verify.assertContainsKeyValue("5", 5, map);
    }

    @Test
    public void toMap2()
    {
        MutableMap<String, Integer> map = ArrayIterate.toMap(INTEGER_ARRAY, Functions.getToString(), Functions.squaredInteger());
        Verify.assertSize(5, map);
        Verify.assertContainsKeyValue("1", 1, map);
        Verify.assertContainsKeyValue("2", 4, map);
        Verify.assertContainsKeyValue("3", 9, map);
        Verify.assertContainsKeyValue("4", 16, map);
        Verify.assertContainsKeyValue("5", 25, map);
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(ArrayIterate.contains(INTEGER_ARRAY, 5));
        Assert.assertFalse(ArrayIterate.contains(INTEGER_ARRAY, 6));
        Assert.assertFalse(ArrayIterate.contains(INTEGER_ARRAY, null));
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
        Assert.assertTrue(ArrayIterate.allSatisfyWith(INTEGER_ARRAY, Predicates2.instanceOf(), Integer.class));
    }

    @Test
    public void noneSatisfy()
    {
        Assert.assertTrue(ArrayIterate.noneSatisfy(INTEGER_ARRAY, Predicates.instanceOf(String.class)));
        Assert.assertFalse(ArrayIterate.noneSatisfy(INTEGER_ARRAY, Predicates.notNull()));
    }

    @Test
    public void noneSatisfyWith()
    {
        Assert.assertTrue(ArrayIterate.noneSatisfyWith(INTEGER_ARRAY, Predicates2.instanceOf(), String.class));
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
        ArrayIterate.forEachInBoth(null, null, new Procedure2<Object, Object>()
        {
            public void value(Object argument1, Object argument2)
            {
                Assert.fail();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void forEachInBothThrowsOnDifferentLengthArrays()
    {
        ArrayIterate.forEachInBoth(new Integer[]{1, 2, 3}, new Integer[]{1, 2}, new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                Assert.fail();
            }
        });
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
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(Integer.valueOf(1), ArrayIterate.detect(array, new Predicate<Integer>()
        {
            public boolean accept(Integer integer)
            {
                return integer == 1;
            }
        }));
    }

    @Test
    public void detectWith()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(
                Integer.valueOf(1),
                ArrayIterate.detectWith(array, Predicates2.<Integer>lessThan(), 2));
        Assert.assertNull(
                ArrayIterate.detectWith(new Integer[0], Predicates2.<Integer>lessThan(), 2));
    }

    @Test
    public void detectIfNone()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(Integer.valueOf(7), ArrayIterate.detectIfNone(array, Predicates.equal(2), 7));
        Assert.assertEquals(Integer.valueOf(1), ArrayIterate.detectIfNone(array, Predicates.equal(1), 7));
    }

    @Test
    public void detectWithIfNone()
    {
        Integer[] array = this.createIntegerArray(1);
        Assert.assertEquals(Integer.valueOf(7),
                ArrayIterate.detectWithIfNone(array, Predicates2.equal(), 2, 7));
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
        Assert.assertEquals(0, ArrayIterate.detectIndex(array, Predicates.instanceOf(String.class)));
        Assert.assertEquals(3, ArrayIterate.detectIndex(array, Predicates.isNull()));
        Assert.assertEquals(0, ArrayIterate.detectIndexWith(array, Predicates2.instanceOf(), String.class));
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
        ArrayIterate.take(Interval.zeroTo(0).toArray(), -1);
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
        ArrayIterate.drop(Interval.zeroTo(0).toArray(), -1);
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
                pairs.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                FastList.newListWith(nulls),
                pairs.collect(Functions.secondOfPair(), Lists.mutable.of()));

        MutableList<Pair<String, Object>> pairsPlusOne = ArrayIterate.zip(array, nullsPlusOne);
        Assert.assertEquals(
                FastList.newListWith(array),
                pairsPlusOne.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(FastList.newListWith(nulls), pairsPlusOne.collect(Functions.secondOfPair(), Lists.mutable.of()));

        MutableList<Pair<String, Object>> pairsMinusOne = ArrayIterate.zip(array, nullsMinusOne);
        Assert.assertEquals(array.length - 1, pairsMinusOne.size());
        Assert.assertTrue(FastList.newListWith(array).containsAll(pairsMinusOne.collect(Functions.<String>firstOfPair())));

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
                pairs.collect(Functions.<String>firstOfPair()));
        Assert.assertEquals(
                Interval.zeroTo(array.length - 1).toList(),
                pairs.collect(Functions.<Integer>secondOfPair(), FastList.<Integer>newList()));

        Assert.assertEquals(
                ArrayIterate.zipWithIndex(array),
                ArrayIterate.zipWithIndex(array, FastList.<Pair<String, Integer>>newList()));
    }

    @Test
    public void chunk()
    {
        String[] array = {"1", "2", "3", "4", "5", "6", "7"};
        RichIterable<RichIterable<String>> groups = ArrayIterate.chunk(array, 2);
        RichIterable<Integer> sizes = groups.collect(new Function<RichIterable<String>, Integer>()
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

    @Test
    public void makeString()
    {
        String[] array = {"1", "2", "3", "4", "5"};
        Assert.assertEquals("1, 2, 3, 4, 5", ArrayIterate.makeString(array));
    }

    @Test
    public void appendString()
    {
        String[] array = {"1", "2", "3", "4", "5"};
        StringBuilder stringBuilder = new StringBuilder();
        ArrayIterate.appendString(array, stringBuilder);
        Assert.assertEquals("1, 2, 3, 4, 5", stringBuilder.toString());
    }

    @Test(expected = RuntimeException.class)
    public void appendStringThrowsIOException()
    {
        ArrayIterate.appendString(new String[]{"1", "2", "3"}, new Appendable()
        {
            public Appendable append(CharSequence csq) throws IOException
            {
                throw new IOException();
            }

            public Appendable append(CharSequence csq, int start, int end) throws IOException
            {
                throw new IOException();
            }

            public Appendable append(char c) throws IOException
            {
                throw new IOException();
            }
        });
    }
}
