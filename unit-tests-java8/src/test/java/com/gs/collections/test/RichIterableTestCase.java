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

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.ByteIterable;
import com.gs.collections.api.CharIterable;
import com.gs.collections.api.DoubleIterable;
import com.gs.collections.api.FloatIterable;
import com.gs.collections.api.IntIterable;
import com.gs.collections.api.LongIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.ShortIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleObjectToDoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatObjectToFloatFunction;
import com.gs.collections.api.block.function.primitive.IntObjectToIntFunction;
import com.gs.collections.api.block.function.primitive.LongObjectToLongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableByteCollection;
import com.gs.collections.api.collection.primitive.MutableCharCollection;
import com.gs.collections.api.collection.primitive.MutableDoubleCollection;
import com.gs.collections.api.collection.primitive.MutableFloatCollection;
import com.gs.collections.api.collection.primitive.MutableIntCollection;
import com.gs.collections.api.collection.primitive.MutableLongCollection;
import com.gs.collections.api.collection.primitive.MutableShortCollection;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.partition.PartitionIterable;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ByteHashBag;
import com.gs.collections.impl.bag.mutable.primitive.CharHashBag;
import com.gs.collections.impl.bag.mutable.primitive.DoubleHashBag;
import com.gs.collections.impl.bag.mutable.primitive.FloatHashBag;
import com.gs.collections.impl.bag.mutable.primitive.IntHashBag;
import com.gs.collections.impl.bag.mutable.primitive.LongHashBag;
import com.gs.collections.impl.bag.mutable.primitive.ShortHashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.Interval;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public interface RichIterableTestCase extends IterableTestCase
{
    @Override
    <T> RichIterable<T> newWith(T... elements);

    <T> RichIterable<T> getExpectedFiltered(T... elements);

    <T> RichIterable<T> getExpectedTransformed(T... elements);

    <T> MutableCollection<T> newMutableForFilter(T... elements);

    <T> MutableCollection<T> newMutableForTransform(T... elements);

    @Test
    default void newMutable_sanity()
    {
        assertEquals(this.getExpectedFiltered(3, 2, 1), this.newMutableForFilter(3, 2, 1));
    }

    MutableBooleanCollection newBooleanForTransform(boolean... elements);

    MutableByteCollection newByteForTransform(byte... elements);

    MutableCharCollection newCharForTransform(char... elements);

    MutableDoubleCollection newDoubleForTransform(double... elements);

    MutableFloatCollection newFloatForTransform(float... elements);

    MutableIntCollection newIntForTransform(int... elements);

    MutableLongCollection newLongForTransform(long... elements);

    MutableShortCollection newShortForTransform(short... elements);

    default BooleanIterable getExpectedBoolean(boolean... elements)
    {
        return this.newBooleanForTransform(elements);
    }

    default ByteIterable getExpectedByte(byte... elements)
    {
        return this.newByteForTransform(elements);
    }

    default CharIterable getExpectedChar(char... elements)
    {
        return this.newCharForTransform(elements);
    }

    default DoubleIterable getExpectedDouble(double... elements)
    {
        return this.newDoubleForTransform(elements);
    }

    default FloatIterable getExpectedFloat(float... elements)
    {
        return this.newFloatForTransform(elements);
    }

    default IntIterable getExpectedInt(int... elements)
    {
        return this.newIntForTransform(elements);
    }

    default LongIterable getExpectedLong(long... elements)
    {
        return this.newLongForTransform(elements);
    }

    default ShortIterable getExpectedShort(short... elements)
    {
        return this.newShortForTransform(elements);
    }

    @Test
    default void InternalIterable_forEach()
    {
        RichIterable<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        MutableCollection<Integer> result = this.newMutableForFilter();
        iterable.forEach(Procedures.cast(i -> result.add(i + 10)));
        assertEquals(this.newMutableForFilter(13, 13, 13, 12, 12, 11), result);
    }

    @Test
    default void InternalIterable_forEachWith()
    {
        RichIterable<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        MutableCollection<Integer> result = this.newMutableForFilter();
        iterable.forEachWith((argument1, argument2) -> result.add(argument1 + argument2), 10);
        assertEquals(this.newMutableForFilter(13, 13, 13, 12, 12, 11), result);
    }

    @Test
    default void RichIterable_size_empty()
    {
        assertEquals(0, this.newWith().size());
    }

    @Test
    default void RichIterable_isEmpty()
    {
        assertFalse(this.newWith(3, 2, 1).isEmpty());
        assertTrue(this.newWith().isEmpty());
    }

    @Test
    default void RichIterable_notEmpty()
    {
        assertTrue(this.newWith(3, 2, 1).notEmpty());
        assertFalse(this.newWith().notEmpty());
    }

    @Test
    default void RichIterable_getFirst_empty_null()
    {
        assertNull(this.newWith().getFirst());
    }

    @Test
    default void RichIterable_getLast_empty_null()
    {
        assertNull(this.newWith().getLast());
    }

    @Test
    default void RichIterable_getFirst()
    {
        RichIterable<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        Integer first = iterable.getFirst();
        assertThat(first, isOneOf(3, 2, 1));
        assertEquals(iterable.iterator().next(), first);
    }

    @Test
    default void RichIterable_getLast()
    {
        RichIterable<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        Integer last = iterable.getLast();
        assertThat(last, isOneOf(3, 2, 1));
        Iterator<Integer> iterator = iterable.iterator();
        Integer iteratorLast = null;
        while (iterator.hasNext())
        {
            iteratorLast = iterator.next();
        }
        assertEquals(iteratorLast, last);
    }

    @Test
    default void RichIterable_getFirst_and_getLast()
    {
        RichIterable<Integer> iterable = this.newWith(3, 2, 1);
        assertNotEquals(iterable.getFirst(), iterable.getLast());
    }

    @Test
    default void RichIterable_contains()
    {
        assertTrue(this.newWith(3, 2, 1).contains(3));
        assertTrue(this.newWith(3, 2, 1).contains(2));
        assertTrue(this.newWith(3, 2, 1).contains(1));
        assertFalse(this.newWith(3, 2, 1).contains(0));
    }

    @Test
    default void RichIterable_containsAllIterable()
    {
        assertTrue(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(3)));
        assertTrue(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(3, 2, 1)));
        assertTrue(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(3, 3, 3)));
        assertTrue(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(3, 3, 3, 3, 2, 2, 2, 1, 1)));
        assertFalse(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(4)));
        assertFalse(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(4, 4, 5)));
        assertFalse(this.newWith(3, 2, 1).containsAllIterable(Lists.immutable.of(3, 2, 1, 0)));
    }

    @Test
    default void RichIterable_containsAll()
    {
        assertTrue(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(3)));
        assertTrue(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(3, 2, 1)));
        assertTrue(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(3, 3, 3)));
        assertTrue(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(3, 3, 3, 3, 2, 2, 2, 1, 1)));
        assertFalse(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(4)));
        assertFalse(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(4, 4, 5)));
        assertFalse(this.newWith(3, 2, 1).containsAll(Lists.mutable.of(3, 2, 1, 0)));
    }

    @Test
    default void RichIterable_containsAllArguments()
    {
        assertTrue(this.newWith(3, 2, 1).containsAllArguments(3));
        assertTrue(this.newWith(3, 2, 1).containsAllArguments(3, 2, 1));
        assertTrue(this.newWith(3, 2, 1).containsAllArguments(3, 3, 3));
        assertTrue(this.newWith(3, 2, 1).containsAllArguments(3, 3, 3, 3, 2, 2, 2, 1, 1));
        assertFalse(this.newWith(3, 2, 1).containsAllArguments(4));
        assertFalse(this.newWith(3, 2, 1).containsAllArguments(4, 4, 5));
        assertFalse(this.newWith(3, 2, 1).containsAllArguments(3, 2, 1, 0));
    }

    @Test
    default void RichIterable_iterator_iterationOrder()
    {
        MutableCollection<Integer> iterationOrder = this.newMutableForFilter();
        Iterator<Integer> iterator = this.getInstanceUnderTest().iterator();
        while (iterator.hasNext())
        {
            iterationOrder.add(iterator.next());
        }
        assertEquals(this.expectedIterationOrder(), iterationOrder);

        MutableCollection<Integer> expectedIterationOrder = this.expectedIterationOrder();
        MutableCollection<Integer> forEachWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().forEachWith((each, param) -> forEachWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, forEachWithIterationOrder);

        MutableCollection<Integer> forEachWithIndexIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().forEachWithIndex((each, index) -> forEachWithIndexIterationOrder.add(each));
        assertEquals(expectedIterationOrder, forEachWithIndexIterationOrder);
    }

    @Test
    default void RichIterable_iterationOrder()
    {
        MutableCollection<Integer> expectedIterationOrder = this.expectedIterationOrder();

        MutableCollection<Integer> selectIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().select(selectIterationOrder::add);
        assertEquals(expectedIterationOrder, selectIterationOrder);

        MutableCollection<Integer> selectTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().select(selectTargetIterationOrder::add, new HashBag<>());
        assertEquals(expectedIterationOrder, selectTargetIterationOrder);

        MutableCollection<Integer> selectWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().selectWith((each, param) -> selectWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, selectWithIterationOrder);

        MutableCollection<Integer> selectWithTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().selectWith((each, param) -> selectWithTargetIterationOrder.add(each), null, new HashBag<>());
        assertEquals(expectedIterationOrder, selectWithTargetIterationOrder);

        MutableCollection<Integer> rejectIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().reject(rejectIterationOrder::add);
        assertEquals(expectedIterationOrder, rejectIterationOrder);

        MutableCollection<Integer> rejectTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().reject(rejectTargetIterationOrder::add, new HashBag<>());
        assertEquals(expectedIterationOrder, rejectTargetIterationOrder);

        MutableCollection<Integer> rejectWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().rejectWith((each, param) -> rejectWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, rejectWithIterationOrder);

        MutableCollection<Integer> rejectWithTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().rejectWith((each, param) -> rejectWithTargetIterationOrder.add(each), null, new HashBag<>());
        assertEquals(expectedIterationOrder, rejectWithTargetIterationOrder);

        MutableCollection<Integer> partitionIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().partition(partitionIterationOrder::add);
        assertEquals(expectedIterationOrder, partitionIterationOrder);

        MutableCollection<Integer> partitionWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().partitionWith((each, param) -> partitionWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, partitionWithIterationOrder);

        MutableCollection<Integer> collectIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collect(collectIterationOrder::add);
        assertEquals(expectedIterationOrder, collectIterationOrder);

        MutableCollection<Integer> collectTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collect(collectTargetIterationOrder::add, new HashBag<>());
        assertEquals(expectedIterationOrder, collectTargetIterationOrder);

        MutableCollection<Integer> collectWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectWith((each, param) -> collectWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, collectWithIterationOrder);

        MutableCollection<Integer> collectWithTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectWith((each, param) -> collectWithTargetIterationOrder.add(each), null, new HashBag<>());
        assertEquals(expectedIterationOrder, collectWithTargetIterationOrder);

        MutableCollection<Integer> collectIfPredicateIterationOrder = this.newMutableForFilter();
        MutableCollection<Integer> collectIfFunctionIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectIf(collectIfPredicateIterationOrder::add, collectIfFunctionIterationOrder::add);
        assertEquals(expectedIterationOrder, collectIfPredicateIterationOrder);
        assertEquals(expectedIterationOrder, collectIfFunctionIterationOrder);

        MutableCollection<Integer> collectIfPredicateTargetIterationOrder = this.newMutableForFilter();
        MutableCollection<Integer> collectIfFunctionTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectIf(collectIfPredicateTargetIterationOrder::add, collectIfFunctionTargetIterationOrder::add, new HashBag<>());
        assertEquals(expectedIterationOrder, collectIfPredicateTargetIterationOrder);
        assertEquals(expectedIterationOrder, collectIfFunctionTargetIterationOrder);

        MutableCollection<Integer> collectBooleanIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectBoolean(collectBooleanIterationOrder::add);
        assertEquals(expectedIterationOrder, collectBooleanIterationOrder);

        MutableCollection<Integer> collectBooleanTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectBoolean(collectBooleanTargetIterationOrder::add, new BooleanHashBag());
        assertEquals(expectedIterationOrder, collectBooleanTargetIterationOrder);

        MutableCollection<Integer> collectByteIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectByte((Integer each) ->
        {
            collectByteIterationOrder.add(each);
            return (byte) 0;
        });
        assertEquals(expectedIterationOrder, collectByteIterationOrder);

        MutableCollection<Integer> collectByteTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectByte((Integer each) ->
        {
            collectByteTargetIterationOrder.add(each);
            return (byte) 0;
        }, new ByteHashBag());
        assertEquals(expectedIterationOrder, collectByteTargetIterationOrder);

        MutableCollection<Integer> collectCharIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectChar((Integer each) ->
        {
            collectCharIterationOrder.add(each);
            return ' ';
        });
        assertEquals(expectedIterationOrder, collectCharIterationOrder);

        MutableCollection<Integer> collectCharTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectChar((Integer each) ->
        {
            collectCharTargetIterationOrder.add(each);
            return ' ';
        }, new CharHashBag());
        assertEquals(expectedIterationOrder, collectCharTargetIterationOrder);

        MutableCollection<Integer> collectDoubleIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectDouble((Integer each) ->
        {
            collectDoubleIterationOrder.add(each);
            return 0.0;
        });
        assertEquals(expectedIterationOrder, collectDoubleIterationOrder);

        MutableCollection<Integer> collectDoubleTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectDouble((Integer each) ->
        {
            collectDoubleTargetIterationOrder.add(each);
            return 0.0;
        }, new DoubleHashBag());
        assertEquals(expectedIterationOrder, collectDoubleTargetIterationOrder);

        MutableCollection<Integer> collectFloatIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectFloat((Integer each) ->
        {
            collectFloatIterationOrder.add(each);
            return 0.0f;
        });
        assertEquals(expectedIterationOrder, collectFloatIterationOrder);

        MutableCollection<Integer> collectFloatTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectFloat((Integer each) ->
        {
            collectFloatTargetIterationOrder.add(each);
            return 0.0f;
        }, new FloatHashBag());
        assertEquals(expectedIterationOrder, collectFloatTargetIterationOrder);

        MutableCollection<Integer> collectIntIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectInt((Integer each) ->
        {
            collectIntIterationOrder.add(each);
            return 0;
        });
        assertEquals(expectedIterationOrder, collectIntIterationOrder);

        MutableCollection<Integer> collectIntTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectInt((Integer each) ->
        {
            collectIntTargetIterationOrder.add(each);
            return 0;
        }, new IntHashBag());
        assertEquals(expectedIterationOrder, collectIntTargetIterationOrder);

        MutableCollection<Integer> collectLongIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectLong((Integer each) ->
        {
            collectLongIterationOrder.add(each);
            return 0L;
        });
        assertEquals(expectedIterationOrder, collectLongIterationOrder);

        MutableCollection<Integer> collectLongTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectLong((Integer each) ->
        {
            collectLongTargetIterationOrder.add(each);
            return 0L;
        }, new LongHashBag());
        assertEquals(expectedIterationOrder, collectLongTargetIterationOrder);

        MutableCollection<Integer> collectShortIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectShort(new ShortFunction<Integer>()
        {
            @Override
            public short shortValueOf(Integer each)
            {
                collectShortIterationOrder.add(each);
                return 0;
            }
        });
        assertEquals(expectedIterationOrder, collectShortIterationOrder);

        MutableCollection<Integer> collectShortTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().collectShort(new ShortFunction<Integer>()
        {
            @Override
            public short shortValueOf(Integer each)
            {
                collectShortTargetIterationOrder.add(each);
                return 0;
            }
        }, new ShortHashBag());
        assertEquals(expectedIterationOrder, collectShortTargetIterationOrder);

        MutableCollection<Integer> flatCollectIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().flatCollect(each -> Lists.immutable.with(flatCollectIterationOrder.add(each)));
        assertEquals(expectedIterationOrder, flatCollectIterationOrder);

        MutableCollection<Integer> flatCollectTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().flatCollect(each -> Lists.immutable.with(flatCollectTargetIterationOrder.add(each)), new HashBag<>());
        assertEquals(expectedIterationOrder, flatCollectTargetIterationOrder);

        MutableCollection<Integer> countIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().count(countIterationOrder::add);
        assertEquals(expectedIterationOrder, countIterationOrder);

        MutableCollection<Integer> countWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().countWith((each, param) -> countWithIterationOrder.add(each), null);
        assertEquals(expectedIterationOrder, countWithIterationOrder);

        MutableCollection<Integer> anySatisfyIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().anySatisfy(each -> {
            anySatisfyIterationOrder.add(each);
            return false;
        });
        assertEquals(expectedIterationOrder, anySatisfyIterationOrder);

        MutableCollection<Integer> anySatisfyWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().anySatisfyWith((each, param) -> {
            anySatisfyWithIterationOrder.add(each);
            return false;
        }, null);
        assertEquals(expectedIterationOrder, anySatisfyWithIterationOrder);

        MutableCollection<Integer> allSatisfyIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().allSatisfy(each -> {
            allSatisfyIterationOrder.add(each);
            return true;
        });
        assertEquals(expectedIterationOrder, allSatisfyIterationOrder);

        MutableCollection<Integer> allSatisfyWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().allSatisfyWith((each, param) -> {
            allSatisfyWithIterationOrder.add(each);
            return true;
        }, null);
        assertEquals(expectedIterationOrder, allSatisfyWithIterationOrder);

        MutableCollection<Integer> noneSatisfyIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().noneSatisfy(each -> {
            noneSatisfyIterationOrder.add(each);
            return false;
        });
        assertEquals(expectedIterationOrder, noneSatisfyIterationOrder);

        MutableCollection<Integer> noneSatisfyWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().noneSatisfyWith((each, param) -> {
            noneSatisfyWithIterationOrder.add(each);
            return false;
        }, null);
        assertEquals(expectedIterationOrder, noneSatisfyWithIterationOrder);

        MutableCollection<Integer> detectIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().detect(each -> {
            detectIterationOrder.add(each);
            return false;
        });
        assertEquals(expectedIterationOrder, detectIterationOrder);

        MutableCollection<Integer> detectWithIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().detectWith((each, param) -> {
            detectWithIterationOrder.add(each);
            return false;
        }, null);
        assertEquals(expectedIterationOrder, detectWithIterationOrder);

        MutableCollection<Integer> detectIfNoneIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().detectIfNone(each -> {
            detectIfNoneIterationOrder.add(each);
            return false;
        }, () -> 0);
        assertEquals(expectedIterationOrder, detectIfNoneIterationOrder);

        MutableCollection<Integer> detectWithIfNoneIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().detectWithIfNone((each, param) -> {
            detectWithIfNoneIterationOrder.add(each);
            return false;
        }, null, () -> 0);
        assertEquals(expectedIterationOrder, detectWithIfNoneIterationOrder);

        MutableCollection<Integer> minComparatorIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().min(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                if (minComparatorIterationOrder.isEmpty())
                {
                    minComparatorIterationOrder.add(o2);
                }
                minComparatorIterationOrder.add(o1);
                return 0;
            }
        });
        assertEquals(expectedIterationOrder, minComparatorIterationOrder);

        MutableCollection<Integer> maxComparatorIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().max(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                if (maxComparatorIterationOrder.isEmpty())
                {
                    maxComparatorIterationOrder.add(o2);
                }
                maxComparatorIterationOrder.add(o1);
                return 0;
            }
        });
        assertEquals(expectedIterationOrder, maxComparatorIterationOrder);

        MutableCollection<Integer> minByIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().minBy(minByIterationOrder::add);
        assertEquals(expectedIterationOrder, minByIterationOrder);

        MutableCollection<Integer> maxByIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().maxBy(maxByIterationOrder::add);
        assertEquals(expectedIterationOrder, maxByIterationOrder);

        MutableCollection<Integer> groupByIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().groupBy(groupByIterationOrder::add);
        assertEquals(expectedIterationOrder, groupByIterationOrder);

        MutableCollection<Integer> groupByTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().groupBy(groupByTargetIterationOrder::add, new HashBagMultimap<>());
        assertEquals(expectedIterationOrder, groupByTargetIterationOrder);

        MutableCollection<Integer> groupByEachIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().groupByEach(each -> {
            groupByEachIterationOrder.add(each);
            return Lists.immutable.with(each);
        });
        assertEquals(expectedIterationOrder, groupByEachIterationOrder);

        MutableCollection<Integer> groupByEachTargetIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().groupByEach(each -> {
            groupByEachTargetIterationOrder.add(each);
            return Lists.immutable.with(each);
        }, new HashBagMultimap<Integer, Integer>());
        assertEquals(expectedIterationOrder, groupByEachTargetIterationOrder);

        MutableCollection<Integer> sumOfFloatIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().sumOfFloat(each -> {
            sumOfFloatIterationOrder.add(each);
            return each.floatValue();
        });
        assertEquals(expectedIterationOrder, sumOfFloatIterationOrder);

        MutableCollection<Integer> sumOfDoubleIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().sumOfDouble(each -> {
            sumOfDoubleIterationOrder.add(each);
            return each.doubleValue();
        });
        assertEquals(expectedIterationOrder, sumOfDoubleIterationOrder);

        MutableCollection<Integer> sumOfIntIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().sumOfInt(each -> {
            sumOfIntIterationOrder.add(each);
            return each.intValue();
        });
        assertEquals(expectedIterationOrder, sumOfIntIterationOrder);

        MutableCollection<Integer> sumOfLongIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().sumOfLong(each -> {
            sumOfLongIterationOrder.add(each);
            return each.longValue();
        });
        assertEquals(expectedIterationOrder, sumOfLongIterationOrder);

        MutableCollection<Integer> injectIntoIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().injectInto(0, new Function2<Integer, Integer, Integer>()
        {
            public Integer value(Integer argument1, Integer argument2)
            {
                injectIntoIterationOrder.add(argument2);
                return argument1 + argument2;
            }
        });
        assertEquals(this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), injectIntoIterationOrder);

        MutableCollection<Integer> injectIntoIntIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().injectInto(0, new IntObjectToIntFunction<Integer>()
        {
            public int intValueOf(int intParameter, Integer objectParameter)
            {
                injectIntoIntIterationOrder.add(objectParameter);
                return intParameter + objectParameter;
            }
        });
        assertEquals(this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), injectIntoIntIterationOrder);

        MutableCollection<Integer> injectIntoLongIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().injectInto(0L, new LongObjectToLongFunction<Integer>()
        {
            public long longValueOf(long longParameter, Integer objectParameter)
            {
                injectIntoLongIterationOrder.add(objectParameter);
                return longParameter + objectParameter;
            }
        });
        assertEquals(this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), injectIntoLongIterationOrder);

        MutableCollection<Integer> injectIntoDoubleIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().injectInto(0L, new DoubleObjectToDoubleFunction<Integer>()
        {
            public double doubleValueOf(double doubleParameter, Integer objectParameter)
            {
                injectIntoDoubleIterationOrder.add(objectParameter);
                return doubleParameter + objectParameter;
            }
        });
        assertEquals(this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), injectIntoDoubleIterationOrder);

        MutableCollection<Integer> injectIntoFloatIterationOrder = this.newMutableForFilter();
        this.getInstanceUnderTest().injectInto(0L, new FloatObjectToFloatFunction<Integer>()
        {
            public float floatValueOf(float floatParameter, Integer objectParameter)
            {
                injectIntoFloatIterationOrder.add(objectParameter);
                return floatParameter + objectParameter;
            }
        });
        assertEquals(this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), injectIntoFloatIterationOrder);
    }

    default MutableCollection<Integer> expectedIterationOrder()
    {
        MutableCollection<Integer> forEach = this.newMutableForFilter();
        this.getInstanceUnderTest().forEach(Procedures.cast(forEach::add));
        return forEach;
    }

    default RichIterable<Integer> getInstanceUnderTest()
    {
        return this.allowsDuplicates()
                ? this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1)
                : this.newWith(4, 3, 2, 1);
    }

    @Test
    default void RichIterable_select()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 4, 4, 4, 2, 2),
                iterable.select(IntegerPredicates.isEven()));
    }

    @Test
    default void RichIterable_select_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.newMutableForFilter(4, 4, 4, 4, 2, 2),
                iterable.select(IntegerPredicates.isEven(), this.<Integer>newMutableForFilter()));
    }

    @Test
    default void RichIterable_selectWith()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 4, 4, 4, 3, 3, 3),
                iterable.selectWith(Predicates2.greaterThan(), 2));
    }

    @Test
    default void RichIterable_selectWith_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3),
                iterable.selectWith(Predicates2.<Integer>greaterThan(), 2, this.<Integer>newMutableForFilter()));
    }

    @Test
    default void RichIterable_reject()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 4, 4, 4, 2, 2),
                iterable.reject(IntegerPredicates.isOdd()));
    }

    @Test
    default void RichIterable_reject_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.newMutableForFilter(4, 4, 4, 4, 2, 2),
                iterable.reject(IntegerPredicates.isOdd(), this.<Integer>newMutableForFilter()));
    }

    @Test
    default void RichIterable_rejectWith()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.getExpectedFiltered(4, 4, 4, 4, 3, 3, 3),
                iterable.rejectWith(Predicates2.lessThan(), 3));
    }

    @Test
    default void RichIterable_rejectWith_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(
                this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3),
                iterable.rejectWith(Predicates2.<Integer>lessThan(), 3, this.<Integer>newMutableForFilter()));
    }

    @Test
    default void RichIterable_partition()
    {
        RichIterable<Integer> iterable = this.newWith(-3, -3, -3, -2, -2, -1, 0, 1, 2, 2, 3, 3, 3);
        PartitionIterable<Integer> result = iterable.partition(IntegerPredicates.isEven());
        assertEquals(this.getExpectedFiltered(-2, -2, 0, 2, 2), result.getSelected());
        assertEquals(this.getExpectedFiltered(-3, -3, -3, -1, 1, 3, 3, 3), result.getRejected());
    }

    @Test
    default void RichIterable_partitionWith()
    {
        RichIterable<Integer> iterable = this.newWith(-3, -3, -3, -2, -2, -1, 0, 1, 2, 2, 3, 3, 3);
        PartitionIterable<Integer> result = iterable.partitionWith(Predicates2.greaterThan(), 0);
        assertEquals(this.getExpectedFiltered(1, 2, 2, 3, 3, 3), result.getSelected());
        assertEquals(this.getExpectedFiltered(-3, -3, -3, -2, -2, -1, 0), result.getRejected());
    }

    @Test
    default void RichIterable_selectInstancesOf()
    {
        RichIterable<Number> iterable = this.<Number>newWith(1, 2.0, 2.0, 3, 3, 3, 4.0, 4.0, 4.0, 4.0);
        assertEquals(this.getExpectedFiltered(1, 3, 3, 3), iterable.selectInstancesOf(Integer.class));
        assertEquals(this.getExpectedFiltered(1, 2.0, 2.0, 3, 3, 3, 4.0, 4.0, 4.0, 4.0), iterable.selectInstancesOf(Number.class));
    }

    @Test
    default void RichIterable_collect()
    {
        assertEquals(
                this.getExpectedTransformed(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collect(i -> i % 10));
    }

    @Test
    default void RichIterable_collect_target()
    {
        assertEquals(
                this.newMutableForTransform(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collect(i -> i % 10, this.newMutableForTransform()));
    }

    @Test
    default void RichIterable_collectWith()
    {
        assertEquals(
                this.getExpectedTransformed(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectWith((i, mod) -> i % mod, 10));
    }

    @Test
    default void RichIterable_collectWith_target()
    {
        assertEquals(
                this.newMutableForTransform(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectWith((i, mod) -> i % mod, 10, this.newMutableForTransform()));
    }

    @Test
    default void RichIterable_collectIf()
    {
        assertEquals(
                this.getExpectedTransformed(3, 3, 1, 1, 3, 3, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectIf(i -> i % 2 != 0, i -> i % 10));
    }

    @Test
    default void RichIterable_collectIf_target()
    {
        assertEquals(
                this.newMutableForTransform(3, 3, 1, 1, 3, 3, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectIf(i -> i % 2 != 0, i -> i % 10, this.newMutableForTransform()));
    }

    @Test
    default void RichIterable_collectBoolean()
    {
        assertEquals(
                this.getExpectedBoolean(false, false, true, true, false, false),
                this.newWith(3, 3, 2, 2, 1, 1).collectBoolean(each -> each % 2 == 0));
    }

    @Test
    default void RichIterable_collectBoolean_target()
    {
        assertEquals(
                this.newBooleanForTransform(false, false, true, true, false, false),
                this.newWith(3, 3, 2, 2, 1, 1).collectBoolean(each -> each % 2 == 0, this.newBooleanForTransform()));
    }

    @Test
    default void RichIterable_collectByte()
    {
        assertEquals(
                this.getExpectedByte((byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectByte(each -> (byte) (each % 10)));
    }

    @Test
    default void RichIterable_collectByte_target()
    {
        assertEquals(
                this.newByteForTransform((byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectByte(each -> (byte) (each % 10), this.newByteForTransform()));
    }

    @Test
    default void RichIterable_collectChar()
    {
        assertEquals(
                this.getExpectedChar((char) 3, (char) 3, (char) 2, (char) 2, (char) 1, (char) 1, (char) 3, (char) 3, (char) 2, (char) 2, (char) 1, (char) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectChar(each -> (char) (each % 10)));
    }

    @Test
    default void RichIterable_collectChar_target()
    {
        assertEquals(
                this.newCharForTransform((char) 3, (char) 3, (char) 2, (char) 2, (char) 1, (char) 1, (char) 3, (char) 3, (char) 2, (char) 2, (char) 1, (char) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectChar(each -> (char) (each % 10), this.newCharForTransform()));
    }

    @Test
    default void RichIterable_collectDouble()
    {
        assertEquals(
                this.getExpectedDouble(3.0, 3.0, 2.0, 2.0, 1.0, 1.0, 3.0, 3.0, 2.0, 2.0, 1.0, 1.0),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectDouble(each -> (double) (each % 10)));
    }

    @Test
    default void RichIterable_collectDouble_target()
    {
        assertEquals(
                this.newDoubleForTransform(3.0, 3.0, 2.0, 2.0, 1.0, 1.0, 3.0, 3.0, 2.0, 2.0, 1.0, 1.0),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectDouble(each -> (double) (each % 10), this.newDoubleForTransform()));
    }

    @Test
    default void RichIterable_collectFloat()
    {
        assertEquals(
                this.getExpectedFloat(3.0f, 3.0f, 2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f, 2.0f, 2.0f, 1.0f, 1.0f),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectFloat(each -> (float) (each % 10)));
    }

    @Test
    default void RichIterable_collectFloat_target()
    {
        assertEquals(
                this.newFloatForTransform(3.0f, 3.0f, 2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f, 2.0f, 2.0f, 1.0f, 1.0f),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectFloat(each -> (float) (each % 10), this.newFloatForTransform()));
    }

    @Test
    default void RichIterable_collectInt()
    {
        assertEquals(
                this.getExpectedInt(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectInt(each -> each % 10));
    }

    @Test
    default void RichIterable_collectInt_target()
    {
        assertEquals(
                this.newIntForTransform(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectInt(each -> each % 10, this.newIntForTransform()));
    }

    @Test
    default void RichIterable_collectLong()
    {
        assertEquals(
                this.getExpectedLong(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectLong(each -> each % 10));
    }

    @Test
    default void RichIterable_collectLong_target()
    {
        assertEquals(
                this.newLongForTransform(3, 3, 2, 2, 1, 1, 3, 3, 2, 2, 1, 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectLong(each -> each % 10, this.newLongForTransform()));
    }

    @Test
    default void RichIterable_collectShort()
    {
        assertEquals(
                this.getExpectedShort((short) 3, (short) 3, (short) 2, (short) 2, (short) 1, (short) 1, (short) 3, (short) 3, (short) 2, (short) 2, (short) 1, (short) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectShort(each -> (short) (each % 10)));
    }

    @Test
    default void RichIterable_collectShort_target()
    {
        assertEquals(
                this.newShortForTransform((short) 3, (short) 3, (short) 2, (short) 2, (short) 1, (short) 1, (short) 3, (short) 3, (short) 2, (short) 2, (short) 1, (short) 1),
                this.newWith(13, 13, 12, 12, 11, 11, 3, 3, 2, 2, 1, 1).collectShort(each -> (short) (each % 10), this.newShortForTransform()));
    }

    @Test
    default void RichIterable_flatCollect()
    {
        assertEquals(
                this.getExpectedTransformed(1, 2, 3, 1, 2, 1, 2, 1),
                this.newWith(3, 2, 2, 1).flatCollect(Interval::oneTo));
    }

    @Test
    default void RichIterable_flatCollect_target()
    {
        assertEquals(
                this.newMutableForTransform(1, 2, 3, 1, 2, 1, 2, 1),
                this.newWith(3, 2, 2, 1).flatCollect(Interval::oneTo, this.newMutableForTransform()));
    }

    @Test
    default void RichIterable_count()
    {
        assertEquals(3, this.newWith(3, 3, 3, 2, 2, 1).count(Integer.valueOf(3)::equals));
        assertEquals(2, this.newWith(3, 3, 3, 2, 2, 1).count(Integer.valueOf(2)::equals));
        assertEquals(1, this.newWith(3, 3, 3, 2, 2, 1).count(Integer.valueOf(1)::equals));
        assertEquals(0, this.newWith(3, 3, 3, 2, 2, 1).count(Integer.valueOf(0)::equals));
        assertEquals(4, this.newWith(3, 3, 3, 2, 2, 1).count(i -> i % 2 != 0));
        assertEquals(6, this.newWith(3, 3, 3, 2, 2, 1).count(i -> i > 0));
    }

    @Test
    default void RichIterable_countWith()
    {
        assertEquals(3, this.newWith(3, 3, 3, 2, 2, 1).countWith(Object::equals, 3));
        assertEquals(2, this.newWith(3, 3, 3, 2, 2, 1).countWith(Object::equals, 2));
        assertEquals(1, this.newWith(3, 3, 3, 2, 2, 1).countWith(Object::equals, 1));
        assertEquals(0, this.newWith(3, 3, 3, 2, 2, 1).countWith(Object::equals, 0));
        assertEquals(6, this.newWith(3, 3, 3, 2, 2, 1).countWith(Predicates2.greaterThan(), 0));
    }

    @Test
    default void RichIterable_anySatisfy()
    {
        assertTrue(this.newWith(3, 2, 1).anySatisfy(Predicates.greaterThan(0)));
        assertTrue(this.newWith(3, 2, 1).anySatisfy(Predicates.greaterThan(1)));
        assertTrue(this.newWith(3, 2, 1).anySatisfy(Predicates.greaterThan(2)));
        assertFalse(this.newWith(3, 2, 1).anySatisfy(Predicates.greaterThan(3)));
    }

    @Test
    default void RichIterable_anySatisfyWith()
    {
        assertTrue(this.newWith(3, 2, 1).anySatisfyWith(Predicates2.greaterThan(), 0));
        assertTrue(this.newWith(3, 2, 1).anySatisfyWith(Predicates2.greaterThan(), 1));
        assertTrue(this.newWith(3, 2, 1).anySatisfyWith(Predicates2.greaterThan(), 2));
        assertFalse(this.newWith(3, 2, 1).anySatisfyWith(Predicates2.greaterThan(), 3));
    }

    @Test
    default void RichIterable_allSatisfy()
    {
        assertTrue(this.newWith(3, 2, 1).allSatisfy(Predicates.greaterThan(0)));
        assertFalse(this.newWith(3, 2, 1).allSatisfy(Predicates.greaterThan(1)));
        assertFalse(this.newWith(3, 2, 1).allSatisfy(Predicates.greaterThan(2)));
        assertFalse(this.newWith(3, 2, 1).allSatisfy(Predicates.greaterThan(3)));
    }

    @Test
    default void RichIterable_allSatisfyWith()
    {
        assertTrue(this.newWith(3, 2, 1).allSatisfyWith(Predicates2.greaterThan(), 0));
        assertFalse(this.newWith(3, 2, 1).allSatisfyWith(Predicates2.greaterThan(), 1));
        assertFalse(this.newWith(3, 2, 1).allSatisfyWith(Predicates2.greaterThan(), 2));
        assertFalse(this.newWith(3, 2, 1).allSatisfyWith(Predicates2.greaterThan(), 3));
    }

    @Test
    default void RichIterable_noneSatisfy()
    {
        assertFalse(this.newWith(3, 2, 1).noneSatisfy(Predicates.greaterThan(0)));
        assertFalse(this.newWith(3, 2, 1).noneSatisfy(Predicates.greaterThan(1)));
        assertFalse(this.newWith(3, 2, 1).noneSatisfy(Predicates.greaterThan(2)));
        assertTrue(this.newWith(3, 2, 1).noneSatisfy(Predicates.greaterThan(3)));
    }

    @Test
    default void RichIterable_noneSatisfyWith()
    {
        assertFalse(this.newWith(3, 2, 1).noneSatisfyWith(Predicates2.greaterThan(), 0));
        assertFalse(this.newWith(3, 2, 1).noneSatisfyWith(Predicates2.greaterThan(), 1));
        assertFalse(this.newWith(3, 2, 1).noneSatisfyWith(Predicates2.greaterThan(), 2));
        assertTrue(this.newWith(3, 2, 1).noneSatisfyWith(Predicates2.greaterThan(), 3));
    }

    @Test
    default void RichIterable_detect()
    {
        assertThat(this.newWith(3, 2, 1).detect(Predicates.greaterThan(0)), is(3));
        assertThat(this.newWith(3, 2, 1).detect(Predicates.greaterThan(1)), is(3));
        assertThat(this.newWith(3, 2, 1).detect(Predicates.greaterThan(2)), is(3));
        assertThat(this.newWith(3, 2, 1).detect(Predicates.greaterThan(3)), nullValue());

        assertThat(this.newWith(3, 2, 1).detect(Predicates.lessThan(1)), nullValue());
        assertThat(this.newWith(3, 2, 1).detect(Predicates.lessThan(2)), is(1));
        assertThat(this.newWith(3, 2, 1).detect(Predicates.lessThan(3)), is(2));
        assertThat(this.newWith(3, 2, 1).detect(Predicates.lessThan(4)), is(3));
    }

    @Test
    default void RichIterable_detectWith()
    {
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.greaterThan(), 0), is(3));
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.greaterThan(), 1), is(3));
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.greaterThan(), 2), is(3));
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.greaterThan(), 3), nullValue());

        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.lessThan(), 1), nullValue());
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.lessThan(), 2), is(1));
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.lessThan(), 3), is(2));
        assertThat(this.newWith(3, 2, 1).detectWith(Predicates2.lessThan(), 4), is(3));
    }

    @Test
    default void RichIterable_detectIfNone()
    {
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.greaterThan(0), () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.greaterThan(1), () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.greaterThan(2), () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.greaterThan(3), () -> 4), is(4));

        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.lessThan(1), () -> 4), is(4));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.lessThan(2), () -> 4), is(1));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.lessThan(3), () -> 4), is(2));
        assertThat(this.newWith(3, 2, 1).detectIfNone(Predicates.lessThan(4), () -> 4), is(3));
    }

    @Test
    default void RichIterable_detectWithIfNone()
    {
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.greaterThan(), 0, () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.greaterThan(), 1, () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.greaterThan(), 2, () -> 4), is(3));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.greaterThan(), 3, () -> 4), is(4));

        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.lessThan(), 1, () -> 4), is(4));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.lessThan(), 2, () -> 4), is(1));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.lessThan(), 3, () -> 4), is(2));
        assertThat(this.newWith(3, 2, 1).detectWithIfNone(Predicates2.lessThan(), 4, () -> 4), is(3));
    }

    @Test
    default void RichIterable_min()
    {
        assertEquals(Integer.valueOf(-1), this.newWith(-1, 0, 1).min());
        assertEquals(Integer.valueOf(-1), this.newWith(1, 0, -1).min());

        assertThrows(NoSuchElementException.class, () -> this.newWith().min());
    }

    @Test
    default void RichIterable_min_non_comparable()
    {
        Object sentinel = new Object();
        assertSame(sentinel, this.newWith(sentinel).min());

        assertThrows(ClassCastException.class, () -> this.newWith(new Object(), new Object()).min());
    }

    @Test
    default void RichIterable_max()
    {
        assertEquals(Integer.valueOf(1), this.newWith(-1, 0, 1).max());
        assertEquals(Integer.valueOf(1), this.newWith(1, 0, -1).max());

        assertThrows(NoSuchElementException.class, () -> this.newWith().max());
    }

    @Test
    default void RichIterable_max_non_comparable()
    {
        Object sentinel = new Object();
        assertSame(sentinel, this.newWith(sentinel).max());

        assertThrows(ClassCastException.class, () -> this.newWith(new Object(), new Object()).max());
    }

    @Test
    default void RichIterable_min_comparator()
    {
        assertEquals(Integer.valueOf(1), this.newWith(-1, 0, 1).min(Comparators.reverseNaturalOrder()));
        assertEquals(Integer.valueOf(1), this.newWith(1, 0, -1).min(Comparators.reverseNaturalOrder()));

        assertThrows(NoSuchElementException.class, () -> this.newWith().min(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void RichIterable_min_comparator_non_comparable()
    {
        Object sentinel = new Object();
        assertSame(sentinel, this.newWith(sentinel).min(Comparators.reverseNaturalOrder()));

        assertThrows(ClassCastException.class, () -> this.newWith(new Object(), new Object()).min(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void RichIterable_max_comparator()
    {
        assertEquals(Integer.valueOf(-1), this.newWith(-1, 0, 1).max(Comparators.reverseNaturalOrder()));
        assertEquals(Integer.valueOf(-1), this.newWith(1, 0, -1).max(Comparators.reverseNaturalOrder()));

        assertThrows(NoSuchElementException.class, () -> this.newWith().max(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void RichIterable_max_comparator_non_comparable()
    {
        Object sentinel = new Object();
        assertSame(sentinel, this.newWith(sentinel).max(Comparators.reverseNaturalOrder()));

        assertThrows(ClassCastException.class, () -> this.newWith(new Object(), new Object()).max(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void RichIterable_minBy()
    {
        assertEquals("da", this.newWith("ed", "da", "ca", "bc", "ab").minBy(string -> string.charAt(string.length() - 1)));

        assertThrows(NoSuchElementException.class, () -> this.<String>newWith().minBy(string -> string.charAt(string.length() - 1)));
    }

    @Test
    default void RichIterable_maxBy()
    {
        assertEquals("dz", this.newWith("ew", "dz", "cz", "bx", "ay").maxBy(string -> string.charAt(string.length() - 1)));

        assertThrows(NoSuchElementException.class, () -> this.<String>newWith().maxBy(string -> string.charAt(string.length() - 1)));
    }

    @Test
    default void RichIterable_groupBy()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        Function<Integer, Boolean> isOddFunction = object -> IntegerPredicates.isOdd().accept(object);

        MutableMap<Boolean, RichIterable<Integer>> expected =
                UnifiedMap.newWithKeysValues(
                        Boolean.TRUE, this.newMutableForFilter(3, 3, 3, 1),
                        Boolean.FALSE, this.newMutableForFilter(4, 4, 4, 4, 2, 2));

        Multimap<Boolean, Integer> multimap = iterable.groupBy(isOddFunction);
        assertEquals(expected, multimap.toMap());

        Function<Integer, Boolean> function = (Integer object) -> true;
        MutableMultimap<Boolean, Integer> multimap2 = iterable.groupBy(
                isOddFunction,
                this.<Integer>newWith().groupBy(function).toMutable());
        assertEquals(expected, multimap2.toMap());
    }

    @Test
    default void RichIterable_groupByEach()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);

        Function<Integer, Iterable<Integer>> function = integer -> Interval.fromTo(-1, -integer);

        MutableMap<Integer, RichIterable<Integer>> expected =
                UnifiedMap.newWithKeysValues(
                        -4, this.newMutableForFilter(4, 4, 4, 4),
                        -3, this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3),
                        -2, this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2),
                        -1, this.newMutableForFilter(4, 4, 4, 4, 3, 3, 3, 2, 2, 1));

        Multimap<Integer, Integer> multimap = iterable.groupByEach(function);
        assertEquals(expected, multimap.toMap());

        Multimap<Integer, Integer> actualWithTarget =
                iterable.groupByEach(function, this.<Integer>newWith().groupByEach(function).toMutable());
        assertEquals(expected, actualWithTarget.toMap());
    }

    @Test
    default void RichIterable_aggregateBy_aggregateInPlaceBy()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);

        MapIterable<String, Integer> aggregateBy = iterable.aggregateBy(
                Object::toString,
                () -> 0,
                (integer1, integer2) -> integer1 + integer2);

        assertEquals(16, aggregateBy.get("4").intValue());
        assertEquals(9, aggregateBy.get("3").intValue());
        assertEquals(4, aggregateBy.get("2").intValue());
        assertEquals(1, aggregateBy.get("1").intValue());

        MapIterable<String, AtomicInteger> aggregateInPlaceBy = iterable.aggregateInPlaceBy(
                String::valueOf,
                AtomicInteger::new,
                AtomicInteger::addAndGet);
        assertEquals(16, aggregateInPlaceBy.get("4").intValue());
        assertEquals(9, aggregateInPlaceBy.get("3").intValue());
        assertEquals(4, aggregateInPlaceBy.get("2").intValue());
        assertEquals(1, aggregateInPlaceBy.get("1").intValue());
    }

    @Test
    default void RichIterable_sumOfPrimitive()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        Assert.assertEquals(30.0f, iterable.sumOfFloat(Integer::floatValue), 0.001);
        Assert.assertEquals(30.0, iterable.sumOfDouble(Integer::doubleValue), 0.001);
        Assert.assertEquals(30, iterable.sumOfInt(integer -> integer));
        Assert.assertEquals(30L, iterable.sumOfLong(Integer::longValue));
    }

    @Test
    default void RichIterable_injectInto()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        assertEquals(Integer.valueOf(31), iterable.injectInto(1, AddFunction.INTEGER));
        assertEquals(Integer.valueOf(30), iterable.injectInto(0, AddFunction.INTEGER));
    }

    @Test
    default void RichIterable_injectInto_primitive()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);

        Assert.assertEquals(31, iterable.injectInto(1, AddFunction.INTEGER_TO_INT));
        Assert.assertEquals(30, iterable.injectInto(0, AddFunction.INTEGER_TO_INT));

        Assert.assertEquals(31L, iterable.injectInto(1, AddFunction.INTEGER_TO_LONG));
        Assert.assertEquals(30L, iterable.injectInto(0, AddFunction.INTEGER_TO_LONG));

        Assert.assertEquals(31.0d, iterable.injectInto(1, AddFunction.INTEGER_TO_DOUBLE), 0.001);
        Assert.assertEquals(30.0d, iterable.injectInto(0, AddFunction.INTEGER_TO_DOUBLE), 0.001);

        Assert.assertEquals(31.0f, iterable.injectInto(1, AddFunction.INTEGER_TO_FLOAT), 0.001f);
        Assert.assertEquals(30.0f, iterable.injectInto(0, AddFunction.INTEGER_TO_FLOAT), 0.001f);
    }

    // TODO to(Sorted)?(List|Set|Bag|Map)(By)?

    class Holder<T extends Comparable<? super T>> implements Comparable<Holder<T>>
    {
        private final T field;

        public Holder(T field)
        {
            this.field = field;
        }

        @Override
        public int compareTo(Holder<T> other)
        {
            return this.field.compareTo(other.field);
        }
    }
}
