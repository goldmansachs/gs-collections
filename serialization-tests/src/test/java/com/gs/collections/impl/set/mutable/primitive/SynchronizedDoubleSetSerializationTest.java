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

public class SynchronizedDoubleSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkRG91YmxlU2V0AAAAAAAAAAECAAB4cgBZY29tLmdzLmNvbGxlY3Rpb25zLmltcGwu\n"
                        + "Y29sbGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFN5bmNocm9uaXplZERvdWJsZUNv\n"
                        + "bGxlY3Rpb24AAAAAAAAAAQIAAkwACmNvbGxlY3Rpb250AEVMY29tL2dzL2NvbGxlY3Rpb25zL2Fw\n"
                        + "aS9jb2xsZWN0aW9uL3ByaW1pdGl2ZS9NdXRhYmxlRG91YmxlQ29sbGVjdGlvbjtMAARsb2NrdAAS\n"
                        + "TGphdmEvbGFuZy9PYmplY3Q7eHBzcgA7Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuc2V0Lm11dGFi\n"
                        + "bGUucHJpbWl0aXZlLkRvdWJsZUhhc2hTZXQAAAAAAAAAAQwAAHhwdwQAAAAAeHEAfgAE",
                new SynchronizedDoubleSet(new DoubleHashSet()));
    }
}
