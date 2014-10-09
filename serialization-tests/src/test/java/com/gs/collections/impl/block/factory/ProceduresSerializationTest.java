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

public class ProceduresSerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "VGhyb3dpbmdQcm9jZWR1cmVBZGFwdGVyAAAAAAAAAAECAAFMABF0aHJvd2luZ1Byb2NlZHVyZXQA\n"
                        + "Q0xjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9wcm9jZWR1cmUvY2hlY2tlZC9UaHJvd2lu\n"
                        + "Z1Byb2NlZHVyZTt4cgBAY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2sucHJvY2VkdXJlLmNo\n"
                        + "ZWNrZWQuQ2hlY2tlZFByb2NlZHVyZQAAAAAAAAABAgAAeHBw",
                Procedures.throwing(null));
    }

    @Test
    public void println()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "UHJpbnRsblByb2NlZHVyZQAAAAAAAAABAgABTAAGc3RyZWFtdAAVTGphdmEvaW8vUHJpbnRTdHJl\n"
                        + "YW07eHBw",
                Procedures.println(null));
    }

    @Test
    public void append()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "QXBwZW5kUHJvY2VkdXJlAAAAAAAAAAECAAFMAAphcHBlbmRhYmxldAAWTGphdmEvbGFuZy9BcHBl\n"
                        + "bmRhYmxlO3hwcA==",
                Procedures.append(null));
    }

    @Test
    public void synchronizedEach()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "U3luY2hyb25pemVkUHJvY2VkdXJlAAAAAAAAAAECAAFMAAlwcm9jZWR1cmV0ADJMY29tL2dzL2Nv\n"
                        + "bGxlY3Rpb25zL2FwaS9ibG9jay9wcm9jZWR1cmUvUHJvY2VkdXJlO3hwcA==",
                Procedures.synchronizedEach(null));
    }

    @Test
    public void asProcedure()
    {
        Verify.assertSerializedForm(
                2L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "T2JqZWN0SW50UHJvY2VkdXJlQWRhcHRlcgAAAAAAAAACAgACSQAFY291bnRMABJvYmplY3RJbnRQ\n"
                        + "cm9jZWR1cmV0AEVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcm9jZWR1cmUvcHJpbWl0\n"
                        + "aXZlL09iamVjdEludFByb2NlZHVyZTt4cAAAAABw",
                Procedures.fromObjectIntProcedure(null));
    }

    @Test
    public void bind()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "QmluZFByb2NlZHVyZQAAAAAAAAABAgACTAAJcGFyYW1ldGVydAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "TAAJcHJvY2VkdXJldAAzTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJvY2VkdXJlL1By\n"
                        + "b2NlZHVyZTI7eHBwcA==",
                Procedures.bind(null, null));
    }
}
