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

package com.gs.collections.impl.utility;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.CharToCharFunction;
import com.gs.collections.api.block.predicate.primitive.CharPredicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.primitive.CharProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.primitive.CharPredicates;
import com.gs.collections.impl.block.factory.primitive.CharToCharFunctions;
import com.gs.collections.impl.block.function.primitive.CharFunction;
import com.gs.collections.impl.block.function.primitive.CodePointFunction;
import com.gs.collections.impl.block.predicate.CodePointPredicate;
import com.gs.collections.impl.block.procedure.primitive.CodePointProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;

/**
 * A string is essentially an array of characters. In Smalltalk a String is a subclass of ArrayedCollection, which means
 * it supports the Collection protocol. StringIterate implements the methods available on the collection protocol that
 * make sense for Strings. Some of the methods are over-specialized, in the form of englishToUppercase() which is a fast
 * form of uppercase, but does not work for different locales.
 */
public final class StringIterate
{
    private StringIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a sorted {@link MutableList}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableList<String> csvTokensToSortedList(String string)
    {
        return StringIterate.tokensToSortedList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a sorted {@link MutableList}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableList<String> csvTrimmedTokensToSortedList(String string)
    {
        return StringIterate.trimmedTokensToSortedList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a sorted {@link MutableList}.
     */
    public static MutableList<String> tokensToSortedList(String string, String separator)
    {
        return StringIterate.tokensToList(string, separator).sortThis();
    }

    /**
     * Converts a string of tokens separated by the specified separator to a sorted {@link MutableList}.
     */
    public static MutableList<String> trimmedTokensToSortedList(String string, String separator)
    {
        return StringIterate.trimmedTokensToList(string, separator).sortThis();
    }

    /**
     * Converts a string of tokens separated by commas to a {@link MutableList}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableList<String> csvTokensToList(String string)
    {
        return StringIterate.tokensToList(string, ",");
    }

    /**
     * Converts a string of tokens separated by commas to a {@link MutableList}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableList<String> csvTrimmedTokensToList(String string)
    {
        return StringIterate.trimmedTokensToList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a {@link MutableList}.
     */
    public static MutableList<String> tokensToList(String string, String separator)
    {
        MutableList<String> list = Lists.mutable.empty();
        for (StringTokenizer stringTokenizer = new StringTokenizer(string, separator); stringTokenizer.hasMoreTokens(); )
        {
            String token = stringTokenizer.nextToken();
            list.add(token);
        }
        return list;
    }

    /**
     * Converts a string of tokens separated by the specified separator to a {@link MutableList}.
     */
    public static MutableList<String> trimmedTokensToList(String string, String separator)
    {
        return StringIterate.trimStringList(StringIterate.tokensToList(string, separator));
    }

    /**
     * Applies {@link String#trim()} to each String in the {@link List}
     *
     * @return the input list, mutated in place, containing trimmed strings.
     */
    private static <L extends List<String>> L trimStringList(L strings)
    {
        for (ListIterator<String> listIt = strings.listIterator(); listIt.hasNext(); )
        {
            String string = listIt.next().trim();
            listIt.set(string);
        }
        return strings;
    }

    /**
     * Converts a string of tokens separated by commas to a {@link MutableSet}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableSet<String> csvTokensToSet(String string)
    {
        return StringIterate.tokensToSet(string, ",");
    }

    /**
     * Converts a string of tokens to a {@link MutableSet}.
     */
    public static MutableSet<String> tokensToSet(String string, String separator)
    {
        MutableSet<String> set = UnifiedSet.newSet();
        for (StringTokenizer stringTokenizer = new StringTokenizer(string, separator); stringTokenizer.hasMoreTokens(); )
        {
            String token = stringTokenizer.nextToken();
            set.add(token);
        }
        return set;
    }

    /**
     * Converts a string of tokens to a {@link MutableMap} using a | to separate pairs, and a : to separate key and
     * value. e.g. "1:Sunday|2:Monday|3:Tuesday|4:Wednesday|5:Thursday|6:Friday|7:Saturday"
     */
    public static MutableMap<String, String> tokensToMap(String string)
    {
        return StringIterate.tokensToMap(string, "|", ":");
    }

    /**
     * Converts a string of tokens to a {@link MutableMap} using the specified separators.
     */
    public static MutableMap<String, String> tokensToMap(
            String string,
            String pairSeparator,
            String keyValueSeparator)
    {
        MutableMap<String, String> map = UnifiedMap.newMap();
        for (StringTokenizer tokenizer = new StringTokenizer(string, pairSeparator); tokenizer.hasMoreTokens(); )
        {
            String token = tokenizer.nextToken();
            String key = token.substring(0, token.indexOf(keyValueSeparator));
            String value = token.substring(token.indexOf(keyValueSeparator) + 1, token.length());
            map.put(key, value);
        }
        return map;
    }

    /**
     * Converts a string of tokens to a {@link MutableMap} using the specified 'key' and 'value'
     * Functions. e.g. "1:2,2:1,3:3" with both functions as Functions.getStringToInteger(), will be
     * translated a map {[1,2],[2,1],[3,3]}
     */
    public static <K, V> MutableMap<K, V> tokensToMap(
            String string,
            String separator,
            String keyValueSeparator,
            Function<String, K> keyFunction,
            Function<String, V> valueFunction)
    {
        MutableMap<K, V> map = UnifiedMap.newMap();
        for (StringTokenizer tokenizer = new StringTokenizer(string, separator); tokenizer.hasMoreTokens(); )
        {
            String token = tokenizer.nextToken();
            String key = token.substring(0, token.indexOf(keyValueSeparator));
            String value = token.substring(token.indexOf(keyValueSeparator) + 1, token.length());
            map.put(keyFunction.valueOf(key), valueFunction.valueOf(value));
        }
        return map;
    }

    /**
     * Converts a string of tokens separated by the specified separator to a reverse sorted {@link MutableList}.
     *
     * @deprecated in 3.0.  Inlineable.  Poorly named method.  Does not actually deal properly with CSV.  This is better handled in a separate library.
     */
    @Deprecated
    public static MutableList<String> csvTokensToReverseSortedList(String string)
    {
        return StringIterate.tokensToReverseSortedList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a reverse sorted {@link MutableList}.
     */
    public static MutableList<String> tokensToReverseSortedList(String string, String separator)
    {
        return StringIterate.tokensToList(string, separator).sortThis(Collections.reverseOrder());
    }

    /**
     * For each token in a string separated by the specified separator, execute the specified StringProcedure
     * by calling the valueOfString method.
     */
    public static void forEachToken(String string, String separator, Procedure<String> procedure)
    {
        for (StringTokenizer stringTokenizer = new StringTokenizer(string, separator); stringTokenizer.hasMoreTokens(); )
        {
            String token = stringTokenizer.nextToken();
            procedure.value(token);
        }
    }

    /**
     * For each token in a string separated by the specified separator, execute the specified Function2,
     * returning the result value from the function. For more information, see
     * {@link Iterate#injectInto(Object, Iterable, Function2)}
     */
    public static <T, R> R injectIntoTokens(
            String string,
            String separator,
            R injectedValue,
            Function2<R, String, R> function)
    {
        R result = injectedValue;
        for (StringTokenizer stringTokenizer = new StringTokenizer(string, separator); stringTokenizer.hasMoreTokens(); )
        {
            String token = stringTokenizer.nextToken();
            result = function.value(result, token);
        }
        return result;
    }

    /**
     * For each token in a {@code string} separated by the specified {@code separator}, execute the specified
     * {@link Procedure}.
     */
    public static void forEachTrimmedToken(String string, String separator, Procedure<String> procedure)
    {
        for (StringTokenizer stringTokenizer = new StringTokenizer(string, separator); stringTokenizer.hasMoreTokens(); )
        {
            String token = stringTokenizer.nextToken().trim();
            procedure.value(token);
        }
    }

    /**
     * For each character in the {@code string}, execute the {@link CharProcedure}.
     *
     * @deprecated since 3.0. Use {@link #forEach(String, CharProcedure)} instead.
     */
    @Deprecated
    public static void forEach(String string, final com.gs.collections.impl.block.procedure.primitive.CharProcedure procedure)
    {
        StringIterate.forEach(string, new CharProcedure()
        {
            public void value(char each)
            {
                procedure.value(each);
            }
        });
    }

    /**
     * For each character in the {@code string}, execute the {@link CharProcedure}.
     */
    public static void forEach(String string, CharProcedure procedure)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            procedure.value(string.charAt(i));
        }
    }

    /**
     * For each character in the {@code string}, execute the {@link CodePointProcedure}.
     */
    public static void forEach(String string, CodePointProcedure procedure)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            procedure.value(string.codePointAt(i));
        }
    }

    /**
     * For each character in the {@code string} in reverse order, execute the {@link CharProcedure}.
     */
    public static void reverseForEach(String string, CharProcedure procedure)
    {
        for (int i = string.length() - 1; i >= 0; i--)
        {
            procedure.value(string.charAt(i));
        }
    }

    /**
     * For each character in the {@code string} in reverse order, execute the {@link CodePointProcedure}.
     */
    public static void reverseForEach(String string, CodePointProcedure procedure)
    {
        for (int i = string.length() - 1; i >= 0; i--)
        {
            procedure.value(string.codePointAt(i));
        }
    }

    /**
     * Count the number of elements that return true for the specified {@code predicate}.
     *
     * @deprecated since 3.0.
     */
    @Deprecated
    public static int count(String string, final com.gs.collections.impl.block.predicate.primitive.CharPredicate predicate)
    {
        return StringIterate.count(string, new CharPredicate()
        {
            public boolean accept(char value)
            {
                return predicate.accept(value);
            }
        });
    }

    /**
     * Count the number of elements that return true for the specified {@code predicate}.
     */
    public static int count(String string, CharPredicate predicate)
    {
        int count = 0;
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.charAt(i)))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Count the number of elements that return true for the specified {@code predicate}.
     */
    public static int count(String string, CodePointPredicate predicate)
    {
        int count = 0;
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.codePointAt(i)))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * @deprecated since 3.0. Use {@link #collect(String, CharToCharFunction)} instead.
     */
    @Deprecated
    public static String collect(String string, final CharFunction function)
    {
        return StringIterate.collect(string, new CharToCharFunction()
        {
            public char valueOf(char charParameter)
            {
                return function.valueOf(charParameter);
            }
        });
    }

    public static String collect(String string, CharToCharFunction function)
    {
        int size = string.length();
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            builder.append(function.valueOf(string.charAt(i)));
        }
        return builder.toString();
    }

    public static String collect(String string, CodePointFunction function)
    {
        int size = string.length();
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            builder.appendCodePoint(function.valueOf(string.codePointAt(i)));
        }
        return builder.toString();
    }

    public static String englishToUpperCase(String string)
    {
        if (StringIterate.anySatisfy(string, CharPredicates.isLowerCase()))
        {
            return StringIterate.collect(string, CharToCharFunctions.toUpperCase());
        }
        return string;
    }

    public static String englishToLowerCase(String string)
    {
        if (StringIterate.anySatisfy(string, CharPredicates.isUpperCase()))
        {
            return StringIterate.collect(string, CharToCharFunctions.toLowerCase());
        }
        return string;
    }

    /**
     * Find the first element that returns true for the specified {@code predicate}.
     */
    public static Character detect(String string, CharPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            char character = string.charAt(i);
            if (predicate.accept(character))
            {
                return character;
            }
        }
        return null;
    }

    /**
     * Find the first element that returns true for the specified {@code predicate}.  Return the default char if
     * no value is found.
     */
    public static Character detectIfNone(String string, CharPredicate predicate, char resultIfNone)
    {
        Character result = StringIterate.detect(string, predicate);
        return result == null ? Character.valueOf(resultIfNone) : result;
    }

    /**
     * Find the first element that returns true for the specified {@code predicate}.  Return the first char of the
     * default string if no value is found.
     */
    public static Character detectIfNone(String string, CharPredicate predicate, String resultIfNone)
    {
        Character result = StringIterate.detect(string, predicate);
        return result == null ? Character.valueOf(resultIfNone.charAt(0)) : result;
    }

    /**
     * Count the number of occurrences of the specified char.
     */
    public static int occurrencesOf(String string, final char value)
    {
        return StringIterate.count(string, new CharPredicate()
        {
            public boolean accept(char character)
            {
                return value == character;
            }
        });
    }

    /**
     * Count the number of occurrences of the specified code point.
     */
    public static int occurrencesOf(String string, final int value)
    {
        return StringIterate.count(string, new CodePointPredicate()
        {
            public boolean accept(int codePoint)
            {
                return value == codePoint;
            }
        });
    }

    /**
     * Count the number of occurrences of the specified {@code string}.
     */
    public static int occurrencesOf(String string, String singleCharacter)
    {
        if (singleCharacter.length() != 1)
        {
            throw new IllegalArgumentException("Argument should be a single character: " + singleCharacter);
        }
        return StringIterate.occurrencesOf(string, singleCharacter.charAt(0));
    }

    /**
     * @return true if any of the characters in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean anySatisfy(String string, CharPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if any of the code points in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean anySatisfy(String string, CodePointPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.codePointAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if all of the characters in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean allSatisfy(String string, CharPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(string.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if all of the code points in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean allSatisfy(String string, CodePointPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (!predicate.accept(string.codePointAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if none of the characters in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean noneSatisfy(String string, CharPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if none of the code points in the {@code string} answer true for the specified {@code predicate}.
     */
    public static boolean noneSatisfy(String string, CodePointPredicate predicate)
    {
        int size = string.length();
        for (int i = 0; i < size; i++)
        {
            if (predicate.accept(string.codePointAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return a new string with all of the characters that return true for the specified {@code predicate}.
     */
    public static String select(String string, CharPredicate predicate)
    {
        int size = string.length();
        StringBuilder buffer = new StringBuilder(string.length());
        for (int i = 0; i < size; i++)
        {
            char character = string.charAt(i);
            if (predicate.accept(character))
            {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }

    /**
     * @return a new string with all of the code points that return true for the specified {@code predicate}.
     */
    public static String select(String string, CodePointPredicate predicate)
    {
        int size = string.length();
        StringBuilder buffer = new StringBuilder(string.length());
        for (int i = 0; i < size; i++)
        {
            int codePoint = string.codePointAt(i);
            if (predicate.accept(codePoint))
            {
                buffer.appendCodePoint(codePoint);
            }
        }
        return buffer.toString();
    }

    /**
     * @return a new string excluding all of the characters that return true for the specified {@code predicate}.
     */
    public static String reject(String string, CharPredicate predicate)
    {
        int size = string.length();
        StringBuilder buffer = new StringBuilder(string.length());
        for (int i = 0; i < size; i++)
        {
            char character = string.charAt(i);
            if (!predicate.accept(character))
            {
                buffer.append(character);
            }
        }
        return buffer.toString();
    }

    /**
     * @return a new string excluding all of the code points that return true for the specified {@code predicate}.
     */
    public static String reject(String string, CodePointPredicate predicate)
    {
        int size = string.length();
        StringBuilder buffer = new StringBuilder(string.length());
        for (int i = 0; i < size; i++)
        {
            int codePoint = string.codePointAt(i);
            if (!predicate.accept(codePoint))
            {
                buffer.appendCodePoint(codePoint);
            }
        }
        return buffer.toString();
    }

    public static String getLastToken(String value, String separator)
    {
        if (StringIterate.notEmpty(value))
        {
            int lastIndex = value.lastIndexOf(separator);
            if (lastIndex > -1)
            {
                return value.substring(lastIndex + separator.length());
            }
            return value;
        }
        return value == null ? null : "";
    }

    public static String getFirstToken(String value, String separator)
    {
        if (StringIterate.notEmpty(value))
        {
            int firstIndex = value.indexOf(separator);
            if (firstIndex > -1)
            {
                return value.substring(0, firstIndex);
            }
            return value;
        }
        return value == null ? null : "";
    }

    public static boolean isEmpty(String string)
    {
        return string == null || string.length() == 0;
    }

    public static boolean isEmptyOrWhitespace(String string)
    {
        return StringIterate.isEmpty(string) || StringIterate.isWhitespace(string);
    }

    private static boolean isWhitespace(String string)
    {
        return StringIterate.allSatisfy(string, CodePointPredicate.IS_WHITESPACE);
    }

    public static boolean isNumber(String string)
    {
        return StringIterate.charactersSatisfy(string, CodePointPredicate.IS_DIGIT);
    }

    public static boolean isAlphaNumeric(String string)
    {
        return StringIterate.charactersSatisfy(string, CodePointPredicate.IS_LETTER_OR_DIGIT);
    }

    private static boolean charactersSatisfy(String string, CodePointPredicate predicate)
    {
        return !"".equals(string) && StringIterate.allSatisfy(string, predicate);
    }

    public static boolean notEmpty(String string)
    {
        return !StringIterate.isEmpty(string);
    }

    public static boolean notEmptyOrWhitespace(String string)
    {
        return !StringIterate.isEmptyOrWhitespace(string);
    }

    public static String repeat(String template, int repeatTimes)
    {
        StringBuilder buf = new StringBuilder(template.length() * repeatTimes);
        for (int i = 0; i < repeatTimes; i++)
        {
            buf.append(template);
        }
        return buf.toString();
    }

    public static String repeat(char c, int repeatTimes)
    {
        StringBuilder buf = new StringBuilder(repeatTimes);
        for (int i = 0; i < repeatTimes; i++)
        {
            buf.append(c);
        }
        return buf.toString();
    }

    public static String padOrTrim(String message, int targetLength)
    {
        int messageLength = message.length();
        if (messageLength >= targetLength)
        {
            return message.substring(0, targetLength);
        }
        return message + StringIterate.repeat(' ', targetLength - messageLength);
    }

    public static MutableList<Character> toList(String string)
    {
        MutableList<Character> characters = FastList.newList(string.length());
        StringIterate.forEach(string, new AddCharacterToCollection(characters));
        return characters;
    }

    public static MutableList<Character> toLowercaseList(String string)
    {
        MutableList<Character> characters = FastList.newList();
        StringIterate.forEach(string, new AddLowercaseCharacterToCollection(characters));
        return characters;
    }

    public static MutableList<Character> toUppercaseList(String string)
    {
        MutableList<Character> characters = FastList.newList();
        StringIterate.forEach(string, new AddUppercaseCharacterToCollection(characters));
        return characters;
    }

    public static MutableBag<Character> toBag(String string)
    {
        MutableBag<Character> characters = HashBag.newBag();
        StringIterate.forEach(string, new AddCharacterToCollection(characters));
        return characters;
    }

    public static MutableBag<Character> toLowercaseBag(String string)
    {
        MutableBag<Character> characters = HashBag.newBag();
        StringIterate.forEach(string, new AddLowercaseCharacterToCollection(characters));
        return characters;
    }

    public static MutableBag<Character> toUppercaseBag(String string)
    {
        MutableBag<Character> characters = HashBag.newBag();
        StringIterate.forEach(string, new AddUppercaseCharacterToCollection(characters));
        return characters;
    }

    public static MutableSet<Character> toSet(String string)
    {
        MutableSet<Character> characters = UnifiedSet.newSet();
        StringIterate.forEach(string, new AddCharacterToCollection(characters));
        return characters;
    }

    /**
     * Partitions String in fixed size chunks.
     *
     * @param size the number of characters per chunk
     * @return A {@code MutableList} containing {@code String}s of size {@code size}, except the last will be
     * truncated (i.e. shorter) if the characters don't divide evenly.
     * @since 5.2
     */
    public static MutableList<String> chunk(String string, int size)
    {
        if (size <= 0)
        {
            throw new IllegalArgumentException("Size for groups must be positive but was: " + size);
        }

        int length = string.length();

        if (length == 0)
        {
            return FastList.newList();
        }

        MutableList<String> result = FastList.newList((length + size - 1) / size);

        int startOffset = 0;
        while (startOffset < length)
        {
            result.add(string.substring(startOffset, Math.min(startOffset + size, length)));
            startOffset += size;
        }

        return result;
    }

    /**
     * @deprecated in 3.0. Inlineable.
     */
    @Deprecated
    public static MutableSet<Character> asUppercaseSet(String string)
    {
        return StringIterate.toUppercaseSet(string);
    }

    public static MutableSet<Character> toUppercaseSet(String string)
    {
        MutableSet<Character> characters = UnifiedSet.newSet();
        StringIterate.forEach(string, new AddUppercaseCharacterToCollection(characters));
        return characters;
    }

    /**
     * @deprecated in 3.0. Inlineable.
     */
    @Deprecated
    public static MutableSet<Character> asLowercaseSet(String string)
    {
        return StringIterate.toLowercaseSet(string);
    }

    public static MutableSet<Character> toLowercaseSet(String string)
    {
        MutableSet<Character> characters = UnifiedSet.newSet();
        StringIterate.forEach(string, new AddLowercaseCharacterToCollection(characters));
        return characters;
    }

    public static Twin<String> splitAtIndex(String aString, int index)
    {
        return Tuples.twin(aString.substring(0, index), aString.substring(index, aString.length()));
    }

    private static final class AddCharacterToCollection implements CharProcedure
    {
        private final MutableCollection<Character> characters;

        private AddCharacterToCollection(MutableCollection<Character> characters)
        {
            this.characters = characters;
        }

        public void value(char character)
        {
            this.characters.add(Character.valueOf(character));
        }
    }

    private static final class AddLowercaseCharacterToCollection implements CharProcedure
    {
        private final MutableCollection<Character> characters;

        private AddLowercaseCharacterToCollection(MutableCollection<Character> characters)
        {
            this.characters = characters;
        }

        public void value(char character)
        {
            this.characters.add(Character.valueOf(Character.toLowerCase(character)));
        }
    }

    private static final class AddUppercaseCharacterToCollection implements CharProcedure
    {
        private final MutableCollection<Character> characters;

        private AddUppercaseCharacterToCollection(MutableCollection<Character> characters)
        {
            this.characters = characters;
        }

        public void value(char character)
        {
            this.characters.add(Character.valueOf(Character.toUpperCase(character)));
        }
    }
}
