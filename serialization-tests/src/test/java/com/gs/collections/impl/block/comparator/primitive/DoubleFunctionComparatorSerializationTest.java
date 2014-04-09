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

package com.gs.collections.impl.block.comparator.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class DoubleFunctionComparatorSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLnByaW1pdGl2\n"
                        + "ZS5Eb3VibGVGdW5jdGlvbkNvbXBhcmF0b3IAAAAAAAAAAQIAAUwACGZ1bmN0aW9udABATGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0RvdWJsZUZ1bmN0aW9u\n"
                        + "O3hwcA==",
                new DoubleFunctionComparator<Object>(null));
    }
}
