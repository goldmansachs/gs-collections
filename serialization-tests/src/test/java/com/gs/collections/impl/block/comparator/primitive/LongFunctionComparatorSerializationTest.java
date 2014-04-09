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

package com.gs.collections.impl.block.comparator.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class LongFunctionComparatorSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEljb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5jb21wYXJhdG9yLnByaW1pdGl2\n"
                        + "ZS5Mb25nRnVuY3Rpb25Db21wYXJhdG9yAAAAAAAAAAECAAFMAAhmdW5jdGlvbnQAPkxjb20vZ3Mv\n"
                        + "Y29sbGVjdGlvbnMvYXBpL2Jsb2NrL2Z1bmN0aW9uL3ByaW1pdGl2ZS9Mb25nRnVuY3Rpb247eHBw\n",
                new LongFunctionComparator<Object>(null));
    }
}
