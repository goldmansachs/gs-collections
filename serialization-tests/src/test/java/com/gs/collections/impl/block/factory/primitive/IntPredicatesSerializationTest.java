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

public class IntPredicatesSerializationTest
{
    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEFsd2F5c0ZhbHNlSW50UHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                IntPredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEFsd2F5c1RydWVJbnRQcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                IntPredicates.alwaysTrue());
    }

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

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJEFuZEludFByZWRpY2F0ZQAAAAAAAAABAgACTAADb25ldAA/TGNvbS9ncy9j\n"
                        + "b2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL3ByaW1pdGl2ZS9JbnRQcmVkaWNhdGU7TAAD\n"
                        + "dHdvcQB+AAF4cHNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1p\n"
                        + "dGl2ZS5JbnRQcmVkaWNhdGVzJEludElzRXZlblByZWRpY2F0ZQAAAAAAAAABAgAAeHBzcgBPY29t\n"
                        + "LmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUuSW50UHJlZGljYXRl\n"
                        + "cyRJbnRJc09kZFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                IntPredicates.and(IntPredicates.isEven(), IntPredicates.isOdd()));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJE9ySW50UHJlZGljYXRlAAAAAAAAAAECAAJMAANvbmV0AD9MY29tL2dzL2Nv\n"
                        + "bGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0ludFByZWRpY2F0ZTtMAAN0\n"
                        + "d29xAH4AAXhwc3IAUGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkucHJpbWl0\n"
                        + "aXZlLkludFByZWRpY2F0ZXMkSW50SXNFdmVuUHJlZGljYXRlAAAAAAAAAAECAAB4cHNyAE9jb20u\n"
                        + "Z3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5JbnRQcmVkaWNhdGVz\n"
                        + "JEludElzT2RkUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                IntPredicates.or(IntPredicates.isEven(), IntPredicates.isOdd()));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5J\n"
                        + "bnRQcmVkaWNhdGVzJE5vdEludFByZWRpY2F0ZQAAAAAAAAABAgABTAAGbmVnYXRldAA/TGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL3ByaW1pdGl2ZS9JbnRQcmVkaWNhdGU7\n"
                        + "eHBzcgBQY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5wcmltaXRpdmUuSW50\n"
                        + "UHJlZGljYXRlcyRJbnRJc0V2ZW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhw",
                IntPredicates.not(IntPredicates.isEven()));
    }
}
