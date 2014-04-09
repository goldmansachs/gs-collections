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

public class SynchronizedDoubleListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlN5\n"
                        + "bmNocm9uaXplZERvdWJsZUxpc3QAAAAAAAAAAQIAAHhyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5jb2xsZWN0aW9uLm11dGFibGUucHJpbWl0aXZlLkFic3RyYWN0U3luY2hyb25pemVkRG91Ymxl\n"
                        + "Q29sbGVjdGlvbgAAAAAAAAABAgACTAAKY29sbGVjdGlvbnQARUxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "YXBpL2NvbGxlY3Rpb24vcHJpbWl0aXZlL011dGFibGVEb3VibGVDb2xsZWN0aW9uO0wABGxvY2t0\n"
                        + "ABJMamF2YS9sYW5nL09iamVjdDt4cHNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11\n"
                        + "dGFibGUucHJpbWl0aXZlLkRvdWJsZUFycmF5TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4cQB+AAQ=\n",
                new SynchronizedDoubleList(new DoubleArrayList()));
    }
}
