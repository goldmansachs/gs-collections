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

package com.gs.collections.impl.map.sorted.mutable;

import com.gs.collections.impl.factory.SortedMaps;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableTreeMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc29ydGVkLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlVHJlZU1hcAAAAAAAAAABAgAAeHIAQGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5z\n"
                        + "b3J0ZWQubXV0YWJsZS5Vbm1vZGlmaWFibGVTb3J0ZWRNYXAAAAAAAAAAAQIAAHhyACdjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5Vbm1vZGlmaWFibGVNYXAAAAAAAAAAAQIAAUwACGRlbGVnYXRldAAP\n"
                        + "TGphdmEvdXRpbC9NYXA7eHBzcgA4Y29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLnNvcnRlZC5t\n"
                        + "dXRhYmxlLlRyZWVTb3J0ZWRNYXAAAAAAAAAAAQwAAHhwcHcEAAAAAHg=",
                UnmodifiableTreeMap.of(SortedMaps.mutable.of()));
    }
}
