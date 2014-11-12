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

package com.gs.collections.impl.set.immutable.primitive;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.factory.set.primitive.ImmutableBooleanSetFactory;
import com.gs.collections.api.set.primitive.ImmutableBooleanSet;

/**
 * ImmutableBooleanSetFactoryImpl is a factory implementation which creates instances of type {@link ImmutableBooleanSet}.
 *
 * @since 4.0.
 */
public class ImmutableBooleanSetFactoryImpl implements ImmutableBooleanSetFactory
{
    public ImmutableBooleanSet empty()
    {
        return ImmutableBooleanEmptySet.INSTANCE;
    }

    public ImmutableBooleanSet of()
    {
        return this.empty();
    }

    public ImmutableBooleanSet with()
    {
        return this.empty();
    }

    public ImmutableBooleanSet of(boolean one)
    {
        return this.with(one);
    }

    public ImmutableBooleanSet with(boolean one)
    {
        return one ? ImmutableTrueSet.INSTANCE : ImmutableFalseSet.INSTANCE;
    }

    public ImmutableBooleanSet of(boolean... items)
    {
        return this.with(items);
    }

    public ImmutableBooleanSet with(boolean... items)
    {
        if (items == null || items.length == 0)
        {
            return this.with();
        }
        if (items.length == 1)
        {
            return this.with(items[0]);
        }
        ImmutableBooleanSet result = ImmutableBooleanEmptySet.INSTANCE;
        for (boolean item : items)
        {
            result = result.newWith(item);
        }
        return result;
    }

    public ImmutableBooleanSet ofAll(BooleanIterable items)
    {
        return this.withAll(items);
    }

    public ImmutableBooleanSet withAll(BooleanIterable items)
    {
        if (items instanceof ImmutableBooleanSet)
        {
            return (ImmutableBooleanSet) items;
        }
        return this.with(items.toArray());
    }
}
