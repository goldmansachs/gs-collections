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

import com.gs.collections.api.block.predicate.primitive.DoublePredicate;

/**
 * Provides a set of common predicates for double values.
 */
public final class DoublePredicates
{
    private DoublePredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("MisspelledEquals")
    public static DoublePredicate equal(double expected)
    {
        return new EqualsDoublePredicate(expected);
    }

    public static DoublePredicate equal(double expected, double delta)
    {
        return new EqualsWithDeltaDoublePredicate(expected, delta);
    }

    public static DoublePredicate lessThan(double expected)
    {
        return new LessThanDoublePredicate(expected);
    }

    public static DoublePredicate greaterThan(double expected)
    {
        return new GreaterThanDoublePredicate(expected);
    }

    private static final class EqualsDoublePredicate implements DoublePredicate
    {
        private static final long serialVersionUID = 1L;
        private final double expected;

        private EqualsDoublePredicate(double expected)
        {
            this.expected = expected;
        }

        public boolean accept(double actual)
        {
            return Double.doubleToLongBits(actual) == Double.doubleToLongBits(this.expected);
        }
    }

    private static final class EqualsWithDeltaDoublePredicate implements DoublePredicate
    {
        private static final long serialVersionUID = 1L;
        private final double expected;
        private final double delta;

        private EqualsWithDeltaDoublePredicate(double expected, double delta)
        {
            this.expected = expected;
            this.delta = delta;
        }

        public boolean accept(double actual)
        {
            return Math.abs(this.expected - actual) <= this.delta;
        }
    }

    private static final class LessThanDoublePredicate implements DoublePredicate
    {
        private static final long serialVersionUID = 1L;

        private final double expected;

        private LessThanDoublePredicate(double expected)
        {
            this.expected = expected;
        }

        public boolean accept(double actual)
        {
            return actual < this.expected;
        }
    }

    private static final class GreaterThanDoublePredicate implements DoublePredicate
    {
        private static final long serialVersionUID = 1L;

        private final double expected;

        private GreaterThanDoublePredicate(double expected)
        {
            this.expected = expected;
        }

        public boolean accept(double actual)
        {
            return actual > this.expected;
        }
    }
}
