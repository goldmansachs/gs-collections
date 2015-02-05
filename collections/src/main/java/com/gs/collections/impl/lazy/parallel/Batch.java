/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel;

import java.util.Comparator;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.block.procedure.DoubleSumResultHolder;

@Beta
public interface Batch<T>
{
    void forEach(Procedure<? super T> procedure);

    Batch<T> select(Predicate<? super T> predicate);

    <V> Batch<V> collect(Function<? super T, ? extends V> function);

    <V> Batch<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    int count(Predicate<? super T> predicate);

    String makeString(String separator);

    T min(Comparator<? super T> comparator);

    T max(Comparator<? super T> comparator);

    <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function);

    <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function);

    long sumOfInt(IntFunction<? super T> function);

    long sumOfLong(LongFunction<? super T> function);

    DoubleSumResultHolder sumOfFloat(FloatFunction<? super T> function);

    DoubleSumResultHolder sumOfDouble(DoubleFunction<? super T> function);
}

