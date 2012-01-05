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

public class ComparatorProcedureTest
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
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQ29tcGFyYXRv\n"
                        + "clByb2NlZHVyZVRlc3QkMQAAAAAAAAABAgAAeHIAO2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJs\n"
                        + "b2NrLnByb2NlZHVyZS5Db21wYXJhdG9yUHJvY2VkdXJlAAAAAAAAAAECAANaABJ2aXNpdGVkQXRM\n"
                        + "ZWFzdE9uY2VMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9Db21wYXJhdG9yO0wABnJlc3VsdHQA\n"
                        + "EkxqYXZhL2xhbmcvT2JqZWN0O3hwAHBw",
                COMPARATOR_PROCEDURE);
    }
}
