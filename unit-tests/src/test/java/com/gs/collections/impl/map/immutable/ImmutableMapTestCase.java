/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.map.immutable;

import java.util.HashMap;
import java.util.Map;

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ImmutableMap}.
 */
public abstract class ImmutableMapTestCase extends ImmutableMapIterableTestCase
{
    @Override
    protected abstract ImmutableMap<Integer, String> classUnderTest();

    @Test
    public void castToMap()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        Map<Integer, String> map = immutable.castToMap();
        Assert.assertSame(immutable, map);
        Assert.assertEquals(immutable, new HashMap<>(map));
    }

    @Test
    public void toMap()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        MutableMap<Integer, String> map = immutable.toMap();
        Assert.assertNotSame(immutable, map);
        Assert.assertEquals(immutable, map);
    }

    @Test
    public void entrySet()
    {
        ImmutableMap<Integer, String> immutable = this.classUnderTest();
        Map<Integer, String> map = new HashMap<>(immutable.castToMap());
        Assert.assertEquals(immutable.size(), immutable.castToMap().entrySet().size());
        Assert.assertEquals(map.entrySet(), immutable.castToMap().entrySet());
    }
}
