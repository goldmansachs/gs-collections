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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class StringPredicates2SerializationTest
{
    @Test
    public void contains()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJENvbnRhaW5zU3RyaW5nAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuYmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                StringPredicates2.contains());
    }

    @Test
    public void startsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJFN0YXJ0c1dpdGgAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5i\n"
                        + "bG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                StringPredicates2.startsWith());
    }

    @Test
    public void notStartsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJE5vdFN0YXJ0c1dpdGgAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                StringPredicates2.notStartsWith());
    }

    @Test
    public void endsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJEVuZHNXaXRoAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxv\n"
                        + "Y2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                StringPredicates2.endsWith());
    }

    @Test
    public void notEndsWith()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJE5vdEVuZHNXaXRoAAAAAAAAAAECAAB4cgAxY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "YmxvY2suZmFjdG9yeS5QcmVkaWNhdGVzMgAAAAAAAAABAgAAeHA=",
                StringPredicates2.notEndsWith());
    }

    @Test
    public void equalsIgnoreCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJEVxdWFsc0lnbm9yZUNhc2UAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlvbnMu\n"
                        + "aW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                StringPredicates2.equalsIgnoreCase());
    }

    @Test
    public void notEqualsIgnoreCase()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJE5vdEVxdWFsc0lnbm9yZUNhc2UAAAAAAAAAAQIAAHhyADFjb20uZ3MuY29sbGVjdGlv\n"
                        + "bnMuaW1wbC5ibG9jay5mYWN0b3J5LlByZWRpY2F0ZXMyAAAAAAAAAAECAAB4cA==",
                StringPredicates2.notEqualsIgnoreCase());
    }

    @Test
    public void matches()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlN0cmluZ1ByZWRp\n"
                        + "Y2F0ZXMyJE1hdGNoZXNSZWdleAAAAAAAAAABAgAAeHIAMWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmJsb2NrLmZhY3RvcnkuUHJlZGljYXRlczIAAAAAAAAAAQIAAHhw",
                StringPredicates2.matches());
    }
}
