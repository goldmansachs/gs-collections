/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.multimap.bag.sorted.mutable;

import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class TreeBagMultimapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5iYWcuc29ydGVkLm11dGFi\n"
                        + "bGUuVHJlZUJhZ011bHRpbWFwAAAAAAAAAAEMAAB4cHB3BAAAAAB4",
                TreeBagMultimap.newMultimap());
    }

    @Test
    public void serializedForm_comparator()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5iYWcuc29ydGVkLm11dGFi\n"
                        + "bGUuVHJlZUJhZ011bHRpbWFwAAAAAAAAAAEMAAB4cHNyAENjb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5ibG9jay5mYWN0b3J5LkNvbXBhcmF0b3JzJFJldmVyc2VDb21wYXJhdG9yAAAAAAAAAAECAAFM\n"
                        + "AApjb21wYXJhdG9ydAAWTGphdmEvdXRpbC9Db21wYXJhdG9yO3hwc3IASGNvbS5ncy5jb2xsZWN0\n"
                        + "aW9ucy5pbXBsLmJsb2NrLmZhY3RvcnkuQ29tcGFyYXRvcnMkTmF0dXJhbE9yZGVyQ29tcGFyYXRv\n"
                        + "cgAAAAAAAAABAgAAeHB3BAAAAAB4",
                TreeBagMultimap.newMultimap(Comparators.reverseNaturalOrder()));
    }
}
