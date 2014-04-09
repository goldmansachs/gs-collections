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

import java.util.TreeMap;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableSortedMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc29ydGVkLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlU29ydGVkTWFwAAAAAAAAAAECAAB4cgAnY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuVW5t\n"
                        + "b2RpZmlhYmxlTWFwAAAAAAAAAAECAAFMAAhkZWxlZ2F0ZXQAD0xqYXZhL3V0aWwvTWFwO3hwc3IA\n"
                        + "EWphdmEudXRpbC5UcmVlTWFwDMH2Pi0lauYDAAFMAApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9D\n"
                        + "b21wYXJhdG9yO3hwcHcEAAAAAHg=",
                new UnmodifiableSortedMap<Object, Object>(new TreeMap<Object, Object>()));
    }
}
