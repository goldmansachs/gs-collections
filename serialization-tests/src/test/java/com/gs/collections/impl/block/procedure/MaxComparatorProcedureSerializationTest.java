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

public class MaxComparatorProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuTWF4Q29tcGFy\n"
                        + "YXRvclByb2NlZHVyZQAAAAAAAAABAgAAeHIAO2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2Nr\n"
                        + "LnByb2NlZHVyZS5Db21wYXJhdG9yUHJvY2VkdXJlAAAAAAAAAAECAANaABJ2aXNpdGVkQXRMZWFz\n"
                        + "dE9uY2VMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9Db21wYXJhdG9yO0wABnJlc3VsdHQAEkxq\n"
                        + "YXZhL2xhbmcvT2JqZWN0O3hwAHBw",
                new MaxComparatorProcedure<Object>(null));
    }
}
