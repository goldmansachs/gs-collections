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

public class CharPredicatesTest
{
    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRFcXVhbHNDaGFyUHJlZGljYXRlAAAAAAAAAAECAAFDAAhleHBlY3RlZHhw\n"
                        + "AAA=",
                CharPredicates.equal((char) 0));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRMZXNzVGhhbkNoYXJQcmVkaWNhdGUAAAAAAAAAAQIAAUMACGV4cGVjdGVk\n"
                        + "eHAAAA==",
                CharPredicates.lessThan((char) 0));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRHcmVhdGVyVGhhbkNoYXJQcmVkaWNhdGUAAAAAAAAAAQIAAUMACGV4cGVj\n"
                        + "dGVkeHAAAA==",
                CharPredicates.greaterThan((char) 0));
    }
}
