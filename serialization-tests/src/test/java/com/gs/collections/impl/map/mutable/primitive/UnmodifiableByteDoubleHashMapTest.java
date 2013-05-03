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

public class UnmodifiableByteDoubleHashMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlQnl0ZURvdWJsZUhhc2hNYXAAAAAAAAAAAQIAAUwAA21hcHQAO0xjb20vZ3MvY29s\n"
                        + "bGVjdGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJsZUJ5dGVEb3VibGVNYXA7eHBzcgA/Y29t\n"
                        + "LmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLm11dGFibGUucHJpbWl0aXZlLkJ5dGVEb3VibGVIYXNo\n"
                        + "TWFwAAAAAAAAAAEMAAB4cHcIAAAAAD8AAAB4",
                new UnmodifiableByteDoubleHashMap(new ByteDoubleHashMap()));
    }
}