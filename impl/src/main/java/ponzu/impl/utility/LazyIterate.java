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

package ponzu.impl.utility;

import ponzu.api.LazyIterable;
import ponzu.api.RichIterable;
import ponzu.api.block.function.Function;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.tuple.Pair;
import ponzu.impl.factory.Lists;
import ponzu.impl.lazy.ChunkIterable;
import ponzu.impl.lazy.CollectIterable;
import ponzu.impl.lazy.CompositeIterable;
import ponzu.impl.lazy.DropIterable;
import ponzu.impl.lazy.FlatCollectIterable;
import ponzu.impl.lazy.LazyIterableAdapter;
import ponzu.impl.lazy.RejectIterable;
import ponzu.impl.lazy.SelectIterable;
import ponzu.impl.lazy.TakeIterable;
import ponzu.impl.lazy.ZipIterable;
import ponzu.impl.lazy.ZipWithIndexIterable;

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
    private static final LazyIterable<?> EMPTY_ITERABLE = Lists.immutable.of().asLazy();

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
    public static <T> LazyIterable<T> filter(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return new SelectIterable<T>(iterable, predicate);
    }

    /**
     * Creates a deferred negative filtering iterable for the specified iterable
     */
    public static <T> LazyIterable<T> filterNot(Iterable<T> iterable, Predicate<? super T> predicate)
    {
        return new RejectIterable<T>(iterable, predicate);
    }

    /**
     * Creates a deferred transforming iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> transform(
            Iterable<T> iterable,
            Function<? super T, ? extends V> function)
    {
        return new CollectIterable<T, V>(iterable, function);
    }

    /**
     * Creates a deferred flattening iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> flatTransform(
            Iterable<T> iterable,
            Function<? super T, ? extends Iterable<V>> function)
    {
        return new FlatCollectIterable<T, V>(iterable, function);
    }

    /**
     * Creates a deferred filtering and transforming iterable for the specified iterable
     */
    public static <T, V> LazyIterable<V> transformIf(
            Iterable<T> iterable,
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return LazyIterate.filter(iterable, predicate).transform(function);
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
}
