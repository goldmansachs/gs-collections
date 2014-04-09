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

public class SynchronizedDoubleStackSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zdGFjay5tdXRhYmxlLnByaW1pdGl2ZS5T\n"
                        + "eW5jaHJvbml6ZWREb3VibGVTdGFjawAAAAAAAAABAgACTAAEbG9ja3QAEkxqYXZhL2xhbmcvT2Jq\n"
                        + "ZWN0O0wABXN0YWNrdAA7TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvc3RhY2svcHJpbWl0aXZlL011\n"
                        + "dGFibGVEb3VibGVTdGFjazt4cHEAfgADc3IAQGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnN0YWNr\n"
                        + "Lm11dGFibGUucHJpbWl0aXZlLkRvdWJsZUFycmF5U3RhY2sAAAAAAAAAAQwAAHhwdwQAAAAAeA==\n",
                new SynchronizedDoubleStack(new DoubleArrayStack()));
    }
}
