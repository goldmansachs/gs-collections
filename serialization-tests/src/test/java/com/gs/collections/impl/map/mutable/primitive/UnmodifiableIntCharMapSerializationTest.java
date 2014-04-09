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

public class UnmodifiableIntCharMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlSW50Q2hhck1hcAAAAAAAAAABAgABTAADbWFwdAA4TGNvbS9ncy9jb2xsZWN0aW9u\n"
                        + "cy9hcGkvbWFwL3ByaW1pdGl2ZS9NdXRhYmxlSW50Q2hhck1hcDt4cHNyADxjb20uZ3MuY29sbGVj\n"
                        + "dGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuSW50Q2hhckhhc2hNYXAAAAAAAAAAAQwA\n"
                        + "AHhwdwQAAAAAeA==",
                new UnmodifiableIntCharMap(new IntCharHashMap()));
    }
}
