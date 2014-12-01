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

package com.gs.collections.impl.block.function;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import org.junit.Assert;
import org.junit.Test;

public class IfFunctionTest
{
    @Test
    public void iterate()
    {
        UnifiedMap<Integer, Integer> map = UnifiedMap.newMap(5);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);

        IfFunction<Integer, Integer> function = new IfFunction<>(
                IntegerPredicates.isEven(),
                (Integer ignored) -> 1,
                (Integer ignored) -> 0);
        MutableList<Integer> result = map.valuesView().collect(function).toList();

        Assert.assertEquals(FastList.newListWith(0, 1, 0, 1, 0), result);
    }

    @Test
    public void testIf()
    {
        IfFunction<Integer, Boolean> function = new IfFunction<>(
                Predicates.greaterThan(5),
                (Integer ignored) -> true);

        Assert.assertTrue(function.valueOf(10));
    }

    @Test
    public void ifElse()
    {
        IfFunction<Integer, Boolean> function = new IfFunction<>(
                Predicates.greaterThan(5),
                (Integer ignored) -> true,
                (Integer ignored) -> false);

        Assert.assertFalse(function.valueOf(1));
    }
}
