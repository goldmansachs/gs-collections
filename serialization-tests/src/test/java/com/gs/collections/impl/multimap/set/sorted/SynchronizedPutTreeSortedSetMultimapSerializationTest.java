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

package com.gs.collections.impl.multimap.set.sorted;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.multimap.MutableMultimapSerializationTestCase;

public class SynchronizedPutTreeSortedSetMultimapSerializationTest
        extends MutableMultimapSerializationTestCase
{
    @Override
    protected long getExpectedSerialVersionUID()
    {
        return 2L;
    }

    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAFBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5zZXQuc29ydGVkLlN5bmNo\n"
                + "cm9uaXplZFB1dFRyZWVTb3J0ZWRTZXRNdWx0aW1hcAAAAAAAAAACDAAAeHBwdwQAAAACdAABQXcE\n"
                + "AAAAAnEAfgACdAABQnEAfgADdwQAAAABcQB+AAJ4";
    }

    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new SynchronizedPutTreeSortedSetMultimap<String, String>();
    }
}
