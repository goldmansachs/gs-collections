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

package com.gs.collections.impl.stack.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLlVubW9kaWZpYWJs\n"
                        + "ZVN0YWNrAAAAAAAAAAECAAFMAAxtdXRhYmxlU3RhY2t0ACtMY29tL2dzL2NvbGxlY3Rpb25zL2Fw\n"
                        + "aS9zdGFjay9NdXRhYmxlU3RhY2s7eHBzcgAwY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuc3RhY2su\n"
                        + "bXV0YWJsZS5BcnJheVN0YWNrAAAAAAAAAAEMAAB4cHcEAAAAAHg=",
                UnmodifiableStack.of(ArrayStack.newStack()));
    }
}
