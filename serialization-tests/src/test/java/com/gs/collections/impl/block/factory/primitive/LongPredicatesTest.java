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

public class LongPredicatesTest
{
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
}
