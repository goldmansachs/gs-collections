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

package com.gs.collections.impl.map.immutable.primitive;

import com.gs.collections.impl.map.mutable.primitive.ByteDoubleHashMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableByteDoubleHashMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAHNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuaW1tdXRhYmxlLnByaW1pdGl2ZS5J\n"
                        + "bW11dGFibGVCeXRlRG91YmxlSGFzaE1hcCRJbW11dGFibGVCeXRlRG91YmxlTWFwU2VyaWFsaXph\n"
                        + "dGlvblByb3h5AAAAAAAAAAEMAAB4cHcWAAAAAgE/8AAAAAAAAAJAAAAAAAAAAHg=",
                new ImmutableByteDoubleHashMap(ByteDoubleHashMap.newWithKeysValues((byte) 1, 1.0, (byte) 2, 2.0)));
    }
}
