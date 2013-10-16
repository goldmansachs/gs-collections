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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * @since 4.2
 */
public class UnmodifiableBagTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcubXV0YWJsZS5Vbm1vZGlmaWFibGVC\n"
                        + "YWcAAAAAAAAAAQIAAHhyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFi\n"
                        + "bGUuVW5tb2RpZmlhYmxlTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAUwACmNvbGxlY3Rpb250\n"
                        + "ADVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xsZWN0aW9uO3hw\n"
                        + "c3IAK2NvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJhZy5tdXRhYmxlLkhhc2hCYWcAAAAAAAAAAQwA\n"
                        + "AHhwdwQAAAAAeA==",
                UnmodifiableBag.of(Bags.mutable.of()));
    }
}
