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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IfProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuSWZQcm9jZWR1\n"
                        + "cmUAAAAAAAAAAQIAA0wADWVsc2VQcm9jZWR1cmV0ADJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9i\n"
                        + "bG9jay9wcm9jZWR1cmUvUHJvY2VkdXJlO0wACXByZWRpY2F0ZXQAMkxjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL2Jsb2NrL3ByZWRpY2F0ZS9QcmVkaWNhdGU7TAAJcHJvY2VkdXJlcQB+AAF4cHBwcA==\n",
                new IfProcedure<Object>(null, null, null));
    }
}
