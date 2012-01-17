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

public class FilterNotProcedureCombinerTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5GaWx0ZXJOb3RQcm9jZWR1\n"
                        + "cmVDb21iaW5lcgAAAAAAAAABAgAAeHIAP2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnBhcmFsbGVs\n"
                        + "LkFic3RyYWN0UHJlZGljYXRlQmFzZWRDb21iaW5lcgAAAAAAAAABAgABTAAGcmVzdWx0dAAWTGph\n"
                        + "dmEvdXRpbC9Db2xsZWN0aW9uO3hyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5B\n"
                        + "YnN0cmFjdFByb2NlZHVyZUNvbWJpbmVyAAAAAAAAAAECAAFaAA11c2VDb21iaW5lT25leHABc3IA\n"
                        + "LWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAA\n"
                        + "eHB3BAAAAAB4",
                new FilterNotProcedureCombiner<Object>(null, null, 1, true));
    }
}
