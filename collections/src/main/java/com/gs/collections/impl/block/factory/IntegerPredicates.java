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

import com.gs.collections.api.block.function.Function;

public final class IntegerPredicates
{
    private static final Predicates<Integer> IS_ODD = new IntegerIsOdd();
    private static final Predicates<Integer> IS_EVEN = new IntegerIsEven();
    private static final Predicates<Integer> IS_POSITIVE = new IntegerIsPositive();
    private static final Predicates<Integer> IS_NEGATIVE = new IntegerIsNegative();
    private static final Predicates<Integer> IS_ZERO = new IntegerIsZero();

    private IntegerPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Predicates<Integer> isOdd()
    {
        return IS_ODD;
    }

    public static Predicates<Integer> isEven()
    {
        return IS_EVEN;
    }

    public static Predicates<Integer> isPositive()
    {
        return IS_POSITIVE;
    }

    public static Predicates<Integer> isNegative()
    {
        return IS_NEGATIVE;
    }

    public static Predicates<Integer> isZero()
    {
        return IS_ZERO;
    }

    public static <T> Predicates<T> attributeIsEven(Function<T, Integer> function)
    {
        return Predicates.attributePredicate(function, IntegerPredicates.isEven());
    }

    public static <T> Predicates<T> attributeIsOdd(Function<T, Integer> function)
    {
        return Predicates.attributePredicate(function, IntegerPredicates.isOdd());
    }

    public static <T> Predicates<T> attributeIsZero(Function<T, Integer> function)
    {
        return Predicates.attributePredicate(function, IntegerPredicates.isZero());
    }

    public static <T> Predicates<T> attributeIsPositive(Function<T, Integer> function)
    {
        return Predicates.attributePredicate(function, IntegerPredicates.isPositive());
    }

    public static <T> Predicates<T> attributeIsNegative(Function<T, Integer> function)
    {
        return Predicates.attributePredicate(function, IntegerPredicates.isNegative());
    }

    private static class IntegerIsPositive extends Predicates<Integer>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Integer i)
        {
            return i.intValue() > 0;
        }
    }

    private static class IntegerIsNegative extends Predicates<Integer>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Integer i)
        {
            return i.intValue() < 0;
        }
    }

    private static class IntegerIsZero extends Predicates<Integer>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Integer i)
        {
            return i.intValue() == 0;
        }
    }

    private static class IntegerIsOdd extends Predicates<Integer>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Integer i)
        {
            return i.intValue() % 2 != 0;
        }
    }

    private static class IntegerIsEven extends Predicates<Integer>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Integer i)
        {
            return i.intValue() % 2 == 0;
        }
    }
}
