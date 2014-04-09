/*
 * Copyright 2013 Goldman Sachs.
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

import com.gs.collections.impl.partition.stack.PartitionArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class PartitionArrayStackPartitionProcedureSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJ0aXRpb24uc3RhY2suUGFydGl0aW9u\n"
                        + "QXJyYXlTdGFjayRQYXJ0aXRpb25Qcm9jZWR1cmUAAAAAAAAAAQIAAkwAFXBhcnRpdGlvbk11dGFi\n"
                        + "bGVTdGFja3QAPUxjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9wYXJ0aXRpb24vc3RhY2svUGFydGl0\n"
                        + "aW9uQXJyYXlTdGFjaztMAAlwcmVkaWNhdGV0ADJMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9ibG9j\n"
                        + "ay9wcmVkaWNhdGUvUHJlZGljYXRlO3hwcHA=",
                new PartitionArrayStack.PartitionProcedure<Object>(null, null));
    }
}
