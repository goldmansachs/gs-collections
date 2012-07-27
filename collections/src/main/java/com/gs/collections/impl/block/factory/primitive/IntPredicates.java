/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.api.block.predicate.primitive.IntPredicate;

/**
 * Provides a set of common predicates for int values.
 */
public final class IntPredicates
{
    private IntPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("MisspelledEquals")
    public static IntPredicate equal(int expected)
    {
        return new EqualsIntPredicate(expected);
    }

    public static IntPredicate lessThan(int expected)
    {
        return new LessThanIntPredicate(expected);
    }

    public static IntPredicate greaterThan(int expected)
    {
        return new GreaterThanIntPredicate(expected);
    }

    private static final class EqualsIntPredicate implements IntPredicate
    {
        private static final long serialVersionUID = -2190061539884351412L;
        private final int expected;

        private EqualsIntPredicate(int expected)
        {
            this.expected = expected;
        }

        public boolean accept(int value)
        {
            return this.expected == value;
        }
    }

    private static final class LessThanIntPredicate implements IntPredicate
    {
        private static final long serialVersionUID = 1559965064386384281L;
        private final int expected;

        private LessThanIntPredicate(int expected)
        {
            this.expected = expected;
        }

        public boolean accept(int actual)
        {
            return actual < this.expected;
        }
    }

    private static final class GreaterThanIntPredicate implements IntPredicate
    {
        private static final long serialVersionUID = 8561389450518463489L;
        private final int expected;

        private GreaterThanIntPredicate(int expected)
        {
            this.expected = expected;
        }

        public boolean accept(int actual)
        {
            return actual > this.expected;
        }
    }
}
