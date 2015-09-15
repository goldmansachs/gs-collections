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

package com.gs.collections.test.set.sorted;

import java.util.NoSuchElementException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.ordered.SortedIterable;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.test.SortedIterableTestCase;
import com.gs.collections.test.domain.A;
import com.gs.collections.test.domain.B;
import com.gs.collections.test.domain.C;
import com.gs.collections.test.list.TransformsToListTrait;
import com.gs.collections.test.set.SetIterableTestCase;
import org.junit.Assert;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;

public interface SortedSetIterableTestCase extends SetIterableTestCase, SortedIterableTestCase, TransformsToListTrait
{
    @Override
    <T> SortedSetIterable<T> newWith(T... elements);

    @Override
    default <T> SortedSetIterable<T> getExpectedFiltered(T... elements)
    {
        return SortedSets.immutable.with(Comparators.reverseNaturalOrder(), elements);
    }

    @Override
    default <T> MutableSortedSet<T> newMutableForFilter(T... elements)
    {
        return SortedSets.mutable.with(Comparators.reverseNaturalOrder(), elements);
    }

    @Test
    default void SortedSetIterable_union()
    {
        SortedSetIterable<Integer> union = this.newWith(1, 2, 3).union(this.newWith(3, 4, 5));
        assertEquals(SortedSets.immutable.with(Comparators.reverseNaturalOrder(), 5, 4, 3, 2, 1), union);
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    default void RichIterable_getFirst_empty_null()
    {
        this.newWith().getFirst();
    }

    @Override
    @Test(expected = NoSuchElementException.class)
    default void RichIterable_getLast_empty_null()
    {
        this.newWith().getLast();
    }

    @Override
    @Test
    default void RichIterable_selectInstancesOf()
    {
        // Must test with two classes that are mutually Comparable

        SortedSetIterable<A> numbers = this.<A>newWith(new C(4.0), new B(3), new C(2.0), new B(1));
        assertEquals(this.<B>getExpectedFiltered(new B(3), new B(1)), numbers.selectInstancesOf(B.class));
        assertEquals(this.getExpectedFiltered(new C(4.0), new B(3), new C(2.0), new B(1)), numbers.selectInstancesOf(A.class));
    }

    @Override
    default void OrderedIterable_getFirst()
    {
        assertEquals(Integer.valueOf(3), this.newWith(3, 2, 1).getFirst());
    }

    @Override
    default void OrderedIterable_getLast()
    {
        assertEquals(Integer.valueOf(1), this.newWith(3, 2, 1).getLast());
    }

    @Override
    default void RichIterable_getFirst()
    {
        assertEquals(Integer.valueOf(3), this.newWith(3, 2, 1).getFirst());
    }

    @Override
    default void RichIterable_getLast()
    {
        assertEquals(Integer.valueOf(1), this.newWith(3, 2, 1).getLast());
    }

    @Override
    default void OrderedIterable_min()
    {
        // Cannot contain duplicates
    }

    @Override
    default void OrderedIterable_max()
    {
        // Cannot contain duplicates
    }

    @Override
    default void OrderedIterable_min_comparator()
    {
        // Cannot contain duplicates
    }

    @Override
    default void OrderedIterable_max_comparator()
    {
        // Cannot contain duplicates
    }

    @Test
    default void OrderedIterable_zipWithIndex()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        Assert.assertEquals(
                Lists.immutable.with(
                        Tuples.pair(4, 0),
                        Tuples.pair(3, 1),
                        Tuples.pair(2, 2),
                        Tuples.pair(1, 3)),
                iterable.zipWithIndex().toList());
    }

    @Test
    default void OrderedIterable_zipWithIndex_target()
    {
        RichIterable<Integer> iterable = this.newWith(4, 3, 2, 1);
        Assert.assertEquals(
                Lists.immutable.with(
                        Tuples.pair(4, 0),
                        Tuples.pair(3, 1),
                        Tuples.pair(2, 2),
                        Tuples.pair(1, 3)),
                iterable.zipWithIndex(Lists.mutable.empty()));
    }

    @Test
    default void OrderedIterable_forEach_from_to()
    {
        SortedIterable<Integer> integers = this.newWith(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);

        MutableList<Integer> result = Lists.mutable.empty();
        integers.forEach(5, 7, result::add);
        assertEquals(Lists.immutable.with(4, 3, 2), result);

        MutableList<Integer> result2 = Lists.mutable.empty();
        integers.forEach(5, 5, result2::add);
        assertEquals(Lists.immutable.with(4), result2);

        MutableList<Integer> result3 = Lists.mutable.empty();
        integers.forEach(0, 9, result3::add);
        assertEquals(Lists.immutable.with(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), result3);

        MutableList<Integer> result4 = Lists.mutable.empty();
        integers.forEach(0, 0, result4::add);
        assertEquals(Lists.immutable.with(9), result4);

        MutableList<Integer> result5 = Lists.mutable.empty();
        integers.forEach(9, 9, result5::add);
        assertEquals(Lists.immutable.with(0), result5);

        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(-1, 0, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(0, -1, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(0, 10, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(10, 0, result::add));
    }

    @Test
    default void OrderedIterable_forEach_from_to_reverse_order()
    {
        SortedIterable<Integer> integers = this.newWith(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        MutableList<Integer> result = Lists.mutable.empty();
        assertThrows(IllegalArgumentException.class, () -> integers.forEach(7, 5, result::add));
    }
}
