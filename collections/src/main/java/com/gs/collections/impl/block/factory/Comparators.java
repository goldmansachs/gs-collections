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

package com.gs.collections.impl.block.factory;

import java.util.Collection;
import java.util.Comparator;

import com.gs.collections.api.block.SerializableComparator;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.comparator.FunctionComparator;

public final class Comparators
{
    private static final SerializableComparator<?> NATURAL_ORDER_COMPARATOR = new NaturalOrderComparator();
    private static final SerializableComparator<?> REVERSE_NATURAL_ORDER_COMPARATOR = new ReverseComparator(NATURAL_ORDER_COMPARATOR);
    private static final SerializableComparator<?> POWER_SET_COMPARATOR = new PowerSetComparator();
    private static final SerializableComparator<Collection<?>> ASCENDING_COLLECTION_SIZE_COMPARATOR = new AscendingCollectionSizeComparator();
    private static final SerializableComparator<Collection<?>> DESCENDING_COLLECTION_SIZE_COMPARATOR = new DescendingCollectionSizeComparator();

    private Comparators()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Uses the natural compareTo methods of the objects which will throw if there are any nulls.
     */
    public static <T> SerializableComparator<T> naturalOrder()
    {
        return (SerializableComparator<T>) NATURAL_ORDER_COMPARATOR;
    }

    /**
     * Uses the natural compareTo methods of the objects which will throw if there are any nulls.
     */
    public static <T> SerializableComparator<T> reverseNaturalOrder()
    {
        return (SerializableComparator<T>) REVERSE_NATURAL_ORDER_COMPARATOR;
    }

    /**
     * @param comparator original comparator whose order will be reversed
     * @return A comparator that reverses the order of any other Serializable Comparator.
     */
    public static <T> SerializableComparator<T> reverse(Comparator<T> comparator)
    {
        if (comparator == null)
        {
            throw new NullPointerException();
        }
        return new ReverseComparator<T>(comparator);
    }

    public static <T> SerializableComparator<T> safeNullsLow(Comparator<T> notNullSafeComparator)
    {
        return new SafeNullsLowComparator<T>(notNullSafeComparator);
    }

    public static <T> SerializableComparator<T> safeNullsHigh(Comparator<T> notNullSafeComparator)
    {
        return new SafeNullsHighComparator<T>(notNullSafeComparator);
    }

    public static <T> SerializableComparator<T> chain(Comparator<T>... comparators)
    {
        if (comparators.length == 0)
        {
            throw new IllegalArgumentException("Nothing to chain");
        }

        return new ChainedComparator<T>(comparators);
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V> one)
    {
        return Comparators.byFunction(one);
    }

    public static <T, V1 extends Comparable<? super V1>, V2 extends Comparable<? super V2>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V1> one,
            Function<? super T, ? extends V2> two)
    {
        return Comparators.chain(
                Comparators.byFunction(one),
                Comparators.byFunction(two));
    }

    public static <T, V1 extends Comparable<? super V1>, V2 extends Comparable<? super V2>, V3 extends Comparable<? super V3>> SerializableComparator<T> fromFunctions(
            Function<? super T, ? extends V1> one,
            Function<? super T, ? extends V2> two,
            Function<? super T, ? extends V3> three)
    {
        return Comparators.chain(
                Comparators.byFunction(one),
                Comparators.byFunction(two),
                Comparators.byFunction(three));
    }

    public static <T> SerializableComparator<SortedSetIterable<T>> powerSet()
    {
        return (SerializableComparator<SortedSetIterable<T>>) POWER_SET_COMPARATOR;
    }

    public static SerializableComparator<Collection<?>> ascendingCollectionSizeComparator()
    {
        return ASCENDING_COLLECTION_SIZE_COMPARATOR;
    }

    public static SerializableComparator<Collection<?>> descendingCollectionSizeComparator()
    {
        return DESCENDING_COLLECTION_SIZE_COMPARATOR;
    }

    /**
     * Creates a comparator for pairs by using an existing comparator that only compares the first element of the pair
     *
     * @param comparator original comparator that compares the first element of the pair
     * @return A comparator that compares pairs only by their first element
     */
    public static <T> SerializableComparator<Pair<T, ?>> byFirstOfPair(Comparator<? super T> comparator)
    {
        return new ByFirstOfPairComparator<T>(comparator);
    }

    /**
     * Creates a comparator for pairs by using an existing comparator that only compares the second element of the pair
     *
     * @param comparator original comparator that compares the second element of the pair
     * @return A comparator that compares pairs only by their second element
     */
    public static <T> SerializableComparator<Pair<?, T>> bySecondOfPair(Comparator<? super T> comparator)
    {
        return new BySecondOfPairComparator<T>(comparator);
    }

    private static final class NaturalOrderComparator<T extends Comparable<T>> implements SerializableComparator<T>
    {
        private static final long serialVersionUID = 1L;

        public int compare(T o1, T o2)
        {
            if (o1 == null || o2 == null)
            {
                throw new NullPointerException();
            }
            return o1.compareTo(o2);
        }
    }

    private static final class ReverseComparator<T> implements SerializableComparator<T>
    {
        private static final long serialVersionUID = 1L;

        private final Comparator<T> comparator;

        private ReverseComparator(Comparator<T> comparator)
        {
            this.comparator = comparator;
        }

        public int compare(T o1, T o2)
        {
            return this.comparator.compare(o2, o1);
        }
    }

    private static final class SafeNullsLowComparator<T>
            implements SerializableComparator<T>
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<T> notNullSafeComparator;

        private SafeNullsLowComparator(Comparator<T> newNotNullSafeComparator)
        {
            this.notNullSafeComparator = newNotNullSafeComparator;
        }

        public int compare(T value1, T value2)
        {
            if (value1 != null && value2 != null)
            {
                return this.notNullSafeComparator.compare(value1, value2);
            }

            if (value1 == null && value2 == null)
            {
                return 0;
            }

            return value1 == null ? -1 : 1;
        }
    }

    private static final class SafeNullsHighComparator<T>
            implements SerializableComparator<T>
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<T> notNullSafeComparator;

        private SafeNullsHighComparator(Comparator<T> newNotNullSafeComparator)
        {
            this.notNullSafeComparator = newNotNullSafeComparator;
        }

        public int compare(T value1, T value2)
        {
            if (value1 != null && value2 != null)
            {
                return this.notNullSafeComparator.compare(value1, value2);
            }

            if (value1 == null && value2 == null)
            {
                return 0;
            }

            return value1 == null ? 1 : -1;
        }
    }

    private static final class ChainedComparator<T>
            implements SerializableComparator<T>
    {
        private static final long serialVersionUID = 1L;
        private final Comparator<T>[] comparators;

        private ChainedComparator(Comparator<T>[] comparators)
        {
            this.comparators = comparators;
        }

        public int compare(T value1, T value2)
        {
            for (Comparator<T> comparator : this.comparators)
            {
                int result = comparator.compare(value1, value2);
                if (result != 0)
                {
                    return result;
                }
            }
            return 0;
        }
    }

    private static final class PowerSetComparator<T> implements SerializableComparator<SortedSetIterable<T>>
    {
        private static final long serialVersionUID = 1L;

        public int compare(SortedSetIterable<T> setA, SortedSetIterable<T> setB)
        {
            int compareTo = Integer.valueOf(setA.size()).compareTo(setB.size());
            if (compareTo == 0)
            {
                return setA.compareTo(setB);
            }
            return compareTo;
        }
    }

    private static final class AscendingCollectionSizeComparator implements SerializableComparator<Collection<?>>
    {
        private static final long serialVersionUID = 1L;

        public int compare(Collection<?> c1, Collection<?> c2)
        {
            return c1.size() - c2.size();
        }
    }

    private static final class DescendingCollectionSizeComparator implements SerializableComparator<Collection<?>>
    {
        private static final long serialVersionUID = 1L;

        public int compare(Collection<?> c1, Collection<?> c2)
        {
            return c2.size() - c1.size();
        }
    }

    public static <T, V extends Comparable<? super V>> SerializableComparator<T> byFunction(Function<? super T, ? extends V> function)
    {
        if (function instanceof BooleanFunction)
        {
            return Functions.toBooleanComparator((BooleanFunction<T>) function);
        }
        if (function instanceof ByteFunction)
        {
            return Functions.toByteComparator((ByteFunction<T>) function);
        }
        if (function instanceof CharFunction)
        {
            return Functions.toCharComparator((CharFunction<T>) function);
        }
        if (function instanceof DoubleFunction)
        {
            return Functions.toDoubleComparator((DoubleFunction<T>) function);
        }
        if (function instanceof FloatFunction)
        {
            return Functions.toFloatComparator((FloatFunction<T>) function);
        }
        if (function instanceof IntFunction)
        {
            return Functions.toIntComparator((IntFunction<T>) function);
        }
        if (function instanceof LongFunction)
        {
            return Functions.toLongComparator((LongFunction<T>) function);
        }
        if (function instanceof ShortFunction)
        {
            return Functions.toShortComparator((ShortFunction<T>) function);
        }
        return Comparators.byFunction(function, naturalOrder());
    }

    public static <T> SerializableComparator<T> byBooleanFunction(BooleanFunction<T> function)
    {
        return Functions.toBooleanComparator(function);
    }

    public static <T> SerializableComparator<T> byByteFunction(ByteFunction<T> function)
    {
        return Functions.toByteComparator(function);
    }

    public static <T> SerializableComparator<T> byCharFunction(CharFunction<T> function)
    {
        return Functions.toCharComparator(function);
    }

    public static <T> SerializableComparator<T> byDoubleFunction(DoubleFunction<T> function)
    {
        return Functions.toDoubleComparator(function);
    }

    public static <T> SerializableComparator<T> byFloatFunction(FloatFunction<T> function)
    {
        return Functions.toFloatComparator(function);
    }

    public static <T> SerializableComparator<T> byIntFunction(IntFunction<T> function)
    {
        return Functions.toIntComparator(function);
    }

    public static <T> SerializableComparator<T> byLongFunction(LongFunction<T> function)
    {
        return Functions.toLongComparator(function);
    }

    public static <T> SerializableComparator<T> byShortFunction(ShortFunction<T> function)
    {
        return Functions.toShortComparator(function);
    }

    public static <T, V> SerializableComparator<T> byFunction(
            Function<? super T, ? extends V> function,
            Comparator<V> comparator)
    {
        return new FunctionComparator<T, V>(function, comparator);
    }

    public static boolean nullSafeEquals(Object value1, Object value2)
    {
        return value1 == null ? value2 == null : value1.equals(value2);
    }

    public static <T extends Comparable<T>> int nullSafeCompare(T value1, T value2)
    {
        if (value1 != null && value2 != null)
        {
            return value1.compareTo(value2);
        }

        if (value1 == null && value2 == null)
        {
            return 0;
        }

        return value1 == null ? -1 : 1;
    }

    private static final class ByFirstOfPairComparator<T> implements SerializableComparator<Pair<T, ?>>
    {
        private static final long serialVersionUID = 1L;

        private final Comparator<? super T> comparator;

        private ByFirstOfPairComparator(Comparator<? super T> comparator)
        {
            this.comparator = comparator;
        }

        public int compare(Pair<T, ?> p1, Pair<T, ?> p2)
        {
            return this.comparator.compare(p1.getOne(), p2.getOne());
        }
    }

    private static final class BySecondOfPairComparator<T> implements SerializableComparator<Pair<?, T>>
    {
        private static final long serialVersionUID = 1L;

        private final Comparator<? super T> comparator;

        private BySecondOfPairComparator(Comparator<? super T> comparator)
        {
            this.comparator = comparator;
        }

        public int compare(Pair<?, T> p1, Pair<?, T> p2)
        {
            return this.comparator.compare(p1.getTwo(), p2.getTwo());
        }
    }
}
