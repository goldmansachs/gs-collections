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

package com.gs.collections.impl.list.mutable;

import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.factory.list.MutableListFactory;
import com.gs.collections.api.list.MutableList;
import net.jcip.annotations.Immutable;

@Immutable
public final class MutableListFactoryImpl implements MutableListFactory
{
    public <T> MutableList<T> empty()
    {
        return FastList.newList();
    }

    public <T> MutableList<T> of()
    {
        return this.empty();
    }

    public <T> MutableList<T> with()
    {
        return this.empty();
    }

    public <T> MutableList<T> of(T... items)
    {
        return this.with(items);
    }

    public <T> MutableList<T> with(T... items)
    {
        return FastList.newListWith(items);
    }

    public <T> MutableList<T> ofAll(Iterable<? extends T> iterable)
    {
        return this.withAll(iterable);
    }

    public <T> MutableList<T> withAll(Iterable<? extends T> iterable)
    {
        return FastList.newList(iterable);
    }

    public <T> MutableList<T> withNValues(int size, Function0<T> factory)
    {
        return FastList.newWithNValues(size, factory);
    }
}
