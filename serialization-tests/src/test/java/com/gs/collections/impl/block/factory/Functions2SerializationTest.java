/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class Functions2SerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczIk\n"
                        + "VGhyb3dpbmdGdW5jdGlvbjJBZGFwdGVyAAAAAAAAAAECAAFMABF0aHJvd2luZ0Z1bmN0aW9uMnQA\n"
                        + "Qkxjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9mdW5jdGlvbi9jaGVja2VkL1Rocm93aW5n\n"
                        + "RnVuY3Rpb24yO3hyAD9jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5jaGVj\n"
                        + "a2VkLkNoZWNrZWRGdW5jdGlvbjIAAAAAAAAAAQIAAHhwcA==",
                Functions2.throwing(null));
    }

    @Test
    public void integerAddition()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczIk\n"
                        + "SW50ZWdlckFkZGl0aW9uAAAAAAAAAAECAAB4cA==",
                Functions2.integerAddition());
    }

    @Test
    public void value()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczIk\n"
                        + "RnVuY3Rpb25BZGFwdGVyAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwcA==",
                Functions2.fromFunction(null));
    }
}
