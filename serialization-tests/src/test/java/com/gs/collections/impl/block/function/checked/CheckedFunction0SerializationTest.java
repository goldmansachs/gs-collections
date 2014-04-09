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

public class CheckedFunction0SerializationTest
{
    private static final CheckedFunction0<?> CHECKED_FUNCTION_0 = new CheckedFunction0<Object>()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Object safeValue() throws Exception
        {
            return null;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVja2VkLkNo\n"
                        + "ZWNrZWRGdW5jdGlvbjBTZXJpYWxpemF0aW9uVGVzdCQxAAAAAAAAAAECAAB4cgA/Y29tLmdzLmNv\n"
                        + "bGxlY3Rpb25zLmltcGwuYmxvY2suZnVuY3Rpb24uY2hlY2tlZC5DaGVja2VkRnVuY3Rpb24wAAAA\n"
                        + "AAAAAAECAAB4cA==",
                CHECKED_FUNCTION_0);
    }
}
