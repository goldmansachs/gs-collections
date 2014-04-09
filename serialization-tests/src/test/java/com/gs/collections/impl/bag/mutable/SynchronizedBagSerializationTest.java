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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * @since 4.2
 */
public class SynchronizedBagSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgArY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuYmFnLm11dGFibGUuSGFzaEJhZwAAAAAAAAABDAAAeHB3BAAAAAB4\n"
                        + "eA==",
                SynchronizedBag.of(Bags.mutable.of()));
    }
}
