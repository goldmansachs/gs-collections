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

package com.gs.collections.impl.bag.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableBooleanBagSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlQm9vbGVhbkJhZwAAAAAAAAABAgAAeHIAWmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "LmNvbGxlY3Rpb24ubXV0YWJsZS5wcmltaXRpdmUuQWJzdHJhY3RVbm1vZGlmaWFibGVCb29sZWFu\n"
                        + "Q29sbGVjdGlvbgAAAAAAAAABAgABTAAKY29sbGVjdGlvbnQARkxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "YXBpL2NvbGxlY3Rpb24vcHJpbWl0aXZlL011dGFibGVCb29sZWFuQ29sbGVjdGlvbjt4cHNyADxj\n"
                        + "b20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcubXV0YWJsZS5wcmltaXRpdmUuQm9vbGVhbkhhc2hC\n"
                        + "YWcAAAAAAAAAAQwAAHhwdwgAAAAAAAAAAHg=",
                new UnmodifiableBooleanBag(new BooleanHashBag()));
    }
}
