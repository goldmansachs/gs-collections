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

public class BytePredicatesSerializationTest
{
    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRBbHdheXNGYWxzZUJ5dGVQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                BytePredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRBbHdheXNUcnVlQnl0ZVByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BytePredicates.alwaysTrue());
    }

    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRFcXVhbHNCeXRlUHJlZGljYXRlAAAAAAAAAAECAAFCAAhleHBlY3RlZHhw\n"
                        + "AA==",
                BytePredicates.equal((byte) 0));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRMZXNzVGhhbkJ5dGVQcmVkaWNhdGUAAAAAAAAAAQIAAUIACGV4cGVjdGVk\n"
                        + "eHAA",
                BytePredicates.lessThan((byte) 0));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRHcmVhdGVyVGhhbkJ5dGVQcmVkaWNhdGUAAAAAAAAAAQIAAUIACGV4cGVj\n"
                        + "dGVkeHAA",
                BytePredicates.greaterThan((byte) 0));
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRCeXRlSXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                BytePredicates.isEven());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRCeXRlSXNPZGRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                BytePredicates.isOdd());
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRBbmRCeXRlUHJlZGljYXRlAAAAAAAAAAECAAJMAANvbmV0AEBMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0J5dGVQcmVkaWNhdGU7\n"
                        + "TAADdHdvcQB+AAF4cHNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnBy\n"
                        + "aW1pdGl2ZS5CeXRlUHJlZGljYXRlcyRCeXRlSXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cHNy\n"
                        + "AFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5CeXRlUHJl\n"
                        + "ZGljYXRlcyRCeXRlSXNPZGRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                BytePredicates.and(BytePredicates.isEven(), BytePredicates.isOdd()));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyRPckJ5dGVQcmVkaWNhdGUAAAAAAAAAAQIAAkwAA29uZXQAQExjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvQnl0ZVByZWRpY2F0ZTtM\n"
                        + "AAN0d29xAH4AAXhwc3IAUmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJp\n"
                        + "bWl0aXZlLkJ5dGVQcmVkaWNhdGVzJEJ5dGVJc0V2ZW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhwc3IA\n"
                        + "UWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJpbWl0aXZlLkJ5dGVQcmVk\n"
                        + "aWNhdGVzJEJ5dGVJc09kZFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BytePredicates.or(BytePredicates.isEven(), BytePredicates.isOdd()));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "eXRlUHJlZGljYXRlcyROb3RCeXRlUHJlZGljYXRlAAAAAAAAAAECAAFMAAZuZWdhdGV0AEBMY29t\n"
                        + "L2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0J5dGVQcmVkaWNh\n"
                        + "dGU7eHBzcgBSY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUu\n"
                        + "Qnl0ZVByZWRpY2F0ZXMkQnl0ZUlzRXZlblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BytePredicates.not(BytePredicates.isEven()));
    }
}
