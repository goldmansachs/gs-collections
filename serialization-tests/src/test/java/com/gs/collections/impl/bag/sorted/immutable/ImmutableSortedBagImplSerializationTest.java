/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.bag.sorted.immutable;

import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.SortedBags;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableSortedBagImplSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcuc29ydGVkLmltbXV0YWJsZS5JbW11\n"
                        + "dGFibGVTb3J0ZWRCYWdTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwAAHhwdwQAAAADcHNyABFq\n"
                        + "YXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoas\n"
                        + "lR0LlOCLAgAAeHAAAAABdwQAAAABc3EAfgACAAAAAncEAAAAAXNxAH4AAgAAAAN3BAAAAAF4",
                SortedBags.immutable.with(1, 2, 3));
    }

    @Test
    public void serializedForm_comparator()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcuc29ydGVkLmltbXV0YWJsZS5JbW11\n"
                        + "dGFibGVTb3J0ZWRCYWdTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwAAHhwdwQAAAADc3IAQ2Nv\n"
                        + "bS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFyYXRvcnMkUmV2ZXJzZUNv\n"
                        + "bXBhcmF0b3IAAAAAAAAAAQIAAUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBhcmF0b3I7\n"
                        + "eHBzcgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Db21wYXJhdG9ycyRO\n"
                        + "YXR1cmFsT3JkZXJDb21wYXJhdG9yAAAAAAAAAAECAAB4cHNyABFqYXZhLmxhbmcuSW50ZWdlchLi\n"
                        + "oKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAADdwQA\n"
                        + "AAABc3EAfgAHAAAAAncEAAAAAXNxAH4ABwAAAAF3BAAAAAF4",
                SortedBags.immutable.of(Comparators.reverseNaturalOrder(), 1, 2, 3));
    }
}
