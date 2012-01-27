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

package ponzu.impl.collection.mutable;

import ponzu.impl.factory.Lists;
import ponzu.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableMutableCollectionTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAUwACmNvbGxlY3Rpb250ADVMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xsZWN0aW9uO3hwc3IALWNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAA\n"
                        + "AAB4",
                UnmodifiableMutableCollection.of(Lists.mutable.of()));
    }
}
