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

package com.gs.collections.impl;

import com.gs.collections.impl.block.function.primitive.IntegerFunctionImpl;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IntegerFunctionImplSerializationTest
{
    private static final IntegerFunctionImpl<Object> FUNCTION = new IntegerFunctionImpl<Object>()
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(Object o)
        {
            return 0;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5JbnRlZ2VyRnVuY3Rpb25JbXBsU2VyaWFs\n"
                        + "aXphdGlvblRlc3QkMQAAAAAAAAABAgAAeHIARGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LmZ1bmN0aW9uLnByaW1pdGl2ZS5JbnRlZ2VyRnVuY3Rpb25JbXBsAAAAAAAAAAECAAB4cA==",
                FUNCTION);
    }
}
