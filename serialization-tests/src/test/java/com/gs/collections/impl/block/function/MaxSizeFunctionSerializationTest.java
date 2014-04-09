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

public class MaxSizeFunctionSerializationTest
{
    @Test
    public void maxSizeCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NYXhTaXplRnVu\n"
                        + "Y3Rpb24kTWF4U2l6ZUNvbGxlY3Rpb25GdW5jdGlvbgAAAAAAAAABAgAAeHA=",
                MaxSizeFunction.COLLECTION);
    }

    @Test
    public void maxSizeMap()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NYXhTaXplRnVu\n"
                        + "Y3Rpb24kTWF4U2l6ZU1hcEZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MaxSizeFunction.MAP);
    }

    @Test
    public void maxSizeString()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NYXhTaXplRnVu\n"
                        + "Y3Rpb24kTWF4U2l6ZVN0cmluZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MaxSizeFunction.STRING);
    }
}
