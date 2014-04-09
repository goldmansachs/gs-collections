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

public class UnmodifiableBooleanListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlVu\n"
                        + "bW9kaWZpYWJsZUJvb2xlYW5MaXN0AAAAAAAAAAECAAB4cgBaY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuY29sbGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFVubW9kaWZpYWJsZUJvb2xl\n"
                        + "YW5Db2xsZWN0aW9uAAAAAAAAAAECAAFMAApjb2xsZWN0aW9udABGTGNvbS9ncy9jb2xsZWN0aW9u\n"
                        + "cy9hcGkvY29sbGVjdGlvbi9wcmltaXRpdmUvTXV0YWJsZUJvb2xlYW5Db2xsZWN0aW9uO3hwc3IA\n"
                        + "P2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5wcmltaXRpdmUuQm9vbGVhbkFy\n"
                        + "cmF5TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                new UnmodifiableBooleanList(new BooleanArrayList()));
    }
}
