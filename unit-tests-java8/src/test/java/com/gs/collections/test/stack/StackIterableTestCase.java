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

package com.gs.collections.test.stack;

import java.util.EmptyStackException;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.stack.StackIterable;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.factory.Stacks;
import com.gs.collections.test.OrderedIterableWithDuplicatesTestCase;
import org.junit.Test;

import static com.gs.collections.impl.test.Verify.assertThrows;
import static com.gs.collections.test.IterableTestCase.assertEquals;

public interface StackIterableTestCase extends OrderedIterableWithDuplicatesTestCase, TransformsToStackTrait
{
    @Override
    <T> StackIterable<T> newWith(T... elements);

    @Override
    default void newMutable_sanity()
    {
        // Cannot treat an ArrayStack as a MutableCollection
    }

    @Override
    default <T> StackIterable<T> getExpectedFiltered(T... elements)
    {
        return Stacks.immutable.withReversed(elements);
    }

    @Override
    default <T> MutableList<T> newMutableForFilter(T... elements)
    {
        return Lists.mutable.with(elements);
    }

    @Override
    @Test
    default void InternalIterable_forEach()
    {
        RichIterable<Integer> integers = this.newWith(3, 3, 3, 2, 2, 1);
        MutableStack<Integer> result = Stacks.mutable.with();
        integers.forEach(Procedures.cast(result::push));
        assertEquals(this.newWith(1, 2, 2, 3, 3, 3), result);
    }

    @Override
    default void InternalIterable_forEachWith()
    {
        RichIterable<Integer> iterable = this.newWith(3, 3, 3, 2, 2, 1);
        MutableStack<Integer> result = Stacks.mutable.with();
        iterable.forEachWith((argument1, argument2) -> result.push(argument1 + argument2), 10);
        assertEquals(this.getExpectedFiltered(11, 12, 12, 13, 13, 13), result);
    }

    @Override
    @Test
    default void RichIterable_getFirst_and_getLast()
    {
        assertThrows(UnsupportedOperationException.class, this.newWith()::getLast);
    }

    @Override
    @Test
    default void RichIterable_getFirst_empty_null()
    {
        assertThrows(EmptyStackException.class, this.newWith()::getFirst);
    }

    @Override
    @Test
    default void RichIterable_getLast_empty_null()
    {
        assertThrows(UnsupportedOperationException.class, this.newWith()::getLast);
    }

    @Override
    @Test
    default void RichIterable_getLast()
    {
        assertThrows(UnsupportedOperationException.class, this.newWith(3, 3, 3, 2, 2, 1)::getLast);
    }

    @Override
    @Test
    default void OrderedIterable_getLast()
    {
        assertThrows(UnsupportedOperationException.class, this.newWith(3, 3, 3, 2, 2, 1)::getLast);
    }

    @Test
    default void StackIterable_peek()
    {
        assertEquals(Integer.valueOf(5), this.newWith(5, 1, 4, 2, 3).peek());
    }

    @Test(expected = EmptyStackException.class)
    default void StackIterable_peek_throws()
    {
        this.newWith().peek();
    }

    @Test
    default void StackIterable_peekAt()
    {
        assertEquals(Integer.valueOf(5), this.newWith(5, 1, 4, 2, 3).peekAt(0));
        assertEquals(Integer.valueOf(1), this.newWith(5, 1, 4, 2, 3).peekAt(1));
        assertEquals(Integer.valueOf(4), this.newWith(5, 1, 4, 2, 3).peekAt(2));
        assertEquals(Integer.valueOf(2), this.newWith(5, 1, 4, 2, 3).peekAt(3));
        assertEquals(Integer.valueOf(3), this.newWith(5, 1, 4, 2, 3).peekAt(4));
    }

    @Test
    default void StackIterable_peekAt_throws()
    {
        StackIterable<Integer> stackIterable = this.newWith(5, 1, 4, 2, 3);
        assertThrows(IllegalArgumentException.class, (Runnable) () -> stackIterable.peekAt(-1));
        assertThrows(IllegalArgumentException.class, (Runnable) () -> stackIterable.peekAt(5));
    }
}
