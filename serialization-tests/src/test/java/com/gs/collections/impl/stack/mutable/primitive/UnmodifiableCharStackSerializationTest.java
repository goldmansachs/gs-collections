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

package com.gs.collections.impl.stack.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableCharStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5V\n"
                        + "bm1vZGlmaWFibGVDaGFyU3RhY2sAAAAAAAAAAQIAAUwABXN0YWNrdAA5TGNvbS9ncy9jb2xsZWN0\n"
                        + "aW9ucy9hcGkvc3RhY2svcHJpbWl0aXZlL011dGFibGVDaGFyU3RhY2s7eHBzcgA+Y29tLmdzLmNv\n"
                        + "bGxlY3Rpb25zLmltcGwuc3RhY2subXV0YWJsZS5wcmltaXRpdmUuQ2hhckFycmF5U3RhY2sAAAAA\n"
                        + "AAAAAQwAAHhwdwQAAAAAeA==",
                new UnmodifiableCharStack(new CharArrayStack()));
    }
}
