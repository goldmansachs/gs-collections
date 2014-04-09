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

public class ImmutableDecapletonListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAD5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVE\n"
                        + "ZWNhcGxldG9uTGlzdAAAAAAAAAABAgAKTAAIZWxlbWVudDF0ABJMamF2YS9sYW5nL09iamVjdDtM\n"
                        + "AAllbGVtZW50MTBxAH4AAUwACGVsZW1lbnQycQB+AAFMAAhlbGVtZW50M3EAfgABTAAIZWxlbWVu\n"
                        + "dDRxAH4AAUwACGVsZW1lbnQ1cQB+AAFMAAhlbGVtZW50NnEAfgABTAAIZWxlbWVudDdxAH4AAUwA\n"
                        + "CGVsZW1lbnQ4cQB+AAFMAAhlbGVtZW50OXEAfgABeHBzcgARamF2YS5sYW5nLkludGVnZXIS4qCk\n"
                        + "94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAXNxAH4A\n"
                        + "AwAAAApzcQB+AAMAAAACc3EAfgADAAAAA3NxAH4AAwAAAARzcQB+AAMAAAAFc3EAfgADAAAABnNx\n"
                        + "AH4AAwAAAAdzcQB+AAMAAAAIc3EAfgADAAAACQ==",
                new ImmutableDecapletonList<Integer>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}
