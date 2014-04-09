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

package com.gs.collections.impl.map.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableCharByteMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlQ2hhckJ5dGVNYXAAAAAAAAAAAQIAAUwAA21hcHQAOUxjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJsZUNoYXJCeXRlTWFwO3hwc3IAPWNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxlLnByaW1pdGl2ZS5DaGFyQnl0ZUhhc2hNYXAAAAAAAAAA\n"
                        + "AQwAAHhwdwQAAAAAeA==",
                new UnmodifiableCharByteMap(new CharByteHashMap()));
    }
}
