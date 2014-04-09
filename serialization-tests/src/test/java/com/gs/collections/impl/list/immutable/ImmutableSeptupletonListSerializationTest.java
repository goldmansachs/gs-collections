/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.list.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableSeptupletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVT\n"
                        + "ZXB0dXBsZXRvbkxpc3QAAAAAAAAAAQIAB0wACGVsZW1lbnQxdAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "TAAIZWxlbWVudDJxAH4AAUwACGVsZW1lbnQzcQB+AAFMAAhlbGVtZW50NHEAfgABTAAIZWxlbWVu\n"
                        + "dDVxAH4AAUwACGVsZW1lbnQ2cQB+AAFMAAhlbGVtZW50N3EAfgABeHBzcgARamF2YS5sYW5nLklu\n"
                        + "dGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhw\n"
                        + "AAAAAXNxAH4AAwAAAAJzcQB+AAMAAAADc3EAfgADAAAABHNxAH4AAwAAAAVzcQB+AAMAAAAGc3EA\n"
                        + "fgADAAAABw==",
                new ImmutableSeptupletonList<Integer>(1, 2, 3, 4, 5, 6, 7));
    }
}
