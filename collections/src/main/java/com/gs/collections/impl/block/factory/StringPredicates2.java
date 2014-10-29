/*
 * Copyright 2014 Goldman Sachs.
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

import com.gs.collections.impl.utility.StringIterate;

/**
 * The StringPredicates2 class is a factory that produces Predicate2 instances that work with Strings.
 */
public final class StringPredicates2
{
    private static final ContainsString CONTAINS_STRING = new ContainsString();
    private static final NotContainsString NOT_CONTAINS_STRING = new NotContainsString();
    private static final StartsWith STARTS_WITH = new StartsWith();
    private static final NotStartsWith NOT_STARTS_WITH = new NotStartsWith();
    private static final EndsWith ENDS_WITH = new EndsWith();
    private static final NotEndsWith NOT_ENDS_WITH = new NotEndsWith();
    private static final EqualsIgnoreCase EQUALS_IGNORE_CASE = new EqualsIgnoreCase();
    private static final NotEqualsIgnoreCase NOT_EQUALS_IGNORE_CASE = new NotEqualsIgnoreCase();
    private static final MatchesRegex MATCHES_REGEX = new MatchesRegex();

    private StringPredicates2()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Returns true if a String specified on the predicate is contained within a String passed to the the accept
     * method.
     */
    public static Predicates2<String, String> contains()
    {
        return CONTAINS_STRING;
    }

    /**
     * Returns true if a String specified on the predicate is contained within a String passed to the the accept
     * method.
     *
     * @since 5.0
     */
    public static Predicates2<String, String> notContains()
    {
        return NOT_CONTAINS_STRING;
    }

    /**
     * Returns true if a String passed to the the accept method starts with the string specified on the predicate.
     */
    public static Predicates2<String, String> startsWith()
    {
        return STARTS_WITH;
    }

    /**
     * Returns false if a String passed to the the accept method starts with the string specified on the predicate.
     *
     * @since 5.0
     */
    public static Predicates2<String, String> notStartsWith()
    {
        return NOT_STARTS_WITH;
    }

    /**
     * Returns true if a String passed to the the accept method ends with the string specified on the predicate.
     */
    public static Predicates2<String, String> endsWith()
    {
        return ENDS_WITH;
    }

    /**
     * Returns false if a String passed to the the accept method ends with the string specified on the predicate.
     *
     * @since 5.0
     */
    public static Predicates2<String, String> notEndsWith()
    {
        return NOT_ENDS_WITH;
    }

    public static Predicates2<String, String> equalsIgnoreCase()
    {
        return EQUALS_IGNORE_CASE;
    }

    /**
     * @since 5.0
     */
    public static Predicates2<String, String> notEqualsIgnoreCase()
    {
        return NOT_EQUALS_IGNORE_CASE;
    }

    public static Predicates2<String, String> matches()
    {
        return MATCHES_REGEX;
    }

    private static final class ContainsString extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return StringIterate.notEmpty(each) && each.contains(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.contains()";
        }
    }

    private static final class NotContainsString extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return StringIterate.isEmpty(each) || !each.contains(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.notContains()";
        }
    }

    private static final class StartsWith extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each != null && each.startsWith(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.startsWith()";
        }
    }

    private static final class NotStartsWith extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each == null || !each.startsWith(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.notStartsWith()";
        }
    }

    private static final class EndsWith extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each != null && each.endsWith(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.endsWith()";
        }
    }

    private static final class NotEndsWith extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each == null || !each.endsWith(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.notEndsWith()";
        }
    }

    private static final class EqualsIgnoreCase extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each != null && each.equalsIgnoreCase(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.equalsIgnoreCase()";
        }
    }

    private static final class NotEqualsIgnoreCase extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each == null || !each.equalsIgnoreCase(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.notEqualsIgnoreCase()";
        }
    }

    private static final class MatchesRegex extends Predicates2<String, String>
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(String each, String parameter)
        {
            return each != null && each.matches(parameter);
        }

        @Override
        public String toString()
        {
            return "StringPredicates2.matches()";
        }
    }
}
