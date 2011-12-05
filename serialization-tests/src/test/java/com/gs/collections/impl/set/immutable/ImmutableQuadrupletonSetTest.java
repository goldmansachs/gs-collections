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

package com.gs.collections.impl.set.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableQuadrupletonSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLkltbXV0YWJsZVF1\n"
                        + "YWRydXBsZXRvblNldAAAAAAAAAABAgAETAAIZWxlbWVudDF0ABJMamF2YS9sYW5nL09iamVjdDtM\n"
                        + "AAhlbGVtZW50MnEAfgABTAAIZWxlbWVudDNxAH4AAUwACGVsZW1lbnQ0cQB+AAF4cHNyABFqYXZh\n"
                        + "LmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0L\n"
                        + "lOCLAgAAeHAAAAABc3EAfgADAAAAAnNxAH4AAwAAAANzcQB+AAMAAAAE",
                new ImmutableQuadrupletonSet<Integer>(1, 2, 3, 4));
    }
}
