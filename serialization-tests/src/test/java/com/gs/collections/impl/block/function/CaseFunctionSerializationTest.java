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

package com.gs.collections.impl.block.function;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CaseFunctionSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mdW5jdGlvbi5DYXNlRnVuY3Rp\n"
                        + "b24AAAAAAAAAAQIAAkwAD2RlZmF1bHRGdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBp\n"
                        + "L2Jsb2NrL2Z1bmN0aW9uL0Z1bmN0aW9uO0wAEnByZWRpY2F0ZUZ1bmN0aW9uc3QAKUxjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2xpc3QvTXV0YWJsZUxpc3Q7eHBwc3IALWNvbS5ncy5jb2xsZWN0aW9u\n"
                        + "cy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4",
                new CaseFunction<String, Object>());
    }
}
