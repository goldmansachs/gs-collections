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

package com.gs.collections.impl.map.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableByteDoubleMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlQnl0ZURvdWJsZU1hcAAAAAAAAAABAgABTAADbWFwdAA7TGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvbWFwL3ByaW1pdGl2ZS9NdXRhYmxlQnl0ZURvdWJsZU1hcDt4cHNyAD9jb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuQnl0ZURvdWJsZUhhc2hNYXAA\n"
                        + "AAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new UnmodifiableByteDoubleMap(new ByteDoubleHashMap()));
    }
}
