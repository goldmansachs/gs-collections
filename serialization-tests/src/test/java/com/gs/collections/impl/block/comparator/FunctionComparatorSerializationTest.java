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

package com.gs.collections.impl.block.comparator;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class FunctionComparatorSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLkZ1bmN0aW9u\n"
                        + "Q29tcGFyYXRvcgAAAAAAAAABAgACTAAKY29tcGFyYXRvcnQAFkxqYXZhL3V0aWwvQ29tcGFyYXRv\n"
                        + "cjtMAAhmdW5jdGlvbnQAMExjb20vZ3MvY29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL0Z1\n"
                        + "bmN0aW9uO3hwcHA=",
                new FunctionComparator<Object, Object>(null, null));
    }
}
