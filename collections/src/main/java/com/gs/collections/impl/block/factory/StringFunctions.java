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

public final class StringFunctions
{
    private static final Function<String, String> TO_UPPER_CASE = new ToUpperCaseFunction();
    private static final Function<String, String> TO_LOWER_CASE = new ToLowerCaseFunction();
    private static final Function<String, Integer> LENGTH = new LengthFunction();
    private static final Function<String, String> TRIM = new TrimFunction();
    private static final Function<String, Character> FIRST_LETTER = new FirstLetterFunction();
    private static final Function<String, Integer> TO_INTEGER = new ToIntegerFunction();

    private StringFunctions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Function<String, String> toUpperCase()
    {
        return TO_UPPER_CASE;
    }

    public static Function<String, String> toLowerCase()
    {
        return TO_LOWER_CASE;
    }

    public static Function<String, Integer> toInteger()
    {
        return TO_INTEGER;
    }

    public static Function<String, Integer> length()
    {
        return LENGTH;
    }

    public static Function<String, Character> firstLetter()
    {
        return FIRST_LETTER;
    }

    public static Function<String, String> subString(int beginIndex, int endIndex)
    {
        return new SubStringFunction(beginIndex, endIndex);
    }

    /**
     * Returns a function that returns a copy of a {@link String}, with leading and trailing whitespace
     * omitted.
     *
     * @see String#trim()
     */
    public static Function<String, String> trim()
    {
        return TRIM;
    }

    private static final class ToUpperCaseFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(String object)
        {
            return object.toUpperCase();
        }

        @Override
        public String toString()
        {
            return "string.toUpperCase()";
        }
    }

    private static final class ToLowerCaseFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(String object)
        {
            return object.toLowerCase();
        }

        @Override
        public String toString()
        {
            return "string.toLowerCase()";
        }
    }

    private static final class LengthFunction implements Function<String, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer valueOf(String string)
        {
            return string.length();
        }

        @Override
        public String toString()
        {
            return "string.length()";
        }
    }

    private static final class TrimFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(String string)
        {
            return string.trim();
        }

        @Override
        public String toString()
        {
            return "string.trim()";
        }
    }

    private static final class FirstLetterFunction
            implements Function<String, Character>
    {
        private static final long serialVersionUID = 1L;

        public Character valueOf(String object)
        {
            return object == null || object.length() < 1 ? null : object.charAt(0);
        }
    }

    private static final class ToIntegerFunction implements Function<String, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer valueOf(String string)
        {
            return Integer.valueOf(string);
        }
    }

    private static final class SubStringFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        private final int beginIndex;
        private final int endIndex;

        private SubStringFunction(int beginIndex, int endIndex)
        {
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        public String valueOf(String string)
        {
            return string.substring(this.beginIndex, this.endIndex);
        }

        @Override
        public String toString()
        {
            return "string.subString(" + this.beginIndex + ',' + this.endIndex + ')';
        }
    }
}
