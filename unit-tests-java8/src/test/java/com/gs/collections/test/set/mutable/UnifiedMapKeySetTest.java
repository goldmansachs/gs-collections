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

package com.gs.collections.test.set.mutable;

import java.util.Random;
import java.util.Set;

import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.test.set.SetTestCase;
import com.gs.junit.runners.Java8Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNull;

// TODO MapIterable.keySet() should return SetIterable, and use SetIterableTestCase here
@RunWith(Java8Runner.class)
public class UnifiedMapKeySetTest implements SetTestCase
{
    private static final long CURRENT_TIME_MILLIS = System.currentTimeMillis();

    @SafeVarargs
    @Override
    public final <T> Set<T> newWith(T... elements)
    {
        Random random = new Random(CURRENT_TIME_MILLIS);

        MutableMap<T, Double> result = new UnifiedMap<>();
        for (T element : elements)
        {
            assertNull(result.put(element, random.nextDouble()));
        }
        return result.keySet();
    }

    @Override
    public boolean allowsDuplicates()
    {
        return false;
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void Collection_add()
    {
        // TODO Move up to a keySet view abstraction
        SetTestCase.super.Collection_add();
    }
}
