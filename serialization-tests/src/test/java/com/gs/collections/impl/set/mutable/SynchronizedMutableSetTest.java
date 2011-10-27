/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.set.mutable;

import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedMutableSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5TeW5jaHJvbml6ZWRN\n"
                        + "dXRhYmxlU2V0AAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29sbGVjdGlv\n"
                        + "bi5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAJMAApjb2xs\n"
                        + "ZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxlQ29sbGVj\n"
                        + "dGlvbjtMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuc2V0Lm11dGFibGUuVW5pZmllZFNldAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeHEAfgAE\n",
                SynchronizedMutableSet.of(Sets.mutable.of()));
    }
}
