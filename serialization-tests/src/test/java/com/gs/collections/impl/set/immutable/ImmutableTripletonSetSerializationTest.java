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

package com.gs.collections.impl.set.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableTripletonSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLkltbXV0YWJsZVNl\n"
                        + "dFNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHB3BAAAAANzcgARamF2YS5sYW5nLkludGVn\n"
                        + "ZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAA\n"
                        + "AXNxAH4AAgAAAAJzcQB+AAIAAAADeA==",
                new ImmutableTripletonSet<Integer>(1, 2, 3));
    }
}
