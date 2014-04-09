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

public class CollectIfProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuQ29sbGVjdElm\n"
                        + "UHJvY2VkdXJlAAAAAAAAAAECAANMAApjb2xsZWN0aW9udAAWTGphdmEvdXRpbC9Db2xsZWN0aW9u\n"
                        + "O0wACGZ1bmN0aW9udAAwTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svZnVuY3Rpb24vRnVu\n"
                        + "Y3Rpb247TAAJcHJlZGljYXRldAAyTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJlZGlj\n"
                        + "YXRlL1ByZWRpY2F0ZTt4cHNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUu\n"
                        + "RmFzdExpc3QAAAAAAAAAAQwAAHhwdwQAAAAAeHBw",
                new CollectIfProcedure<Object, Object>(10, null, null));
    }
}
