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

package com.gs.collections.impl.list.mutable;

import java.util.LinkedList;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableMutableListSerializationTest
{
    @Test
    public void serializedForm_random_access()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgAtY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwubGlzdC5tdXRhYmxlLkZhc3RMaXN0AAAAAAAAAAEMAAB4cHcEAAAA\n"
                        + "AHh4",
                UnmodifiableMutableList.of(FastList.newList()));
    }

    @Test
    public void serializedForm_not_random_access()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgAwY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwubGlzdC5tdXRhYmxlLkxpc3RBZGFwdGVyAAAAAAAAAAECAAFMAAhk\n"
                        + "ZWxlZ2F0ZXQAEExqYXZhL3V0aWwvTGlzdDt4cHNyABRqYXZhLnV0aWwuTGlua2VkTGlzdAwpU11K\n"
                        + "YIgiAwAAeHB3BAAAAAB4eA==",
                UnmodifiableMutableList.of(new LinkedList<Object>()));
    }
}
