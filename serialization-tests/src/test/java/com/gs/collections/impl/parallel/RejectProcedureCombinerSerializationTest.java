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

public class RejectProcedureCombinerSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5SZWplY3RQcm9jZWR1cmVD\n"
                        + "b21iaW5lcgAAAAAAAAABAgAAeHIAP2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnBhcmFsbGVsLkFi\n"
                        + "c3RyYWN0UHJlZGljYXRlQmFzZWRDb21iaW5lcgAAAAAAAAABAgABTAAGcmVzdWx0dAAWTGphdmEv\n"
                        + "dXRpbC9Db2xsZWN0aW9uO3hyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5BYnN0\n"
                        + "cmFjdFByb2NlZHVyZUNvbWJpbmVyAAAAAAAAAAECAAFaAA11c2VDb21iaW5lT25leHABc3IALWNv\n"
                        + "bS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3\n"
                        + "BAAAAAB4",
                new RejectProcedureCombiner<Object>(null, null, 1, true));
    }
}
