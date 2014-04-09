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

public class CheckedProcedure2SerializationTest
{
    private static final CheckedProcedure2<?, ?> CHECKED_PROCEDURE_2 = new CheckedProcedure2<Object, Object>()
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
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5D\n"
                        + "aGVja2VkUHJvY2VkdXJlMlNlcmlhbGl6YXRpb25UZXN0JDEAAAAAAAAAAQIAAHhyAEFjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5DaGVja2VkUHJvY2VkdXJl\n"
                        + "MgAAAAAAAAABAgAAeHA=",
                CHECKED_PROCEDURE_2);
    }
}
