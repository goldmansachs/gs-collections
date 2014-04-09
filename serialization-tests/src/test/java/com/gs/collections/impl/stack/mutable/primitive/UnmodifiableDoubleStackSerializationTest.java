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

public class UnmodifiableDoubleStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5V\n"
                        + "bm1vZGlmaWFibGVEb3VibGVTdGFjawAAAAAAAAABAgABTAAFc3RhY2t0ADtMY29tL2dzL2NvbGxl\n"
                        + "Y3Rpb25zL2FwaS9zdGFjay9wcmltaXRpdmUvTXV0YWJsZURvdWJsZVN0YWNrO3hwc3IAQGNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLnN0YWNrLm11dGFibGUucHJpbWl0aXZlLkRvdWJsZUFycmF5U3Rh\n"
                        + "Y2sAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                new UnmodifiableDoubleStack(new DoubleArrayStack()));
    }
}
