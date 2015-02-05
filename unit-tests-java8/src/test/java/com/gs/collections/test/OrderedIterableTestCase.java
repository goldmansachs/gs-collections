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

package com.gs.collections.test;

import java.util.Iterator;

import com.gs.collections.api.RichIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.junit.Assert.assertSame;

public interface OrderedIterableTestCase extends RichIterableTestCase
{
    @Test
    default void OrderedIterable_getFirst()
    {
        assertEquals(Integer.valueOf(3), this.newWith(3, 3, 3, 2, 2, 1).getFirst());
    }

    @Test
    default void OrderedIterable_getLast()
    {
        assertEquals(Integer.valueOf(1), this.newWith(3, 3, 3, 2, 2, 1).getLast());
    }

    @Test
    default void OrderedIterable_next()
    {
        Iterator<Integer> iterator = this.newWith(3, 2, 1).iterator();
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
    }

    @Test
    default void OrderedIterable_min()
    {
        Holder<Integer> first = new Holder<>(-1);
        Holder<Integer> second = new Holder<>(-1);
        assertSame(first, this.newWith(new Holder<>(2), first, new Holder<>(0), second).min());
    }

    @Test
    default void OrderedIterable_max()
    {
        Holder<Integer> first = new Holder<>(1);
        Holder<Integer> second = new Holder<>(1);
        assertSame(first, this.newWith(new Holder<>(-2), first, new Holder<>(0), second).max());
    }

    @Test
    default void OrderedIterable_min_comparator()
    {
        Holder<Integer> first = new Holder<>(1);
        Holder<Integer> second = new Holder<>(1);
        assertSame(first, this.newWith(new Holder<>(-2), first, new Holder<>(0), second).min(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void OrderedIterable_max_comparator()
    {
        Holder<Integer> first = new Holder<>(-1);
        Holder<Integer> second = new Holder<>(-1);
        assertSame(first, this.newWith(new Holder<>(2), first, new Holder<>(0), second).max(Comparators.reverseNaturalOrder()));
    }

    @Test
    default void OrderedIterable_zipWithIndex()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        Assert.assertEquals(
                Lists.immutable.with(
                        Tuples.pair(4, 0),
                        Tuples.pair(4, 1),
                        Tuples.pair(4, 2),
                        Tuples.pair(4, 3),
                        Tuples.pair(3, 4),
                        Tuples.pair(3, 5),
                        Tuples.pair(3, 6),
                        Tuples.pair(2, 7),
                        Tuples.pair(2, 8),
                        Tuples.pair(1, 9)),
                iterable.zipWithIndex().toList());
    }

    @Test
    default void OrderedIterable_zipWithIndex_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        Assert.assertEquals(
                Lists.immutable.with(
                        Tuples.pair(4, 0),
                        Tuples.pair(4, 1),
                        Tuples.pair(4, 2),
                        Tuples.pair(4, 3),
                        Tuples.pair(3, 4),
                        Tuples.pair(3, 5),
                        Tuples.pair(3, 6),
                        Tuples.pair(2, 7),
                        Tuples.pair(2, 8),
                        Tuples.pair(1, 9)),
                iterable.zipWithIndex(Lists.mutable.empty()));
    }
}
