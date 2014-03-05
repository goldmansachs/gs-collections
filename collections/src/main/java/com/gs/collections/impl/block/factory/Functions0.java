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

package com.gs.collections.impl.block.factory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.factory.Sets;

public final class Functions0
{
    private static final NewFastListFunction<?> NEW_FAST_LIST_FUNCTION = new NewFastListFunction<Object>();
    private static final NewUnifiedSetFunction<?> NEW_UNIFIED_SET_FUNCTION = new NewUnifiedSetFunction<Object>();
    private static final NewHashBagFunction<?> NEW_HASH_BAG_FUNCTION = new NewHashBagFunction<Object>();
    private static final NewUnifiedMapFunction<?, ?> NEW_UNIFIED_MAP_FUNCTION = new NewUnifiedMapFunction<Object, Object>();
    private static final NullFunction<?> NULL_FUNCTION = new NullFunction<Object>();
    private static final AtomicIntegerZeroFunction ATOMIC_INTEGER_ZERO = new AtomicIntegerZeroFunction();
    private static final AtomicLongZeroFunction ATOMIC_LONG_ZERO = new AtomicLongZeroFunction();
    private static final IntegerZeroFunction INTEGER_ZERO = new IntegerZeroFunction();

    private Functions0()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
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

    public static <T> Function0<T> nullValue()
    {
        return (Function0<T>) NULL_FUNCTION;
    }

    public static <T> Function0<T> value(T t)
    {
        return new PassThruFunction0<T>(t);
    }

    public static Function0<AtomicInteger> zeroAtomicInteger()
    {
        return ATOMIC_INTEGER_ZERO;
    }

    public static Function0<AtomicLong> zeroAtomicLong()
    {
        return ATOMIC_LONG_ZERO;
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

    private static class NullFunction<T> implements Function0<T>
    {
        private static final long serialVersionUID = 1L;

        public T value()
        {
            return null;
        }
    }

    private static class IntegerZeroFunction implements Function0<Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value()
        {
            return Integer.valueOf(0);
        }
    }

    private static class AtomicIntegerZeroFunction implements Function0<AtomicInteger>
    {
        private static final long serialVersionUID = 1L;

        public AtomicInteger value()
        {
            return new AtomicInteger(0);
        }
    }

    private static class AtomicLongZeroFunction implements Function0<AtomicLong>
    {
        private static final long serialVersionUID = 1L;

        public AtomicLong value()
        {
            return new AtomicLong(0);
        }
    }
}
