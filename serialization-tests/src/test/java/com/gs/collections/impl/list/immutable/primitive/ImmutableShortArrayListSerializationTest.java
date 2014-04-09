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

package com.gs.collections.impl.list.immutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableShortArrayListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5wcmltaXRpdmUu\n"
                        + "SW1tdXRhYmxlU2hvcnRBcnJheUxpc3QAAAAAAAAAAQIAAVsABWl0ZW1zdAACW1N4cHVyAAJbU++D\n"
                        + "LgblXbD6AgAAeHAAAAALAAEAAgADAAQABQAGAAcACAAJAAoACw==",
                ImmutableShortArrayList.newListWith((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10, (short) 11));
    }
}
