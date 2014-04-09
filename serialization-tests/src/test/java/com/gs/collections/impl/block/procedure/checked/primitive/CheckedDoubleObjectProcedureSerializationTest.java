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

public class CheckedDoubleObjectProcedureSerializationTest
{
    private static final CheckedDoubleObjectProcedure<?> CHECKED_DOUBLE_OBJECT_PROCEDURE = new CheckedDoubleObjectProcedure<Object>()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void safeValue(double value, Object object) throws Exception
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAGljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hlY2tlZC5w\n"
                        + "cmltaXRpdmUuQ2hlY2tlZERvdWJsZU9iamVjdFByb2NlZHVyZVNlcmlhbGl6YXRpb25UZXN0JDEA\n"
                        + "AAAAAAAAAQIAAHhyAFZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuY2hl\n"
                        + "Y2tlZC5wcmltaXRpdmUuQ2hlY2tlZERvdWJsZU9iamVjdFByb2NlZHVyZQAAAAAAAAABAgAAeHA=\n",
                CHECKED_DOUBLE_OBJECT_PROCEDURE);
    }
}
