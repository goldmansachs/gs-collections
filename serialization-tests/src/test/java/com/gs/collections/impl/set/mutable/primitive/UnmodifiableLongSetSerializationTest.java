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

package com.gs.collections.impl.set.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableLongSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlTG9uZ1NldAAAAAAAAAABAgAAeHIAV2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmNv\n"
                        + "bGxlY3Rpb24ubXV0YWJsZS5wcmltaXRpdmUuQWJzdHJhY3RVbm1vZGlmaWFibGVMb25nQ29sbGVj\n"
                        + "dGlvbgAAAAAAAAABAgABTAAKY29sbGVjdGlvbnQAQ0xjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Nv\n"
                        + "bGxlY3Rpb24vcHJpbWl0aXZlL011dGFibGVMb25nQ29sbGVjdGlvbjt4cHNyADljb20uZ3MuY29s\n"
                        + "bGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuTG9uZ0hhc2hTZXQAAAAAAAAAAQwA\n"
                        + "AHhwdwQAAAAAeA==",
                new UnmodifiableLongSet(new LongHashSet()));
    }
}
