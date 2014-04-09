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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class AtomicCountProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQXRvbWljQ291\n"
                        + "bnRQcm9jZWR1cmUAAAAAAAAAAQIAAkwABWNvdW50dAArTGphdmEvdXRpbC9jb25jdXJyZW50L2F0\n"
                        + "b21pYy9BdG9taWNJbnRlZ2VyO0wACXByZWRpY2F0ZXQAMkxjb20vZ3MvY29sbGVjdGlvbnMvYXBp\n"
                        + "L2Jsb2NrL3ByZWRpY2F0ZS9QcmVkaWNhdGU7eHBzcgApamF2YS51dGlsLmNvbmN1cnJlbnQuYXRv\n"
                        + "bWljLkF0b21pY0ludGVnZXJWP17MjGwWigIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKG\n"
                        + "rJUdC5TgiwIAAHhwAAAAAHA=",
                new AtomicCountProcedure<Object>(null));
    }
}
