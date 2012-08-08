/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.api;

import com.gs.collections.api.block.function.primitive.FloatToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.FloatPredicate;
import com.gs.collections.api.block.procedure.primitive.FloatProcedure;
import com.gs.collections.api.iterator.FloatIterator;

/**
 * FloatIterable is an interface which is memory-optimized for float primitives.
 * It is inspired by the interface RichIterable, and contains a subset of the internal iterator methods on RichIterable like collect, sum, etc.
 * The API also includes an external iterator method, which returns an FloatIterator. FloatIterator helps iterate over the FloatIterable without boxing the primitives.
 */
public interface FloatIterable
{
    FloatIterator floatIterator();

    void forEach(FloatProcedure procedure);

    int size();

    int count(FloatPredicate predicate);

    boolean anySatisfy(FloatPredicate predicate);

    boolean allSatisfy(FloatPredicate predicate);

    <V> RichIterable<V> collect(FloatToObjectFunction<? extends V> function);

    double sum();

    float max();

    float min();

    double average();

    double median();

    float[] toArray();

    float[] toSortedArray();
}
