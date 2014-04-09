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

package com.gs.collections.impl.block.predicate.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CharPredicateSerializationTest
{
    @Test
    public void isUpperCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkMQAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_UPPERCASE);
    }

    @Test
    public void isLowerCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkMgAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_LOWERCASE);
    }

    @Test
    public void isDigit()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkMwAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_DIGIT);
    }

    @Test
    public void isDigitOrDot()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkNAAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_DIGIT_OR_DOT);
    }

    @Test
    public void isLetter()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkNQAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_LETTER);
    }

    @Test
    public void isLetterOrDigit()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkNgAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_LETTER_OR_DIGIT);
    }

    @Test
    public void isWhitespace()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkNwAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_WHITESPACE);
    }

    @Test
    public void isUndefined()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUucHJpbWl0aXZl\n"
                        + "LkNoYXJQcmVkaWNhdGUkOAAAAAAAAAABAgAAeHA=",
                CharPredicate.IS_UNDEFINED);
    }
}
