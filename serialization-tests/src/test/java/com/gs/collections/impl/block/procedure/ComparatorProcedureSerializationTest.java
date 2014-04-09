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

public class ComparatorProcedureSerializationTest
{
    private static final ComparatorProcedure<Object> COMPARATOR_PROCEDURE = new ComparatorProcedure<Object>(null)
    {
        private static final long serialVersionUID = 1L;

        public void value(Object each)
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQ29tcGFyYXRv\n"
                        + "clByb2NlZHVyZVNlcmlhbGl6YXRpb25UZXN0JDEAAAAAAAAAAQIAAHhyADtjb20uZ3MuY29sbGVj\n"
                        + "dGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQ29tcGFyYXRvclByb2NlZHVyZQAAAAAAAAABAgAD\n"
                        + "WgASdmlzaXRlZEF0TGVhc3RPbmNlTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRv\n"
                        + "cjtMAAZyZXN1bHR0ABJMamF2YS9sYW5nL09iamVjdDt4cABwcA==",
                COMPARATOR_PROCEDURE);
    }
}
