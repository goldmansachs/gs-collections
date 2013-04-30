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
 * MaxSizeFunction compares the size of strings, collections, or maps to determine the max size.
 */
public final class MaxSizeFunction
{
    public static final Function2<Integer, String, Integer> STRING = new MaxSizeStringFunction();
    public static final Function2<Integer, Collection<?>, Integer> COLLECTION = new MaxSizeCollectionFunction();
    public static final Function2<Integer, Map<?, ?>, Integer> MAP = new MaxSizeMapFunction();

    private MaxSizeFunction()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    private static class MaxSizeStringFunction implements Function2<Integer, String, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMax, String s)
        {
            return Math.max(previousMax, s.length());
        }
    }

    private static class MaxSizeCollectionFunction implements Function2<Integer, Collection<?>, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMax, Collection<?> collection)
        {
            return Math.max(previousMax, collection.size());
        }
    }

    private static class MaxSizeMapFunction implements Function2<Integer, Map<?, ?>, Integer>
    {
        private static final long serialVersionUID = 1L;

        public Integer value(Integer previousMax, Map<?, ?> map)
        {
            return Math.max(previousMax, map.size());
        }
    }
}
