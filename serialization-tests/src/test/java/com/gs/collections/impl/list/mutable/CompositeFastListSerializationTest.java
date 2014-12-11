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

package com.gs.collections.impl.list.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CompositeFastListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                2L,
                "rO0ABXNyADZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuQ29tcG9zaXRlRmFz\n"
                        + "dExpc3QAAAAAAAAAAgIAAkkABHNpemVMAAVsaXN0c3QAL0xjb20vZ3MvY29sbGVjdGlvbnMvaW1w\n"
                        + "bC9saXN0L211dGFibGUvRmFzdExpc3Q7eHAAAAAAc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "Lmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                new CompositeFastList<Object>());
    }
}
