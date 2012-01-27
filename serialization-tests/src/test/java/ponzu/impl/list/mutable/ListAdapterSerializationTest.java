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

package ponzu.impl.list.mutable;

import ponzu.impl.factory.Lists;
import ponzu.impl.test.Verify;
import org.junit.Test;

public class ListAdapterSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuTGlzdEFkYXB0ZXIA\n"
                        + "AAAAAAAAAQIAAUwACGRlbGVnYXRldAAQTGphdmEvdXRpbC9MaXN0O3hwc3IALWNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                new ListAdapter<Object>(Lists.mutable.of()));
    }

    @Test
    public void subList()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAA\n"
                        + "AAAAAQwAAHhwdwQAAAAAeA==",
                new ListAdapter<Object>(Lists.mutable.of()).subList(0, 0));
    }
}
