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

package com.gs.collections.impl.block.predicate;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CodePointPredicateSerializationTest
{
    @Test
    public void isUpperCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDEAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_UPPERCASE);
    }

    @Test
    public void isLowerCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDIAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_LOWERCASE);
    }

    @Test
    public void isDigit()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDMAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_DIGIT);
    }

    @Test
    public void isLetter()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDQAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_LETTER);
    }

    @Test
    public void isLetterOrDigit()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDUAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_LETTER_OR_DIGIT);
    }

    @Test
    public void isWhitespace()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDYAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_WHITESPACE);
    }

    @Test
    public void isDefined()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuQ29kZVBvaW50\n"
                        + "UHJlZGljYXRlJDcAAAAAAAAAAQIAAHhw",
                CodePointPredicate.IS_UNDEFINED);
    }
}
