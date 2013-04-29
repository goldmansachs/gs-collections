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

public class SynchronizedObjectShortHashMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAExjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkT2JqZWN0U2hvcnRIYXNoTWFwAAAAAAAAAAECAAJMAARsb2NrdAASTGphdmEvbGFu\n"
                        + "Zy9PYmplY3Q7TAADbWFwdAA8TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvbWFwL3ByaW1pdGl2ZS9N\n"
                        + "dXRhYmxlT2JqZWN0U2hvcnRNYXA7eHBxAH4AA3NyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5t\n"
                        + "YXAubXV0YWJsZS5wcmltaXRpdmUuT2JqZWN0U2hvcnRIYXNoTWFwAAAAAAAAAAEMAAB4cHcIAAAA\n"
                        + "AD8AAAB4",
                new SynchronizedObjectShortHashMap<Object>(new ObjectShortHashMap<Object>()));
    }
}
