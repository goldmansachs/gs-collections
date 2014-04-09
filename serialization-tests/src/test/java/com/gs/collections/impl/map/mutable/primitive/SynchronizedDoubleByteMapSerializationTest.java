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

public class SynchronizedDoubleByteMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkRG91YmxlQnl0ZU1hcAAAAAAAAAABAgACTAAEbG9ja3QAEkxqYXZhL2xhbmcvT2Jq\n"
                        + "ZWN0O0wAA21hcHQAO0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJs\n"
                        + "ZURvdWJsZUJ5dGVNYXA7eHBxAH4AA3NyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0\n"
                        + "YWJsZS5wcmltaXRpdmUuRG91YmxlQnl0ZUhhc2hNYXAAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new SynchronizedDoubleByteMap(new DoubleByteHashMap()));
    }
}
