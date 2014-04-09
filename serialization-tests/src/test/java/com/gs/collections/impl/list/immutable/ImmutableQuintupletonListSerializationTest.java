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

public class ImmutableQuintupletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVR\n"
                        + "dWludHVwbGV0b25MaXN0AAAAAAAAAAECAAVMAAhlbGVtZW50MXQAEkxqYXZhL2xhbmcvT2JqZWN0\n"
                        + "O0wACGVsZW1lbnQycQB+AAFMAAhlbGVtZW50M3EAfgABTAAIZWxlbWVudDRxAH4AAUwACGVsZW1l\n"
                        + "bnQ1cQB+AAF4cHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2\n"
                        + "YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAABc3EAfgADAAAAAnNxAH4AAwAAAANzcQB+AAMA\n"
                        + "AAAEc3EAfgADAAAABQ==",
                new ImmutableQuintupletonList<Integer>(1, 2, 3, 4, 5));
    }
}
