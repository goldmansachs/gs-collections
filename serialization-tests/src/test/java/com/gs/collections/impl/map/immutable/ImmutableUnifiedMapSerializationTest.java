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

public class ImmutableUnifiedMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuaW1tdXRhYmxlLkltbXV0YWJsZU1h\n"
                        + "cFNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHB3BAAAAARzcgARamF2YS5sYW5nLkludGVn\n"
                        + "ZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAA\n"
                        + "AXEAfgAEc3EAfgACAAAAAnEAfgAFc3EAfgACAAAAA3EAfgAGc3EAfgACAAAABHEAfgAHeA==",
                new ImmutableUnifiedMap<Integer, Integer>(
                        Tuples.pair(1, 1),
                        Tuples.pair(2, 2),
                        Tuples.pair(3, 3),
                        Tuples.pair(4, 4)));
    }
}
