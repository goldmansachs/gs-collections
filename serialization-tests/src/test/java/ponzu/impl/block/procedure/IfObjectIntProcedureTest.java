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

package ponzu.impl.block.procedure;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class IfObjectIntProcedureTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcm9jZWR1cmUuSWZPYmplY3RJ\n"
                        + "bnRQcm9jZWR1cmUAAAAAAAAAAQIAA0wABWluZGV4dAAhTGNvbS9ncy9jb2xsZWN0aW9ucy9pbXBs\n"
                        + "L0NvdW50ZXI7TAASb2JqZWN0SW50UHJvY2VkdXJldAA7TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkv\n"
                        + "YmxvY2svcHJvY2VkdXJlL09iamVjdEludFByb2NlZHVyZTtMAAlwcmVkaWNhdGV0ADJMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9ibG9jay9wcmVkaWNhdGUvUHJlZGljYXRlO3hwc3IAH2NvbS5ncy5j\n"
                        + "b2xsZWN0aW9ucy5pbXBsLkNvdW50ZXIAAAAAAAAAAQwAAHhwdwQAAAAAeHBw",
                new IfObjectIntProcedure<Object>(null, null));
    }
}
