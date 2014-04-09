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

package com.gs.collections.impl.bag.sorted.mutable;

import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * @since 4.2
 */
public class UnmodifiableSortedBagSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgAyY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuYmFnLnNvcnRlZC5tdXRhYmxlLlRyZWVCYWcAAAAAAAAAAQwAAHhw\n"
                        + "c3IAQ2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFyYXRvcnMkUmV2\n"
                        + "ZXJzZUNvbXBhcmF0b3IAAAAAAAAAAQIAAUwACmNvbXBhcmF0b3J0ABZMamF2YS91dGlsL0NvbXBh\n"
                        + "cmF0b3I7eHBzcgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5Db21wYXJh\n"
                        + "dG9ycyROYXR1cmFsT3JkZXJDb21wYXJhdG9yAAAAAAAAAAECAAB4cHcEAAAAAHh4",
                UnmodifiableSortedBag.of(TreeBag.newBag(Comparators.reverseNaturalOrder())));
    }
}
