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

public class SynchronizedCharStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5T\n"
                        + "eW5jaHJvbml6ZWRDaGFyU3RhY2sAAAAAAAAAAQIAAkwABGxvY2t0ABJMamF2YS9sYW5nL09iamVj\n"
                        + "dDtMAAVzdGFja3QAOUxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL3N0YWNrL3ByaW1pdGl2ZS9NdXRh\n"
                        + "YmxlQ2hhclN0YWNrO3hwcQB+AANzcgA+Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwuc3RhY2subXV0\n"
                        + "YWJsZS5wcmltaXRpdmUuQ2hhckFycmF5U3RhY2sAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new SynchronizedCharStack(new CharArrayStack()));
    }
}
