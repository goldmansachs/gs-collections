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

public class ImmutableQuadrupletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVR\n"
                        + "dWFkcnVwbGV0b25MaXN0AAAAAAAAAAECAARMAAhlbGVtZW50MXQAEkxqYXZhL2xhbmcvT2JqZWN0\n"
                        + "O0wACGVsZW1lbnQycQB+AAFMAAhlbGVtZW50M3EAfgABTAAIZWxlbWVudDRxAH4AAXhwc3IAEWph\n"
                        + "dmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyV\n"
                        + "HQuU4IsCAAB4cAAAAAFzcQB+AAMAAAACc3EAfgADAAAAA3NxAH4AAwAAAAQ=",
                new ImmutableQuadrupletonList<Integer>(1, 2, 3, 4));
    }
}
