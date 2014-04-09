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

package com.gs.collections.impl.block.procedure;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class MapCollectProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuTWFwQ29sbGVj\n"
                        + "dFByb2NlZHVyZQAAAAAAAAABAgADTAALa2V5RnVuY3Rpb250ADBMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2FwaS9ibG9jay9mdW5jdGlvbi9GdW5jdGlvbjtMAANtYXB0AA9MamF2YS91dGlsL01hcDtMAA12\n"
                        + "YWx1ZUZ1bmN0aW9ucQB+AAF4cHBwcA==",
                new MapCollectProcedure<Object, Object, Object>(null, null, null));
    }
}
