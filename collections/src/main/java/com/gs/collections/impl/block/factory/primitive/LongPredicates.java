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

import com.gs.collections.api.block.predicate.primitive.LongPredicate;

/**
 * Provides a set of common predicates for long values.
 */
public final class LongPredicates
{
    private LongPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("MisspelledEquals")
    public static LongPredicate equal(long expected)
    {
        return new EqualsLongPredicate(expected);
    }

    public static LongPredicate lessThan(long expected)
    {
        return new LessThanLongPredicate(expected);
    }

    public static LongPredicate greaterThan(long expected)
    {
        return new GreaterThanLongPredicate(expected);
    }

    private static final class EqualsLongPredicate implements LongPredicate
    {
        private static final long serialVersionUID = 1L;
        private final long expected;

        private EqualsLongPredicate(long expected)
        {
            this.expected = expected;
        }

        public boolean accept(long value)
        {
            return this.expected == value;
        }
    }

    private static final class LessThanLongPredicate implements LongPredicate
    {
        private static final long serialVersionUID = 1L;
        private final long expected;

        private LessThanLongPredicate(long expected)
        {
            this.expected = expected;
        }

        public boolean accept(long actual)
        {
            return actual < this.expected;
        }
    }

    private static final class GreaterThanLongPredicate implements LongPredicate
    {
        private static final long serialVersionUID = 1L;
        private final long expected;

        private GreaterThanLongPredicate(long expected)
        {
            this.expected = expected;
        }

        public boolean accept(long actual)
        {
            return actual > this.expected;
        }
    }
}
