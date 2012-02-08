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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ProceduresTest
{
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
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMk\n"
                        + "T2JqZWN0SW50UHJvY2VkdXJlQWRhcHRlcgAAAAAAAAABAgACSQAFY291bnRMABJvYmplY3RJbnRQ\n"
                        + "cm9jZWR1cmV0ADtMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcm9jZWR1cmUvT2JqZWN0\n"
                        + "SW50UHJvY2VkdXJlO3hwAAAAAHA=",
                Procedures.fromObjectIntProcedure(null));
    }
}
