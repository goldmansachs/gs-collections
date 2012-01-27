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

package ponzu.impl.set.immutable;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class ImmutableUnifiedSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLkltbXV0YWJsZVVu\n"
                        + "aWZpZWRTZXQAAAAAAAAAAQIAAUwACGRlbGVnYXRldAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9pbXBs\n"
                        + "L3NldC9tdXRhYmxlL1VuaWZpZWRTZXQ7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuc2V0\n"
                        + "Lm11dGFibGUuVW5pZmllZFNldAAAAAAAAAABDAAAeHB3CAAAAAs/QAAAc3IAEWphdmEubGFuZy5J\n"
                        + "bnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4\n"
                        + "cAAAAAFzcQB+AAUAAAACc3EAfgAFAAAAA3NxAH4ABQAAAARzcQB+AAUAAAAFc3EAfgAFAAAABnNx\n"
                        + "AH4ABQAAAAdzcQB+AAUAAAAIc3EAfgAFAAAACXNxAH4ABQAAAApzcQB+AAUAAAALeA==",
                ImmutableUnifiedSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    }
}
