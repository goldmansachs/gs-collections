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

package com.gs.collections.impl.block.function;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class MinSizeFunctionSerializationTest
{
    @Test
    public void minSizeCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NaW5TaXplRnVu\n"
                        + "Y3Rpb24kTWluU2l6ZUNvbGxlY3Rpb25GdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                MinSizeFunction.COLLECTION);
    }

    @Test
    public void minSizeMap()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NaW5TaXplRnVu\n"
                        + "Y3Rpb24kTWluU2l6ZU1hcEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MinSizeFunction.MAP);
    }

    @Test
    public void minSizeString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NaW5TaXplRnVu\n"
                        + "Y3Rpb24kTWluU2l6ZVN0cmluZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MinSizeFunction.STRING);
    }
}
