/*
 * Copyright 2011 Goldman Sachs & Co.
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

public class CheckedProcedure2Test
{
    private static final CheckedProcedure2 CHECKED_PROCEDURE_2 = new CheckedProcedure2()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void safeValue(Object object, Object parameter) throws Exception
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5D\n"
                        + "aGVja2VkUHJvY2VkdXJlMlRlc3QkMQAAAAAAAAABAgAAeHIAQWNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
                        + "bXBsLmJsb2NrLnByb2NlZHVyZS5jaGVja2VkLkNoZWNrZWRQcm9jZWR1cmUyAAAAAAAAAAECAAB4\n"
                        + "cA==",
                CHECKED_PROCEDURE_2);
    }
}
