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

import com.gs.collections.api.block.function.primitive.DoubleToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.DoublePredicate;
import com.gs.collections.api.block.procedure.primitive.DoubleProcedure;
import com.gs.collections.api.iterator.DoubleIterator;

/**
 * DoubleIterable is an interface which is memory-optimized for double primitives.
 * It is inspired by the interface RichIterable, and contains a subset of the internal iterator methods on RichIterable like collect, sum, etc.
 * The API also includes an external iterator method, which returns an DoubleIterator. DoubleIterator helps iterate over the DoubleIterable without boxing the primitives.
 */
public interface DoubleIterable
{
    DoubleIterator doubleIterator();

    void forEach(DoubleProcedure procedure);

    int size();

    int count(DoublePredicate predicate);

    boolean anySatisfy(DoublePredicate predicate);

    boolean allSatisfy(DoublePredicate predicate);

    <V> RichIterable<V> collect(DoubleToObjectFunction<? extends V> function);

    double sum();

    double max();

    double min();

    double average();

    double median();

    double[] toArray();

    double[] toSortedArray();
}
