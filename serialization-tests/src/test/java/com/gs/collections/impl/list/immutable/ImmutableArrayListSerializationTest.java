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

public class ImmutableArrayListSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0LmltbXV0YWJsZS5JbW11dGFibGVB\n"
                        + "cnJheUxpc3QAAAAAAAAAAQIAAVsABWl0ZW1zdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwdXIAFFtM\n"
                        + "amF2YS5sYW5nLkludGVnZXI7/petoAGD4hsCAAB4cAAAAAtzcgARamF2YS5sYW5nLkludGVnZXIS\n"
                        + "4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAXNx\n"
                        + "AH4ABQAAAAJzcQB+AAUAAAADc3EAfgAFAAAABHNxAH4ABQAAAAVzcQB+AAUAAAAGc3EAfgAFAAAA\n"
                        + "B3NxAH4ABQAAAAhzcQB+AAUAAAAJc3EAfgAFAAAACnNxAH4ABQAAAAs=",
                ImmutableArrayList.newListWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    }
}
