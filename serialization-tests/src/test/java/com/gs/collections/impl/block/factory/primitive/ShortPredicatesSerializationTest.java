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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ShortPredicatesSerializationTest
{
    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkQWx3YXlzRmFsc2VTaG9ydFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                ShortPredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkQWx3YXlzVHJ1ZVNob3J0UHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                ShortPredicates.alwaysTrue());
    }

    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkRXF1YWxzU2hvcnRQcmVkaWNhdGUAAAAAAAAAAQIAAVMACGV4cGVjdGVk\n"
                        + "eHAAAA==",
                ShortPredicates.equal((short) 0));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkTGVzc1RoYW5TaG9ydFByZWRpY2F0ZQAAAAAAAAABAgABUwAIZXhwZWN0\n"
                        + "ZWR4cAAA",
                ShortPredicates.lessThan((short) 0));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkR3JlYXRlclRoYW5TaG9ydFByZWRpY2F0ZQAAAAAAAAABAgABUwAIZXhw\n"
                        + "ZWN0ZWR4cAAA",
                ShortPredicates.greaterThan((short) 0));
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkU2hvcnRJc0V2ZW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                ShortPredicates.isEven());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkU2hvcnRJc09kZFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                ShortPredicates.isOdd());
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkQW5kU2hvcnRQcmVkaWNhdGUAAAAAAAAAAQIAAkwAA29uZXQAQUxjb20v\n"
                        + "Z3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvU2hvcnRQcmVkaWNh\n"
                        + "dGU7TAADdHdvcQB+AAF4cHNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5\n"
                        + "LnByaW1pdGl2ZS5TaG9ydFByZWRpY2F0ZXMkU2hvcnRJc0V2ZW5QcmVkaWNhdGUAAAAAAAAAAQIA\n"
                        + "AHhwc3IAU2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJpbWl0aXZlLlNo\n"
                        + "b3J0UHJlZGljYXRlcyRTaG9ydElzT2RkUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                ShortPredicates.and(ShortPredicates.isEven(), ShortPredicates.isOdd()));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkT3JTaG9ydFByZWRpY2F0ZQAAAAAAAAABAgACTAADb25ldABBTGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL3ByaW1pdGl2ZS9TaG9ydFByZWRpY2F0\n"
                        + "ZTtMAAN0d29xAH4AAXhwc3IAVGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rvcnku\n"
                        + "cHJpbWl0aXZlLlNob3J0UHJlZGljYXRlcyRTaG9ydElzRXZlblByZWRpY2F0ZQAAAAAAAAABAgAA\n"
                        + "eHBzcgBTY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUuU2hv\n"
                        + "cnRQcmVkaWNhdGVzJFNob3J0SXNPZGRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                ShortPredicates.or(ShortPredicates.isEven(), ShortPredicates.isOdd()));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5T\n"
                        + "aG9ydFByZWRpY2F0ZXMkTm90U2hvcnRQcmVkaWNhdGUAAAAAAAAAAQIAAUwABm5lZ2F0ZXQAQUxj\n"
                        + "b20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvU2hvcnRQcmVk\n"
                        + "aWNhdGU7eHBzcgBUY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRp\n"
                        + "dmUuU2hvcnRQcmVkaWNhdGVzJFNob3J0SXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                ShortPredicates.not(ShortPredicates.isEven()));
    }
}
