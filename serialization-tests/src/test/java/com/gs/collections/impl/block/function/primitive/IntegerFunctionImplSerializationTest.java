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

package com.gs.collections.impl.block.function.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public final class IntegerFunctionImplSerializationTest
{
    private static final IntegerFunctionImpl<?> INTEGER_FUNCTION = new IntegerFunctionImpl<Object>()
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(Object anObject)
        {
            return 0;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5wcmltaXRpdmUu\n"
                        + "SW50ZWdlckZ1bmN0aW9uSW1wbFNlcmlhbGl6YXRpb25UZXN0JDEAAAAAAAAAAQIAAHhyAERjb20u\n"
                        + "Z3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5wcmltaXRpdmUuSW50ZWdlckZ1bmN0\n"
                        + "aW9uSW1wbAAAAAAAAAABAgAAeHA=",
                INTEGER_FUNCTION);
    }
}
