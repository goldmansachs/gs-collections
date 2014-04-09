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

public class SynchronizedObjectBooleanMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkT2JqZWN0Qm9vbGVhbk1hcAAAAAAAAAABAgACTAAEbG9ja3QAEkxqYXZhL2xhbmcv\n"
                        + "T2JqZWN0O0wAA21hcHQAPkxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0\n"
                        + "YWJsZU9iamVjdEJvb2xlYW5NYXA7eHBxAH4AA3NyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5t\n"
                        + "YXAubXV0YWJsZS5wcmltaXRpdmUuT2JqZWN0Qm9vbGVhbkhhc2hNYXAAAAAAAAAAAQwAAHhwdwgA\n"
                        + "AAAAPwAAAHg=",
                new SynchronizedObjectBooleanMap<Object>(new ObjectBooleanHashMap<Object>()));
    }
}
