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

package com.gs.collections.impl;

import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableRichIterableSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5Vbm1vZGlmaWFibGVSaWNoSXRlcmFibGUA\n"
                        + "AAAAAAAAAQIAAUwACGl0ZXJhYmxldAAlTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvUmljaEl0ZXJh\n"
                        + "YmxlO3hwc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAA\n"
                        + "AAAAAAABDAAAeHB3BAAAAAB4",
                UnmodifiableRichIterable.of(Lists.mutable.of()));
    }
}
