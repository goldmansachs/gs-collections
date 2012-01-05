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

import com.gs.collections.api.factory.list.FixedSizeListFactory;
import com.gs.collections.api.factory.list.ImmutableListFactory;
import com.gs.collections.api.factory.list.MutableListFactory;
import com.gs.collections.impl.list.fixed.FixedSizeListFactoryImpl;
import com.gs.collections.impl.list.immutable.ImmutableListFactoryImpl;
import com.gs.collections.impl.list.mutable.MutableListFactoryImpl;

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
