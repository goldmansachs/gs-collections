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

package com.gs.collections.impl.list.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableLongListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlVu\n"
                        + "bW9kaWZpYWJsZUxvbmdMaXN0AAAAAAAAAAECAAB4cgBXY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "Y29sbGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFVubW9kaWZpYWJsZUxvbmdDb2xs\n"
                        + "ZWN0aW9uAAAAAAAAAAECAAFMAApjb2xsZWN0aW9udABDTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkv\n"
                        + "Y29sbGVjdGlvbi9wcmltaXRpdmUvTXV0YWJsZUxvbmdDb2xsZWN0aW9uO3hwc3IAPGNvbS5ncy5j\n"
                        + "b2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5wcmltaXRpdmUuTG9uZ0FycmF5TGlzdAAAAAAA\n"
                        + "AAABDAAAeHB3BAAAAAB4",
                new UnmodifiableLongList(new LongArrayList()));
    }
}
