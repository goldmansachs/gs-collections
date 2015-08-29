/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.block.predicate;

import java.io.Serializable;

/**
 * A Predicate that accepts an int value
 */
public interface CodePointPredicate
        extends Serializable
{
    CodePointPredicate IS_UPPERCASE = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isUpperCase(codePoint);
        }
    };

    CodePointPredicate IS_LOWERCASE = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isLowerCase(codePoint);
        }
    };

    CodePointPredicate IS_DIGIT = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isDigit(codePoint);
        }
    };

    CodePointPredicate IS_LETTER = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isLetter(codePoint);
        }
    };

    CodePointPredicate IS_LETTER_OR_DIGIT = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isLetterOrDigit(codePoint);
        }
    };

    CodePointPredicate IS_WHITESPACE = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isWhitespace(codePoint);
        }
    };

    CodePointPredicate IS_UNDEFINED = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return !Character.isDefined(codePoint);
        }
    };

    CodePointPredicate IS_BMP = new CodePointPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(int codePoint)
        {
            return Character.isBmpCodePoint(codePoint);
        }
    };

    boolean accept(int codePoint);
}
