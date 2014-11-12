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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.factory.bag.MutableBagFactory;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableBagFactoryImpl implements MutableBagFactory
{
    public <T> MutableBag<T> empty()
    {
        return HashBag.newBag();
    }

    public <T> MutableBag<T> of()
    {
        return this.empty();
    }

    public <T> MutableBag<T> with()
    {
        return this.empty();
    }

    public <T> MutableBag<T> of(T... elements)
    {
        return this.with(elements);
    }

    public <T> MutableBag<T> with(T... elements)
    {
        return HashBag.newBagWith(elements);
    }
}
