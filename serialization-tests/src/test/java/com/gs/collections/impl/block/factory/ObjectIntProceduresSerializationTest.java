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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ObjectIntProceduresSerializationTest
{
    @Test
    public void fromProcedure()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lk9iamVjdEludFBy\n"
                        + "b2NlZHVyZXMkUHJvY2VkdXJlQWRhcHRlcgAAAAAAAAABAgABTAAJcHJvY2VkdXJldAAyTGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJvY2VkdXJlL1Byb2NlZHVyZTt4cHA=",
                ObjectIntProcedures.fromProcedure(null));
    }
}
