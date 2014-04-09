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

package com.gs.collections.impl.block.procedure.checked.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CheckedObjectBooleanProcedureSerializationTest
{
    private static final CheckedObjectBooleanProcedure<?> CHECKED_OBJECT_BOOLEAN_PROCEDURE = new CheckedObjectBooleanProcedure<Object>()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void safeValue(Object item1, boolean item2) throws Exception
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAGpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5w\n"
                        + "cmltaXRpdmUuQ2hlY2tlZE9iamVjdEJvb2xlYW5Qcm9jZWR1cmVTZXJpYWxpemF0aW9uVGVzdCQx\n"
                        + "AAAAAAAAAAECAAB4cgBXY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2sucHJvY2VkdXJlLmNo\n"
                        + "ZWNrZWQucHJpbWl0aXZlLkNoZWNrZWRPYmplY3RCb29sZWFuUHJvY2VkdXJlAAAAAAAAAAECAAB4\n"
                        + "cA==",
                CHECKED_OBJECT_BOOLEAN_PROCEDURE);
    }
}
