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

package com.gs.collections.test.list;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.test.OrderedIterableWithDuplicatesTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;

public interface ListIterableTestCase extends OrderedIterableWithDuplicatesTestCase, TransformsToListTrait
{
    @Override
    <T> ListIterable<T> newWith(T... elements);

    @Override
    default <T> ListIterable<T> getExpectedFiltered(T... elements)
    {
        return Lists.immutable.with(elements);
    }

    @Override
    default <T> MutableList<T> newMutableForFilter(T... elements)
    {
        return Lists.mutable.with(elements);
    }

    @Override
    default <T> ListIterable<T> getExpectedTransformed(T... elements)
    {
        return Lists.immutable.with(elements);
    }

    @Test
    default void ListIterable_forEachWithIndex()
    {
        RichIterable<Integer> integers = this.newWith(1, 2, 3);
        MutableCollection<Pair<Integer, Integer>> result = Lists.mutable.with();
        integers.forEachWithIndex((each, index) -> result.add(Tuples.pair(each, index)));
        assertEquals(
                Lists.immutable.with(Tuples.pair(1, 0), Tuples.pair(2, 1), Tuples.pair(3, 2)),
                result);
    }

    @Test
    default void ListIterable_indexOf()
    {
        ListIterable<Integer> integers = this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        assertEquals(3, integers.indexOf(3));
        assertEquals(-1, integers.indexOf(0));
        assertEquals(-1, integers.indexOf(null));
        ListIterable<Integer> integers2 = this.newWith(1, 2, 2, null, 3, 3, 3, null, 4, 4, 4, 4);
        assertEquals(3, integers2.indexOf(null));
    }

    @Test
    default void ListIterable_lastIndexOf()
    {
        ListIterable<Integer> integers = this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        assertEquals(5, integers.lastIndexOf(3));
        assertEquals(-1, integers.lastIndexOf(0));
        assertEquals(-1, integers.lastIndexOf(null));
        ListIterable<Integer> integers2 = this.newWith(1, 2, 2, null, 3, 3, 3, null, 4, 4, 4, 4);
        assertEquals(7, integers2.lastIndexOf(null));
    }

    @Test
    default void OrderedIterable_forEach_from_to()
    {
        ListIterable<Integer> integers = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);

        MutableList<Integer> result = Lists.mutable.empty();
        integers.forEach(5, 7, result::add);
        assertEquals(Lists.immutable.with(3, 3, 2), result);

        MutableList<Integer> result2 = Lists.mutable.empty();
        integers.forEach(0, 9, result2::add);
        assertEquals(Lists.immutable.with(4, 4, 4, 4, 3, 3, 3, 2, 2, 1), result2);

        ListIterable<Integer> integers2 = this.newWith(4, 4, 4, 4, 3, 3, 3);
        MutableList<Integer> result3 = Lists.mutable.empty();
        integers2.forEach(5, 6, result3::add);
        assertEquals(Lists.immutable.with(3, 3), result3);

        MutableList<Integer> result4 = Lists.mutable.empty();
        integers2.forEach(3, 3, result4::add);
        assertEquals(Lists.immutable.with(4), result4);

        MutableList<Integer> result5 = Lists.mutable.empty();
        integers2.forEach(4, 4, result5::add);
        assertEquals(Lists.immutable.with(3), result5);

        MutableList<Integer> result6 = Lists.mutable.empty();
        integers2.forEach(5, 5, result6::add);
        assertEquals(Lists.immutable.with(3), result6);

        MutableList<Integer> result7 = Lists.mutable.empty();
        integers2.forEach(6, 6, result7::add);
        assertEquals(Lists.immutable.with(3), result7);

        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(-1, 0, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(0, -1, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(0, 10, result::add));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEach(10, 0, result::add));
    }

    @Test
    default void OrderedIterable_forEach_from_to_reverse_order()
    {
        ListIterable<Integer> integers = this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1);
        MutableList<Integer> result = Lists.mutable.empty();
        integers.forEach(7, 5, result::add);
        assertEquals(Lists.immutable.with(2, 3, 3), result);
    }

    @Test
    default void ListIterable_distinct()
    {
        ListIterable<String> letters = this.newWith("A", "a", "b", "c", "B", "D", "e", "e", "E", "D").distinct(HashingStrategies.fromFunction(String::toLowerCase));
        ListIterable<String> expected = FastList.newListWith("A", "b", "c", "D", "e");
        assertEquals(letters, expected);

        ListIterable<String> empty = this.<String>newWith().distinct(HashingStrategies.fromFunction(String::toLowerCase));
        assertEquals(empty, this.newWith());
    }
}

