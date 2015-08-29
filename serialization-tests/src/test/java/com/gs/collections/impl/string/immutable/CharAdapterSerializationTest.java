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

public class CharAdapterSerializationTest
{
    public static final String EMPTY_CHAR_ADAPTER = "rO0ABXNyADRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNoYXJBZGFw\n"
            + "dGVyAAAAAAAAAAECAAFMAAdhZGFwdGVkdAASTGphdmEvbGFuZy9TdHJpbmc7eHB0AAA=";
    public static final String HELLO_WORLD_STRING = "rO0ABXNyADRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdHJpbmcuaW1tdXRhYmxlLkNoYXJBZGFw\n"
            + "dGVyAAAAAAAAAAECAAFMAAdhZGFwdGVkdAASTGphdmEvbGFuZy9TdHJpbmc7eHB0AAxIZWxsbyBX\n"
            + "b3JsZCE=";

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                EMPTY_CHAR_ADAPTER,
                CharAdapter.adapt(""));
    }

    @Test
    public void serializedFormNotEmpty()
    {
        Verify.assertSerializedForm(
                1L,
                HELLO_WORLD_STRING,
                CharAdapter.adapt("Hello World!"));
    }
}
