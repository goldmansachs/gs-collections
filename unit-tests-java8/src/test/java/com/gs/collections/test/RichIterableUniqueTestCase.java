/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.test;

import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.SerializeTestHelper;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertPostSerializedEqualsAndHashCode;
import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

public interface RichIterableUniqueTestCase extends RichIterableTestCase
{
    @Override
    default boolean allowsDuplicates()
    {
        return false;
    }

    @Override
    @Test
    default void Object_PostSerializedEqualsAndHashCode()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);
        Object deserialized = SerializeTestHelper.serializeDeserialize(iterable);
        Assert.assertNotSame(iterable, deserialized);
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        assertPostSerializedEqualsAndHashCode(this.newWith(3, 2, 1));

        assertNotEquals(this.newWith(4, 3, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(4, 3, 2, 1));

        assertNotEquals(this.newWith(2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(2, 1));

        assertNotEquals(this.newWith(4, 2, 1), this.newWith(3, 2, 1));
        assertNotEquals(this.newWith(3, 2, 1), this.newWith(4, 2, 1));
    }

    @Test
    default void Iterable_sanity_check()
    {
        String s = "";
        assertThrows(IllegalStateException.class, () -> this.newWith(s, s));
    }

    @Override
    @Test
    default void InternalIterable_forEach()
    {
        RichIterable<Integer> iterable = this.newWith(3, 2, 1);
        MutableCollection<Integer> result = this.newMutableForFilter();
        iterable.forEach(Procedures.cast(i -> result.add(i + 10)));
        assertEquals(this.newMutableForFilter(13, 12, 11), result);
    }

    @Override
    @Test
    default void InternalIterable_forEachWith()
    {
        RichIterable<Integer> iterable = this.newWith(3, 2, 1);
        MutableCollection<Integer> result = this.newMutableForFilter();
        iterable.forEachWith((argument1, argument2) -> result.add(argument1 + argument2), 10);
        assertEquals(this.newMutableForFilter(13, 12, 11), result);
    }

    @Test
    default void RichIterable_size()
    {
        assertEquals(3, this.newWith(3, 2, 1).size());
    }

    @Test
    default void RichIterable_toArray()
    {
        Object[] array = this.newWith(3, 2, 1).toArray();
        assertArrayEquals(new Object[]{3, 2, 1}, array);
    }

    @Override
    @Test
    default void RichIterable_select()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 2),
                iterable.select(IntegerPredicates.isEven()));
    }

    @Override
    @Test
    default void RichIterable_select_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 2),
                iterable.select(IntegerPredicates.isEven(), this.<Integer>newMutableForFilter()));
    }

    @Override
    @Test
    default void RichIterable_selectWith()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 3),
                iterable.selectWith(Predicates2.greaterThan(), 2));
    }

    @Override
    @Test
    default void RichIterable_selectWith_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 3),
                iterable.selectWith(Predicates2.<Integer>greaterThan(), 2, this.<Integer>newMutableForFilter()));
    }

    @Override
    @Test
    default void RichIterable_reject()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 2),
                iterable.reject(IntegerPredicates.isOdd()));
    }

    @Override
    @Test
    default void RichIterable_reject_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 2),
                iterable.reject(IntegerPredicates.isOdd(), this.<Integer>newMutableForFilter()));
    }

    @Override
    @Test
    default void RichIterable_rejectWith()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 3),
                iterable.rejectWith(Predicates2.lessThan(), 3));
    }

    @Override
    @Test
    default void RichIterable_rejectWith_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 3),
                iterable.rejectWith(Predicates2.<Integer>lessThan(), 3, this.<Integer>newMutableForFilter()));
    }

    @Override
    @Test
    default void RichIterable_partition()
    {
        RichIterable<Integer> iterable = this.newWith(-3, -2, -1, 0, 1, 2, 3);
        PartitionIterable<Integer> result = iterable.partition(IntegerPredicates.isEven());
        assertEquals(this.getExpectedFiltered(-2, 0, 2), result.getSelected());
        assertEquals(this.getExpectedFiltered(-3, -1, 1, 3), result.getRejected());
    }

    @Override
    @Test
    default void RichIterable_partitionWith()
    {
        RichIterable<Integer> iterable = this.newWith(-3, -2, -1, 0, 1, 2, 3);
        PartitionIterable<Integer> result = iterable.partitionWith(Predicates2.greaterThan(), 0);
        assertEquals(this.getExpectedFiltered(1, 2, 3), result.getSelected());
        assertEquals(this.getExpectedFiltered(-3, -2, -1, 0), result.getRejected());
    }

    @Override
    @Test
    default void RichIterable_selectInstancesOf()
    {
        RichIterable<Number> iterable = this.<Number>newWith(1, 2.0, 3, 4.0);
        assertEquals(this.getExpectedFiltered(), iterable.selectInstancesOf(String.class));
        assertEquals(this.getExpectedFiltered(1, 3), iterable.selectInstancesOf(Integer.class));
        assertEquals(this.getExpectedFiltered(1, 2.0, 3, 4.0), iterable.selectInstancesOf(Number.class));
    }

    @Override
    @Test
    default void RichIterable_collect()
    {
        assertEquals(
                this.getExpectedTransformed(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collect(i -> i % 10));
    }

    @Override
    @Test
    default void RichIterable_collect_target()
    {
        assertEquals(
                this.getExpectedTransformed(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collect(i -> i % 10, this.newMutableForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectWith()
    {
        assertEquals(
                this.getExpectedTransformed(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectWith((i, mod) -> i % mod, 10));
    }

    @Override
    @Test
    default void RichIterable_collectWith_target()
    {
        assertEquals(
                this.getExpectedTransformed(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectWith((i, mod) -> i % mod, 10, this.newMutableForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectIf()
    {
        assertEquals(
                this.getExpectedTransformed(3, 1, 3, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectIf(i -> i % 2 != 0, i -> i % 10));
    }

    @Override
    @Test
    default void RichIterable_collectIf_target()
    {
        assertEquals(
                this.newMutableForTransform(3, 1, 3, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectIf(i -> i % 2 != 0, i -> i % 10, this.newMutableForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectBoolean()
    {
        assertEquals(
                this.getExpectedBoolean(false, true, false),
                this.newWith(3, 2, 1).collectBoolean(each -> each % 2 == 0));
    }

    @Override
    @Test
    default void RichIterable_collectBoolean_target()
    {
        assertEquals(
                this.getExpectedBoolean(false, true, false),
                this.newWith(3, 2, 1).collectBoolean(each -> each % 2 == 0, this.newBooleanForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectByte()
    {
        assertEquals(
                this.getExpectedByte((byte) 3, (byte) 2, (byte) 1, (byte) 3, (byte) 2, (byte) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectByte(each -> (byte) (each % 10)));
    }

    @Override
    @Test
    default void RichIterable_collectByte_target()
    {
        assertEquals(
                this.getExpectedByte((byte) 3, (byte) 2, (byte) 1, (byte) 3, (byte) 2, (byte) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectByte(each -> (byte) (each % 10), this.newByteForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectChar()
    {
        assertEquals(
                this.getExpectedChar((char) 3, (char) 2, (char) 1, (char) 3, (char) 2, (char) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectChar(each -> (char) (each % 10)));
    }

    @Override
    @Test
    default void RichIterable_collectChar_target()
    {
        assertEquals(
                this.getExpectedChar((char) 3, (char) 2, (char) 1, (char) 3, (char) 2, (char) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectChar(each -> (char) (each % 10), this.newCharForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectDouble()
    {
        assertEquals(
                this.getExpectedDouble(3.0, 2.0, 1.0, 3.0, 2.0, 1.0),
                this.newWith(13, 12, 11, 3, 2, 1).collectDouble(each -> (double) (each % 10)));
    }

    @Override
    @Test
    default void RichIterable_collectDouble_target()
    {
        assertEquals(
                this.getExpectedDouble(3.0, 2.0, 1.0, 3.0, 2.0, 1.0),
                this.newWith(13, 12, 11, 3, 2, 1).collectDouble(each -> (double) (each % 10), this.newDoubleForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectFloat()
    {
        assertEquals(
                this.getExpectedFloat(3.0f, 2.0f, 1.0f, 3.0f, 2.0f, 1.0f),
                this.newWith(13, 12, 11, 3, 2, 1).collectFloat(each -> (float) (each % 10)));
    }

    @Override
    @Test
    default void RichIterable_collectFloat_target()
    {
        assertEquals(
                this.getExpectedFloat(3.0f, 2.0f, 1.0f, 3.0f, 2.0f, 1.0f),
                this.newWith(13, 12, 11, 3, 2, 1).collectFloat(each -> (float) (each % 10), this.newFloatForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectInt()
    {
        assertEquals(
                this.getExpectedInt(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectInt(each -> each % 10));
    }

    @Override
    @Test
    default void RichIterable_collectInt_target()
    {
        assertEquals(
                this.getExpectedInt(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectInt(each -> each % 10, this.newIntForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectLong()
    {
        assertEquals(
                this.getExpectedLong(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectLong(each -> each % 10));
    }

    @Override
    @Test
    default void RichIterable_collectLong_target()
    {
        assertEquals(
                this.getExpectedLong(3, 2, 1, 3, 2, 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectLong(each -> each % 10, this.newLongForTransform()));
    }

    @Override
    @Test
    default void RichIterable_collectShort()
    {
        assertEquals(
                this.getExpectedShort((short) 3, (short) 2, (short) 1, (short) 3, (short) 2, (short) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectShort(each -> (short) (each % 10)));
    }

    @Override
    @Test
    default void RichIterable_collectShort_target()
    {
        assertEquals(
                this.getExpectedShort((short) 3, (short) 2, (short) 1, (short) 3, (short) 2, (short) 1),
                this.newWith(13, 12, 11, 3, 2, 1).collectShort(each -> (short) (each % 10), this.newShortForTransform()));
    }

    @Override
    @Test
    default void RichIterable_flatCollect()
    {
        assertEquals(
                this.getExpectedTransformed(1, 2, 3, 1, 2, 1),
                this.newWith(3, 2, 1).flatCollect(Interval::oneTo));
    }

    @Override
    @Test
    default void RichIterable_flatCollect_target()
    {
        assertEquals(
                this.getExpectedTransformed(1, 2, 3, 1, 2, 1),
                this.newWith(3, 2, 1).flatCollect(Interval::oneTo, this.newMutableForTransform()));
    }

    @Override
    @Test
    default void RichIterable_count()
    {
        assertEquals(1, this.newWith(3, 2, 1).count(Integer.valueOf(3)::equals));
        assertEquals(1, this.newWith(3, 2, 1).count(Integer.valueOf(2)::equals));
        assertEquals(1, this.newWith(3, 2, 1).count(Integer.valueOf(1)::equals));
        assertEquals(0, this.newWith(3, 2, 1).count(Integer.valueOf(0)::equals));
        assertEquals(2, this.newWith(3, 2, 1).count(i -> i % 2 != 0));
        assertEquals(3, this.newWith(3, 2, 1).count(i -> i > 0));
    }

    @Override
    @Test
    default void RichIterable_countWith()
    {
        assertEquals(1, this.newWith(3, 2, 1).countWith(Object::equals, 3));
        assertEquals(1, this.newWith(3, 2, 1).countWith(Object::equals, 2));
        assertEquals(1, this.newWith(3, 2, 1).countWith(Object::equals, 1));
        assertEquals(0, this.newWith(3, 2, 1).countWith(Object::equals, 0));
        assertEquals(3, this.newWith(3, 2, 1).countWith(Predicates2.greaterThan(), 0));
    }

    @Override
    @Test
    default void RichIterable_groupBy()
    {
        RichIterable<Integer> collection = this.newWith(4, 3, 2, 1);
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.newWithKeysValues(
                        Boolean.TRUE, this.newMutableForFilter(3, 1),
                        Boolean.FALSE, this.newMutableForFilter(4, 2));

        Multimap<Boolean, Integer> multimap = collection.groupBy(isOddFunction);
        assertEquals(expected, multimap.toMap());

        Function<Integer, Boolean> function = (Integer object) -> true;
        MutableMultimap<Boolean, Integer> multimap2 = collection.groupBy(
                isOddFunction,
                this.<Integer>newWith().groupBy(function).toMutable());
        assertEquals(expected, multimap2.toMap());
    }

    @Override
    @Test
    default void RichIterable_groupByEach()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);

        Function<Integer, Iterable<Integer>> function = integer -> Interval.fromTo(-1, -integer);

        MutableMap<Integer, RichIterable<Integer>> expected =
                UnifiedMap.newWithKeysValues(
                        -4, this.newMutableForFilter(4),
                        -3, this.newMutableForFilter(4, 3),
                        -2, this.newMutableForFilter(4, 3, 2),
                        -1, this.newMutableForFilter(4, 3, 2, 1));

        Multimap<Integer, Integer> multimap = iterable.groupByEach(function);
        assertEquals(expected, multimap.toMap());

        Multimap<Integer, Integer> actualWithTarget =
                iterable.groupByEach(function, this.<Integer>newWith().groupByEach(function).toMutable());
        assertEquals(expected, actualWithTarget.toMap());
    }

    @Override
    @Test
    default void RichIterable_aggregateBy_aggregateInPlaceBy()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);

        MapIterable<String, Integer> aggregateBy = iterable.aggregateBy(
                Object::toString,
                () -> 0,
                (integer1, integer2) -> integer1 + integer2);

        assertEquals(4, aggregateBy.get("4").intValue());
        assertEquals(3, aggregateBy.get("3").intValue());
        assertEquals(2, aggregateBy.get("2").intValue());
        assertEquals(1, aggregateBy.get("1").intValue());

        MapIterable<String, AtomicInteger> aggregateInPlaceBy = iterable.aggregateInPlaceBy(
                String::valueOf,
                AtomicInteger::new,
                AtomicInteger::addAndGet);
        assertEquals(4, aggregateInPlaceBy.get("4").intValue());
        assertEquals(3, aggregateInPlaceBy.get("3").intValue());
        assertEquals(2, aggregateInPlaceBy.get("2").intValue());
        assertEquals(1, aggregateInPlaceBy.get("1").intValue());
    }

    @Override
    @Test
    default void RichIterable_sumOfPrimitive()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        Assert.assertEquals(10.0f, iterable.sumOfFloat(Integer::floatValue), 0.001);
        Assert.assertEquals(10.0, iterable.sumOfDouble(Integer::doubleValue), 0.001);
        Assert.assertEquals(10, iterable.sumOfInt(integer -> integer));
        Assert.assertEquals(10L, iterable.sumOfLong(Integer::longValue));
    }

    @Override
    @Test
    default void RichIterable_injectInto()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals(Integer.valueOf(11), iterable.injectInto(1, new Function2<Integer, Integer, Integer>()
        {
            private static final long serialVersionUID = 1L;

            public Integer value(Integer argument1, Integer argument2)
            {
                return argument1 + argument2;
            }
        }));
        assertEquals(Integer.valueOf(10), iterable.injectInto(0, new Function2<Integer, Integer, Integer>()
        {
            private static final long serialVersionUID = 1L;

            public Integer value(Integer argument1, Integer argument2)
            {
                return argument1 + argument2;
            }
        }));
    }

    @Override
    @Test
    default void RichIterable_injectInto_primitive()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);

        Assert.assertEquals(11, iterable.injectInto(1, AddFunction.INTEGER_TO_INT));
        Assert.assertEquals(10, iterable.injectInto(0, AddFunction.INTEGER_TO_INT));

        Assert.assertEquals(11L, iterable.injectInto(1, AddFunction.INTEGER_TO_LONG));
        Assert.assertEquals(10L, iterable.injectInto(0, AddFunction.INTEGER_TO_LONG));

        Assert.assertEquals(11.0d, iterable.injectInto(1, AddFunction.INTEGER_TO_DOUBLE), 0.001);
        Assert.assertEquals(10.0d, iterable.injectInto(0, AddFunction.INTEGER_TO_DOUBLE), 0.001);

        Assert.assertEquals(11.0f, iterable.injectInto(1, AddFunction.INTEGER_TO_FLOAT), 0.001f);
        Assert.assertEquals(10.0f, iterable.injectInto(0, AddFunction.INTEGER_TO_FLOAT), 0.001f);
    }

    @Override
    @Test
    default void RichIterable_makeString_appendString()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        assertEquals("4, 3, 2, 1", iterable.makeString());
        assertEquals("4/3/2/1", iterable.makeString("/"));
        assertEquals("[4/3/2/1]", iterable.makeString("[", "/", "]"));

        StringBuilder stringBuilder1 = new StringBuilder();
        iterable.appendString(stringBuilder1);
        assertEquals("4, 3, 2, 1", stringBuilder1.toString());

        StringBuilder stringBuilder2 = new StringBuilder();
        iterable.appendString(stringBuilder2, "/");
        assertEquals("4/3/2/1", stringBuilder2.toString());

        StringBuilder stringBuilder3 = new StringBuilder();
        iterable.appendString(stringBuilder3, "[", "/", "]");
        assertEquals("[4/3/2/1]", stringBuilder3.toString());
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        assertEquals("[4, 3, 2, 1]", this.newWith(4, 3, 2, 1).toString());
    }
}
