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

import com.gs.collections.api.factory.bag.ImmutableBagFactory;
import com.gs.collections.api.factory.bag.MutableBagFactory;
import com.gs.collections.impl.bag.immutable.ImmutableBagFactoryImpl;
import com.gs.collections.impl.bag.mutable.MutableBagFactoryImpl;

/**
 * This class should be used to create instances of MutableBag and ImmutableBag
 * <p>
 * Mutable Examples:
 *
 * <pre>
 * MutableBag&lt;String&gt; emptyBag = Bags.mutable.empty();
 * MutableBag&lt;String&gt; bagWith = Bags.mutable.with("a", "b", "c");
 * MutableBag&lt;String&gt; bagOf = Bags.mutable.of("a", "b", "c");
 * </pre>
 *
 * Immutable Examples:
 *
 * <pre>
 * ImmutableBag&lt;String&gt; emptyBag = Bags.immutable.empty();
 * ImmutableBag&lt;String&gt; bagWith = Bags.immutable.with("a", "b", "c");
 * ImmutableBag&lt;String&gt; bagOf = Bags.immutable.of("a", "b", "c");
 * </pre>
 */

@SuppressWarnings("ConstantNamingConvention")
public final class Bags
{
    public static final ImmutableBagFactory immutable = new ImmutableBagFactoryImpl();
    public static final MutableBagFactory mutable = new MutableBagFactoryImpl();

    private Bags()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
