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

package com.gs.collections.impl.list.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IntIntervalSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LnByaW1pdGl2ZS5JbnRJbnRlcnZh\n"
                        + "bAAAAAAAAAABAgADSQAEZnJvbUkABHN0ZXBJAAJ0b3hwAAAAAAAAAAEAAAAA",
                IntInterval.fromToBy(0, 0, 1));
    }
}
