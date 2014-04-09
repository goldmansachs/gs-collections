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

public class LongPredicatesSerializationTest
{
    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRBbHdheXNGYWxzZUxvbmdQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                LongPredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRBbHdheXNUcnVlTG9uZ1ByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                LongPredicates.alwaysTrue());
    }

    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRFcXVhbHNMb25nUHJlZGljYXRlAAAAAAAAAAECAAFKAAhleHBlY3RlZHhw\n"
                        + "AAAAAAAAAAA=",
                LongPredicates.equal(0L));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRMZXNzVGhhbkxvbmdQcmVkaWNhdGUAAAAAAAAAAQIAAUoACGV4cGVjdGVk\n"
                        + "eHAAAAAAAAAAAA==",
                LongPredicates.lessThan(0L));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRHcmVhdGVyVGhhbkxvbmdQcmVkaWNhdGUAAAAAAAAAAQIAAUoACGV4cGVj\n"
                        + "dGVkeHAAAAAAAAAAAA==",
                LongPredicates.greaterThan(0L));
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRMb25nSXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                LongPredicates.isEven());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRMb25nSXNPZGRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                LongPredicates.isOdd());
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRBbmRMb25nUHJlZGljYXRlAAAAAAAAAAECAAJMAANvbmV0AEBMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0xvbmdQcmVkaWNhdGU7\n"
                        + "TAADdHdvcQB+AAF4cHNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnBy\n"
                        + "aW1pdGl2ZS5Mb25nUHJlZGljYXRlcyRMb25nSXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cHNy\n"
                        + "AFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5Mb25nUHJl\n"
                        + "ZGljYXRlcyRMb25nSXNPZGRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                LongPredicates.and(LongPredicates.isEven(), LongPredicates.isOdd()));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyRPckxvbmdQcmVkaWNhdGUAAAAAAAAAAQIAAkwAA29uZXQAQExjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvTG9uZ1ByZWRpY2F0ZTtM\n"
                        + "AAN0d29xAH4AAXhwc3IAUmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJp\n"
                        + "bWl0aXZlLkxvbmdQcmVkaWNhdGVzJExvbmdJc0V2ZW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhwc3IA\n"
                        + "UWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJpbWl0aXZlLkxvbmdQcmVk\n"
                        + "aWNhdGVzJExvbmdJc09kZFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                LongPredicates.or(LongPredicates.isEven(), LongPredicates.isOdd()));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5M\n"
                        + "b25nUHJlZGljYXRlcyROb3RMb25nUHJlZGljYXRlAAAAAAAAAAECAAFMAAZuZWdhdGV0AEBMY29t\n"
                        + "L2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0xvbmdQcmVkaWNh\n"
                        + "dGU7eHBzcgBSY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUu\n"
                        + "TG9uZ1ByZWRpY2F0ZXMkTG9uZ0lzRXZlblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                LongPredicates.not(LongPredicates.isEven()));
    }
}
