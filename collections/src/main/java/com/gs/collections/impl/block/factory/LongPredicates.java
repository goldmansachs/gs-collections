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

public final class LongPredicates
{
    private static final Predicates<Long> IS_ODD = new LongIsOdd();
    private static final Predicates<Long> IS_EVEN = new LongIsEven();
    private static final Predicates<Long> IS_POSITIVE = new LongIsPositive();
    private static final Predicates<Long> IS_NEGATIVE = new LongIsNegative();
    private static final Predicates<Long> IS_ZERO = new LongIsZero();

    private LongPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Predicates<Long> isOdd()
    {
        return IS_ODD;
    }

    public static Predicates<Long> isEven()
    {
        return IS_EVEN;
    }

    public static Predicates<Long> isPositive()
    {
        return IS_POSITIVE;
    }

    public static Predicates<Long> isNegative()
    {
        return IS_NEGATIVE;
    }

    public static Predicates<Long> isZero()
    {
        return IS_ZERO;
    }

    public static <T> Predicates<T> attributeIsEven(Function<T, Long> function)
    {
        return Predicates.attributePredicate(function, LongPredicates.isEven());
    }

    public static <T> Predicates<T> attributeIsOdd(Function<T, Long> function)
    {
        return Predicates.attributePredicate(function, LongPredicates.isOdd());
    }

    public static <T> Predicates<T> attributeIsZero(Function<T, Long> function)
    {
        return Predicates.attributePredicate(function, LongPredicates.isZero());
    }

    public static <T> Predicates<T> attributeIsPositive(Function<T, Long> function)
    {
        return Predicates.attributePredicate(function, LongPredicates.isPositive());
    }

    public static <T> Predicates<T> attributeIsNegative(Function<T, Long> function)
    {
        return Predicates.attributePredicate(function, LongPredicates.isNegative());
    }

    private static class LongIsOdd extends Predicates<Long>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Long l)
        {
            return l.longValue() % 2 != 0;
        }
    }

    private static class LongIsEven extends Predicates<Long>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Long l)
        {
            return l.longValue() % 2 == 0;
        }
    }

    private static class LongIsPositive extends Predicates<Long>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Long l)
        {
            return l.longValue() > 0;
        }
    }

    private static class LongIsNegative extends Predicates<Long>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Long l)
        {
            return l.longValue() < 0;
        }
    }

    private static class LongIsZero extends Predicates<Long>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(Long l)
        {
            return l.longValue() == 0;
        }
    }
}
