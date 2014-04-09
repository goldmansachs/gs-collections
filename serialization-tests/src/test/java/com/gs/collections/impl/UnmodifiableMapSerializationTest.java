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

package com.gs.collections.impl;

import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyACdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5Vbm1vZGlmaWFibGVNYXAAAAAAAAAAAQIA\n"
                        + "AUwACGRlbGVnYXRldAAPTGphdmEvdXRpbC9NYXA7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwubWFwLm11dGFibGUuVW5pZmllZE1hcAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeA==",
                new UnmodifiableMap<Object, Object>(Maps.mutable.of()));
    }

    @Test
    public void keySet()
    {
        Verify.assertSerializedForm(
                -9215047833775013803L,
                "rO0ABXNyACVqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlU2V0gB2S0Y+bgFUCAAB4\n"
                        + "cgAsamF2YS51dGlsLkNvbGxlY3Rpb25zJFVubW9kaWZpYWJsZUNvbGxlY3Rpb24ZQgCAy173HgIA\n"
                        + "AUwAAWN0ABZMamF2YS91dGlsL0NvbGxlY3Rpb247eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuc2V0Lm11dGFibGUuVW5pZmllZFNldAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeA==",
                new UnmodifiableMap<Object, Object>(Maps.mutable.of()).keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                7854390611657943733L,
                "rO0ABXNyADpqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlTWFwJFVubW9kaWZpYWJs\n"
                        + "ZUVudHJ5U2V0bQBmpZ8I6rUCAAB4cgAlamF2YS51dGlsLkNvbGxlY3Rpb25zJFVubW9kaWZpYWJs\n"
                        + "ZVNldIAdktGPm4BVAgAAeHIALGphdmEudXRpbC5Db2xsZWN0aW9ucyRVbm1vZGlmaWFibGVDb2xs\n"
                        + "ZWN0aW9uGUIAgMte9x4CAAFMAAFjdAAWTGphdmEvdXRpbC9Db2xsZWN0aW9uO3hwc3IAN2NvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxlLlVuaWZpZWRNYXAkRW50cnlTZXQAAAAAAAAA\n"
                        + "AQIAAUwABnRoaXMkMHQAMExjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9tYXAvbXV0YWJsZS9Vbmlm\n"
                        + "aWVkTWFwO3hwc3IALmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxlLlVuaWZpZWRN\n"
                        + "YXAAAAAAAAAAAQwAAHhwdwgAAAAAP0AAAHg=",
                new UnmodifiableMap<Object, Object>(Maps.mutable.of()).entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                1820017752578914078L,
                "rO0ABXNyACxqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlQ29sbGVjdGlvbhlCAIDL\n"
                        + "XvceAgABTAABY3QAFkxqYXZhL3V0aWwvQ29sbGVjdGlvbjt4cHNyAC1jb20uZ3MuY29sbGVjdGlv\n"
                        + "bnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new UnmodifiableMap<Object, Object>(Maps.mutable.of()).values());
    }
}
