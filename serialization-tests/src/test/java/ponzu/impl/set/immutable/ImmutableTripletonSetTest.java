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

public class ImmutableTripletonSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLkltbXV0YWJsZVRy\n"
                        + "aXBsZXRvblNldAAAAAAAAAABAgADTAAIZWxlbWVudDF0ABJMamF2YS9sYW5nL09iamVjdDtMAAhl\n"
                        + "bGVtZW50MnEAfgABTAAIZWxlbWVudDNxAH4AAXhwc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeB\n"
                        + "hzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAFzcQB+AAMA\n"
                        + "AAACc3EAfgADAAAAAw==",
                new ImmutableTripletonSet<Integer>(1, 2, 3));
    }
}
