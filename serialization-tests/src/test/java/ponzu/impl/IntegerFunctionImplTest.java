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

package ponzu.impl;

import ponzu.impl.block.function.primitive.IntegerFunctionImpl;
import ponzu.impl.test.Verify;
import org.junit.Test;

public class IntegerFunctionImplTest
{
    private static final IntegerFunctionImpl<Object> FUNCTION = new IntegerFunctionImpl<Object>()
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(Object o)
        {
            return 0;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5JbnRlZ2VyRnVuY3Rpb25JbXBsVGVzdCQx\n"
                        + "AAAAAAAAAAECAAB4cgBEY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZnVuY3Rpb24ucHJp\n"
                        + "bWl0aXZlLkludGVnZXJGdW5jdGlvbkltcGwAAAAAAAAAAQIAAHhw",
                FUNCTION);
    }
}
