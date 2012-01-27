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

package ponzu.impl.utility;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.list.MutableList;
import ponzu.api.map.MutableMap;
import ponzu.api.set.MutableSet;
import ponzu.impl.block.function.primitive.CharFunction;
import ponzu.impl.block.function.primitive.CodePointFunction;
import ponzu.impl.block.predicate.CodePointPredicate;
import ponzu.impl.block.predicate.primitive.CharPredicate;
import ponzu.impl.block.procedure.primitive.CharProcedure;
import ponzu.impl.block.procedure.primitive.CodePointProcedure;
import ponzu.impl.factory.Lists;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.set.mutable.UnifiedSet;

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
     */
    public static MutableList<String> csvTokensToSortedList(String string)
    {
        return StringIterate.tokensToSortedList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a sorted {@link MutableList}.
     */
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
     */
    public static MutableList<String> csvTokensToList(String string)
    {
        return StringIterate.tokensToList(string, ",");
    }

    /**
     * Converts a string of tokens separated by commas to a {@link MutableList}.
     */
    public static MutableList<String> csvTrimmedTokensToList(String string)
    {
        return StringIterate.trimmedTokensToList(string, ",");
    }

    /**
     * Converts a string of tokens separated by the specified separator to a {@link MutableList}.
     */
    public static MutableList<String> tokensToList(String string, String separator)
    {
        MutableList<String> list = Lists.mutable.of();
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
     */
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
     */
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
     * {@link Iterate#foldLeft(Object, Iterable, Function2)}
     */
    public static <T, R> R foldLeft(
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
        if (!string.isEmpty())
        {
            for (int i = string.length() - 1; i >= 0; i--)
            {
                procedure.value(string.codePointAt(i));
            }
        }
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

    public static String transform(String string, CharFunction function)
    {
        int size = string.length();
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++)
        {
            builder.append(function.valueOf(string.charAt(i)));
        }
        return builder.toString();
    }

    public static String transform(String string, CodePointFunction function)
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
        if (StringIterate.anySatisfy(string, CharPredicate.IS_LOWERCASE))
        {
            return StringIterate.transform(string, CharFunction.TO_UPPERCASE);
        }
        return string;
    }

    public static String englishToLowerCase(String string)
    {
        if (StringIterate.anySatisfy(string, CharPredicate.IS_UPPERCASE))
        {
            return StringIterate.transform(string, CharFunction.TO_LOWERCASE);
        }
        return string;
    }

    /**
     * Find the first element that returns true for the specified {@code predicate}.
     */
    public static Character find(String string, CharPredicate predicate)
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
    public static Character findIfNone(String string, CharPredicate predicate, char resultIfNone)
    {
        Character result = StringIterate.find(string, predicate);
        return result == null ? Character.valueOf(resultIfNone) : result;
    }

    /**
     * Find the first element that returns true for the specified {@code predicate}.  Return the first char of the
     * default string if no value is found.
     */
    public static Character findIfNone(String string, CharPredicate predicate, String resultIfNone)
    {
        Character result = StringIterate.find(string, predicate);
        return result == null ? Character.valueOf(resultIfNone.charAt(0)) : result;
    }

    /**
     * Count the number of occurrences of the specified char.
     */
    public static int occurrencesOf(String string, final char value)
    {
        return count(string, new CharPredicate()
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
        return count(string, new CodePointPredicate()
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
     * @return a new string with all of the characters that return true for the specified {@code predicate}.
     */
    public static String filter(String string, CharPredicate predicate)
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
    public static String filter(String string, CodePointPredicate predicate)
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
    public static String filterNot(String string, CharPredicate predicate)
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
    public static String filterNot(String string, CodePointPredicate predicate)
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
        return allSatisfy(string, CodePointPredicate.IS_WHITESPACE);
    }

    public static boolean isNumber(String string)
    {
        return charactersSatisfy(string, CodePointPredicate.IS_DIGIT);
    }

    public static boolean isAlphaNumeric(String string)
    {
        return charactersSatisfy(string, CodePointPredicate.IS_LETTER_OR_DIGIT);
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

    public static MutableSet<Character> toSet(String string)
    {
        final MutableSet<Character> characters = UnifiedSet.newSet();
        forEach(string, new CharProcedure()
        {
            public void value(char character)
            {
                characters.add(character);
            }
        });
        return characters;
    }

    public static MutableSet<Character> asUppercaseSet(String string)
    {
        final MutableSet<Character> characters = UnifiedSet.newSet();
        forEach(string, new CharProcedure()
        {
            public void value(char character)
            {
                characters.add(Character.toUpperCase(character));
            }
        });
        return characters;
    }

    public static MutableSet<Character> asLowercaseSet(String string)
    {
        final MutableSet<Character> characters = UnifiedSet.newSet();
        forEach(string, new CharProcedure()
        {
            public void value(char character)
            {
                characters.add(Character.toLowerCase(character));
            }
        });
        return characters;
    }
}
