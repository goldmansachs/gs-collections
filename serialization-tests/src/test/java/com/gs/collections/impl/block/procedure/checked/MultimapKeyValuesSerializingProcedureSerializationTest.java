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

package com.gs.collections.impl.block.procedure.checked;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class MultimapKeyValuesSerializingProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5N\n"
                        + "dWx0aW1hcEtleVZhbHVlc1NlcmlhbGl6aW5nUHJvY2VkdXJlAAAAAAAAAAECAAFMAANvdXR0ABZM\n"
                        + "amF2YS9pby9PYmplY3RPdXRwdXQ7eHIAQWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLnBy\n"
                        + "b2NlZHVyZS5jaGVja2VkLkNoZWNrZWRQcm9jZWR1cmUyAAAAAAAAAAECAAB4cHA=",
                new MultimapKeyValuesSerializingProcedure<Object, Object>(null));
    }
}
