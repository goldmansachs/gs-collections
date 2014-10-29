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

import java.util.Collections;

import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class PredicatesSerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "VGhyb3dpbmdQcmVkaWNhdGVBZGFwdGVyAAAAAAAAAAECAAFMABF0aHJvd2luZ1ByZWRpY2F0ZXQA\n"
                        + "Q0xjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9wcmVkaWNhdGUvY2hlY2tlZC9UaHJvd2lu\n"
                        + "Z1ByZWRpY2F0ZTt4cgBAY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2sucHJlZGljYXRlLmNo\n"
                        + "ZWNrZWQuQ2hlY2tlZFByZWRpY2F0ZQAAAAAAAAABAgAAeHBw",
                Predicates.throwing(null));
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QWx3YXlzVHJ1ZQAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZh\n"
                        + "Y3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                Predicates.alwaysTrue());
    }

    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QWx3YXlzRmFsc2UAAAAAAAAAAQIAAHhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "YWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw",
                Predicates.alwaysFalse());
    }

    @Test
    public void adapt()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "UHJlZGljYXRlQWRhcHRlcgAAAAAAAAABAgABTAAJcHJlZGljYXRldAAyTGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTt4cgAwY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHA=",
                Predicates.adapt(null));
    }

    @Test
    public void attributePredicate()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QXR0cmlidXRlUHJlZGljYXRlAAAAAAAAAAECAAJMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVj\n"
                        + "dGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wACXByZWRpY2F0ZXQAMkxjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9QcmVkaWNhdGU7eHIAMGNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBwcA==",
                Predicates.attributePredicate(null, null));
    }

    @Test
    public void ifTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QXR0cmlidXRlVHJ1ZQAAAAAAAAABAgAAeHIAQ2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZhY3RvcnkuUHJlZGljYXRlcyRBdHRyaWJ1dGVQcmVkaWNhdGUAAAAAAAAAAQIAAkwACGZ1bmN0\n"
                        + "aW9udAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVuY3Rpb247TAAJ\n"
                        + "cHJlZGljYXRldAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL1ByZWRp\n"
                        + "Y2F0ZTt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVz\n"
                        + "AAAAAAAAAAECAAB4cHBzcgA7Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Q\n"
                        + "cmVkaWNhdGVzJFRydWVFcXVhbHMAAAAAAAAAAQIAAHhw",
                Predicates.ifTrue(null));
    }

    @Test
    public void ifFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QXR0cmlidXRlRmFsc2UAAAAAAAAAAQIAAHhyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9j\n"
                        + "ay5mYWN0b3J5LlByZWRpY2F0ZXMkQXR0cmlidXRlUHJlZGljYXRlAAAAAAAAAAECAAJMAAhmdW5j\n"
                        + "dGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wA\n"
                        + "CXByZWRpY2F0ZXQAMkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9QcmVk\n"
                        + "aWNhdGU7eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRl\n"
                        + "cwAAAAAAAAABAgAAeHBwc3IAPGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rvcnku\n"
                        + "UHJlZGljYXRlcyRGYWxzZUVxdWFscwAAAAAAAAABAgAAeHA=",
                Predicates.ifFalse(null));
    }

    @Test
    public void anySatisfy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QW55U2F0aXNmeQAAAAAAAAABAgABTAAJcHJlZGljYXRldAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9h\n"
                        + "cGkvYmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHA=",
                Predicates.anySatisfy(null));
    }

    @Test
    public void allSatisfy()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QWxsU2F0aXNmeQAAAAAAAAABAgABTAAJcHJlZGljYXRldAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9h\n"
                        + "cGkvYmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHA=",
                Predicates.allSatisfy(null));
    }

    @Test
    public void assignableFrom()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QXNzaWduYWJsZUZyb21QcmVkaWNhdGUAAAAAAAAAAQIAAUwABWNsYXp6dAARTGphdmEvbGFuZy9D\n"
                        + "bGFzczt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVz\n"
                        + "AAAAAAAAAAECAAB4cHZyABBqYXZhLmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cA==",
                Predicates.assignableFrom(Object.class));
    }

    @Test
    public void instanceOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SW5zdGFuY2VPZlByZWRpY2F0ZQAAAAAAAAABAgABTAAFY2xhenp0ABFMamF2YS9sYW5nL0NsYXNz\n"
                        + "O3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAA\n"
                        + "AAAAAQIAAHhwdnIAEGphdmEubGFuZy5PYmplY3QAAAAAAAAAAAAAAHhw",
                Predicates.instanceOf(Object.class));
    }

    @Test
    public void notInstanceOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90SW5zdGFuY2VPZlByZWRpY2F0ZQAAAAAAAAABAgABTAAFY2xhenp0ABFMamF2YS9sYW5nL0Ns\n"
                        + "YXNzO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMA\n"
                        + "AAAAAAAAAQIAAHhwdnIAEGphdmEubGFuZy5PYmplY3QAAAAAAAAAAAAAAHhw",
                Predicates.notInstanceOf(Object.class));
    }

    @Test
    public void lessThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "TGVzc1RoYW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5i\n"
                        + "bG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkQ29tcGFyZVRvUHJlZGljYXRlAAAAAAAAAAECAAFMAAlj\n"
                        + "b21wYXJlVG90ABZMamF2YS9sYW5nL0NvbXBhcmFibGU7eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
                        + "bXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBw",
                Predicates.lessThan((String) null));
    }

    @Test
    public void lessThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "TGVzc1RoYW5PckVxdWFsUHJlZGljYXRlAAAAAAAAAAECAAB4cgBDY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzJENvbXBhcmVUb1ByZWRpY2F0ZQAAAAAAAAAB\n"
                        + "AgABTAAJY29tcGFyZVRvdAAWTGphdmEvbGFuZy9Db21wYXJhYmxlO3hyADBjb20uZ3MuY29sbGVj\n"
                        + "dGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwcA==",
                Predicates.lessThanOrEqualTo((String) null));
    }

    @Test
    public void greaterThan()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "R3JlYXRlclRoYW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkQ29tcGFyZVRvUHJlZGljYXRlAAAAAAAAAAECAAFM\n"
                        + "AAljb21wYXJlVG90ABZMamF2YS9sYW5nL0NvbXBhcmFibGU7eHIAMGNvbS5ncy5jb2xsZWN0aW9u\n"
                        + "cy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBw",
                Predicates.greaterThan((String) null));
    }

    @Test
    public void greaterThanOrEqualTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "R3JlYXRlclRoYW5PckVxdWFsUHJlZGljYXRlAAAAAAAAAAECAAB4cgBDY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzJENvbXBhcmVUb1ByZWRpY2F0ZQAAAAAA\n"
                        + "AAABAgABTAAJY29tcGFyZVRvdAAWTGphdmEvbGFuZy9Db21wYXJhYmxlO3hyADBjb20uZ3MuY29s\n"
                        + "bGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwcA==",
                Predicates.greaterThanOrEqualTo((String) null));
    }

    @Test
    public void equal()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "RXF1YWxQcmVkaWNhdGUAAAAAAAAAAQIAAUwADWNvbXBhcmVPYmplY3R0ABJMamF2YS9sYW5nL09i\n"
                        + "amVjdDt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVz\n"
                        + "AAAAAAAAAAECAAB4cHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQ\n"
                        + "amF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAA",
                Predicates.equal(0));
    }

    @Test
    public void notEqual()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90RXF1YWxQcmVkaWNhdGUAAAAAAAAAAQIAAUwADWNvbXBhcmVPYmplY3R0ABJMamF2YS9sYW5n\n"
                        + "L09iamVjdDt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNh\n"
                        + "dGVzAAAAAAAAAAECAAB4cHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4\n"
                        + "cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAA",
                Predicates.notEqual(0));
    }

    @Test
    public void betweenExclusive()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QmV0d2VlbkV4Y2x1c2l2ZQAAAAAAAAABAgAAeHIAP2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJs\n"
                        + "b2NrLmZhY3RvcnkuUHJlZGljYXRlcyRSYW5nZVByZWRpY2F0ZQAAAAAAAAABAgABTAALY29tcGFy\n"
                        + "ZUZyb210ABZMamF2YS9sYW5nL0NvbXBhcmFibGU7eHIAQ2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcyRDb21wYXJlVG9QcmVkaWNhdGUAAAAAAAAAAQIAAUwA\n"
                        + "CWNvbXBhcmVUb3EAfgACeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rvcnku\n"
                        + "UHJlZGljYXRlcwAAAAAAAAABAgAAeHB0AABxAH4ABg==",
                Predicates.betweenExclusive("", ""));
    }

    @Test
    public void betweenInclusive()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QmV0d2VlbkluY2x1c2l2ZQAAAAAAAAABAgAAeHIAP2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJs\n"
                        + "b2NrLmZhY3RvcnkuUHJlZGljYXRlcyRSYW5nZVByZWRpY2F0ZQAAAAAAAAABAgABTAALY29tcGFy\n"
                        + "ZUZyb210ABZMamF2YS9sYW5nL0NvbXBhcmFibGU7eHIAQ2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcyRDb21wYXJlVG9QcmVkaWNhdGUAAAAAAAAAAQIAAUwA\n"
                        + "CWNvbXBhcmVUb3EAfgACeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rvcnku\n"
                        + "UHJlZGljYXRlcwAAAAAAAAABAgAAeHB0AABxAH4ABg==",
                Predicates.betweenInclusive("", ""));
    }

    @Test
    public void betweenInclusiveFrom()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QmV0d2VlbkluY2x1c2l2ZUZyb20AAAAAAAAAAQIAAHhyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkUmFuZ2VQcmVkaWNhdGUAAAAAAAAAAQIAAUwAC2Nv\n"
                        + "bXBhcmVGcm9tdAAWTGphdmEvbGFuZy9Db21wYXJhYmxlO3hyAENjb20uZ3MuY29sbGVjdGlvbnMu\n"
                        + "aW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkQ29tcGFyZVRvUHJlZGljYXRlAAAAAAAAAAEC\n"
                        + "AAFMAAljb21wYXJlVG9xAH4AAnhyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0\n"
                        + "b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwdAAAcQB+AAY=",
                Predicates.betweenInclusiveFrom("", ""));
    }

    @Test
    public void betweenInclusiveTo()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QmV0d2VlbkluY2x1c2l2ZVRvAAAAAAAAAAECAAB4cgA/Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzJFJhbmdlUHJlZGljYXRlAAAAAAAAAAECAAFMAAtjb21w\n"
                        + "YXJlRnJvbXQAFkxqYXZhL2xhbmcvQ29tcGFyYWJsZTt4cgBDY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzJENvbXBhcmVUb1ByZWRpY2F0ZQAAAAAAAAABAgAB\n"
                        + "TAAJY29tcGFyZVRvcQB+AAJ4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9y\n"
                        + "eS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHQAAHEAfgAG",
                Predicates.betweenInclusiveTo("", ""));
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QW5kUHJlZGljYXRlAAAAAAAAAAECAAJMAARsZWZ0dAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkv\n"
                        + "YmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTtMAAVyaWdodHEAfgABeHIAMGNvbS5ncy5jb2xsZWN0\n"
                        + "aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBwcA==",
                Predicates.and(null, null));
    }

    @Test
    public void andCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QW5kSXRlcmFibGVQcmVkaWNhdGUAAAAAAAAAAQIAAHhyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkQWJzdHJhY3RJdGVyYWJsZVByZWRpY2F0ZQAAAAAA\n"
                        + "AAABAgABTAAKcHJlZGljYXRlc3QAFExqYXZhL2xhbmcvSXRlcmFibGU7eHIAMGNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBzcgAaamF2\n"
                        + "YS51dGlsLkFycmF5cyRBcnJheUxpc3TZpDy+zYgG0gIAAVsAAWF0ABNbTGphdmEvbGFuZy9PYmpl\n"
                        + "Y3Q7eHB1cgAzW0xjb20uZ3MuY29sbGVjdGlvbnMuYXBpLmJsb2NrLnByZWRpY2F0ZS5QcmVkaWNh\n"
                        + "dGU7Q7YSGUSRPkwCAAB4cAAAAANwcHA=",
                Predicates.and(null, null, null));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "T3JQcmVkaWNhdGUAAAAAAAAAAQIAAkwABGxlZnR0ADJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9i\n"
                        + "bG9jay9wcmVkaWNhdGUvUHJlZGljYXRlO0wABXJpZ2h0cQB+AAF4cgAwY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHBw",
                Predicates.or(null, null));
    }

    @Test
    public void orCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "T3JJdGVyYWJsZVByZWRpY2F0ZQAAAAAAAAABAgAAeHIASmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcyRBYnN0cmFjdEl0ZXJhYmxlUHJlZGljYXRlAAAAAAAA\n"
                        + "AAECAAFMAApwcmVkaWNhdGVzdAAUTGphdmEvbGFuZy9JdGVyYWJsZTt4cgAwY29tLmdzLmNvbGxl\n"
                        + "Y3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cHNyABpqYXZh\n"
                        + "LnV0aWwuQXJyYXlzJEFycmF5TGlzdNmkPL7NiAbSAgABWwABYXQAE1tMamF2YS9sYW5nL09iamVj\n"
                        + "dDt4cHVyADNbTGNvbS5ncy5jb2xsZWN0aW9ucy5hcGkuYmxvY2sucHJlZGljYXRlLlByZWRpY2F0\n"
                        + "ZTtDthIZRJE+TAIAAHhwAAAAA3BwcA==",
                Predicates.or(null, null, null));
    }

    @Test
    public void neither()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "TmVpdGhlclByZWRpY2F0ZQAAAAAAAAABAgACTAAEbGVmdHQAMkxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "YXBpL2Jsb2NrL3ByZWRpY2F0ZS9QcmVkaWNhdGU7TAAFcmlnaHRxAH4AAXhyADBjb20uZ3MuY29s\n"
                        + "bGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwcHA=",
                Predicates.neither(null, null));
    }

    @Test
    public void noneOf()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm9uZU9mSXRlcmFibGVQcmVkaWNhdGUAAAAAAAAAAQIAAHhyAEpjb20uZ3MuY29sbGVjdGlvbnMu\n"
                        + "aW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMkQWJzdHJhY3RJdGVyYWJsZVByZWRpY2F0ZQAA\n"
                        + "AAAAAAABAgABTAAKcHJlZGljYXRlc3QAFExqYXZhL2xhbmcvSXRlcmFibGU7eHIAMGNvbS5ncy5j\n"
                        + "b2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBzcgAa\n"
                        + "amF2YS51dGlsLkFycmF5cyRBcnJheUxpc3TZpDy+zYgG0gIAAVsAAWF0ABNbTGphdmEvbGFuZy9P\n"
                        + "YmplY3Q7eHB1cgAzW0xjb20uZ3MuY29sbGVjdGlvbnMuYXBpLmJsb2NrLnByZWRpY2F0ZS5QcmVk\n"
                        + "aWNhdGU7Q7YSGUSRPkwCAAB4cAAAAANwcHA=",
                Predicates.noneOf(null, null, null));
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90UHJlZGljYXRlAAAAAAAAAAECAAFMAAlwcmVkaWNhdGV0ADJMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9wcmVkaWNhdGUvUHJlZGljYXRlO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwcA==",
                Predicates.not(null));
    }

    @Test
    public void in_SetIterable()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SW5TZXRJdGVyYWJsZVByZWRpY2F0ZQAAAAAAAAABAgABTAALc2V0SXRlcmFibGV0AChMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9zZXQvU2V0SXRlcmFibGU7eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
                        + "bXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBzcgBEY29tLmdzLmNvbGxl\n"
                        + "Y3Rpb25zLmltcGwuc2V0LmltbXV0YWJsZS5JbW11dGFibGVTZXRTZXJpYWxpemF0aW9uUHJveHkA\n"
                        + "AAAAAAAAAQwAAHhwdwQAAAAAeA==",
                Predicates.in(Sets.immutable.with()));
    }

    @Test
    public void notIn_SetIterable()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90SW5TZXRJdGVyYWJsZVByZWRpY2F0ZQAAAAAAAAABAgABTAALc2V0SXRlcmFibGV0AChMY29t\n"
                        + "L2dzL2NvbGxlY3Rpb25zL2FwaS9zZXQvU2V0SXRlcmFibGU7eHIAMGNvbS5ncy5jb2xsZWN0aW9u\n"
                        + "cy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHBzcgBEY29tLmdzLmNv\n"
                        + "bGxlY3Rpb25zLmltcGwuc2V0LmltbXV0YWJsZS5JbW11dGFibGVTZXRTZXJpYWxpemF0aW9uUHJv\n"
                        + "eHkAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                Predicates.notIn(Sets.immutable.with()));
    }

    @Test
    public void in_Set()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SW5TZXRQcmVkaWNhdGUAAAAAAAAAAQIAAUwAA3NldHQAD0xqYXZhL3V0aWwvU2V0O3hyADBjb20u\n"
                        + "Z3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhw\n"
                        + "c3IAIGphdmEudXRpbC5Db2xsZWN0aW9ucyRDaGVja2VkU2V0QSSbonrZ/6sCAAB4cgAnamF2YS51\n"
                        + "dGlsLkNvbGxlY3Rpb25zJENoZWNrZWRDb2xsZWN0aW9uFelt/RjmzG8CAANMAAFjdAAWTGphdmEv\n"
                        + "dXRpbC9Db2xsZWN0aW9uO0wABHR5cGV0ABFMamF2YS9sYW5nL0NsYXNzO1sAFnplcm9MZW5ndGhF\n"
                        + "bGVtZW50QXJyYXl0ABNbTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuc2V0Lm11dGFibGUuVW5pZmllZFNldAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeHZyABBq\n"
                        + "YXZhLmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cHA=",
                Predicates.in(Collections.checkedSet(Sets.mutable.with(), Object.class)));
    }

    @Test
    public void notIn_Set()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90SW5TZXRQcmVkaWNhdGUAAAAAAAAAAQIAAUwAA3NldHQAD0xqYXZhL3V0aWwvU2V0O3hyADBj\n"
                        + "b20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMAAAAAAAAAAQIA\n"
                        + "AHhwc3IAIGphdmEudXRpbC5Db2xsZWN0aW9ucyRDaGVja2VkU2V0QSSbonrZ/6sCAAB4cgAnamF2\n"
                        + "YS51dGlsLkNvbGxlY3Rpb25zJENoZWNrZWRDb2xsZWN0aW9uFelt/RjmzG8CAANMAAFjdAAWTGph\n"
                        + "dmEvdXRpbC9Db2xsZWN0aW9uO0wABHR5cGV0ABFMamF2YS9sYW5nL0NsYXNzO1sAFnplcm9MZW5n\n"
                        + "dGhFbGVtZW50QXJyYXl0ABNbTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAuY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuc2V0Lm11dGFibGUuVW5pZmllZFNldAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeHZy\n"
                        + "ABBqYXZhLmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cHA=",
                Predicates.notIn(Collections.checkedSet(Sets.mutable.with(), Object.class)));
    }

    @Test
    public void in_small_Collection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SW5Db2xsZWN0aW9uUHJlZGljYXRlAAAAAAAAAAECAAFMAApjb2xsZWN0aW9udAAWTGphdmEvdXRp\n"
                        + "bC9Db2xsZWN0aW9uO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlBy\n"
                        + "ZWRpY2F0ZXMAAAAAAAAAAQIAAHhwc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0\n"
                        + "YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                Predicates.in(Lists.mutable.with()));
    }

    @Test
    public void notIn_small_Collection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90SW5Db2xsZWN0aW9uUHJlZGljYXRlAAAAAAAAAAECAAFMAApjb2xsZWN0aW9udAAWTGphdmEv\n"
                        + "dXRpbC9Db2xsZWN0aW9uO3hyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5\n"
                        + "LlByZWRpY2F0ZXMAAAAAAAAAAQIAAHhwc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3Qu\n"
                        + "bXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                Predicates.notIn(Lists.mutable.with()));
    }

    @Test
    public void isNull()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SXNOdWxsAAAAAAAAAAECAAB4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9y\n"
                        + "eS5QcmVkaWNhdGVzAAAAAAAAAAECAAB4cA==",
                Predicates.isNull());
    }

    @Test
    public void notNull()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90TnVsbAAAAAAAAAABAgAAeHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3Rv\n"
                        + "cnkuUHJlZGljYXRlcwAAAAAAAAABAgAAeHA=",
                Predicates.notNull());
    }

    @Test
    public void sameAs()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "SWRlbnRpdHlQcmVkaWNhdGUAAAAAAAAAAQIAAUwABHR3aW50ABJMamF2YS9sYW5nL09iamVjdDt4\n"
                        + "cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAAAAAA\n"
                        + "AAECAAB4cHA=",
                Predicates.sameAs(null));
    }

    @Test
    public void notSameAs()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "Tm90SWRlbnRpdHlQcmVkaWNhdGUAAAAAAAAAAQIAAUwABHR3aW50ABJMamF2YS9sYW5nL09iamVj\n"
                        + "dDt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAA\n"
                        + "AAAAAAECAAB4cHA=",
                Predicates.notSameAs(null));
    }

    @Test
    public void synchronizedEach()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "U3luY2hyb25pemVkUHJlZGljYXRlAAAAAAAAAAECAAFMAAlwcmVkaWNhdGV0ADJMY29tL2dzL2Nv\n"
                        + "bGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvUHJlZGljYXRlO3hwcA==",
                Predicates.synchronizedEach(null));
    }

    @Test
    public void subClass()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "U3ViY2xhc3NQcmVkaWNhdGUAAAAAAAAAAQIAAUwABmFDbGFzc3QAEUxqYXZhL2xhbmcvQ2xhc3M7\n"
                        + "eHIAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlcwAAAAAA\n"
                        + "AAABAgAAeHB2cgAQamF2YS5sYW5nLk9iamVjdAAAAAAAAAAAAAAAeHA=",
                Predicates.subClass(Object.class));
    }

    @Test
    public void superClass()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "U3VwZXJjbGFzc1ByZWRpY2F0ZQAAAAAAAAABAgABTAAGYUNsYXNzdAARTGphdmEvbGFuZy9DbGFz\n"
                        + "czt4cgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzAAAA\n"
                        + "AAAAAAECAAB4cHZyABBqYXZhLmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cA==",
                Predicates.superClass(Object.class));
    }

    @Test
    public void bind()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMk\n"
                        + "QmluZFByZWRpY2F0ZTIAAAAAAAAAAQIAAkwACXBhcmFtZXRlcnQAEkxqYXZhL2xhbmcvT2JqZWN0\n"
                        + "O0wACXByZWRpY2F0ZXQAM0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9Q\n"
                        + "cmVkaWNhdGUyO3hwcHNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlBy\n"
                        + "ZWRpY2F0ZXMyJEVxdWFsAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                Predicates.bind(Predicates2.equal(), null));
    }
}

