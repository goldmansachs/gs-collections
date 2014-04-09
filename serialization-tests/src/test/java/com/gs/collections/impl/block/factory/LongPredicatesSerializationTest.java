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

public class LongPredicatesSerializationTest
{
    @Test
    public void isZero()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkxvbmdQcmVkaWNh\n"
                        + "dGVzJExvbmdJc1plcm8AAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                LongPredicates.isZero());
    }

    @Test
    public void isPositive()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkxvbmdQcmVkaWNh\n"
                        + "dGVzJExvbmdJc1Bvc2l0aXZlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                LongPredicates.isPositive());
    }

    @Test
    public void isNegative()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkxvbmdQcmVkaWNh\n"
                        + "dGVzJExvbmdJc05lZ2F0aXZlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                LongPredicates.isNegative());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkxvbmdQcmVkaWNh\n"
                        + "dGVzJExvbmdJc09kZAAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                LongPredicates.isOdd());
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkxvbmdQcmVkaWNh\n"
                        + "dGVzJExvbmdJc0V2ZW4AAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                LongPredicates.isEven());
    }
}
