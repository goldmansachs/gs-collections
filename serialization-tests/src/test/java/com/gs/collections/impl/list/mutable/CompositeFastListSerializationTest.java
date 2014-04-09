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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CompositeFastListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuQ29tcG9zaXRlRmFz\n"
                        + "dExpc3QAAAAAAAAAAQIAAUwABWxpc3RzdAAvTGNvbS9ncy9jb2xsZWN0aW9ucy9pbXBsL2xpc3Qv\n"
                        + "bXV0YWJsZS9GYXN0TGlzdDt4cHNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFi\n"
                        + "bGUuRmFzdExpc3QAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new CompositeFastList<Object>());
    }
}
