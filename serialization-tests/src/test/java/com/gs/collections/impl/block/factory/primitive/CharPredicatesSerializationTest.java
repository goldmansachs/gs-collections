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

public class CharPredicatesSerializationTest
{
    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRBbHdheXNGYWxzZUNoYXJQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                CharPredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRBbHdheXNUcnVlQ2hhclByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                CharPredicates.alwaysTrue());
    }

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

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRBbmRDaGFyUHJlZGljYXRlAAAAAAAAAAECAAJMAANvbmV0AEBMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0NoYXJQcmVkaWNhdGU7\n"
                        + "TAADdHdvcQB+AAF4cHNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnBy\n"
                        + "aW1pdGl2ZS5DaGFyUHJlZGljYXRlcyRMZXNzVGhhbkNoYXJQcmVkaWNhdGUAAAAAAAAAAQIAAUMA\n"
                        + "CGV4cGVjdGVkeHAAAHNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnBy\n"
                        + "aW1pdGl2ZS5DaGFyUHJlZGljYXRlcyRHcmVhdGVyVGhhbkNoYXJQcmVkaWNhdGUAAAAAAAAAAQIA\n"
                        + "AUMACGV4cGVjdGVkeHAAAA==",
                CharPredicates.and(CharPredicates.lessThan((char) 0), CharPredicates.greaterThan((char) 0)));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyRPckNoYXJQcmVkaWNhdGUAAAAAAAAAAQIAAkwAA29uZXQAQExjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvQ2hhclByZWRpY2F0ZTtM\n"
                        + "AAN0d29xAH4AAXhwc3IAVGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJp\n"
                        + "bWl0aXZlLkNoYXJQcmVkaWNhdGVzJExlc3NUaGFuQ2hhclByZWRpY2F0ZQAAAAAAAAABAgABQwAI\n"
                        + "ZXhwZWN0ZWR4cAAAc3IAV2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJp\n"
                        + "bWl0aXZlLkNoYXJQcmVkaWNhdGVzJEdyZWF0ZXJUaGFuQ2hhclByZWRpY2F0ZQAAAAAAAAABAgAB\n"
                        + "QwAIZXhwZWN0ZWR4cAAA",
                CharPredicates.or(CharPredicates.lessThan((char) 0), CharPredicates.greaterThan((char) 0)));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5D\n"
                        + "aGFyUHJlZGljYXRlcyROb3RDaGFyUHJlZGljYXRlAAAAAAAAAAECAAFMAAZuZWdhdGV0AEBMY29t\n"
                        + "L2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0NoYXJQcmVkaWNh\n"
                        + "dGU7eHBzcgBUY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUu\n"
                        + "Q2hhclByZWRpY2F0ZXMkTGVzc1RoYW5DaGFyUHJlZGljYXRlAAAAAAAAAAECAAFDAAhleHBlY3Rl\n"
                        + "ZHhwAAA=",
                CharPredicates.not(CharPredicates.lessThan((char) 0)));
    }
}
