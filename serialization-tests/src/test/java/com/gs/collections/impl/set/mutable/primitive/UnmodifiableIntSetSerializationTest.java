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

package com.gs.collections.impl.set.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableIntSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuVW5t\n"
                        + "b2RpZmlhYmxlSW50U2V0AAAAAAAAAAECAAB4cgBWY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29s\n"
                        + "bGVjdGlvbi5tdXRhYmxlLnByaW1pdGl2ZS5BYnN0cmFjdFVubW9kaWZpYWJsZUludENvbGxlY3Rp\n"
                        + "b24AAAAAAAAAAQIAAUwACmNvbGxlY3Rpb250AEJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xs\n"
                        + "ZWN0aW9uL3ByaW1pdGl2ZS9NdXRhYmxlSW50Q29sbGVjdGlvbjt4cHNyADhjb20uZ3MuY29sbGVj\n"
                        + "dGlvbnMuaW1wbC5zZXQubXV0YWJsZS5wcmltaXRpdmUuSW50SGFzaFNldAAAAAAAAAABDAAAeHB3\n"
                        + "BAAAAAB4",
                new UnmodifiableIntSet(new IntHashSet()));
    }
}
