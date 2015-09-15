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

package com.gs.collections.impl.factory;

import com.gs.collections.api.factory.bag.sorted.ImmutableSortedBagFactory;
import com.gs.collections.api.factory.bag.sorted.MutableSortedBagFactory;
import com.gs.collections.impl.bag.sorted.immutable.ImmutableSortedBagFactoryImpl;
import com.gs.collections.impl.bag.sorted.mutable.MutableSortedBagFactoryImpl;

@SuppressWarnings("ConstantNamingConvention")
public final class SortedBags
{
    public static final MutableSortedBagFactory mutable = new MutableSortedBagFactoryImpl();
    public static final ImmutableSortedBagFactory immutable = new ImmutableSortedBagFactoryImpl();

    private SortedBags()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
