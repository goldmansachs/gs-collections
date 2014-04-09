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

public class SynchronizedIntListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlN5\n"
                        + "bmNocm9uaXplZEludExpc3QAAAAAAAAAAQIAAHhyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5j\n"
                        + "b2xsZWN0aW9uLm11dGFibGUucHJpbWl0aXZlLkFic3RyYWN0U3luY2hyb25pemVkSW50Q29sbGVj\n"
                        + "dGlvbgAAAAAAAAABAgACTAAKY29sbGVjdGlvbnQAQkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Nv\n"
                        + "bGxlY3Rpb24vcHJpbWl0aXZlL011dGFibGVJbnRDb2xsZWN0aW9uO0wABGxvY2t0ABJMamF2YS9s\n"
                        + "YW5nL09iamVjdDt4cHNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJp\n"
                        + "bWl0aXZlLkludEFycmF5TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4cQB+AAQ=",
                new SynchronizedIntList(new IntArrayList()));
    }
}
