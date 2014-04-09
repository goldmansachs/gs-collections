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

package com.gs.collections.impl.bag.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedIntBagSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkSW50QmFnAAAAAAAAAAECAAB4cgBWY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29s\n"
                        + "bGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFN5bmNocm9uaXplZEludENvbGxlY3Rp\n"
                        + "b24AAAAAAAAAAQIAAkwACmNvbGxlY3Rpb250AEJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xs\n"
                        + "ZWN0aW9uL3ByaW1pdGl2ZS9NdXRhYmxlSW50Q29sbGVjdGlvbjtMAARsb2NrdAASTGphdmEvbGFu\n"
                        + "Zy9PYmplY3Q7eHBzcgA4Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmFnLm11dGFibGUucHJpbWl0\n"
                        + "aXZlLkludEhhc2hCYWcAAAAAAAAAAQwAAHhwdwQAAAAAeHEAfgAE",
                new SynchronizedIntBag(new IntHashBag()));
    }
}
