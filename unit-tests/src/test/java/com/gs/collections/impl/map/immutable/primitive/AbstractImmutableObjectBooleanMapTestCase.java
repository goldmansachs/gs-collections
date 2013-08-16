/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.map.immutable.primitive;

import com.gs.collections.api.map.primitive.ImmutableObjectBooleanMap;
import com.gs.collections.impl.map.mutable.primitive.AbstractObjectBooleanMapTestCase;
import com.gs.collections.impl.map.mutable.primitive.ObjectBooleanHashMap;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractImmutableObjectBooleanMapTestCase extends AbstractObjectBooleanMapTestCase
{
    @Override
    protected abstract ImmutableObjectBooleanMap<String> classUnderTest();

    @Override
    protected <T> ImmutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1).toImmutable();
    }

    @Override
    protected <T> ImmutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2).toImmutable();
    }

    @Override
    protected <T> ImmutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3).toImmutable();
    }

    @Override
    protected <T> ImmutableObjectBooleanMap<T> newWithKeysValues(T key1, boolean value1, T key2, boolean value2, T key3, boolean value3, T key4, boolean value4)
    {
        return ObjectBooleanHashMap.newWithKeysValues(key1, value1, key2, value2, key3, value3, key4, value4).toImmutable();
    }

    @Override
    protected <T> ImmutableObjectBooleanMap<T> getEmptyMap()
    {
        return new ObjectBooleanHashMap<T>().toImmutable();
    }

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();
        ImmutableObjectBooleanMap<String> map = this.classUnderTest();
        Assert.assertSame(map, map.toImmutable());
    }
}
