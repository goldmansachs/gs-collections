/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.block.procedure.primitive.InjectIntoLongProcedure;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class InjectIntoLongProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUucHJpbWl0aXZl\n"
                        + "LkluamVjdEludG9Mb25nUHJvY2VkdXJlAAAAAAAAAAECAAJKAAZyZXN1bHRMAAhmdW5jdGlvbnQA\n"
                        + "Skxjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9Mb25nT2Jq\n"
                        + "ZWN0VG9Mb25nRnVuY3Rpb247eHAAAAAAAAAAAHA=",
                new InjectIntoLongProcedure<Object>(0L, null));
    }
}
