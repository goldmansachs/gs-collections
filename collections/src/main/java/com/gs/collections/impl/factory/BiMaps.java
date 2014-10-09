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

package com.gs.collections.impl.factory;

import com.gs.collections.api.factory.bimap.ImmutableBiMapFactory;
import com.gs.collections.api.factory.bimap.MutableBiMapFactory;
import com.gs.collections.impl.bimap.immutable.ImmutableBiMapFactoryImpl;
import com.gs.collections.impl.bimap.mutable.MutableBiMapFactoryImpl;

/**
 * @since 6.0
 */
@SuppressWarnings("ConstantNamingConvention")
public final class BiMaps
{
    public static final ImmutableBiMapFactory immutable = new ImmutableBiMapFactoryImpl();
    public static final MutableBiMapFactory mutable = new MutableBiMapFactoryImpl();

    private BiMaps()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
