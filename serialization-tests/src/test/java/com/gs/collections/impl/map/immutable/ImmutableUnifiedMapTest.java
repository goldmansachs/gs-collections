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

package com.gs.collections.impl.map.immutable;

import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Test;

public class ImmutableUnifiedMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuaW1tdXRhYmxlLkFic3RyYWN0SW1t\n"
                        + "dXRhYmxlTWFwJEltbXV0YWJsZU1hcFNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHB3BAAA\n"
                        + "AARzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5O\n"
                        + "dW1iZXKGrJUdC5TgiwIAAHhwAAAAAXEAfgAEc3EAfgACAAAAAnEAfgAFc3EAfgACAAAAA3EAfgAG\n"
                        + "c3EAfgACAAAABHEAfgAHeA==",
                new ImmutableUnifiedMap<Integer, Integer>(
                        Tuples.pair(1, 1),
                        Tuples.pair(2, 2),
                        Tuples.pair(3, 3),
                        Tuples.pair(4, 4)));
    }
}
