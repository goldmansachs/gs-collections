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

public class UnmodifiableObjectFloatMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlT2JqZWN0RmxvYXRNYXAAAAAAAAAAAQIAAUwAA21hcHQAPExjb20vZ3MvY29sbGVj\n"
                        + "dGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJsZU9iamVjdEZsb2F0TWFwO3hwc3IAQGNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxlLnByaW1pdGl2ZS5PYmplY3RGbG9hdEhhc2hN\n"
                        + "YXAAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new UnmodifiableObjectFloatMap<Object>(new ObjectFloatHashMap<Object>()));
    }
}
