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

package com.gs.collections.impl.tuple;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableEntrySerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyACxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5JbW11dGFibGVFbnRyeQAAAAAA\n"
                        + "AAABAgAAeHIANGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnR1cGxlLkFic3RyYWN0SW1tdXRhYmxl\n"
                        + "RW50cnkAAAAAAAAAAQIAAkwAA2tleXQAEkxqYXZhL2xhbmcvT2JqZWN0O0wABXZhbHVlcQB+AAJ4\n"
                        + "cHBw",
                new ImmutableEntry<Object, Object>(null, null));
    }
}
