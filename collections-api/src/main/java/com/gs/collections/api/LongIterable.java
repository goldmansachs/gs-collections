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

import com.gs.collections.api.block.function.primitive.LongToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.LongPredicate;
import com.gs.collections.api.block.procedure.primitive.LongProcedure;
import com.gs.collections.api.iterator.LongIterator;

/**
 * LongIterable is an interface which is memory-optimized for long primitives.
 * It is inspired by the interface RichIterable, and contains a subset of the internal iterator methods on RichIterable like collect, sum, etc.
 * The API also includes an external iterator method, which returns an LongIterator. LongIterator helps iterate over the LongIterable without boxing the primitives.
 */
public interface LongIterable
{
    LongIterator longIterator();

    void forEach(LongProcedure procedure);

    int size();

    int count(LongPredicate predicate);

    boolean anySatisfy(LongPredicate predicate);

    boolean allSatisfy(LongPredicate predicate);

    <V> RichIterable<V> collect(LongToObjectFunction<? extends V> function);

    long sum();

    long max();

    long min();

    double average();

    double median();

    long[] toArray();

    long[] toSortedArray();
}
