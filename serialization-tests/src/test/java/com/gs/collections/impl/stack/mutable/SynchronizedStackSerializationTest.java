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

package com.gs.collections.impl.stack.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLlN5bmNocm9uaXpl\n"
                        + "ZFN0YWNrAAAAAAAAAAECAAJMAAhkZWxlZ2F0ZXQAK0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL3N0\n"
                        + "YWNrL011dGFibGVTdGFjaztMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAwY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuc3RhY2subXV0YWJsZS5BcnJheVN0YWNrAAAAAAAAAAEMAAB4cHcE\n"
                        + "AAAAAHhxAH4AAw==",
                SynchronizedStack.of(ArrayStack.newStack()));
    }
}
