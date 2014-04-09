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

public class SynchronizedFloatListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlN5\n"
                        + "bmNocm9uaXplZEZsb2F0TGlzdAAAAAAAAAABAgAAeHIAWGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmNvbGxlY3Rpb24ubXV0YWJsZS5wcmltaXRpdmUuQWJzdHJhY3RTeW5jaHJvbml6ZWRGbG9hdENv\n"
                        + "bGxlY3Rpb24AAAAAAAAAAQIAAkwACmNvbGxlY3Rpb250AERMY29tL2dzL2NvbGxlY3Rpb25zL2Fw\n"
                        + "aS9jb2xsZWN0aW9uL3ByaW1pdGl2ZS9NdXRhYmxlRmxvYXRDb2xsZWN0aW9uO0wABGxvY2t0ABJM\n"
                        + "amF2YS9sYW5nL09iamVjdDt4cHNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFi\n"
                        + "bGUucHJpbWl0aXZlLkZsb2F0QXJyYXlMaXN0AAAAAAAAAAEMAAB4cHcEAAAAAHhxAH4ABA==",
                new SynchronizedFloatList(new FloatArrayList()));
    }
}
