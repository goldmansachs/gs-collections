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

package com.gs.collections.impl.utility;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.lazy.ChunkIterable;
import com.gs.collections.impl.lazy.CollectIterable;
import com.gs.collections.impl.lazy.CompositeIterable;
import com.gs.collections.impl.lazy.DistinctIterable;
import com.gs.collections.impl.lazy.DropIterable;
import com.gs.collections.impl.lazy.FlatCollectIterable;
import com.gs.collections.impl.lazy.LazyIterableAdapter;
import com.gs.collections.impl.lazy.RejectIterable;
import com.gs.collections.impl.lazy.SelectInstancesOfIterable;
import com.gs.collections.impl.lazy.SelectIterable;
import com.gs.collections.impl.lazy.TakeIterable;
import com.gs.collections.impl.lazy.TapIterable;
import com.gs.collections.impl.lazy.ZipIterable;
import com.gs.collections.impl.lazy.ZipWithIndexIterable;

/**
 * LazyIterate is a factory class which creates "deferred" iterables around the specified iterables. A "deferred"
 * iterable performs some operation, such as filtering or transforming, when the result iterable is iterated over.  This
 * makes the operation very memory efficient, because you don't have to create intermediate collections during the
 * operation.
 *
 * @since 1.0
 */
public final class LazyIterate
{
    private static final LazyIterable<?> EMPTY_ITERABLE = Lists.immutable.empty().asLazy();

    private LazyIterate()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    /**
     * Creates a deferred rich iterable for the specified iterable
     */
    public static <T> LazyIterable<T> adapt(Iterable<T> iterable)
    {
        return new LazyIterableAdapter<T>(iterable);
    }

    /**
     * Creates a deferred filtering iterable for the specified iterable
     */
    public static <T> LazyIterable<T> select(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return new SelectIterable<T>(iterable, predicate);
    }

    /**
     * Creates a deferred negative filtering iterable for the specified iterable
     */
    public static <T> LazyIterable<T> reject(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return new RejectIterable<T>(iterable, predicate);
    }

    public static <T> LazyIterable<T> selectInstancesOf(Iterable<?> iterable, Class<T> clazz)
    {
        return new SelectInstancesOfIterable<T>(iterable, clazz);
    }

    /**
     * Creates a deferred transforming iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> collect(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        return new CollectIterable<T, V>(iterable, function);
    }

    /**
     * Creates a deferred flattening iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> flatCollect(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectIterable<T, V>(iterable, function);
    }

    /**
     * Creates a deferred filtering and transforming iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> collectIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return LazyIterate.select(iterable, predicate).collect(function);
    }

    /**
     * Creates a deferred take iterable for the specified iterable using the specified count as the limit
     */
    public static <T> LazyIterable<T> take(Iterable<T> iterable, int count)
    {
        return new TakeIterable<T>(iterable, count);
    }

    /**
     * Creates a deferred drop iterable for the specified iterable using the specified count as the size to drop
     */
    public static <T> LazyIterable<T> drop(Iterable<T> iterable, int count)
    {
        return new DropIterable<T>(iterable, count);
    }

    /**
     * Creates a deferred distinct iterable for the specified iterable
     *
     * @since 5.0
     */
    public static <T> LazyIterable<T> distinct(Iterable<T> iterable)
    {
        return new DistinctIterable<T>(iterable);
    }

    /**
     * Combines iterables into a deferred composite iterable
     */
    public static <T> LazyIterable<T> concatenate(Iterable<T>... iterables)
    {
        return CompositeIterable.with(iterables);
    }

    public static <T> LazyIterable<T> empty()
    {
        return (LazyIterable<T>) EMPTY_ITERABLE;
    }

    public static <A, B> LazyIterable<Pair<A, B>> zip(Iterable<A> as, Iterable<B> bs)
    {
        return new ZipIterable<A, B>(as, bs);
    }

    public static <T> LazyIterable<Pair<T, Integer>> zipWithIndex(Iterable<T> iterable)
    {
        return new ZipWithIndexIterable<T>(iterable);
    }

    public static <T> LazyIterable<RichIterable<T>> chunk(Iterable<T> iterable, int size)
    {
        return new ChunkIterable<T>(iterable, size);
    }

    /**
     * Creates a deferred tap iterable for the specified iterable.
     *
     * @since 6.0
     */
    public static <T> LazyIterable<T> tap(Iterable<T> iterable, Procedure<? super T> procedure)
    {
        return new TapIterable<T>(iterable, procedure);
    }
}
