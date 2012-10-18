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

import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;

/**
 * Provides a set of common predicates for boolean values.
 */
public final class BooleanPredicates
{
    private static final BooleanPredicate IS_TRUE_BOOLEAN_PREDICATE = new IsTrueBooleanPredicate();
    private static final BooleanPredicate IS_FALSE_BOOLEAN_PREDICATE = new IsFalseBooleanPredicate();

    private BooleanPredicates()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    @SuppressWarnings("MisspelledEquals")
    public static BooleanPredicate equal(boolean expected)
    {
        return expected ? IS_TRUE_BOOLEAN_PREDICATE : IS_FALSE_BOOLEAN_PREDICATE;
    }

    public static BooleanPredicate not(boolean expected)
    {
        return expected ? IS_FALSE_BOOLEAN_PREDICATE : IS_TRUE_BOOLEAN_PREDICATE;
    }

    public static BooleanPredicate isTrue()
    {
        return IS_TRUE_BOOLEAN_PREDICATE;
    }

    public static BooleanPredicate isFalse()
    {
        return IS_FALSE_BOOLEAN_PREDICATE;
    }

    private static final class IsTrueBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return value;
        }
    }

    private static final class IsFalseBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return !value;
        }
    }
}
