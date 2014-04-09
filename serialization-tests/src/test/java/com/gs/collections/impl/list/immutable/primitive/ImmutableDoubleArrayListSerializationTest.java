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

public class ImmutableDoubleArrayListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5wcmltaXRpdmUu\n"
                        + "SW1tdXRhYmxlRG91YmxlQXJyYXlMaXN0AAAAAAAAAAECAAFbAAVpdGVtc3QAAltEeHB1cgACW0Q+\n"
                        + "powUq2NaHgIAAHhwAAAACz/wAAAAAAAAQAAAAAAAAABACAAAAAAAAEAQAAAAAAAAQBQAAAAAAABA\n"
                        + "GAAAAAAAAEAcAAAAAAAAQCAAAAAAAABAIgAAAAAAAEAkAAAAAAAAQCYAAAAAAAA=",
                ImmutableDoubleArrayList.newListWith(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0));
    }
}
