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

package ponzu.impl.factory;

import ponzu.api.factory.map.FixedSizeMapFactory;
import ponzu.api.factory.map.ImmutableMapFactory;
import ponzu.api.factory.map.MutableMapFactory;
import ponzu.impl.map.fixed.FixedSizeMapFactoryImpl;
import ponzu.impl.map.immutable.ImmutableMapFactoryImpl;
import ponzu.impl.map.mutable.MutableMapFactoryImpl;

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
