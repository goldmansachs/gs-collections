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

package com.gs.collections.impl.utility;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.function.AddFunction;
import com.gs.collections.impl.block.function.primitive.CharFunction;
import com.gs.collections.impl.block.function.primitive.CodePointFunction;
import com.gs.collections.impl.block.predicate.CodePointPredicate;
import com.gs.collections.impl.block.predicate.primitive.CharPredicate;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure;
import com.gs.collections.impl.block.procedure.primitive.CharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CodePointProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link StringIterate}.
 */
public class StringIterateTest
{
    @Test
    public void englishToUpperLowerCase()
    {
        Assert.assertEquals("ABC", StringIterate.englishToUpperCase("abc"));
        Assert.assertEquals("abc", StringIterate.englishToLowerCase("ABC"));
    }

    @Test
    public void collect()
    {
        Assert.assertEquals("ABC", StringIterate.collect("abc", CharFunction.TO_UPPERCASE));
        Assert.assertEquals("abc", StringIterate.collect("abc", CharFunction.TO_LOWERCASE));
    }

    @Test
    public void collectCodePoint()
    {
        Assert.assertEquals("ABC", StringIterate.collect("abc", CodePointFunction.TO_UPPERCASE));
        Assert.assertEquals("abc", StringIterate.collect("abc", CodePointFunction.TO_LOWERCASE));
    }

    @Test
    public void englishToUpperCase()
    {
        Assert.assertEquals("ABC", StringIterate.englishToUpperCase("abc"));
        Assert.assertEquals("A,B,C", StringIterate.englishToUpperCase("a,b,c"));
        Assert.assertSame("A,B,C", StringIterate.englishToUpperCase("A,B,C"));
    }

    @Test
    public void englishToLowerCase()
    {
        Assert.assertEquals("abc", StringIterate.englishToLowerCase("ABC"));
        Assert.assertEquals("a,b,c", StringIterate.englishToLowerCase("A,B,C"));
        Assert.assertSame("a,b,c", StringIterate.englishToLowerCase("a,b,c"));
    }

    @Test
    public void englishIsUpperLowerCase()
    {
        String allValues = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~`!@#$%^&*()_-+=[]{};<>,.?/|";
        String jdkUpper = allValues.toUpperCase();
        String upper = StringIterate.englishToUpperCase(allValues);
        Assert.assertEquals(jdkUpper.length(), upper.length());
        Assert.assertEquals(jdkUpper, upper);
        String jdkLower = allValues.toLowerCase();
        String lower = StringIterate.englishToLowerCase(allValues);
        Assert.assertEquals(jdkLower.length(), lower.length());
        Assert.assertEquals(jdkLower, lower);
    }

    @Test
    public void select()
    {
        String string = StringIterate.select("1a2a3", CharPredicate.IS_DIGIT);
        Assert.assertEquals("123", string);
    }

    @Test
    public void selectCodePoint()
    {
        String string = StringIterate.select("1a2a3", CodePointPredicate.IS_DIGIT);
        Assert.assertEquals("123", string);
    }

    @Test
    public void detect()
    {
        char character = StringIterate.detect("1a2a3", CharPredicate.IS_LETTER);
        Assert.assertEquals('a', character);
    }

    @Test
    public void detectIfNone()
    {
        char character = StringIterate.detectIfNone("123", CharPredicate.IS_LETTER, "b".charAt(0));
        Assert.assertEquals('b', character);
    }

    @Test
    public void detectIfNoneWithString()
    {
        char character = StringIterate.detectIfNone("123", CharPredicate.IS_LETTER, "b");
        Assert.assertEquals('b', character);
    }

    @Test
    public void allSatisfy()
    {
        Assert.assertTrue(StringIterate.allSatisfy("MARY", CharPredicate.IS_UPPERCASE));
        Assert.assertFalse(StringIterate.allSatisfy("Mary", CharPredicate.IS_UPPERCASE));
    }

    @Test
    public void isNumber()
    {
        Assert.assertTrue(StringIterate.isNumber("123"));
        Assert.assertFalse(StringIterate.isNumber("abc"));
        Assert.assertFalse(StringIterate.isNumber(""));
    }

    @Test
    public void isAlphaNumeric()
    {
        Assert.assertTrue(StringIterate.isAlphaNumeric("123"));
        Assert.assertTrue(StringIterate.isAlphaNumeric("abc"));
        Assert.assertTrue(StringIterate.isAlphaNumeric("123abc"));
        Assert.assertFalse(StringIterate.isAlphaNumeric("!@#"));
        Assert.assertFalse(StringIterate.isAlphaNumeric(""));
    }

    @Test
    public void toList()
    {
        String tokens = "Ted,Mary  ";
        MutableList<String> results = StringIterate.csvTokensToList(tokens);
        Verify.assertSize(2, results);
        Verify.assertStartsWith(results, "Ted", "Mary  ");
    }

    @Test
    public void toSortedList()
    {
        String tokens = " Ted , Mary ";
        MutableList<String> results = StringIterate.csvTokensToSortedList(tokens);
        Verify.assertSize(2, results);
        Verify.assertStartsWith(results, " Mary ", " Ted ");
    }

    @Test
    public void asTrimmedSortedList()
    {
        String tokens = " Ted,Mary ";
        MutableList<String> results = StringIterate.csvTrimmedTokensToSortedList(tokens);
        Verify.assertSize(2, results);
        Verify.assertStartsWith(results, "Mary", "Ted");
    }

    @Test
    public void csvTokensToSet()
    {
        String tokens = "Ted,Mary";
        MutableSet<String> results = StringIterate.csvTokensToSet(tokens);
        Verify.assertSize(2, results);
        Verify.assertContainsAll(results, "Mary", "Ted");
    }

    @Test
    public void toReverseSortedList()
    {
        String tokens = "Ted,Mary";
        MutableList<String> results = StringIterate.csvTokensToReverseSortedList(tokens);
        Verify.assertSize(2, results);
    }

    @Test
    public void toMap()
    {
        String tokens = "1:Ted|2:Mary";
        MutableMap<String, String> results = StringIterate.tokensToMap(tokens);
        Verify.assertSize(2, results);
        Verify.assertContainsKeyValue("1", "Ted", results);
        Verify.assertContainsKeyValue("2", "Mary", results);
    }

    @Test
    public void toMapWithFunctions()
    {
        String tokens = "1:Ted|2:Mary";
        Function<String, String> stringPassThruFunction = Functions.getPassThru();
        MutableMap<Integer, String> results = StringIterate.tokensToMap(tokens, "|", ":", Functions.getStringToInteger(), stringPassThruFunction);
        Verify.assertSize(2, results);
        Verify.assertContainsKeyValue(1, "Ted", results);
        Verify.assertContainsKeyValue(2, "Mary", results);
    }

    @Test
    public void reject()
    {
        String string = StringIterate.reject("1a2b3c", CharPredicate.IS_DIGIT);
        Assert.assertEquals("abc", string);
    }

    @Test
    public void rejectCodePoint()
    {
        String string = StringIterate.reject("1a2b3c", CodePointPredicate.IS_DIGIT);
        Assert.assertEquals("abc", string);
    }

    @Test
    public void count()
    {
        int count = StringIterate.count("1a2a3", CharPredicate.IS_DIGIT);
        Assert.assertEquals(3, count);
    }

    @Test
    public void countCodePoint()
    {
        int count = StringIterate.count("1a2a3", CodePointPredicate.IS_DIGIT);
        Assert.assertEquals(3, count);
    }

    @Test
    public void occurrencesOf()
    {
        int count = StringIterate.occurrencesOf("1a2a3", 'a');
        Assert.assertEquals(2, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void occurrencesOf_multiple_character_string_throws()
    {
        StringIterate.occurrencesOf("1a2a3", "abc");
    }

    @Test
    public void occurrencesOfCodePoint()
    {
        int count = StringIterate.occurrencesOf("1a2a3", "a".codePointAt(0));
        Assert.assertEquals(2, count);
    }

    @Test
    public void occurrencesOfString()
    {
        int count = StringIterate.occurrencesOf("1a2a3", "a");
        Assert.assertEquals(2, count);
    }

    @Test
    public void count2()
    {
        int count = StringIterate.count("1a2a3", CharPredicate.IS_UNDEFINED);
        Assert.assertEquals(0, count);
    }

    @Test
    public void count2CodePoint()
    {
        int count = StringIterate.count("1a2a3", CodePointPredicate.IS_UNDEFINED);
        Assert.assertEquals(0, count);
    }

    @Test
    public void forEach()
    {
        final StringBuffer buffer = new StringBuffer();
        StringIterate.forEach("1a2b3c", new CharProcedure()
        {
            public void value(char character)
            {
                buffer.append(character);
            }
        });
        Assert.assertEquals("1a2b3c", buffer.toString());
    }

    @Test
    public void forEachCodePoint()
    {
        final StringBuffer buffer = new StringBuffer();
        StringIterate.forEach("1a2b3c", new CodePointProcedure()
        {
            public void value(int character)
            {
                buffer.appendCodePoint(character);
            }
        });
        Assert.assertEquals("1a2b3c", buffer.toString());
    }

    @Test
    public void reverseForEach()
    {
        final StringBuffer buffer = new StringBuffer();
        StringIterate.reverseForEach("1a2b3c", new CharProcedure()
        {
            public void value(char character)
            {
                buffer.append(character);
            }
        });
        Assert.assertEquals("c3b2a1", buffer.toString());
    }

    @Test
    public void testReverseForEach_emptyString()
    {
        final StringBuffer buffer = new StringBuffer();
        StringIterate.reverseForEach("", new CharProcedure()
        {
            public void value(char character)
            {
                buffer.append(character);
            }
        });
        Assert.assertEquals("", buffer.toString());
    }

    @Test
    public void reverseForEachCodePoint()
    {
        final StringBuffer buffer = new StringBuffer();
        StringIterate.reverseForEach("1a2b3c", new CodePointProcedure()
        {
            public void value(int character)
            {
                buffer.appendCodePoint(character);
            }
        });
        Assert.assertEquals("c3b2a1", buffer.toString());
    }

    @Test
    public void forEachToken()
    {
        String tokens = "1,2";
        final MutableList<Integer> list = Lists.mutable.of();
        StringIterate.forEachToken(tokens, ",", new CheckedProcedure<String>()
        {
            @Override
            public void safeValue(String string)
            {
                list.add(Integer.valueOf(string));
            }
        });
        Verify.assertSize(2, list);
        Verify.assertContains(1, list);
        Verify.assertContains(2, list);
    }

    @Test
    public void forEachTrimmedToken()
    {
        String tokens = "1 ,2 ";
        final MutableList<Integer> list = Lists.mutable.of();
        StringIterate.forEachTrimmedToken(tokens, ",", new CheckedProcedure<String>()
        {
            @Override
            public void safeValue(String string)
            {
                list.add(Integer.valueOf(string));
            }
        });
        Verify.assertSize(2, list);
        Verify.assertContains(1, list);
        Verify.assertContains(2, list);
    }

    @Test
    public void csvTrimmedTokenToList()
    {
        String tokens = "1 ,2 ";
        Assert.assertEquals(FastList.newListWith("1", "2"), StringIterate.csvTrimmedTokensToList(tokens));
    }

    @Test
    public void injectIntoTokens()
    {
        Assert.assertEquals("123", StringIterate.injectIntoTokens("1,2,3", ",", null, AddFunction.STRING));
    }

    @Test
    public void getLastToken()
    {
        Assert.assertEquals("charlie", StringIterate.getLastToken("alpha~|~beta~|~charlie", "~|~"));
        Assert.assertEquals("123", StringIterate.getLastToken("123", "~|~"));
        Assert.assertEquals("", StringIterate.getLastToken("", "~|~"));
        Assert.assertNull(StringIterate.getLastToken(null, "~|~"));
        Assert.assertEquals("", StringIterate.getLastToken("123~|~", "~|~"));
        Assert.assertEquals("123", StringIterate.getLastToken("~|~123", "~|~"));
    }

    @Test
    public void getFirstToken()
    {
        Assert.assertEquals("alpha", StringIterate.getFirstToken("alpha~|~beta~|~charlie", "~|~"));
        Assert.assertEquals("123", StringIterate.getFirstToken("123", "~|~"));
        Assert.assertEquals("", StringIterate.getFirstToken("", "~|~"));
        Assert.assertNull(StringIterate.getFirstToken(null, "~|~"));
        Assert.assertEquals("123", StringIterate.getFirstToken("123~|~", "~|~"));
        Assert.assertEquals("", StringIterate.getFirstToken("~|~123,", "~|~"));
    }

    @Test
    public void isEmptyOrWhitespace()
    {
        Assert.assertTrue(StringIterate.isEmptyOrWhitespace("   "));
        Assert.assertFalse(StringIterate.isEmptyOrWhitespace(" 1  "));
    }

    @Test
    public void notEmptyOrWhitespace()
    {
        Assert.assertFalse(StringIterate.notEmptyOrWhitespace("   "));
        Assert.assertTrue(StringIterate.notEmptyOrWhitespace(" 1  "));
    }

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(StringIterate.isEmpty(""));
        Assert.assertFalse(StringIterate.isEmpty("   "));
        Assert.assertFalse(StringIterate.isEmpty("1"));
    }

    @Test
    public void notEmpty()
    {
        Assert.assertFalse(StringIterate.notEmpty(""));
        Assert.assertTrue(StringIterate.notEmpty("   "));
        Assert.assertTrue(StringIterate.notEmpty("1"));
    }

    @Test
    public void repeat()
    {
        Assert.assertEquals("", StringIterate.repeat("", 42));
        Assert.assertEquals("    ", StringIterate.repeat(' ', 4));
        Assert.assertEquals("        ", StringIterate.repeat(" ", 8));
        Assert.assertEquals("CubedCubedCubed", StringIterate.repeat("Cubed", 3));
    }

    @Test
    public void padOrTrim()
    {
        Assert.assertEquals("abcdefghijkl", StringIterate.padOrTrim("abcdefghijkl", 12));
        Assert.assertEquals("this n", StringIterate.padOrTrim("this needs to be trimmed", 6));
        Assert.assertEquals("pad this      ", StringIterate.padOrTrim("pad this", 14));
    }

    @Test
    public void string()
    {
        Assert.assertEquals("Token2", StringIterate.getLastToken("Token1DelimiterToken2", "Delimiter"));
    }

    @Test
    public void toSet()
    {
        Verify.assertSetsEqual(
                UnifiedSet.newSetWith('a', 'b', 'c', 'd', 'e'),
                StringIterate.toSet("aabcde"));
    }
}
