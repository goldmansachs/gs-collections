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

public class IntegerPredicatesSerializationTest
{
    @Test
    public void isZero()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkludGVnZXJQcmVk\n"
                        + "aWNhdGVzJEludGVnZXJJc1plcm8AAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                IntegerPredicates.isZero());
    }

    @Test
    public void isPositive()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkludGVnZXJQcmVk\n"
                        + "aWNhdGVzJEludGVnZXJJc1Bvc2l0aXZlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                IntegerPredicates.isPositive());
    }

    @Test
    public void isNegative()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkludGVnZXJQcmVk\n"
                        + "aWNhdGVzJEludGVnZXJJc05lZ2F0aXZlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                IntegerPredicates.isNegative());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkludGVnZXJQcmVk\n"
                        + "aWNhdGVzJEludGVnZXJJc09kZAAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                IntegerPredicates.isOdd());
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkludGVnZXJQcmVk\n"
                        + "aWNhdGVzJEludGVnZXJJc0V2ZW4AAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                IntegerPredicates.isEven());
    }
}
