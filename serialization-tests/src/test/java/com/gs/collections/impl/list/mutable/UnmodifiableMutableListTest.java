/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.list.mutable;

import java.util.LinkedList;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableMutableListTest
{
    @Test
    public void serializedForm_random_access()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAGBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuVW5tb2RpZmlhYmxl\n"
                        + "TXV0YWJsZUxpc3QkUmFuZG9tQWNjZXNzVW5tb2RpZmlhYmxlTXV0YWJsZUxpc3QAAAAAAAAAAQIA\n"
                        + "AHhyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuVW5tb2RpZmlhYmxlTXV0\n"
                        + "YWJsZUxpc3QAAAAAAAAAAQIAAHhyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9u\n"
                        + "Lm11dGFibGUuVW5tb2RpZmlhYmxlTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAUwACmNvbGxl\n"
                        + "Y3Rpb250ADVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xsZWN0\n"
                        + "aW9uO3hwc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAA\n"
                        + "AAAAAAABDAAAeHB3BAAAAAB4",
                UnmodifiableMutableList.of(FastList.newList()));
    }

    @Test
    public void serializedForm_not_random_access()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuVW5tb2RpZmlhYmxl\n"
                        + "TXV0YWJsZUxpc3QAAAAAAAAAAQIAAHhyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0\n"
                        + "aW9uLm11dGFibGUuVW5tb2RpZmlhYmxlTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAUwACmNv\n"
                        + "bGxlY3Rpb250ADVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xs\n"
                        + "ZWN0aW9uO3hwc3IAMGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5MaXN0QWRh\n"
                        + "cHRlcgAAAAAAAAABAgABTAAIZGVsZWdhdGV0ABBMamF2YS91dGlsL0xpc3Q7eHBzcgAUamF2YS51\n"
                        + "dGlsLkxpbmtlZExpc3QMKVNdSmCIIgMAAHhwdwQAAAAAeA==",
                UnmodifiableMutableList.of(new LinkedList<Object>()));
    }
}
