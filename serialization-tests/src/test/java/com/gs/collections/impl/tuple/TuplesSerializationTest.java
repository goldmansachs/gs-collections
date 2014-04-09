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

package com.gs.collections.impl.tuple;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class TuplesSerializationTest
{
    @Test
    public void pair()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyACZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5QYWlySW1wbAAAAAAAAAABAgAC\n"
                        + "TAADb25ldAASTGphdmEvbGFuZy9PYmplY3Q7TAADdHdvcQB+AAF4cHBw",
                Tuples.pair(null, null));
    }

    @Test
    public void twin()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyACZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC50dXBsZS5Ud2luSW1wbAAAAAAAAAABAgAA\n"
                        + "eHIAJmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLnR1cGxlLlBhaXJJbXBsAAAAAAAAAAECAAJMAANv\n"
                        + "bmV0ABJMamF2YS9sYW5nL09iamVjdDtMAAN0d29xAH4AAnhwcHA=",
                Tuples.twin(null, null));
    }
}
