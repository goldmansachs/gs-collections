/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.multimap.set;

import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.multimap.set.ImmutableSetMultimap;
import com.gs.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class ImmutableSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected ImmutableSetMultimap<String, String> classUnderTest()
    {
        return UnifiedSetMultimap.<String, String>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return UnifiedSet.newSet();
    }

    @Override
    public void allowDuplicates()
    {
        // Sets do not allow duplicates
    }
}
