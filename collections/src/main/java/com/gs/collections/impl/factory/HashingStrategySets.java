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

package com.gs.collections.impl.factory;

import com.gs.collections.api.factory.set.strategy.ImmutableHashingStrategySetFactory;
import com.gs.collections.api.factory.set.strategy.MutableHashingStrategySetFactory;
import com.gs.collections.impl.set.strategy.immutable.ImmutableHashingStrategySetFactoryImpl;
import com.gs.collections.impl.set.strategy.mutable.MutableHashingStrategySetFactoryImpl;

@SuppressWarnings("ConstantNamingConvention")
public final class HashingStrategySets
{
    public static final ImmutableHashingStrategySetFactory immutable = new ImmutableHashingStrategySetFactoryImpl();
    public static final MutableHashingStrategySetFactory mutable = new MutableHashingStrategySetFactoryImpl();

    private HashingStrategySets()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
