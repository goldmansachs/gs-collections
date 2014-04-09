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

package com.gs.collections.impl.block.function.checked;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CheckedFunctionSerializationTest
{
    private static final CheckedFunction<?, ?> CHECKED_FUNCTION = new CheckedFunction<Object, Object>()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Object safeValueOf(Object object) throws Exception
        {
            return null;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVja2VkLkNo\n"
                        + "ZWNrZWRGdW5jdGlvblNlcmlhbGl6YXRpb25UZXN0JDEAAAAAAAAAAQIAAHhyAD5jb20uZ3MuY29s\n"
                        + "bGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVja2VkLkNoZWNrZWRGdW5jdGlvbgAAAAAA\n"
                        + "AAABAgAAeHA=",
                CHECKED_FUNCTION);
    }
}
