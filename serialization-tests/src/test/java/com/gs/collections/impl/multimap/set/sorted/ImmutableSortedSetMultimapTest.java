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

package com.gs.collections.impl.multimap.set.sorted;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.impl.multimap.ImmutableMultimapTestCase;

public class ImmutableSortedSetMultimapTest extends ImmutableMultimapTestCase
{
    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new TreeSortedSetMultimap<String, String>();
    }

    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAHdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tdWx0aW1hcC5zZXQuc29ydGVkLkltbXV0\n"
                + "YWJsZVNvcnRlZFNldE11bHRpbWFwSW1wbCRJbW11dGFibGVTb3J0ZWRTZXRNdWx0aW1hcFNlcmlh\n"
                + "bGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHIARGNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLm11bHRp\n"
                + "bWFwLkltbXV0YWJsZU11bHRpbWFwU2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHB3BAAA\n"
                + "AAJ0AAFBdwQAAAACcQB+AAN0AAFCcQB+AAR3BAAAAAFxAH4AA3g=";
    }
}
