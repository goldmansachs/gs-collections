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

package com.gs.collections.impl.multimap;

import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.multimap.MutableMultimap;
import org.junit.Before;

public abstract class ImmutableMultimapSerializationTestCase extends MultimapSerializationTestCase
{
    private ImmutableMultimap<String, String> undertest;

    @Before
    public void buildUnderTest()
    {
        MutableMultimap<String, String> map = this.createEmpty();
        map.put("A", "A");
        map.put("A", "B");
        map.put("A", "B");
        map.put("B", "A");
        this.undertest = map.toImmutable();
    }

    @Override
    protected Multimap<String, String> getMultimapUnderTest()
    {
        return this.undertest;
    }
}
