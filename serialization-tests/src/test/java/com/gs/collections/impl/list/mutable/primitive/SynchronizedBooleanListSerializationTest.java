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

public class SynchronizedBooleanListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUucHJpbWl0aXZlLlN5\n"
                        + "bmNocm9uaXplZEJvb2xlYW5MaXN0AAAAAAAAAAECAAB4cgBaY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuY29sbGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFN5bmNocm9uaXplZEJvb2xl\n"
                        + "YW5Db2xsZWN0aW9uAAAAAAAAAAECAAJMAApjb2xsZWN0aW9udABGTGNvbS9ncy9jb2xsZWN0aW9u\n"
                        + "cy9hcGkvY29sbGVjdGlvbi9wcmltaXRpdmUvTXV0YWJsZUJvb2xlYW5Db2xsZWN0aW9uO0wABGxv\n"
                        + "Y2t0ABJMamF2YS9sYW5nL09iamVjdDt4cHNyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0\n"
                        + "Lm11dGFibGUucHJpbWl0aXZlLkJvb2xlYW5BcnJheUxpc3QAAAAAAAAAAQwAAHhwdwQAAAAAeHEA\n"
                        + "fgAE",
                new SynchronizedBooleanList(new BooleanArrayList()));
    }
}
