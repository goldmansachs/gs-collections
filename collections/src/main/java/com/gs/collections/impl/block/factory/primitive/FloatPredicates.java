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

import com.gs.collections.api.block.predicate.primitive.FloatPredicate;

/**
 * Provides a set of common predicates for float values.
 */
public final class FloatPredicates
{
    private FloatPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("MisspelledEquals")
    public static FloatPredicate equal(float expected)
    {
        return new EqualsFloatPredicate(expected);
    }

    public static FloatPredicate equal(float expected, float delta)
    {
        return new EqualsWithDeltaFloatPredicate(expected, delta);
    }

    public static FloatPredicate lessThan(float expected)
    {
        return new LessThanFloatPredicate(expected);
    }

    public static FloatPredicate greaterThan(float expected)
    {
        return new GreaterThanFloatPredicate(expected);
    }

    private static final class EqualsFloatPredicate implements FloatPredicate
    {
        private static final long serialVersionUID = 1L;
        private final float expected;

        private EqualsFloatPredicate(float expected)
        {
            this.expected = expected;
        }

        public boolean accept(float actual)
        {
            return Float.floatToIntBits(actual) == Float.floatToIntBits(this.expected);
        }
    }

    private static final class EqualsWithDeltaFloatPredicate implements FloatPredicate
    {
        private static final long serialVersionUID = 1L;
        private final float expected;
        private final float delta;

        private EqualsWithDeltaFloatPredicate(float expected, float delta)
        {
            this.expected = expected;
            this.delta = delta;
        }

        public boolean accept(float actual)
        {
            return Math.abs(this.expected - actual) <= this.delta;
        }
    }

    private static final class LessThanFloatPredicate implements FloatPredicate
    {
        private static final long serialVersionUID = 1L;

        private final float expected;

        private LessThanFloatPredicate(float expected)
        {
            this.expected = expected;
        }

        public boolean accept(float actual)
        {
            return actual < this.expected;
        }
    }

    private static final class GreaterThanFloatPredicate implements FloatPredicate
    {
        private static final long serialVersionUID = 1L;

        private final float expected;

        private GreaterThanFloatPredicate(float expected)
        {
            this.expected = expected;
        }

        public boolean accept(float actual)
        {
            return actual > this.expected;
        }
    }
}
