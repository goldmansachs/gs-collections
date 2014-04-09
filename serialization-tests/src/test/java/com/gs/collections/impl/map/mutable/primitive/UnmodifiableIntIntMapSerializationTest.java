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

public class UnmodifiableIntIntMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlSW50SW50TWFwAAAAAAAAAAECAAFMAANtYXB0ADdMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9tYXAvcHJpbWl0aXZlL011dGFibGVJbnRJbnRNYXA7eHBzcgA7Y29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwubWFwLm11dGFibGUucHJpbWl0aXZlLkludEludEhhc2hNYXAAAAAAAAAAAQwAAHhw\n"
                        + "dwQAAAAAeA==",
                new UnmodifiableIntIntMap(new IntIntHashMap()));
    }
}
