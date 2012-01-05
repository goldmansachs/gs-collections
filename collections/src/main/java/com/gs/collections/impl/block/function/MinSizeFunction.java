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

package com.gs.collections.impl.block.function;

import java.util.Collection;
import java.util.Map;

import com.gs.collections.api.block.function.Function2;

/**
 * MinSizeFunction compares the size of strings, collections, or maps to determine the min size.
 */
public final class MinSizeFunction
{
    public static final Function2<Integer, String, Integer> STRING = new MinSizeStringFunction();
    public static final Function2<Integer, Collection<?>, Integer> COLLECTION = new MinSizeCollectionFunction();
    public static final Function2<Integer, Map<?, ?>, Integer> MAP = new MinSizeMapFunction();

    private MinSizeFunction()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    private static class MinSizeStringFunction implements Function2<Integer, String, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMin, String s)
        {
            return Math.min(previousMin, s.length());
        }
    }

    private static class MinSizeCollectionFunction implements Function2<Integer, Collection<?>, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMin, Collection<?> collection)
        {
            return Math.min(previousMin, collection.size());
        }
    }

    private static class MinSizeMapFunction implements Function2<Integer, Map<?, ?>, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMin, Map<?, ?> map)
        {
            return Math.min(previousMin, map.size());
        }
    }
}
