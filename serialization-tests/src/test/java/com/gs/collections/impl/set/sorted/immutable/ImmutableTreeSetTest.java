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

package com.gs.collections.impl.set.sorted.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableTreeSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc29ydGVkLmltbXV0YWJsZS5JbW11\n"
                        + "dGFibGVUcmVlU2V0AAAAAAAAAAECAAFMAAhkZWxlZ2F0ZXQAOkxjb20vZ3MvY29sbGVjdGlvbnMv\n"
                        + "aW1wbC9zZXQvc29ydGVkL211dGFibGUvVHJlZVNvcnRlZFNldDt4cHNyADhjb20uZ3MuY29sbGVj\n"
                        + "dGlvbnMuaW1wbC5zZXQuc29ydGVkLm11dGFibGUuVHJlZVNvcnRlZFNldAAAAAAAAAABDAAAeHBw\n"
                        + "dwQAAAAEc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxh\n"
                        + "bmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAFzcQB+AAUAAAACc3EAfgAFAAAAA3NxAH4ABQAAAAR4\n",
                ImmutableTreeSet.newSetWith(1, 2, 3, 4));
    }
}
