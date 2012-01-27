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

package ponzu.impl.block.function.checked;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class CheckedFunction2Test
{
    private static final CheckedFunction2 CHECKED_FUNCTION_2 = new CheckedFunction2()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public Object safeValue(Object argument1, Object argument2) throws Exception
        {
            return null;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVja2VkLkNo\n"
                        + "ZWNrZWRGdW5jdGlvbjJUZXN0JDEAAAAAAAAAAQIAAHhyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mdW5jdGlvbi5jaGVja2VkLkNoZWNrZWRGdW5jdGlvbjIAAAAAAAAAAQIAAHhw",
                CHECKED_FUNCTION_2);
    }
}
