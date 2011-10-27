/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;

public final class Functions0
{
    private static final NewFastListFunction<?> NEW_FAST_LIST_FUNCTION = new NewFastListFunction();
    private static final NewUnifiedSetFunction<?> NEW_UNIFIED_SET_FUNCTION = new NewUnifiedSetFunction();
    private static final NewHashBagFunction<?> NEW_HASH_BAG_FUNCTION = new NewHashBagFunction();
    private static final NewUnifiedMapFunction<?, ?> NEW_UNIFIED_MAP_FUNCTION = new NewUnifiedMapFunction();

    private Functions0()
    {
        // Utility Class
    }

    public static <T> Function0<MutableList<T>> newFastList()
    {
        return (Function0<MutableList<T>>) NEW_FAST_LIST_FUNCTION;
    }

    public static <T> Function0<MutableSet<T>> newUnifiedSet()
    {
        return (Function0<MutableSet<T>>) NEW_UNIFIED_SET_FUNCTION;
    }

    public static <T> Function0<MutableBag<T>> newHashBag()
    {
        return (Function0<MutableBag<T>>) NEW_HASH_BAG_FUNCTION;
    }

    public static <K, V> Function0<MutableMap<K, V>> newUnifiedMap()
    {
        return (Function0<MutableMap<K, V>>) NEW_UNIFIED_MAP_FUNCTION;
    }

    private static final class NewFastListFunction<T> implements Function0<MutableList<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableList<T> value()
        {
            return Lists.mutable.of();
        }
    }

    private static final class NewUnifiedMapFunction<K, V> implements Function0<MutableMap<K, V>>
    {
        private static final long serialVersionUID = 1L;

        public MutableMap<K, V> value()
        {
            return Maps.mutable.of();
        }
    }

    private static final class NewUnifiedSetFunction<T> implements Function0<MutableSet<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableSet<T> value()
        {
            return Sets.mutable.of();
        }
    }

    private static final class NewHashBagFunction<T> implements Function0<MutableBag<T>>
    {
        private static final long serialVersionUID = 1L;

        public MutableBag<T> value()
        {
            return Bags.mutable.of();
        }
    }
}
