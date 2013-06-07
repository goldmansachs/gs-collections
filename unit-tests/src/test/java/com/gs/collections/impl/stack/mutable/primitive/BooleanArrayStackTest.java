/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.stack.mutable.primitive;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link BooleanArrayStack}.
 */
public class BooleanArrayStackTest
{
    @Test
    public void testPushPopAndPeek()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.push(true);
        Assert.assertTrue(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true), stack);

        stack.push(false);
        Assert.assertFalse(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, true), stack);

        stack.push(true);
        Assert.assertTrue(stack.peek());
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, false, true), stack);

        Assert.assertFalse(stack.peekAt(1));
        Assert.assertTrue(stack.pop());
        Assert.assertFalse(stack.peek());
        Assert.assertFalse(stack.pop());
        Assert.assertTrue(stack.peek());
        Assert.assertTrue(stack.pop());

        BooleanArrayStack stack2 = BooleanArrayStack.newStackFromTopToBottom(true, false, true, false, true);
        stack2.pop(2);
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, false, true), stack2);
        Assert.assertEquals(BooleanArrayList.newListWith(true, false), stack2.peek(2));

        BooleanArrayStack stack8 = BooleanArrayStack.newStackFromTopToBottom(false, true, false, true);
        Verify.assertEmpty(stack8.pop(0));
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, true, false, true), stack8);
        Assert.assertEquals(new BooleanArrayList(), stack8.peek(0));

        BooleanArrayStack stack9 = BooleanArrayStack.newStackFromTopToBottom();
        Assert.assertEquals(new BooleanArrayList(), stack9.pop(0));
        Assert.assertEquals(new BooleanArrayList(), stack9.peek(0));
    }

    @Test
    public void clear()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        stack.clear();
        Verify.assertEmpty(stack);
        BooleanArrayStack stack1 = BooleanArrayStack.newStackFromTopToBottom();
        Verify.assertEmpty(stack1);
        stack1.clear();
        Verify.assertEmpty(stack1);
    }

    @Test
    public void testNewStackFromTopToBottomOrder()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(false, false, true);
        Assert.assertFalse(stack.pop());
        Assert.assertFalse(stack.pop());
        Assert.assertTrue(stack.pop());
        BooleanArrayStack stack1 = BooleanArrayStack.newStackWith(false, false, true);
        Assert.assertTrue(stack1.pop());
        Assert.assertFalse(stack1.pop());
        Assert.assertFalse(stack1.pop());
    }

    @Test
    public void testNewStackFromTopToBottomIterableOrder()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(BooleanArrayList.newListWith(false, false, true));
        Assert.assertFalse(stack.pop());
        Assert.assertFalse(stack.pop());
        Assert.assertTrue(stack.pop());
        BooleanArrayStack stack1 = BooleanArrayStack.newStack(BooleanArrayList.newListWith(false, false, true));
        Assert.assertTrue(stack1.pop());
        Assert.assertFalse(stack1.pop());
        Assert.assertFalse(stack1.pop());
    }

    @Test
    public void push()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        stack.push(true);
        Verify.assertSize(6, stack);
        stack.pop();
        Verify.assertSize(5, stack);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), stack.peek(2));
    }

    @Test
    public void pop()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        Assert.assertTrue(stack.pop());
        Assert.assertTrue(stack.pop());
        Assert.assertFalse(stack.pop());
    }

    @Test
    public void popWithCount()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), stack.pop(2));
    }

    @Test
    public void select()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, true, true), stack.select(BooleanPredicates.equal(true)));
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, false), stack.select(BooleanPredicates.equal(false)));
    }

    @Test
    public void reject()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(false, false), stack.reject(BooleanPredicates.equal(true)));
        Assert.assertEquals(BooleanArrayStack.newStackFromTopToBottom(true, true, true), stack.reject(BooleanPredicates.equal(false)));

    }

    @Test
    public void peek()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        Assert.assertTrue(stack.peek());
        stack.pop();
        Assert.assertTrue(stack.peek());
        stack.pop();
        Assert.assertFalse(stack.peek());
    }

    @Test
    public void peekWithCount()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        Assert.assertEquals(BooleanArrayList.newListWith(true, true), stack.peek(2));
        stack.pop(2);
        Assert.assertEquals(BooleanArrayList.newListWith(false), stack.peek(1));
    }

    @Test
    public void peekAtIndex()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        Assert.assertTrue(stack.peekAt(0));
        Assert.assertTrue(stack.peekAt(1));
        Assert.assertFalse(stack.peekAt(2));
        Assert.assertTrue(stack.peekAt(3));
        Assert.assertFalse(stack.peekAt(4));
    }

    @Test
    public void testIterator()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        BooleanIterator iterator = stack.booleanIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertFalse(iterator.next());
        Assert.assertFalse(iterator.hasNext());
        Assert.assertTrue(stack.booleanIterator().next());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator_throws()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true);
        BooleanIterator iterator = stack.booleanIterator();
        while (iterator.hasNext())
        {
            iterator.next();
        }
        iterator.next();
    }

    @Test
    public void forEach()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        stack.forEach(new VerificationProcedure(BooleanArrayList.newListWith(true, true, false, true, false)));
    }

    @Test
    public void notEmpty()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.notEmpty());
        stack.pop();
        Assert.assertTrue(stack.notEmpty());
        stack.pop(4);
        Assert.assertFalse(stack.notEmpty());
    }

    @Test
    public void isEmpty()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertFalse(stack.isEmpty());
        stack.pop();
        Assert.assertFalse(stack.isEmpty());
        stack.pop(4);
        Verify.assertEmpty(stack);
        BooleanArrayStack stack2 = new BooleanArrayStack();
        stack2.push(false);
        stack2.push(true);
        stack2.push(false);
        stack2.push(true);
        stack2.push(true);
        Assert.assertFalse(stack2.isEmpty());
        stack2.pop();
        Assert.assertFalse(stack2.isEmpty());
        stack2.pop(4);
        Verify.assertEmpty(stack2);
    }

    @Test
    public void size()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Verify.assertSize(5, stack);
        stack.pop();
        Verify.assertSize(4, stack);
        stack.pop(4);
        Verify.assertEmpty(stack);
    }

    @Test
    public void anySatisfy()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.anySatisfy(BooleanPredicates.equal(true)));
    }

    @Test
    public void allSatisfy()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.allSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return true;
            }
        }));
        Assert.assertFalse(stack.allSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return value;
            }
        }));
    }

    @Test
    public void noneSatisfy()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.noneSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return false;
            }
        }));
        Assert.assertFalse(stack.noneSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return value;
            }
        }));
    }

    @Test
    public void detectIfNone()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true);
        Assert.assertTrue(stack.detectIfNone(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return false;
            }
        }, true));
        Assert.assertTrue(stack.detectIfNone(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return true;
            }
        }, false));
    }

    @Test
    public void count()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals(2, stack.count(BooleanPredicates.equal(false)));
    }

    @Test
    public void collect()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        RichIterable<Boolean> actualValues = stack.collect(new BooleanToObjectFunction<Boolean>()
        {
            public Boolean valueOf(boolean parameter)
            {
                return !parameter;
            }
        });
        Assert.assertEquals("false, false, true, false, true", actualValues.makeString());
    }

    @Test
    public void injectInto()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Integer total = stack.injectInto(Integer.valueOf(0), new ObjectBooleanToObjectFunction<Integer, Integer>()
        {
            public Integer valueOf(Integer result, boolean value)
            {
                if (value)
                {
                    return result += 2;
                }

                return result;
            }
        });
        Assert.assertEquals(Integer.valueOf(6), total);
    }

    @Test
    public void toArray()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        boolean[] expected = {true, true, false, true, false};
        boolean[] actual = stack.toArray();
        this.checkArrayValues("Array value does not match at index ", expected, actual);
    }

    @Test
    public void contains()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.contains(true));
    }

    @Test
    public void containsAll()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertTrue(stack.containsAll(true, false));
    }

    @Test
    public void testToString()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals("[true, true, false, true, false]", stack.toString());
        Assert.assertEquals("[]", BooleanArrayStack.newStackFromTopToBottom().toString());
    }

    @Test
    public void makeString()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals("true, true, false, true, false", stack.makeString());
        Assert.assertEquals("", BooleanArrayStack.newStackFromTopToBottom().makeString());
    }

    @Test
    public void makeStringWithSeparator()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals("true| true| false| true| false", stack.makeString("| "));
        Assert.assertEquals("", BooleanArrayStack.newStackFromTopToBottom().makeString("|"));
    }

    @Test
    public void makeStringWithStartEndAndSeparator()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals("{ true| true| false| true| false }", stack.makeString("{ ", "| ", " }"));
        Assert.assertEquals("{}", BooleanArrayStack.newStackFromTopToBottom().makeString("{", "|", "}"));
    }

    @Test
    public void appendString()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        StringBuilder appendable1 = new StringBuilder();
        stack.appendString(appendable1);
        Assert.assertEquals("true, true, false, true, false", appendable1.toString());
        StringBuilder appendable2 = new StringBuilder();
        BooleanArrayStack.newStackFromTopToBottom().appendString(appendable2);
        Assert.assertEquals("", appendable2.toString());

    }

    @Test
    public void appendStringWithSeparator()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        StringBuilder appendable1 = new StringBuilder();
        stack.appendString(appendable1, "| ");
        Assert.assertEquals("true| true| false| true| false", appendable1.toString());
        StringBuilder appendable2 = new StringBuilder();
        BooleanArrayStack.newStackFromTopToBottom().appendString(appendable2, "|");
        Assert.assertEquals("", appendable2.toString());

    }

    @Test
    public void appendStringWithStartEndAndSeparator()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        StringBuilder appendable1 = new StringBuilder();
        stack.appendString(appendable1, "{ ", "| ", " }");
        Assert.assertEquals("{ true| true| false| true| false }", appendable1.toString());
        StringBuilder appendable2 = new StringBuilder();
        BooleanArrayStack.newStackFromTopToBottom().appendString(appendable2, "{", "|", "}");
        Assert.assertEquals("{}", appendable2.toString());

    }

    @Test
    public void testEqualitySerializationAndHashCode()
    {
        BooleanArrayStack stack1 = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        BooleanArrayStack stack2 = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals(stack1, stack2);
        Assert.assertNotSame(stack1, stack2);
        Verify.assertPostSerializedEqualsAndHashCode(stack1);
        BooleanArrayStack stack3 = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        Assert.assertEquals(stack1, stack3);
        Assert.assertNotSame(stack1, stack3);
        stack2.pop();
        stack3.pop();
        Assert.assertEquals(stack2, stack3);
        Assert.assertNotSame(stack2, stack3);
        Assert.assertNotEquals(stack1, stack3);
        Assert.assertNotSame(stack1, stack3);
        stack1.pop(2);
        stack2.pop();
        stack3.pop();
        Assert.assertEquals(stack2, stack3);
        Assert.assertEquals(stack1, stack3);
        Assert.assertNotSame(stack2, stack3);
        Assert.assertNotSame(stack1, stack3);
        Verify.assertPostSerializedEqualsAndHashCode(stack1);
        Assert.assertEquals(ArrayStack.newStackFromTopToBottom(true, false).hashCode(), BooleanArrayStack.newStackFromTopToBottom(true, false).hashCode());
    }

    private void checkArrayValues(String message, boolean[] expected, boolean[] actual)
    {
        for (int i = 0; i < 5; i++)
        {
            Assert.assertEquals(message + i, expected[i], actual[i]);
        }
    }

    @Test(expected = EmptyStackException.class)
    public void pop_empty_stack_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.pop();
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_with_negative_count_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        stack.pop(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pop_with_count_greater_than_stack_size_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom(true, true, false, true, false);
        stack.pop(6);
    }

    @Test(expected = EmptyStackException.class)
    public void peek_empty_stack_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.peek();
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_at_index_less_than_zero_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.peekAt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void peek_at_index_greater_than_size_throws_exception()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackFromTopToBottom();
        stack.peekAt(1);
    }

    @Test
    public void testEquals()
    {
        BooleanArrayStack stack1 = BooleanArrayStack.newStackWith(true, false, true);
        BooleanArrayStack stack2 = BooleanArrayStack.newStackWith(true, false, true);
        BooleanArrayStack stack3 = BooleanArrayStack.newStackWith(false, true, false);
        BooleanArrayStack stack4 = BooleanArrayStack.newStackWith(true, false, true, false);
        BooleanArrayStack stack5 = BooleanArrayStack.newStackWith(true, false);

        Verify.assertEqualsAndHashCode(stack1, stack2);
        Verify.assertPostSerializedEqualsAndHashCode(stack1);
        Assert.assertNotEquals(stack1, stack3);
        Assert.assertNotEquals(stack1, stack4);
        Assert.assertNotEquals(stack1, stack5);
    }

    @Test
    public void testHashCode()
    {
        Assert.assertEquals(ArrayStack.newStackWith(true, false, true).hashCode(), BooleanArrayStack.newStackWith(true, false, true).hashCode());
        Assert.assertEquals(ArrayStack.newStack().hashCode(), new BooleanArrayStack().hashCode());
    }

    @Test
    public void asLazy()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackWith(true, false, true);
        Assert.assertEquals(stack.toSet(), stack.asLazy().toSet());
        Verify.assertInstanceOf(LazyBooleanIterable.class, stack.asLazy());
    }

    @Test
    public void asSynchronized()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackWith(true, false, true);
        Verify.assertInstanceOf(SynchronizedBooleanStack.class, stack.asSynchronized());
        Assert.assertEquals(new SynchronizedBooleanStack(stack), stack.asSynchronized());
    }

    @Test
    public void asUnmodifiable()
    {
        BooleanArrayStack stack = BooleanArrayStack.newStackWith(true, false, true);
        Verify.assertInstanceOf(UnmodifiableBooleanStack.class, stack.asUnmodifiable());
        Assert.assertEquals(new UnmodifiableBooleanStack(stack), stack.asUnmodifiable());
    }

    private static class VerificationProcedure implements BooleanProcedure
    {
        private final BooleanArrayList checkingList;

        VerificationProcedure(BooleanArrayList listToCheck)
        {
            this.checkingList = listToCheck;
        }

        public void value(boolean each)
        {
            if (!this.checkingList.contains(each))
            {
                Assert.fail("Expected element " + each + " not found");
            }
        }
    }
}
