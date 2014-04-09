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

package com.gs.collections.impl.block;

import com.gs.collections.impl.block.factory.Functions2;
import com.gs.collections.impl.block.factory.ObjectIntProcedures;
import com.gs.collections.impl.block.factory.Procedures2;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class BlocksSerializationTest
{
    @Test
    public void asObjectIntProcedure()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5Lk9iamVjdEludFBy\n"
                        + "b2NlZHVyZXMkUHJvY2VkdXJlQWRhcHRlcgAAAAAAAAABAgABTAAJcHJvY2VkdXJldAAyTGNvbS9n\n"
                        + "cy9jb2xsZWN0aW9ucy9hcGkvYmxvY2svcHJvY2VkdXJlL1Byb2NlZHVyZTt4cHA=",
                ObjectIntProcedures.fromProcedure(null));
    }

    @Test
    public void asProcedure2()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMy\n"
                        + "JFByb2NlZHVyZUFkYXB0ZXIAAAAAAAAAAQIAAUwACXByb2NlZHVyZXQAMkxjb20vZ3MvY29sbGVj\n"
                        + "dGlvbnMvYXBpL2Jsb2NrL3Byb2NlZHVyZS9Qcm9jZWR1cmU7eHBw",
                Procedures2.fromProcedure(null));
    }

    @Test
    public void asFunction2()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LkZ1bmN0aW9uczIk\n"
                        + "RnVuY3Rpb25BZGFwdGVyAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlv\n"
                        + "bnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO3hwcA==",
                Functions2.fromFunction(null));
    }
}
