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

package com.gs.collections.impl.list.immutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableBooleanArrayListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAHFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5wcmltaXRpdmUu\n"
                        + "SW1tdXRhYmxlQm9vbGVhbkFycmF5TGlzdCRJbW11dGFibGVCb29sZWFuTGlzdFNlcmlhbGl6YXRp\n"
                        + "b25Qcm94eQAAAAAAAAABDAAAeHB3DwAAAAsBAAEAAQABAQEAAXg=",
                ImmutableBooleanArrayList.newListWith(true, false, true, false, true, false, true, true, true, false, true));
    }
}
