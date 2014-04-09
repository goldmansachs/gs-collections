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

public class MultimapEachPutProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuTXVsdGltYXBF\n"
                        + "YWNoUHV0UHJvY2VkdXJlAAAAAAAAAAECAANMAA1lYWNoUHJvY2VkdXJldAAzTGNvbS9ncy9jb2xs\n"
                        + "ZWN0aW9ucy9hcGkvYmxvY2svcHJvY2VkdXJlL1Byb2NlZHVyZTI7TAALa2V5RnVuY3Rpb250ADBM\n"
                        + "Y29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAAhtdWx0aW1h\n"
                        + "cHQAMUxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL211bHRpbWFwL011dGFibGVNdWx0aW1hcDt4cHNy\n"
                        + "AEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuTXVsdGltYXBFYWNoUHV0\n"
                        + "UHJvY2VkdXJlJDE7vei5hMKQJQIAAUwABnRoaXMkMHQAQkxjb20vZ3MvY29sbGVjdGlvbnMvaW1w\n"
                        + "bC9ibG9jay9wcm9jZWR1cmUvTXVsdGltYXBFYWNoUHV0UHJvY2VkdXJlO3hwcQB+AARwcA==",
                new MultimapEachPutProcedure<Object, Object>(null, null));
    }
}
