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

public class ChainedProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQ2hhaW5lZFBy\n"
                        + "b2NlZHVyZQAAAAAAAAABAgABTAAKcHJvY2VkdXJlc3QAEExqYXZhL3V0aWwvTGlzdDt4cHNyAC1j\n"
                        + "b20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAAAAAAAQwAAHhw\n"
                        + "dwQAAAAAeA==",
                new ChainedProcedure<Object>());
    }
}
