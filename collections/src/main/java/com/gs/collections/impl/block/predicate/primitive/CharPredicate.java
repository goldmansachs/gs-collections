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

package com.gs.collections.impl.block.predicate.primitive;

import java.io.Serializable;

/**
 * A Predicate that accepts a char value
 *
 * @deprecated since 3.0. Use {@link com.gs.collections.api.block.predicate.primitive.CharPredicate} instead.
 */
@Deprecated
public interface CharPredicate
        extends Serializable
{
    CharPredicate IS_UPPERCASE = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isUpperCase(character);
        }
    };

    CharPredicate IS_LOWERCASE = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isLowerCase(character);
        }
    };

    CharPredicate IS_DIGIT = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isDigit(character);
        }
    };

    CharPredicate IS_DIGIT_OR_DOT = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isDigit(character) || character == '.';
        }
    };

    CharPredicate IS_LETTER = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isLetter(character);
        }
    };

    CharPredicate IS_LETTER_OR_DIGIT = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isLetterOrDigit(character);
        }
    };

    CharPredicate IS_WHITESPACE = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return Character.isWhitespace(character);
        }
    };

    CharPredicate IS_UNDEFINED = new CharPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(char character)
        {
            return !Character.isDefined(character);
        }
    };

    boolean accept(char character);
}
