/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.bimap.immutable;

import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableHashBiMapSerializationTest
{
    private static final String SERIALIZED_FORM = "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iaW1hcC5pbW11dGFibGUuSW1tdXRhYmxl\n"
            + "QmlNYXBTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwAAHhwdwQAAAABc3IAEWphdmEubGFuZy5J\n"
            + "bnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4\n"
            + "cAAAAAFzcgATamF2YS5sYW5nLkNoYXJhY3RlcjSLR9lrGiZ4AgABQwAFdmFsdWV4cABheA==";

    @Test
    public void serializedForm()
    {
        ImmutableHashBiMap<Integer, Character> biMap = new ImmutableHashBiMap<Integer, Character>(
                Maps.immutable.with(1, 'a'),
                Maps.immutable.with('a', 1));

        Verify.assertSerializedForm(1L, SERIALIZED_FORM, biMap);
    }

    @Test
    public void inverse()
    {
        ImmutableHashBiMap<Character, Integer> biMap = new ImmutableHashBiMap<Character, Integer>(
                Maps.immutable.with('a', 1),
                Maps.immutable.with(1, 'a'));

        Verify.assertSerializedForm(1L, SERIALIZED_FORM, biMap.inverse());
    }
}
