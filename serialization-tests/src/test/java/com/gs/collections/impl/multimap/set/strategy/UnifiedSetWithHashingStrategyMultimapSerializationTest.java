/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.multimap.set.strategy;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.multimap.MutableMultimapSerializationTestCase;

public class UnifiedSetWithHashingStrategyMultimapSerializationTest
        extends MutableMultimapSerializationTestCase
{
    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5zZXQuc3RyYXRlZ3kuVW5p\n"
                + "ZmllZFNldFdpdGhIYXNoaW5nU3RyYXRlZ3lNdWx0aW1hcAAAAAAAAAABDAAAeHBzcgBHY29tLmdz\n"
                + "LmNvbGxlY3Rpb25zLmltcGwuYmxvY2suZmFjdG9yeS5IYXNoaW5nU3RyYXRlZ2llcyREZWZhdWx0\n"
                + "U3RyYXRlZ3kAAAAAAAAAAQIAAHhwdwQAAAACdAABQXcEAAAAAnEAfgAEdAABQnEAfgAFdwQAAAAB\n"
                + "cQB+AAR4";
    }

    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new UnifiedSetWithHashingStrategyMultimap<String, String>(HashingStrategies.<String>defaultStrategy());
    }
}
