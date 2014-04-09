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

package com.gs.collections.impl.list.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableNonupletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVO\n"
                        + "b251cGxldG9uTGlzdAAAAAAAAAABAgAJTAAIZWxlbWVudDF0ABJMamF2YS9sYW5nL09iamVjdDtM\n"
                        + "AAhlbGVtZW50MnEAfgABTAAIZWxlbWVudDNxAH4AAUwACGVsZW1lbnQ0cQB+AAFMAAhlbGVtZW50\n"
                        + "NXEAfgABTAAIZWxlbWVudDZxAH4AAUwACGVsZW1lbnQ3cQB+AAFMAAhlbGVtZW50OHEAfgABTAAI\n"
                        + "ZWxlbWVudDlxAH4AAXhwc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhy\n"
                        + "ABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAFzcQB+AAMAAAACc3EAfgADAAAAA3Nx\n"
                        + "AH4AAwAAAARzcQB+AAMAAAAFc3EAfgADAAAABnNxAH4AAwAAAAdzcQB+AAMAAAAIc3EAfgADAAAA\n"
                        + "CQ==",
                new ImmutableNonupletonList<Integer>(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }
}
