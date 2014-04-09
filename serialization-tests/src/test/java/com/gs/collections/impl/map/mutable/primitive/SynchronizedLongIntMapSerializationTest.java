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

public class SynchronizedLongIntMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkTG9uZ0ludE1hcAAAAAAAAAABAgACTAAEbG9ja3QAEkxqYXZhL2xhbmcvT2JqZWN0\n"
                        + "O0wAA21hcHQAOExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJsZUxv\n"
                        + "bmdJbnRNYXA7eHBxAH4AA3NyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5w\n"
                        + "cmltaXRpdmUuTG9uZ0ludEhhc2hNYXAAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new SynchronizedLongIntMap(new LongIntHashMap()));
    }
}
