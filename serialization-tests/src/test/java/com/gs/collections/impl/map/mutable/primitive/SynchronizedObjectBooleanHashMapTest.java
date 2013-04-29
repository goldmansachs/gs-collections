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

public class SynchronizedObjectBooleanHashMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkT2JqZWN0Qm9vbGVhbkhhc2hNYXAAAAAAAAAAAQIAAkwABGxvY2t0ABJMamF2YS9s\n"
                        + "YW5nL09iamVjdDtMAANtYXB0AD5MY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9tYXAvcHJpbWl0aXZl\n"
                        + "L011dGFibGVPYmplY3RCb29sZWFuTWFwO3hwcQB+AANzcgBCY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwubWFwLm11dGFibGUucHJpbWl0aXZlLk9iamVjdEJvb2xlYW5IYXNoTWFwAAAAAAAAAAEMAAB4\n"
                        + "cHcIAAAAAD8AAAB4",
                new SynchronizedObjectBooleanHashMap<Object>(new ObjectBooleanHashMap<Object>()));
    }
}
