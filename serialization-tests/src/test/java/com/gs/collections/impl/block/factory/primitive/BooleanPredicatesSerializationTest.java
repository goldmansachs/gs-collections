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

import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class BooleanPredicatesSerializationTest
{
    private static final BooleanPredicate PREDICATE = new BooleanPredicate()
    {
        private static final long serialVersionUID = 1L;

        public boolean accept(boolean value)
        {
            return false;
        }
    };

    @Test
    public void alwaysFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAF1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRBbHdheXNGYWxzZUJvb2xlYW5QcmVkaWNhdGUAAAAAAAAAAQIAAHhw\n",
                BooleanPredicates.alwaysFalse());
    }

    @Test
    public void alwaysTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRBbHdheXNUcnVlQm9vbGVhblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=\n",
                BooleanPredicates.alwaysTrue());
    }

    @Test
    public void isTrue()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRJc1RydWVCb29sZWFuUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                BooleanPredicates.isTrue());
    }

    @Test
    public void isFalse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRJc0ZhbHNlQm9vbGVhblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BooleanPredicates.isFalse());
    }

    @Test
    public void not()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRJc0ZhbHNlQm9vbGVhblByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BooleanPredicates.not(BooleanPredicates.isTrue()));
    }

    @Test
    public void and()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRGYWxzZVByZWRpY2F0ZQAAAAAAAAABAgAAeHA=",
                BooleanPredicates.and(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()));
    }

    @Test
    public void or()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRUcnVlUHJlZGljYXRlAAAAAAAAAAECAAB4cA==",
                BooleanPredicates.or(BooleanPredicates.isTrue(), BooleanPredicates.isFalse()));
    }

    @Test
    public void not_custom()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyROb3RCb29sZWFuUHJlZGljYXRlAAAAAAAAAAECAAFMAAZuZWdhdGV0\n"
                        + "AENMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0Jvb2xl\n"
                        + "YW5QcmVkaWNhdGU7eHBzcgBUY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5w\n"
                        + "cmltaXRpdmUuQm9vbGVhblByZWRpY2F0ZXNTZXJpYWxpemF0aW9uVGVzdCQxAAAAAAAAAAECAAB4\n"
                        + "cA==",
                BooleanPredicates.not(PREDICATE));
    }

    @Test
    public void and_custom()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRBbmRCb29sZWFuUHJlZGljYXRlAAAAAAAAAAECAAJMAANvbmV0AENM\n"
                        + "Y29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvcHJpbWl0aXZlL0Jvb2xlYW5Q\n"
                        + "cmVkaWNhdGU7TAADdHdvcQB+AAF4cHNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "YWN0b3J5LnByaW1pdGl2ZS5Cb29sZWFuUHJlZGljYXRlc1NlcmlhbGl6YXRpb25UZXN0JDEAAAAA\n"
                        + "AAAAAQIAAHhwcQB+AAQ=",
                BooleanPredicates.and(PREDICATE, PREDICATE));
    }

    @Test
    public void or_custom()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LnByaW1pdGl2ZS5C\n"
                        + "b29sZWFuUHJlZGljYXRlcyRPckJvb2xlYW5QcmVkaWNhdGUAAAAAAAAAAQIAAkwAA29uZXQAQ0xj\n"
                        + "b20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9wcmltaXRpdmUvQm9vbGVhblBy\n"
                        + "ZWRpY2F0ZTtMAAN0d29xAH4AAXhwc3IAVGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZh\n"
                        + "Y3RvcnkucHJpbWl0aXZlLkJvb2xlYW5QcmVkaWNhdGVzU2VyaWFsaXphdGlvblRlc3QkMQAAAAAA\n"
                        + "AAABAgAAeHBxAH4ABA==",
                BooleanPredicates.or(PREDICATE, PREDICATE));
    }
}
