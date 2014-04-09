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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class StringFunctionsSerializationTest
{
    @Test
    public void length()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRMZW5ndGhGdW5jdGlvbgAAAAAAAAABAgAAeHIARGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZ1bmN0aW9uLnByaW1pdGl2ZS5JbnRlZ2VyRnVuY3Rpb25JbXBsAAAAAAAAAAECAAB4\n"
                        + "cA==",
                StringFunctions.length());
    }

    @Test
    public void toLowerCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb0xvd2VyQ2FzZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toLowerCase());
    }

    @Test
    public void toUpperCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1VwcGVyQ2FzZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toUpperCase());
    }

    @Test
    public void toInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb0ludGVnZXJGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.toInteger());
    }

    @Test
    public void trim()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUcmltRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                StringFunctions.trim());
    }

    @Test
    public void subString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRTdWJTdHJpbmdGdW5jdGlvbgAAAAAAAAABAgACSQAKYmVnaW5JbmRleEkACGVuZEluZGV4\n"
                        + "eHAAAAAAAAAAAQ==",
                StringFunctions.subString(0, 1));
    }

    @Test
    public void toPrimitiveBoolean()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUJvb2xlYW5GdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.toPrimitiveBoolean());
    }

    @Test
    public void toPrimitiveByte()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUJ5dGVGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.toPrimitiveByte());
    }

    @Test
    public void toPrimitiveChar()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUNoYXJGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.toPrimitiveChar());
    }

    @Test
    public void toFirstChar()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb0ZpcnN0Q2hhckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toFirstChar());
    }

    @Test
    public void toFirstLetter()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRGaXJzdExldHRlckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.firstLetter());
    }

    @Test
    public void toPrimitiveDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZURvdWJsZUZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toPrimitiveDouble());
    }

    @Test
    public void toPrimitiveFloat()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUZsb2F0RnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                StringFunctions.toPrimitiveFloat());
    }

    @Test
    public void toPrimitiveInt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUludEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                StringFunctions.toPrimitiveInt());
    }

    @Test
    public void toPrimitiveLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZUxvbmdGdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                StringFunctions.toPrimitiveLong());
    }

    @Test
    public void toPrimitiveShort()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRUb1ByaW1pdGl2ZVNob3J0RnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                StringFunctions.toPrimitiveShort());
    }

    @Test
    public void append()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRBcHBlbmRGdW5jdGlvbgAAAAAAAAABAgABTAANdmFsdWVUb0FwcGVuZHQAEkxqYXZhL2xh\n"
                        + "bmcvU3RyaW5nO3hwcA==",
                StringFunctions.append(null));
    }

    @Test
    public void prepend()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ0Z1bmN0\n"
                        + "aW9ucyRQcmVwZW5kRnVuY3Rpb24AAAAAAAAAAQIAAUwADnZhbHVlVG9QcmVwZW5kdAASTGphdmEv\n"
                        + "bGFuZy9TdHJpbmc7eHBw",
                StringFunctions.prepend(null));
    }
}
