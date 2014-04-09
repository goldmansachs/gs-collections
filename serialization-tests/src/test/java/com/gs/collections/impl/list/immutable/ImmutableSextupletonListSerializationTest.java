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

public class ImmutableSextupletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVT\n"
                        + "ZXh0dXBsZXRvbkxpc3QAAAAAAAAAAQIABkwACGVsZW1lbnQxdAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "TAAIZWxlbWVudDJxAH4AAUwACGVsZW1lbnQzcQB+AAFMAAhlbGVtZW50NHEAfgABTAAIZWxlbWVu\n"
                        + "dDVxAH4AAUwACGVsZW1lbnQ2cQB+AAF4cHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgAB\n"
                        + "SQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAABc3EAfgADAAAAAnNx\n"
                        + "AH4AAwAAAANzcQB+AAMAAAAEc3EAfgADAAAABXNxAH4AAwAAAAY=",
                new ImmutableSextupletonList<Integer>(1, 2, 3, 4, 5, 6));
    }
}
