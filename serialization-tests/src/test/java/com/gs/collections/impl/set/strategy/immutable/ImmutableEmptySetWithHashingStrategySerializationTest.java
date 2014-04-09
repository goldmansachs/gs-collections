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

package com.gs.collections.impl.set.strategy.immutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableEmptySetWithHashingStrategySerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAGBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc3RyYXRlZ3kuaW1tdXRhYmxlLklt\n"
                        + "bXV0YWJsZVNldFdpdGhIYXNoaW5nU3RyYXRlZ3lTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwA\n"
                        + "AHhwc3IAR2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuSGFzaGluZ1N0cmF0\n"
                        + "ZWdpZXMkRGVmYXVsdFN0cmF0ZWd5AAAAAAAAAAECAAB4cHcEAAAAAHg=",
                new ImmutableEmptySetWithHashingStrategy<Object>(HashingStrategies.defaultStrategy()));
    }
}
