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

public class MultiplyFunctionSerializationTest
{
    @Test
    public void multiplyDouble()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NdWx0aXBseUZ1\n"
                        + "bmN0aW9uJE11bHRpcGx5RG91YmxlRnVuY3Rpb24AAAAAAAAAAQIAAHhw",
                MultiplyFunction.DOUBLE);
    }

    @Test
    public void multiplyInteger()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NdWx0aXBseUZ1\n"
                        + "bmN0aW9uJE11bHRpcGx5SW50ZWdlckZ1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MultiplyFunction.INTEGER);
    }

    @Test
    public void multiplyLong()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5NdWx0aXBseUZ1\n"
                        + "bmN0aW9uJE11bHRpcGx5TG9uZ0Z1bmN0aW9uAAAAAAAAAAECAAB4cA==",
                MultiplyFunction.LONG);
    }
}
