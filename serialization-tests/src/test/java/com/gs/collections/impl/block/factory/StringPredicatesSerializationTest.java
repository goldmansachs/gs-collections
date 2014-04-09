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

public class StringPredicatesSerializationTest
{
    @Test
    public void empty()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkRW1wdHkAAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "YWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                StringPredicates.empty());
    }

    @Test
    public void notEmpty()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkTm90RW1wdHkAAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                StringPredicates.notEmpty());
    }

    @Test
    public void containsCharacter()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkQ29udGFpbnNDaGFyYWN0ZXIAAAAAAAAAAQIAAUMACWNoYXJhY3RlcnhyADBjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwACA=\n",
                StringPredicates.contains(' '));
    }

    @Test
    public void containsString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkQ29udGFpbnNTdHJpbmcAAAAAAAAAAQIAAUwAC290aGVyU3RyaW5ndAASTGphdmEvbGFu\n"
                        + "Zy9TdHJpbmc7eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGlj\n"
                        + "YXRlcwAAAAAAAAABAgAAeHBw",
                StringPredicates.contains(null));
    }

    @Test
    public void startsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkU3RhcnRzV2l0aAAAAAAAAAABAgABTAAJc3Vic3RyaW5ndAASTGphdmEvbGFuZy9TdHJp\n"
                        + "bmc7eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAA\n"
                        + "AAAAAAABAgAAeHBw",
                StringPredicates.startsWith(null));
    }

    @Test
    public void endsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkRW5kc1dpdGgAAAAAAAAAAQIAAUwACXN1YnN0cmluZ3QAEkxqYXZhL2xhbmcvU3RyaW5n\n"
                        + "O3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAA\n"
                        + "AAAAAQIAAHhwcA==",
                StringPredicates.endsWith(null));
    }

    @Test
    public void equalsIgnoreCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkRXF1YWxzSWdub3JlQ2FzZQAAAAAAAAABAgABTAALb3RoZXJTdHJpbmd0ABJMamF2YS9s\n"
                        + "YW5nL1N0cmluZzt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVk\n"
                        + "aWNhdGVzAAAAAAAAAAECAAB4cHA=",
                StringPredicates.equalsIgnoreCase(null));
    }

    @Test
    public void matches()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkTWF0Y2hlc1JlZ2V4AAAAAAAAAAECAAFMAAVyZWdleHQAEkxqYXZhL2xhbmcvU3RyaW5n\n"
                        + "O3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAA\n"
                        + "AAAAAQIAAHhwcA==",
                StringPredicates.matches(null));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkTGVzc1RoYW4AAAAAAAAAAQIAAUwABnN0cmluZ3QAEkxqYXZhL2xhbmcvU3RyaW5nO3hy\n"
                        + "ADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAA\n"
                        + "AQIAAHhwcA==",
                StringPredicates.lessThan(null));
    }

    @Test
    public void lessThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkTGVzc1RoYW5PckVxdWFsVG8AAAAAAAAAAQIAAUwABnN0cmluZ3QAEkxqYXZhL2xhbmcv\n"
                        + "U3RyaW5nO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0\n"
                        + "ZXMAAAAAAAAAAQIAAHhwcA==",
                StringPredicates.lessThanOrEqualTo(null));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkR3JlYXRlclRoYW4AAAAAAAAAAQIAAUwABnN0cmluZ3QAEkxqYXZhL2xhbmcvU3RyaW5n\n"
                        + "O3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAA\n"
                        + "AAAAAQIAAHhwcA==",
                StringPredicates.greaterThan(null));
    }

    @Test
    public void greaterThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkR3JlYXRlclRoYW5PckVxdWFsVG8AAAAAAAAAAQIAAUwABnN0cmluZ3QAEkxqYXZhL2xh\n"
                        + "bmcvU3RyaW5nO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRp\n"
                        + "Y2F0ZXMAAAAAAAAAAQIAAHhwcA==",
                StringPredicates.greaterThanOrEqualTo(null));
    }

    @Test
    public void hasLetters()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzTGV0dGVycwAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJs\n"
                        + "b2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                StringPredicates.hasLetters());
    }

    @Test
    public void hasDigits()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzRGlnaXRzAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasDigits());
    }

    @Test
    public void hasLettersOrDigits()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzTGV0dGVyc09yRGlnaXRzAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasLettersOrDigits());
    }

    @Test
    public void hasLettersAndDigits()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzTGV0dGVyc0FuZERpZ2l0cwAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9u\n"
                        + "cy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                StringPredicates.hasLettersAndDigits());
    }

    @Test
    public void hasSpaces()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzU3BhY2VzAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasSpaces());
    }

    @Test
    public void hasUpperCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzVXBwZXJjYXNlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasUpperCase());
    }

    @Test
    public void hasLowerCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzTG93ZXJjYXNlAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasLowerCase());
    }

    @Test
    public void hasUndefined()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSGFzVW5kZWZpbmVkAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.hasUndefined());
    }

    @Test
    public void isNumeric()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSXNOdW1lcmljAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                StringPredicates.isNumeric());
    }

    @Test
    public void isAlphanumeric()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSXNBbHBoYW51bWVyaWMAAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                StringPredicates.isAlphanumeric());
    }

    @Test
    public void isBlank()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSXNCbGFuawAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                StringPredicates.isBlank());
    }

    @Test
    public void notBlank()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkTm90QmxhbmsAAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                StringPredicates.notBlank());
    }

    @Test
    public void isAlpha()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMkSXNBbHBoYQAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                StringPredicates.isAlpha());
    }
}
