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

public class CollectIfProcedureCombinerSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5Db2xsZWN0SWZQcm9jZWR1\n"
                        + "cmVDb21iaW5lcgAAAAAAAAABAgAAeHIAQWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnBhcmFsbGVs\n"
                        + "LkFic3RyYWN0VHJhbnNmb3JtZXJCYXNlZENvbWJpbmVyAAAAAAAAAAECAAFMAAZyZXN1bHR0ABZM\n"
                        + "amF2YS91dGlsL0NvbGxlY3Rpb247eHIAOmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnBhcmFsbGVs\n"
                        + "LkFic3RyYWN0UHJvY2VkdXJlQ29tYmluZXIAAAAAAAAAAQIAAVoADXVzZUNvbWJpbmVPbmV4cABz\n"
                        + "cgAtY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubGlzdC5tdXRhYmxlLkZhc3RMaXN0AAAAAAAAAAEM\n"
                        + "AAB4cHcEAAAAAHg=",
                new CollectIfProcedureCombiner<Object, Object>(null, null, 1, false));
    }
}
