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

import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedMutableListTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuU3luY2hyb25pemVk\n"
                        + "TXV0YWJsZUxpc3QAAAAAAAAAAQIAAHhyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0\n"
                        + "aW9uLm11dGFibGUuU3luY2hyb25pemVkTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAkwACmNv\n"
                        + "bGxlY3Rpb250ADVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xs\n"
                        + "ZWN0aW9uO0wABGxvY2t0ABJMamF2YS9sYW5nL09iamVjdDt4cHNyAC1jb20uZ3MuY29sbGVjdGlv\n"
                        + "bnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAAAAAAAQwAAHhwdwQAAAAAeHEAfgAE",
                SynchronizedMutableList.of(Lists.mutable.of()));
    }
}
