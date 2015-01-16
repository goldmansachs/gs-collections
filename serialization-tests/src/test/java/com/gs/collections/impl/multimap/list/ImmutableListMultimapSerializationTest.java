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

package com.gs.collections.impl.multimap.list;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapSerializationTestCase;

public class ImmutableListMultimapSerializationTest
        extends ImmutableMultimapSerializationTestCase
{
    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return FastListMultimap.newMultimap();
    }

    @Override
    public String getSerializedForm()
    {
        return "rO0ABXNyAGdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5saXN0LkltbXV0YWJsZUxp\n"
                + "c3RNdWx0aW1hcEltcGwkSW1tdXRhYmxlTGlzdE11bHRpbWFwU2VyaWFsaXphdGlvblByb3h5AAAA\n"
                + "AAAAAAEMAAB4cHcEAAAAAnQAAUF3BAAAAANxAH4AAnQAAUJxAH4AA3EAfgADdwQAAAABcQB+AAJ4\n";
    }
}
