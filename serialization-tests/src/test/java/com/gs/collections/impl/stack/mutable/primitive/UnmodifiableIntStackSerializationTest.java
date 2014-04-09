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

package com.gs.collections.impl.stack.mutable.primitive;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableIntStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAERjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5V\n"
                        + "bm1vZGlmaWFibGVJbnRTdGFjawAAAAAAAAABAgABTAAFc3RhY2t0ADhMY29tL2dzL2NvbGxlY3Rp\n"
                        + "b25zL2FwaS9zdGFjay9wcmltaXRpdmUvTXV0YWJsZUludFN0YWNrO3hwc3IAPWNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLnN0YWNrLm11dGFibGUucHJpbWl0aXZlLkludEFycmF5U3RhY2sAAAAAAAAA\n"
                        + "AQwAAHhwdwQAAAAAeA==",
                new UnmodifiableIntStack(new IntArrayStack()));
    }
}
