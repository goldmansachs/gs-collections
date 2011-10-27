/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.set.strategy.immutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableEmptySetWithHashingStrategyTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc3RyYXRlZ3kuaW1tdXRhYmxlLklt\n"
                        + "bXV0YWJsZUVtcHR5U2V0V2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABAgABTAAPaGFzaGluZ1N0\n"
                        + "cmF0ZWd5dAAuTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svSGFzaGluZ1N0cmF0ZWd5O3hw\n"
                        + "c3IAR2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuSGFzaGluZ1N0cmF0ZWdp\n"
                        + "ZXMkRGVmYXVsdFN0cmF0ZWd5AAAAAAAAAAECAAB4cA==",
                new ImmutableEmptySetWithHashingStrategy<Object>(HashingStrategies.<Object>defaultStrategy()));
    }
}
