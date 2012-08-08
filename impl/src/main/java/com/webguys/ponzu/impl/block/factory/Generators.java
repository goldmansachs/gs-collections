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

package com.webguys.ponzu.impl.block.factory;

import com.webguys.ponzu.api.bag.MutableBag;
import com.webguys.ponzu.api.block.function.Generator;
import com.webguys.ponzu.api.list.MutableList;
import com.webguys.ponzu.api.map.MutableMap;
import com.webguys.ponzu.api.set.MutableSet;
import com.webguys.ponzu.impl.factory.Bags;
import com.webguys.ponzu.impl.factory.Lists;
import com.webguys.ponzu.impl.factory.Maps;
import com.webguys.ponzu.impl.factory.Sets;

public final class Generators
{
    private static final NewFastListFunction<?> NEW_FAST_LIST_FUNCTION = new NewFastListFunction();
    private static final NewUnifiedSetFunction<?> NEW_UNIFIED_SET_FUNCTION = new NewUnifiedSetFunction();
    private static final NewHashBagFunction<?> NEW_HASH_BAG_FUNCTION = new NewHashBagFunction();
    private static final NewUnifiedMapFunction<?, ?> NEW_UNIFIED_MAP_FUNCTION = new NewUnifiedMapFunction();

    private Generators()
    {
        // Utility Class
    }

    public static <T> Generator<MutableList<T>> newFastList()
    {
        return (Generator<MutableList<T>>) NEW_FAST_LIST_FUNCTION;
    }

    public static <T> Generator<MutableSet<T>> newUnifiedSet()
    {
        return (Generator<MutableSet<T>>) NEW_UNIFIED_SET_FUNCTION;
    }

    public static <T> Generator<MutableBag<T>> newHashBag()
    {
        return (Generator<MutableBag<T>>) NEW_HASH_BAG_FUNCTION;
    }

    public static <K, V> Generator<MutableMap<K, V>> newUnifiedMap()
    {
        return (Generator<MutableMap<K, V>>) NEW_UNIFIED_MAP_FUNCTION;
    }

    private static final class NewFastListFunction<T> implements Generator<MutableList<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableList<T> value()
        {
            return Lists.mutable.of();
        }
    }

    private static final class NewUnifiedMapFunction<K, V> implements Generator<MutableMap<K, V>>
    {
        private static final long serialVersionUID = 1L;

        public MutableMap<K, V> value()
        {
            return Maps.mutable.of();
        }
    }

    private static final class NewUnifiedSetFunction<T> implements Generator<MutableSet<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableSet<T> value()
        {
            return Sets.mutable.of();
        }
    }

    private static final class NewHashBagFunction<T> implements Generator<MutableBag<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableBag<T> value()
        {
            return Bags.mutable.of();
        }
    }
}
