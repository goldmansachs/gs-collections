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

public class IntPredicatesTest
{
    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEVxdWFsc0ludFByZWRpY2F0ZQAAAAAAAAABAgABSQAIZXhwZWN0ZWR4cAAA\n"
                        + "AAA=",
                IntPredicates.equal(0));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJExlc3NUaGFuSW50UHJlZGljYXRlAAAAAAAAAAECAAFJAAhleHBlY3RlZHhw\n"
                        + "AAAAAA==",
                IntPredicates.lessThan(0));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEdyZWF0ZXJUaGFuSW50UHJlZGljYXRlAAAAAAAAAAECAAFJAAhleHBlY3Rl\n"
                        + "ZHhwAAAAAA==",
                IntPredicates.greaterThan(0));
    }

    @Test
    public void isEven()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEludElzRXZlblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                IntPredicates.isEven());
    }

    @Test
    public void isOdd()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEludElzT2RkUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                IntPredicates.isOdd());
    }
}
