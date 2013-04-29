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

package com.gs.collections.impl.map.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedByteObjectHashMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkQnl0ZU9iamVjdEhhc2hNYXAAAAAAAAAAAQIAAkwABGxvY2t0ABJMamF2YS9sYW5n\n"
                        + "L09iamVjdDtMAANtYXB0ADtMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9tYXAvcHJpbWl0aXZlL011\n"
                        + "dGFibGVCeXRlT2JqZWN0TWFwO3hwcQB+AANzcgA/Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFw\n"
                        + "Lm11dGFibGUucHJpbWl0aXZlLkJ5dGVPYmplY3RIYXNoTWFwAAAAAAAAAAEMAAB4cHcIAAAAAD8A\n"
                        + "AAB4",
                new SynchronizedByteObjectHashMap<Object>(new ByteObjectHashMap<Object>()));
    }
}
