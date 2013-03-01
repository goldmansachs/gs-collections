/*
 * Copyright 2013 Goldman Sachs.
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

import com.gs.collections.api.block.function.primitive.CharToCharFunction;

public final class CharToCharFunctions
{
    private static final CharToCharFunction TO_UPPERCASE = new ToUpperCaseCharToCharFunction();

    private static final CharToCharFunction TO_LOWERCASE = new ToLowerCaseCharToCharFunction();

    private CharToCharFunctions()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static CharToCharFunction toUpperCase()
    {
        return TO_UPPERCASE;
    }

    public static CharToCharFunction toLowerCase()
    {
        return TO_LOWERCASE;
    }

    private static class ToUpperCaseCharToCharFunction implements CharToCharFunction
    {
        private static final long serialVersionUID = 1L;

        public char valueOf(char character)
        {
            return Character.toUpperCase(character);
        }
    }

    private static class ToLowerCaseCharToCharFunction implements CharToCharFunction
    {
        private static final long serialVersionUID = 1L;

        public char valueOf(char character)
        {
            return Character.toLowerCase(character);
        }
    }
}
