/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.bimap.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class HashBiMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAC9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iaW1hcC5tdXRhYmxlLkhhc2hCaU1hcAAA\n"
                        + "AAAAAAABDAAAeHB3CAAAAAE/QAAAc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2\n"
                        + "YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAFzcgATamF2YS5sYW5nLkNo\n"
                        + "YXJhY3RlcjSLR9lrGiZ4AgABQwAFdmFsdWV4cABheA==",
                HashBiMap.newWithKeysValues(1, 'a'));
    }

    @Test
    public void inverse()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iaW1hcC5tdXRhYmxlLkFic3RyYWN0TXV0\n"
                        + "YWJsZUJpTWFwJEludmVyc2UAAAAAAAAAAQwAAHhwdwgAAAABP0AAAHNyABNqYXZhLmxhbmcuQ2hh\n"
                        + "cmFjdGVyNItH2WsaJngCAAFDAAV2YWx1ZXhwAGFzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GH\n"
                        + "OAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAXg=",
                HashBiMap.newWithKeysValues(1, 'a').inverse());
    }
}
