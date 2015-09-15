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

package com.gs.collections.impl.multimap.bag.sorted.immutable;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationTestCase;
import com.gs.collections.impl.multimap.bag.sorted.mutable.TreeBagMultimap;

public class ImmutableSortedBagMultimapImplSerializationTest extends ImmutableMultimapSerializationTestCase
{
    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new TreeBagMultimap<String, String>();
    }

    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAIFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5iYWcuc29ydGVkLmltbXV0\n"
                + "YWJsZS5JbW11dGFibGVTb3J0ZWRCYWdNdWx0aW1hcEltcGwkSW1tdXRhYmxlU29ydGVkQmFnTXVs\n"
                + "dGltYXBTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwAAHhwcHcEAAAAAnQAAUF3BAAAAANxAH4A\n"
                + "AnQAAUJxAH4AA3EAfgADdwQAAAABcQB+AAJ4";
    }
}

