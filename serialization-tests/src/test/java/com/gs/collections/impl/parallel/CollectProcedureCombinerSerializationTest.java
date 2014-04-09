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

package com.gs.collections.impl.parallel;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CollectProcedureCombinerSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5Db2xsZWN0UHJvY2VkdXJl\n"
                        + "Q29tYmluZXIAAAAAAAAAAQIAAHhyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5B\n"
                        + "YnN0cmFjdFRyYW5zZm9ybWVyQmFzZWRDb21iaW5lcgAAAAAAAAABAgABTAAGcmVzdWx0dAAWTGph\n"
                        + "dmEvdXRpbC9Db2xsZWN0aW9uO3hyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5B\n"
                        + "YnN0cmFjdFByb2NlZHVyZUNvbWJpbmVyAAAAAAAAAAECAAFaAA11c2VDb21iaW5lT25leHAAc3IA\n"
                        + "LWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAA\n"
                        + "eHB3BAAAAAB4",
                new CollectProcedureCombiner<Object, Object>(null, null, 1, false));
    }
}
