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

package com.gs.collections.impl.bag.immutable;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class ImmutableBagFactoryTest
{
    @Test
    public void immutables()
    {
        Verify.assertIterableSize(0, Bags.immutable.of());
        Verify.assertIterableSize(4, Bags.immutable.of(1, 2, 2, 3));
    }

    @Test
    public void singletonBagCreation()
    {
        Bag<String> singleton = Bags.immutable.of("a");
        Verify.assertInstanceOf(ImmutableSingletonBag.class, singleton);
    }
}
