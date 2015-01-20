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

package com.gs.collections.test.bag;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.UnsortedBag;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.test.UnorderedIterableTestCase;
import org.junit.Test;

import static com.gs.collections.test.IterableTestCase.assertEquals;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public interface UnsortedBagTestCase extends UnorderedIterableTestCase, BagTestCase, TransformsToBagTrait
{
    @Override
    <T> UnsortedBag<T> newWith(T... elements);

    @Override
    default <T> UnsortedBag<T> getExpectedFiltered(T... elements)
    {
        return Bags.immutable.with(elements);
    }

    @Override
    default <T> MutableBag<T> newMutableForFilter(T... elements)
    {
        return Bags.mutable.with(elements);
    }

    @Test
    default void UnsortedBag_forEachWith()
    {
        UnsortedBag<Integer> bag = this.newWith(3, 3, 3, 2, 2, 1);
        MutableBag<Integer> result = Bags.mutable.with();
        Object sentinel = new Object();
        bag.forEachWith((argument1, argument2) -> {
            result.add(argument1);
            assertSame(sentinel, argument2);
        }, sentinel);
        assertEquals(Bags.immutable.with(3, 3, 3, 2, 2, 1), result);
    }

    @Override
    @Test
    default void RichIterable_makeString_appendString()
    {
        RichIterable<Integer> iterable = this.newWith(2, 2, 1);
        assertThat(iterable.makeString(), isOneOf("2, 2, 1", "1, 2, 2"));
        assertThat(iterable.makeString("/"), isOneOf("2/2/1", "1/2/2"));
        assertThat(iterable.makeString("[", "/", "]"), isOneOf("[2/2/1]", "[1/2/2]"));

        StringBuilder builder1 = new StringBuilder();
        iterable.appendString(builder1);
        assertThat(builder1.toString(), isOneOf("2, 2, 1", "1, 2, 2"));

        StringBuilder builder2 = new StringBuilder();
        iterable.appendString(builder2, "/");
        assertThat(builder2.toString(), isOneOf("2/2/1", "1/2/2"));

        StringBuilder builder3 = new StringBuilder();
        iterable.appendString(builder3, "[", "/", "]");
        assertThat(builder3.toString(), isOneOf("[2/2/1]", "[1/2/2]"));
    }

    @Override
    @Test
    default void RichIterable_toString()
    {
        assertThat(this.newWith(2, 2, 1).toString(), isOneOf("[2, 2, 1]", "[1, 2, 2]"));
    }

    @Override
    @Test
    default void RichIterable_toList()
    {
        assertThat(
                this.newWith(2, 2, 1).toList(),
                isOneOf(
                        Lists.immutable.with(2, 2, 1),
                        Lists.immutable.with(1, 2, 2),
                        Lists.immutable.with(2, 1, 2)));
    }
}
