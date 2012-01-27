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

package ponzu.impl.map.mutable;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class MapAdapterSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5NYXBBZGFwdGVyAAAA\n"
                        + "AAAAAAECAAFMAAhkZWxlZ2F0ZXQAD0xqYXZhL3V0aWwvTWFwO3hwc3IALmNvbS5ncy5jb2xsZWN0\n"
                        + "aW9ucy5pbXBsLm1hcC5tdXRhYmxlLlVuaWZpZWRNYXAAAAAAAAAAAQwAAHhwdwgAAAAAP0AAAHg=\n",
                new MapAdapter<Object, Object>(UnifiedMap.<Object, Object>newMap()));
    }

    @Test
    public void keySet()
    {
        Verify.assertSerializedForm(
                1L,
                UnifiedMapTest.UNIFIED_MAP_KEY_SET,
                new MapAdapter<Object, Object>(UnifiedMap.<Object, Object>newMap()).keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                1L,
                UnifiedMapTest.UNIFIED_MAP_ENTRY_SET,
                new MapAdapter<Object, Object>(UnifiedMap.<Object, Object>newMap()).entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                1L,
                UnifiedMapTest.UNIFIED_MAP_VALUES,
                new MapAdapter<Object, Object>(UnifiedMap.<Object, Object>newMap()).values());
    }
}
