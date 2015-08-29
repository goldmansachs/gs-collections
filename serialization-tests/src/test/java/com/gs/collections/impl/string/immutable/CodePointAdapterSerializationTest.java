/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.string.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CodePointAdapterSerializationTest
{
    public static final String EMPTY_CODE_LIST = "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNvZGVQb2lu\n"
            + "dEFkYXB0ZXIAAAAAAAAAAQIAAUwAB2FkYXB0ZWR0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQAAA==\n";
    public static final String HELLO_WORLD_STRING = "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNvZGVQb2lu\n"
            + "dEFkYXB0ZXIAAAAAAAAAAQIAAUwAB2FkYXB0ZWR0ABJMamF2YS9sYW5nL1N0cmluZzt4cHQADEhl\n"
            + "bGxvIFdvcmxkIQ==";

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                EMPTY_CODE_LIST,
                CodePointAdapter.adapt(""));
    }

    @Test
    public void serializedFormNotEmpty()
    {
        Verify.assertSerializedForm(
                1L,
                HELLO_WORLD_STRING,
                CodePointAdapter.adapt("Hello World!"));
    }
}
