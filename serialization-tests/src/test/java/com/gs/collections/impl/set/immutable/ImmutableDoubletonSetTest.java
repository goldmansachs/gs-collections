/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.set.immutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableDoubletonSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuaW1tdXRhYmxlLkltbXV0YWJsZURv\n"
                        + "dWJsZXRvblNldAAAAAAAAAABAgACTAAIZWxlbWVudDF0ABJMamF2YS9sYW5nL09iamVjdDtMAAhl\n"
                        + "bGVtZW50MnEAfgABeHBzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIA\n"
                        + "EGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAXNxAH4AAwAAAAI=",
                new ImmutableDoubletonSet<Integer>(1, 2));
    }
}
