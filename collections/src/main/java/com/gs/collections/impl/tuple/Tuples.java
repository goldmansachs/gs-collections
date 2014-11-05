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

package com.gs.collections.impl.tuple;

import java.util.Map;

import com.gs.collections.api.tuple.Pair;
import com.gs.collections.api.tuple.Twin;

/**
 * A Pair is a container that holds two related objects.  It is the equivalent of an Association in Smalltalk, or an
 * implementation of Map.Entry in the JDK.  A Twin is a Pair with the same types.  This class is a factory class
 * for Pairs and Twins.
 */
public final class Tuples
{
    private Tuples()
    {
    }

    public static <K, V> Pair<K, V> pairFrom(Map.Entry<K, V> entry)
    {
        return Tuples.pair(entry.getKey(), entry.getValue());
    }

    public static <T1, T2> Pair<T1, T2> pair(T1 one, T2 two)
    {
        return new PairImpl<T1, T2>(one, two);
    }

    public static <T> Twin<T> twin(T one, T two)
    {
        return new TwinImpl<T>(one, two);
    }
}
