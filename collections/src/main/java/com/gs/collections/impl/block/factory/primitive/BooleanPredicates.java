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
    private static final BooleanPredicate FALSE_PREDICATE = new FalsePredicate();
    private static final BooleanPredicate TRUE_PREDICATE = new TruePredicate();
    private static final BooleanPredicate ALWAYS_TRUE = new AlwaysTrueBooleanPredicate();
    private static final BooleanPredicate ALWAYS_FALSE = new AlwaysFalseBooleanPredicate();

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

    public static BooleanPredicate alwaysTrue()
    {
        return ALWAYS_TRUE;
    }

    public static BooleanPredicate alwaysFalse()
    {
        return ALWAYS_FALSE;
    }

    public static BooleanPredicate and(BooleanPredicate one, BooleanPredicate two)
    {
        if (one == IS_TRUE_BOOLEAN_PREDICATE && two == IS_TRUE_BOOLEAN_PREDICATE)
        {
            return IS_TRUE_BOOLEAN_PREDICATE;
        }
        if (one == IS_FALSE_BOOLEAN_PREDICATE && two == IS_TRUE_BOOLEAN_PREDICATE || one == IS_TRUE_BOOLEAN_PREDICATE && two == IS_FALSE_BOOLEAN_PREDICATE)
        {
            return FALSE_PREDICATE;
        }
        if (one == IS_FALSE_BOOLEAN_PREDICATE && two == IS_FALSE_BOOLEAN_PREDICATE)
        {
            return IS_FALSE_BOOLEAN_PREDICATE;
        }
        return new AndBooleanPredicate(one, two);
    }

    public static BooleanPredicate or(BooleanPredicate one, BooleanPredicate two)
    {
        if (one == IS_TRUE_BOOLEAN_PREDICATE && two == IS_TRUE_BOOLEAN_PREDICATE)
        {
            return IS_TRUE_BOOLEAN_PREDICATE;
        }
        if (one == IS_FALSE_BOOLEAN_PREDICATE && two == IS_TRUE_BOOLEAN_PREDICATE || one == IS_TRUE_BOOLEAN_PREDICATE && two == IS_FALSE_BOOLEAN_PREDICATE)
        {
            return TRUE_PREDICATE;
        }
        if (one == IS_FALSE_BOOLEAN_PREDICATE && two == IS_FALSE_BOOLEAN_PREDICATE)
        {
            return IS_FALSE_BOOLEAN_PREDICATE;
        }
        return new OrBooleanPredicate(one, two);
    }

    public static BooleanPredicate not(BooleanPredicate negate)
    {
        if (negate == IS_TRUE_BOOLEAN_PREDICATE)
        {
            return IS_FALSE_BOOLEAN_PREDICATE;
        }
        if (negate == IS_FALSE_BOOLEAN_PREDICATE)
        {
            return IS_TRUE_BOOLEAN_PREDICATE;
        }
        return new NotBooleanPredicate(negate);
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

    private static final class AndBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        private final BooleanPredicate one;
        private final BooleanPredicate two;

        private AndBooleanPredicate(BooleanPredicate one, BooleanPredicate two)
        {
            this.one = one;
            this.two = two;
        }

        public boolean accept(boolean actual)
        {
            return this.one.accept(actual) && this.two.accept(actual);
        }
    }

    private static final class OrBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        private final BooleanPredicate one;
        private final BooleanPredicate two;

        private OrBooleanPredicate(BooleanPredicate one, BooleanPredicate two)
        {
            this.one = one;
            this.two = two;
        }

        public boolean accept(boolean actual)
        {
            return this.one.accept(actual) || this.two.accept(actual);
        }
    }

    private static final class NotBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        private final BooleanPredicate negate;

        private NotBooleanPredicate(BooleanPredicate negate)
        {
            this.negate = negate;
        }

        public boolean accept(boolean actual)
        {
            return !this.negate.accept(actual);
        }
    }

    private static final class FalsePredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return false;
        }
    }

    private static final class TruePredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return true;
        }
    }

    private static final class AlwaysTrueBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return true;
        }
    }

    private static final class AlwaysFalseBooleanPredicate implements BooleanPredicate
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return false;
        }
    }
}
