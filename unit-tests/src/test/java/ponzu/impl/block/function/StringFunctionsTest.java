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

package ponzu.impl.block.function;

import ponzu.api.block.function.Function;
import ponzu.impl.block.factory.StringFunctions;
import org.junit.Assert;
import org.junit.Test;

public final class StringFunctionsTest
{
    @Test
    public void toUpperCase()
    {
        Function<String, String> function = StringFunctions.toUpperCase();
        Assert.assertEquals("UPPER", function.valueOf("upper"));
        Assert.assertEquals("UPPER", function.valueOf("Upper"));
        Assert.assertEquals("UPPER", function.valueOf("UPPER"));
        Assert.assertSame("UPPER", function.valueOf("UPPER"));
    }

    @Test
    public void toLowerCase()
    {
        Function<String, String> function = StringFunctions.toLowerCase();
        Assert.assertEquals("lower", function.valueOf("LOWER"));
        Assert.assertEquals("lower", function.valueOf("Lower"));
        Assert.assertEquals("lower", function.valueOf("lower"));
        Assert.assertSame("lower", function.valueOf("lower"));
    }

    @Test
    public void length()
    {
        Function<String, Integer> function = StringFunctions.length();
        Assert.assertEquals(Integer.valueOf(6), function.valueOf("string"));
        Assert.assertEquals(Integer.valueOf(0), function.valueOf(""));
    }

    @Test
    public void trim()
    {
        Function<String, String> function = StringFunctions.trim();
        Assert.assertEquals("trim", function.valueOf("trim "));
        Assert.assertEquals("trim", function.valueOf(" trim"));
        Assert.assertEquals("trim", function.valueOf("  trim  "));
        Assert.assertEquals("trim", function.valueOf("trim"));
        Assert.assertSame("trim", function.valueOf("trim"));
    }

    @Test
    public void firstLetter()
    {
        Function<String, Character> function = StringFunctions.firstLetter();
        Assert.assertNull(function.valueOf(null));
        Assert.assertNull(function.valueOf(""));
        Assert.assertEquals('A', function.valueOf("Autocthonic").charValue());
    }

    @Test
    public void subString()
    {
        Assert.assertEquals("bS", StringFunctions.subString(2, 4).valueOf("subString"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void subString_throws_on_short_string()
    {
        StringFunctions.subString(2, 4).valueOf("hi");
    }

    @Test(expected = NullPointerException.class)
    public void subString_throws_on_null()
    {
        StringFunctions.subString(2, 4).valueOf(null);
    }
}
