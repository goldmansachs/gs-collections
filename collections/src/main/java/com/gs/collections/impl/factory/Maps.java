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

import com.gs.collections.api.factory.map.FixedSizeMapFactory;
import com.gs.collections.api.factory.map.ImmutableMapFactory;
import com.gs.collections.api.factory.map.MutableMapFactory;
import com.gs.collections.impl.map.fixed.FixedSizeMapFactoryImpl;
import com.gs.collections.impl.map.immutable.ImmutableMapFactoryImpl;
import com.gs.collections.impl.map.mutable.MutableMapFactoryImpl;

/**
 * This class should be used to create instances of MutableMap, ImmutableMap and FixedSizeMap
 * <p>
 * Mutable Examples:
 *
 * <pre>
 * MutableMap&lt;String, String&gt; emptyMap = Maps.mutable.empty();
 * MutableMap&lt;String, String&gt; mapWith = Maps.mutable.with("a", "A", "b", "B", "c", "C");
 * MutableMap&lt;String, String&gt; mapOf = Maps.mutable.of("a", "A", "b", "B", "c", "C");
 * </pre>
 *
 * Immutable Examples:
 *
 * <pre>
 * ImmutableMap&lt;String&gt; mapList = Maps.immutable.empty();
 * ImmutableMap&lt;String&gt; mapWith = Maps.immutable.with("a", "A", "b", "B", "c", "C");
 * ImmutableMap&lt;String&gt; mapOf = Maps.immutable.of("a", "A", "b", "B", "c", "C");
 * </pre>
 *
 * FixedSize Examples:
 *
 * <pre>
 * FixedSizeMap&lt;String&gt; emptyList = Maps.fixedSize.empty();
 * FixedSizeMap&lt;String&gt; listWith = Maps.fixedSize.with("a", "A", "b", "B", "c", "C");
 * FixedSizeMap&lt;String&gt; listOf = Maps.fixedSize.of("a", "A", "b", "B", "c", "C");
 * </pre>
 */
@SuppressWarnings("ConstantNamingConvention")
public final class Maps
{
    public static final ImmutableMapFactory immutable = new ImmutableMapFactoryImpl();
    public static final FixedSizeMapFactory fixedSize = new FixedSizeMapFactoryImpl();
    public static final MutableMapFactory mutable = new MutableMapFactoryImpl();

    private Maps()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
