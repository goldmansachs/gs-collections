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

public final class LongFunctionImplSerializationTest
{
    private static final LongFunctionImpl<?> LONG_FUNCTION = new LongFunctionImpl<Object>()
    {
        private static final long serialVersionUID = 1L;

        public long longValueOf(Object anObject)
        {
            return 0;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFRjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5wcmltaXRpdmUu\n"
                        + "TG9uZ0Z1bmN0aW9uSW1wbFNlcmlhbGl6YXRpb25UZXN0JDEAAAAAAAAAAQIAAHhyAEFjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5wcmltaXRpdmUuTG9uZ0Z1bmN0aW9uSW1w\n"
                        + "bAAAAAAAAAABAgAAeHA=",
                LONG_FUNCTION);
    }
}
