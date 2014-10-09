/*
 * Copyright 2014 Goldman Sachs.
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

public class Predicates2SerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JFRocm93aW5nUHJlZGljYXRlMkFkYXB0ZXIAAAAAAAAAAQIAAUwAEnRocm93aW5nUHJlZGljYXRl\n"
                        + "MnQARExjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9wcmVkaWNhdGUvY2hlY2tlZC9UaHJv\n"
                        + "d2luZ1ByZWRpY2F0ZTI7eHIAQWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLnByZWRpY2F0\n"
                        + "ZS5jaGVja2VkLkNoZWNrZWRQcmVkaWNhdGUyAAAAAAAAAAECAAB4cHA=",
                Predicates2.throwing(null));
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEFsd2F5c1RydWUAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "YWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                Predicates2.alwaysTrue());
    }

    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEFsd2F5c0ZhbHNlAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2su\n"
                        + "ZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.alwaysFalse());
    }

    @Test
    public void attributeEqual()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEF0dHJpYnV0ZVByZWRpY2F0ZXMyAAAAAAAAAAECAAJMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29s\n"
                        + "bGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wACXByZWRpY2F0ZXQAM0xjb20v\n"
                        + "Z3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9QcmVkaWNhdGUyO3hyADFjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cHBz\n"
                        + "cgA3Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzMiRFcXVh\n"
                        + "bAAAAAAAAAABAgAAeHEAfgAD",
                Predicates2.attributeEqual(null));
    }

    @Test
    public void instanceOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JElzSW5zdGFuY2VPZgAAAAAAAAABAgAAeHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhw",
                Predicates2.instanceOf());
    }

    @Test
    public void notInstanceOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdEluc3RhbmNlT2YAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                Predicates2.notInstanceOf());
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JExlc3NUaGFuAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFj\n"
                        + "dG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.<Integer>lessThan());
    }

    @Test
    public void lessThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JExlc3NUaGFuT3JFcXVhbAAAAAAAAAABAgAAeHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJs\n"
                        + "b2NrLmZhY3RvcnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhw",
                Predicates2.<Integer>lessThanOrEqualTo());
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEdyZWF0ZXJUaGFuAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2su\n"
                        + "ZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.<Integer>greaterThan());
    }

    @Test
    public void greaterThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEdyZWF0ZXJUaGFuT3JFcXVhbAAAAAAAAAABAgAAeHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhw",
                Predicates2.<Integer>greaterThanOrEqualTo());
    }

    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEVxdWFsAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9y\n"
                        + "eS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.equal());
    }

    @Test
    public void notEqual()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdEVxdWFsAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFj\n"
                        + "dG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.notEqual());
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEFuZAAAAAAAAAABAgACTAAEbGVmdHQAM0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3By\n"
                        + "ZWRpY2F0ZS9QcmVkaWNhdGUyO0wABXJpZ2h0cQB+AAF4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHBwcA==",
                Predicates2.and(null, null));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE9yAAAAAAAAAAECAAJMAARsZWZ0dAAzTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJl\n"
                        + "ZGljYXRlL1ByZWRpY2F0ZTI7TAAFcmlnaHRxAH4AAXhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cHBw",
                Predicates2.or(null, null));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdAAAAAAAAAABAgABTAAJcHJlZGljYXRldAAzTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxv\n"
                        + "Y2svcHJlZGljYXRlL1ByZWRpY2F0ZTI7eHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhwcA==",
                Predicates2.not(null));
    }

    @Test
    public void in()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JEluAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Q\n"
                        + "cmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.in());
    }

    @Test
    public void notIn()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdEluAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9y\n"
                        + "eS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.notIn());
    }

    @Test
    public void isNull()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JElzTnVsbAAAAAAAAAABAgAAeHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rv\n"
                        + "cnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhw",
                Predicates2.isNull());
    }

    @Test
    public void notNull()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdE51bGwAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0\n"
                        + "b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                Predicates2.notNull());
    }

    @Test
    public void sameAs()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JElzSWRlbnRpY2FsAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2su\n"
                        + "ZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.sameAs());
    }

    @Test
    public void notSameAs()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMy\n"
                        + "JE5vdElkZW50aXRpY2FsAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates2.notSameAs());
    }
}
