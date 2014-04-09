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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IfObjectIntProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                2L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuSWZPYmplY3RJ\n"
                        + "bnRQcm9jZWR1cmUAAAAAAAAAAgIAA0wABWluZGV4dAAhTGNvbS9ncy9jb2xsZWN0aW9ucy9pbXBs\n"
                        + "L0NvdW50ZXI7TAASb2JqZWN0SW50UHJvY2VkdXJldABFTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkv\n"
                        + "YmxvY2svcHJvY2VkdXJlL3ByaW1pdGl2ZS9PYmplY3RJbnRQcm9jZWR1cmU7TAAJcHJlZGljYXRl\n"
                        + "dAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTt4cHNy\n"
                        + "AB9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5Db3VudGVyAAAAAAAAAAEMAAB4cHcEAAAAAHhwcA==\n",
                new IfObjectIntProcedure<Object>(null, null));
    }
}
