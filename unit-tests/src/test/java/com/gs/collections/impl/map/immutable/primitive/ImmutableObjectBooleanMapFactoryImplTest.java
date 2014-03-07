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

package com.gs.collections.impl.map.immutable.primitive;

import com.gs.collections.impl.factory.primitive.ObjectBooleanMaps;
import com.gs.collections.impl.map.mutable.primitive.ObjectBooleanHashMap;
import org.junit.Assert;
import org.junit.Test;

public class ImmutableObjectBooleanMapFactoryImplTest
{
    @Test
    public void of()
    {
        Assert.assertEquals(new ObjectBooleanHashMap<String>().toImmutable(), ObjectBooleanMaps.immutable.of());
        Assert.assertEquals(ObjectBooleanHashMap.newWithKeysValues("1", true).toImmutable(), ObjectBooleanMaps.immutable.of("1", true));
    }

    @Test
    public void with()
    {
        Assert.assertEquals(ObjectBooleanHashMap.newWithKeysValues("1", true).toImmutable(), ObjectBooleanMaps.immutable.with("1", true));
    }

    @Test
    public void ofAll()
    {
        Assert.assertEquals(new ObjectBooleanHashMap().toImmutable(), ObjectBooleanMaps.immutable.ofAll(ObjectBooleanMaps.immutable.of()));
    }

    @Test
    public void withAll()
    {
        Assert.assertEquals(new ObjectBooleanHashMap().toImmutable(), ObjectBooleanMaps.immutable.withAll(ObjectBooleanMaps.immutable.of()));
    }
}
