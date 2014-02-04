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
public class UnmodifiableSortedBagTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcuc29ydGVkLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlU29ydGVkQmFnAAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29s\n"
                        + "bGVjdGlvbi5tdXRhYmxlLlVubW9kaWZpYWJsZU11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAFM\n"
                        + "AApjb2xsZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxl\n"
                        + "Q29sbGVjdGlvbjt4cHNyADJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcuc29ydGVkLm11dGFi\n"
                        + "bGUuVHJlZUJhZwAAAAAAAAABDAAAeHBzcgBDY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuYmxvY2su\n"
                        + "ZmFjdG9yeS5Db21wYXJhdG9ycyRSZXZlcnNlQ29tcGFyYXRvcgAAAAAAAAABAgABTAAKY29tcGFy\n"
                        + "YXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRvcjt4cHNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3JzJE5hdHVyYWxPcmRlckNvbXBhcmF0b3IAAAAAAAAA\n"
                        + "AQIAAHhwdwQAAAAAeA==",
                UnmodifiableSortedBag.of(TreeBag.newBag(Comparators.reverseNaturalOrder())));
    }
}
