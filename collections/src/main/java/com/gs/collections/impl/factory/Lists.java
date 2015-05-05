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

import com.gs.collections.api.factory.list.FixedSizeListFactory;
import com.gs.collections.api.factory.list.ImmutableListFactory;
import com.gs.collections.api.factory.list.MutableListFactory;
import com.gs.collections.impl.list.fixed.FixedSizeListFactoryImpl;
import com.gs.collections.impl.list.immutable.ImmutableListFactoryImpl;
import com.gs.collections.impl.list.mutable.MutableListFactoryImpl;

/**
 * This class should be used to create instances of MutableList, ImmutableList and FixedSizeList
 * <p>
 * Mutable Examples:
 *
 * <pre>
 * MutableList&lt;String&gt; emptyList = Lists.mutable.empty();
 * MutableList&lt;String&gt; listWith = Lists.mutable.with("a", "b", "c");
 * MutableList&lt;String&gt; listOf = Lists.mutable.of("a", "b", "c");
 * </pre>
 *
 * Immutable Examples:
 *
 * <pre>
 * ImmutableList&lt;String&gt; emptyList = Lists.immutable.empty();
 * ImmutableList&lt;String&gt; listWith = Lists.immutable.with("a", "b", "c");
 * ImmutableList&lt;String&gt; listOf = Lists.immutable.of("a", "b", "c");
 * </pre>
 *
 * FixedSize Examples:
 *
 * <pre>
 * FixedSizeList&lt;String&gt; emptyList = Lists.fixedSize.empty();
 * FixedSizeList&lt;String&gt; listWith = Lists.fixedSize.with("a", "b", "c");
 * FixedSizeList&lt;String&gt; listOf = Lists.fixedSize.of("a", "b", "c");
 * </pre>
 */
@SuppressWarnings("ConstantNamingConvention")
public final class Lists
{
    public static final ImmutableListFactory immutable = new ImmutableListFactoryImpl();
    public static final MutableListFactory mutable = new MutableListFactoryImpl();
    public static final FixedSizeListFactory fixedSize = new FixedSizeListFactoryImpl();

    private Lists()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
