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

package com.gs.collections.impl.set.strategy.mutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnifiedSetWithHashingStrategySerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc3RyYXRlZ3kubXV0YWJsZS5Vbmlm\n"
                        + "aWVkU2V0V2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABDAAAeHBzcgBHY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5IYXNoaW5nU3RyYXRlZ2llcyREZWZhdWx0U3RyYXRlZ3kA\n"
                        + "AAAAAAAAAQIAAHhwdwgAAAAAP0AAAHg=",
                UnifiedSetWithHashingStrategy.newSet(HashingStrategies.defaultStrategy()));
    }
}
