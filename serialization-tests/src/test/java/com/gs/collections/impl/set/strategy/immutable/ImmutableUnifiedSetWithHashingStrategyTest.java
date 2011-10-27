/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.set.strategy.immutable;

import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableUnifiedSetWithHashingStrategyTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc3RyYXRlZ3kuaW1tdXRhYmxlLklt\n"
                        + "bXV0YWJsZVVuaWZpZWRTZXRXaXRoSGFzaGluZ1N0cmF0ZWd5AAAAAAAAAAECAAFMAAhkZWxlZ2F0\n"
                        + "ZXQATExjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9zZXQvc3RyYXRlZ3kvbXV0YWJsZS9VbmlmaWVk\n"
                        + "U2V0V2l0aEhhc2hpbmdTdHJhdGVneTt4cHNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQu\n"
                        + "c3RyYXRlZ3kubXV0YWJsZS5VbmlmaWVkU2V0V2l0aEhhc2hpbmdTdHJhdGVneQAAAAAAAAABDAAA\n"
                        + "eHBzcgBHY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5IYXNoaW5nU3RyYXRl\n"
                        + "Z2llcyREZWZhdWx0U3RyYXRlZ3kAAAAAAAAAAQIAAHhwdwgAAAALP0AAAHNyABFqYXZhLmxhbmcu\n"
                        + "SW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAA\n"
                        + "eHAAAAABc3EAfgAHAAAAAnNxAH4ABwAAAANzcQB+AAcAAAAEc3EAfgAHAAAABXNxAH4ABwAAAAZz\n"
                        + "cQB+AAcAAAAHc3EAfgAHAAAACHNxAH4ABwAAAAlzcQB+AAcAAAAKc3EAfgAHAAAAC3g=",
                ImmutableUnifiedSetWithHashingStrategy.newSetWith(HashingStrategies.<Integer>defaultStrategy(), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    }
}
