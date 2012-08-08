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

package com.webguys.ponzu.impl.factory;

import com.webguys.ponzu.api.factory.bag.ImmutableBagFactory;
import com.webguys.ponzu.api.factory.bag.MutableBagFactory;
import com.webguys.ponzu.impl.bag.immutable.ImmutableBagFactoryImpl;
import com.webguys.ponzu.impl.bag.mutable.MutableBagFactoryImpl;

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
