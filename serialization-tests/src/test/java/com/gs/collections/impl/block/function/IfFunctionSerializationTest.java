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

package com.gs.collections.impl.block.function;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class IfFunctionSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5JZkZ1bmN0aW9u\n"
                        + "AAAAAAAAAAECAANMAAxlbHNlRnVuY3Rpb250ADBMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9j\n"
                        + "ay9mdW5jdGlvbi9GdW5jdGlvbjtMAAhmdW5jdGlvbnEAfgABTAAJcHJlZGljYXRldAAyTGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGljYXRlL1ByZWRpY2F0ZTt4cHBwcA==",
                new IfFunction<Object, Object>(null, null));
    }
}
