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

package com.gs.collections.impl.list.fixed;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;

public class SingletonListSubListTest extends UnmodifiableMemoryEfficientListTestCase<String>
{
    @Override
    protected MutableList<String> getCollection()
    {
        return Lists.fixedSize.of("1").subList(0, 1);
    }
}
