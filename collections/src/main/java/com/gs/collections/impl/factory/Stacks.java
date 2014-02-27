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

import com.gs.collections.api.factory.stack.ImmutableStackFactory;
import com.gs.collections.api.factory.stack.MutableStackFactory;
import com.gs.collections.impl.stack.immutable.ImmutableStackFactoryImpl;
import com.gs.collections.impl.stack.mutable.MutableStackFactoryImpl;

@SuppressWarnings("ConstantNamingConvention")
public final class Stacks
{
    public static final ImmutableStackFactory immutable = new ImmutableStackFactoryImpl();
    public static final MutableStackFactory mutable = new MutableStackFactoryImpl();

    private Stacks()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }
}
