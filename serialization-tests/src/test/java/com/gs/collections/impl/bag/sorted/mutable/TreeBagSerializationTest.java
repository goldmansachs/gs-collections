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
public class TreeBagSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcuc29ydGVkLm11dGFibGUuVHJlZUJh\n"
                        + "ZwAAAAAAAAABDAAAeHBzcgBDY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5D\n"
                        + "b21wYXJhdG9ycyRSZXZlcnNlQ29tcGFyYXRvcgAAAAAAAAABAgABTAAKY29tcGFyYXRvcnQAFkxq\n"
                        + "YXZhL3V0aWwvQ29tcGFyYXRvcjt4cHNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5m\n"
                        + "YWN0b3J5LkNvbXBhcmF0b3JzJE5hdHVyYWxPcmRlckNvbXBhcmF0b3IAAAAAAAAAAQIAAHhwdwQA\n"
                        + "AAAAeA==",
                TreeBag.newBag(Comparators.reverseNaturalOrder()));
    }
}
