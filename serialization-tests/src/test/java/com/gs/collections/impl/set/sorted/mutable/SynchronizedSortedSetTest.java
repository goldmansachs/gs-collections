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

package com.gs.collections.impl.set.sorted.mutable;

import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedSortedSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc29ydGVkLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkU29ydGVkU2V0AAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29s\n"
                        + "bGVjdGlvbi5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAJM\n"
                        + "AApjb2xsZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxl\n"
                        + "Q29sbGVjdGlvbjtMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgA4Y29tLmdzLmNvbGxl\n"
                        + "Y3Rpb25zLmltcGwuc2V0LnNvcnRlZC5tdXRhYmxlLlRyZWVTb3J0ZWRTZXQAAAAAAAAAAQwAAHhw\n"
                        + "cHcEAAAAAHhxAH4ABA==",
                SynchronizedSortedSet.of(SortedSets.mutable.of()));
    }
}
