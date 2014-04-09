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

package com.gs.collections.impl.stack.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedShortStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5T\n"
                        + "eW5jaHJvbml6ZWRTaG9ydFN0YWNrAAAAAAAAAAECAAJMAARsb2NrdAASTGphdmEvbGFuZy9PYmpl\n"
                        + "Y3Q7TAAFc3RhY2t0ADpMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9zdGFjay9wcmltaXRpdmUvTXV0\n"
                        + "YWJsZVNob3J0U3RhY2s7eHBxAH4AA3NyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5t\n"
                        + "dXRhYmxlLnByaW1pdGl2ZS5TaG9ydEFycmF5U3RhY2sAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new SynchronizedShortStack(new ShortArrayStack()));
    }
}
