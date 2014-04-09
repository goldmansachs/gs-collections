/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.map.strategy.immutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableEntryWithHashingStrategySerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc3RyYXRlZ3kuaW1tdXRhYmxlLklt\n"
                        + "bXV0YWJsZUVudHJ5V2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAPaGFzaGluZ1N0cmF0\n"
                        + "ZWd5dAAuTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svSGFzaGluZ1N0cmF0ZWd5O3hyADRj\n"
                        + "b20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5BYnN0cmFjdEltbXV0YWJsZUVudHJ5AAAAAAAA\n"
                        + "AAECAAJMAANrZXl0ABJMamF2YS9sYW5nL09iamVjdDtMAAV2YWx1ZXEAfgADeHBwcHNyAEdjb20u\n"
                        + "Z3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJhdGVnaWVzJERlZmF1\n"
                        + "bHRTdHJhdGVneQAAAAAAAAABAgAAeHA=",
                new ImmutableEntryWithHashingStrategy<Object, Object>(null, null, HashingStrategies.defaultStrategy()));
    }
}
