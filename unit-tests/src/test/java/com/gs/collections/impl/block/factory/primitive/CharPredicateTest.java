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

package com.gs.collections.impl.block.factory.primitive;

import com.gs.collections.api.list.primitive.CharList;
import com.gs.collections.impl.block.predicate.primitive.CharPredicate;
import com.gs.collections.impl.factory.primitive.CharLists;
import org.junit.Assert;
import org.junit.Test;

/**
 * Junit test for {@link CharPredicate}.
 *
 * @deprecated in 6.0
 */
@Deprecated
public class CharPredicateTest
{
    @Test
    public void isUpperCase()
    {
        assertTrue(CharLists.mutable.of('A', 'B', 'C'), CharPredicate.IS_UPPERCASE);
        assertFalse(CharLists.mutable.of('a', 'b', 'c', '1', '.'), CharPredicate.IS_UPPERCASE);
    }

    @Test
    public void isLowerCase()
    {
        assertTrue(CharLists.mutable.of('a', 'b', 'c'), CharPredicate.IS_LOWERCASE);
        assertFalse(CharLists.mutable.of('A', 'B', 'C', '1', '.'), CharPredicate.IS_LOWERCASE);
    }

    @Test
    public void isDigit()
    {
        assertTrue(CharLists.mutable.of('0', '1', '2', '3'), CharPredicate.IS_DIGIT);
        assertFalse(CharLists.mutable.of('A', 'B', 'C', '.'), CharPredicate.IS_DIGIT);
        assertFalse(CharLists.mutable.of('a', 'b', 'c', '.'), CharPredicate.IS_DIGIT);
    }

    @Test
    public void isDigitOrDot()
    {
        assertTrue(CharLists.mutable.of('0', '1', '2', '3', '.'), CharPredicate.IS_DIGIT_OR_DOT);
        assertFalse(CharLists.mutable.of('A', 'B', 'C'), CharPredicate.IS_DIGIT_OR_DOT);
        assertFalse(CharLists.mutable.of('a', 'b', 'c'), CharPredicate.IS_DIGIT_OR_DOT);
    }

    @Test
    public void isLetter()
    {
        assertTrue(CharLists.mutable.of('A', 'B', 'C'), CharPredicate.IS_LETTER);
        assertTrue(CharLists.mutable.of('a', 'b', 'c'), CharPredicate.IS_LETTER);
        assertFalse(CharLists.mutable.of('0', '1', '2', '3', '.'), CharPredicate.IS_LETTER);
    }

    @Test
    public void isLetterOrDigit()
    {
        assertTrue(CharLists.mutable.of('A', 'B', 'C', '0', '1', '2', '3'), CharPredicate.IS_LETTER_OR_DIGIT);
        assertTrue(CharLists.mutable.of('a', 'b', 'c', '0', '1', '2', '3'), CharPredicate.IS_LETTER_OR_DIGIT);
        assertFalse(CharLists.mutable.of('.', '$', '*'), CharPredicate.IS_LETTER_OR_DIGIT);
    }

    @Test
    public void isWhitespace()
    {
        assertTrue(CharLists.mutable.of(' '), CharPredicate.IS_WHITESPACE);
        assertFalse(CharLists.mutable.of('A', 'B', 'C', '0', '1', '2', '3'), CharPredicate.IS_WHITESPACE);
        assertFalse(CharLists.mutable.of('a', 'b', 'c', '0', '1', '2', '3'), CharPredicate.IS_WHITESPACE);
        assertFalse(CharLists.mutable.of('.', '$', '*'), CharPredicate.IS_WHITESPACE);
    }

    @Test
    public void isUndefined()
    {
        Assert.assertTrue(CharPredicates.isUndefined().accept((char) 888));
        assertFalse(CharLists.mutable.of('A', 'B', 'C', '0', '1', '2', '3'), CharPredicate.IS_UNDEFINED);
        assertFalse(CharLists.mutable.of('a', 'b', 'c', '0', '1', '2', '3'), CharPredicate.IS_UNDEFINED);
        assertFalse(CharLists.mutable.of('.', '$', '*'), CharPredicate.IS_UNDEFINED);
    }

    private static void assertTrue(CharList charList, CharPredicate predicate)
    {
        charList.forEach(element -> Assert.assertTrue(predicate.accept(element)));
    }

    private static void assertFalse(CharList charList, CharPredicate predicate)
    {
        charList.forEach(element -> Assert.assertFalse(predicate.accept(element)));
    }
}
