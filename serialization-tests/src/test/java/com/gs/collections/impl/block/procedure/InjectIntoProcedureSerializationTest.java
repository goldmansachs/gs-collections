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

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class InjectIntoProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuSW5qZWN0SW50\n"
                        + "b1Byb2NlZHVyZQAAAAAAAAABAgACTAAIZnVuY3Rpb250ADFMY29tL2dzL2NvbGxlY3Rpb25zL2Fw\n"
                        + "aS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjI7TAAGcmVzdWx0dAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "eHBwcA==",
                new InjectIntoProcedure<Object, Object>(null, null));
    }
}
