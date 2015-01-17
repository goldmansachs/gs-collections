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

import com.gs.collections.impl.block.procedure.primitive.InjectIntoDoubleProcedure;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class InjectIntoDoubleProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUucHJpbWl0aXZl\n"
                        + "LkluamVjdEludG9Eb3VibGVQcm9jZWR1cmUAAAAAAAAAAQIAAkQABnJlc3VsdEwACGZ1bmN0aW9u\n"
                        + "dABOTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vcHJpbWl0aXZlL0RvdWJs\n"
                        + "ZU9iamVjdFRvRG91YmxlRnVuY3Rpb247eHAAAAAAAAAAAHA=",
                new InjectIntoDoubleProcedure<Object>(0.0, null));
    }
}
