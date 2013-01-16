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

public class ShortPredicatesTest
{
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
}
