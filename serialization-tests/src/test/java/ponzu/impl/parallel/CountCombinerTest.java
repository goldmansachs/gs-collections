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

package ponzu.impl.parallel;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class CountCombinerTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5Db3VudENvbWJpbmVyAAAA\n"
                        + "AAAAAAECAAFJAAVjb3VudHhyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5BYnN0\n"
                        + "cmFjdFByb2NlZHVyZUNvbWJpbmVyAAAAAAAAAAECAAFaAA11c2VDb21iaW5lT25leHABAAAAAA==\n",
                new CountCombiner<Object>());
    }
}