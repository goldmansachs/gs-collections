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

public class SynchronizedIntIntHashMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5wcmltaXRpdmUuU3lu\n"
                        + "Y2hyb25pemVkSW50SW50SGFzaE1hcAAAAAAAAAABAgACTAAEbG9ja3QAEkxqYXZhL2xhbmcvT2Jq\n"
                        + "ZWN0O0wAA21hcHQAN0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL21hcC9wcmltaXRpdmUvTXV0YWJs\n"
                        + "ZUludEludE1hcDt4cHEAfgADc3IAO2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxl\n"
                        + "LnByaW1pdGl2ZS5JbnRJbnRIYXNoTWFwAAAAAAAAAAEMAAB4cHcIAAAAAD8AAAB4",
                new SynchronizedIntIntHashMap(new IntIntHashMap()));
    }
}