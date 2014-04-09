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

package com.gs.collections.impl.set.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedFloatSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkRmxvYXRTZXQAAAAAAAAAAQIAAHhyAFhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5j\n"
                        + "b2xsZWN0aW9uLm11dGFibGUucHJpbWl0aXZlLkFic3RyYWN0U3luY2hyb25pemVkRmxvYXRDb2xs\n"
                        + "ZWN0aW9uAAAAAAAAAAECAAJMAApjb2xsZWN0aW9udABETGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkv\n"
                        + "Y29sbGVjdGlvbi9wcmltaXRpdmUvTXV0YWJsZUZsb2F0Q29sbGVjdGlvbjtMAARsb2NrdAASTGph\n"
                        + "dmEvbGFuZy9PYmplY3Q7eHBzcgA6Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuc2V0Lm11dGFibGUu\n"
                        + "cHJpbWl0aXZlLkZsb2F0SGFzaFNldAAAAAAAAAABDAAAeHB3BAAAAAB4cQB+AAQ=",
                new SynchronizedFloatSet(new FloatHashSet()));
    }
}
