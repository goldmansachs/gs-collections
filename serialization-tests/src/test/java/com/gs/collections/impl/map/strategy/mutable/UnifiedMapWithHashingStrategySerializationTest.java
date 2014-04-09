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

package com.gs.collections.impl.map.strategy.mutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnifiedMapWithHashingStrategySerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc3RyYXRlZ3kubXV0YWJsZS5Vbmlm\n"
                        + "aWVkTWFwV2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABDAAAeHBzcgBHY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5IYXNoaW5nU3RyYXRlZ2llcyREZWZhdWx0U3RyYXRlZ3kA\n"
                        + "AAAAAAAAAQIAAHhwdwgAAAAAP0AAAHg=",
                UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy()));
    }

    @Test
    public void keySet()
    {
        // SerialVersionUID not important for objects with writeReplace()
        Verify.assertSerializedForm(
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc3RyYXRlZ3kubXV0YWJsZS5Vbmlm\n"
                        + "aWVkU2V0V2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABDAAAeHBzcgBHY29tLmdzLmNvbGxlY3Rp\n"
                        + "b25zLmltcGwuYmxvY2suZmFjdG9yeS5IYXNoaW5nU3RyYXRlZ2llcyREZWZhdWx0U3RyYXRlZ3kA\n"
                        + "AAAAAAAAAQIAAHhwdwgAAAAAP0AAAHg=",
                UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy()).keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc3RyYXRlZ3kubXV0YWJsZS5Vbmlm\n"
                        + "aWVkTWFwV2l0aEhhc2hpbmdTdHJhdGVneSRFbnRyeVNldAAAAAAAAAABAgABTAAGdGhpcyQwdABM\n"
                        + "TGNvbS9ncy9jb2xsZWN0aW9ucy9pbXBsL21hcC9zdHJhdGVneS9tdXRhYmxlL1VuaWZpZWRNYXBX\n"
                        + "aXRoSGFzaGluZ1N0cmF0ZWd5O3hwc3IASmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5zdHJh\n"
                        + "dGVneS5tdXRhYmxlLlVuaWZpZWRNYXBXaXRoSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAEMAAB4cHNy\n"
                        + "AEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lkhhc2hpbmdTdHJhdGVnaWVz\n"
                        + "JERlZmF1bHRTdHJhdGVneQAAAAAAAAABAgAAeHB3CAAAAAA/QAAAeA==",
                UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy()).entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAA\n"
                        + "AAAAAQwAAHhwdwQAAAAAeA==",
                UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy()).values());
    }
}
