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

package com.gs.collections.impl.stack.mutable.primitive;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.EmptyStackException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.iterator.MutableBooleanIterator;
import com.gs.collections.api.list.primitive.BooleanList;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.stack.primitive.BooleanStack;
import com.gs.collections.api.stack.primitive.ImmutableBooleanStack;
import com.gs.collections.api.stack.primitive.MutableBooleanStack;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.factory.primitive.BooleanStacks;
import com.gs.collections.impl.iterator.UnmodifiableBooleanIterator;
import com.gs.collections.impl.lazy.primitive.LazyBooleanIterableAdapter;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import net.jcip.annotations.NotThreadSafe;

/**
 * BooleanArrayStack is similar to {@link ArrayStack}, and is memory-optimized for boolean primitives.
 */
@NotThreadSafe
public final class BooleanArrayStack implements MutableBooleanStack, Externalizable
{
    private static final long serialVersionUID = 1L;

    private transient BooleanArrayList delegate;

    public BooleanArrayStack()
    {
        this.delegate = new BooleanArrayList();
    }

    private BooleanArrayStack(int size)
    {
        this.delegate = new BooleanArrayList(size);
    }

    private BooleanArrayStack(boolean... items)
    {
        this.delegate = new BooleanArrayList(items);
    }

    public static BooleanArrayStack newStackFromTopToBottom(boolean... items)
    {
        BooleanArrayStack stack = new BooleanArrayStack(items.length);
        for (int i = items.length - 1; i >= 0; i--)
        {
            stack.push(items[i]);
        }
        return stack;
    }

    public static BooleanArrayStack newStackWith(boolean... items)
    {
        return new BooleanArrayStack(items);
    }

    public static BooleanArrayStack newStack(BooleanIterable items)
    {
        BooleanArrayStack stack = new BooleanArrayStack(items.size());
        stack.delegate = BooleanArrayList.newList(items);
        return stack;
    }

    public static BooleanArrayStack newStackFromTopToBottom(BooleanIterable items)
    {
        BooleanArrayStack stack = new BooleanArrayStack(items.size());
        stack.delegate = BooleanArrayList.newList(items).reverseThis();
        return stack;
    }

    public void push(boolean item)
    {
        this.delegate.add(item);
    }

    public boolean pop()
    {
        this.checkEmptyStack();
        return this.delegate.removeAtIndex(this.delegate.size() - 1);
    }

    private void checkEmptyStack()
    {
        if (this.delegate.isEmpty())
        {
            throw new EmptyStackException();
        }
    }

    public BooleanList pop(int count)
    {
        this.checkPositiveValueForCount(count);
        this.checkSizeLessThanCount(count);
        if (count == 0)
        {
            return new BooleanArrayList(0);
        }
        MutableBooleanList subList = new BooleanArrayList(count);
        while (count > 0)
        {
            subList.add(this.pop());
            count--;
        }
        return subList;
    }

    private void checkPositiveValueForCount(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be positive but was " + count);
        }
    }

    private void checkSizeLessThanCount(int count)
    {
        if (this.delegate.size() < count)
        {
            throw new IllegalArgumentException("Count must be less than size: Count = " + count + " Size = " + this.delegate.size());
        }
    }

    public MutableBooleanStack select(BooleanPredicate predicate)
    {
        return newStackFromTopToBottom(this.delegate.asReversed().select(predicate));
    }

    public MutableBooleanStack reject(BooleanPredicate predicate)
    {
        return newStackFromTopToBottom(this.delegate.asReversed().reject(predicate));
    }

    public MutableBooleanStack asUnmodifiable()
    {
        return new UnmodifiableBooleanStack(this);
    }

    public MutableBooleanStack asSynchronized()
    {
        return new SynchronizedBooleanStack(this);
    }

    public ImmutableBooleanStack toImmutable()
    {
        return BooleanStacks.immutable.withAll(this.delegate);
    }

    public boolean peek()
    {
        this.checkEmptyStack();
        return this.delegate.getLast();
    }

    public BooleanList peek(int count)
    {
        this.checkPositiveValueForCount(count);
        this.checkSizeLessThanCount(count);
        if (count == 0)
        {
            return new BooleanArrayList(0);
        }
        MutableBooleanList subList = new BooleanArrayList(count);
        int index = this.delegate.size() - 1;
        for (int i = 0; i < count; i++)
        {
            subList.add(this.delegate.get(index - i));
        }
        return subList;
    }

    public boolean peekAt(int index)
    {
        this.rangeCheck(index);
        return this.delegate.get(this.delegate.size() - 1 - index);
    }

    private void rangeCheck(int index)
    {
        if (index < 0 || index > this.delegate.size() - 1)
        {
            throw new IllegalArgumentException("Index " + index + " out of range.Should be between 0 and " + (this.delegate.size() - 1));
        }
    }

    public MutableBooleanIterator booleanIterator()
    {
        return new UnmodifiableBooleanIterator(this.delegate.asReversed().booleanIterator());
    }

    public void forEach(BooleanProcedure procedure)
    {
        this.each(procedure);
    }

    /**
     * @since 7.0.
     */
    public void each(BooleanProcedure procedure)
    {
        this.delegate.asReversed().forEach(procedure);
    }

    public int size()
    {
        return this.delegate.size();
    }

    public boolean isEmpty()
    {
        return this.delegate.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.delegate.notEmpty();
    }

    public int count(BooleanPredicate predicate)
    {
        return this.delegate.asReversed().count(predicate);
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return this.delegate.asReversed().anySatisfy(predicate);
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return this.delegate.asReversed().allSatisfy(predicate);
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        return this.delegate.asReversed().noneSatisfy(predicate);
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        return this.delegate.asReversed().detectIfNone(predicate, ifNone);
    }

    public <V> MutableStack<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return ArrayStack.newStackFromTopToBottom(this.delegate.asReversed().collect(function));
    }

    public <V> V injectInto(V injectedValue, ObjectBooleanToObjectFunction<? super V, ? extends V> function)
    {
        return this.delegate.asReversed().injectInto(injectedValue, function);
    }

    public boolean[] toArray()
    {
        return this.delegate.asReversed().toArray();
    }

    public boolean contains(boolean value)
    {
        return this.delegate.asReversed().contains(value);
    }

    public boolean containsAll(boolean... source)
    {
        return this.delegate.asReversed().containsAll(source);
    }

    public boolean containsAll(BooleanIterable source)
    {
        return this.delegate.asReversed().containsAll(source);
    }

    public void clear()
    {
        this.delegate.clear();
    }

    @Override
    public boolean equals(Object otherStack)
    {
        if (otherStack == this)
        {
            return true;
        }
        if (!(otherStack instanceof BooleanStack))
        {
            return false;
        }
        BooleanStack stack = (BooleanStack) otherStack;
        if (this.size() != stack.size())
        {
            return false;
        }
        for (int i = 0; i < this.size(); i++)
        {
            if (this.peekAt(i) != stack.peekAt(i))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 1;
        for (int i = this.size() - 1; i >= 0; i--)
        {
            boolean item = this.delegate.get(i);
            hashCode = 31 * hashCode + (item ? 1231 : 1237);
        }
        return hashCode;
    }

    @Override
    public String toString()
    {
        return this.delegate.asReversed().toString();
    }

    public String makeString()
    {
        return this.delegate.asReversed().makeString();
    }

    public String makeString(String separator)
    {
        return this.delegate.asReversed().makeString(separator);
    }

    public String makeString(String start, String separator, String end)
    {
        return this.delegate.asReversed().makeString(start, separator, end);
    }

    public void appendString(Appendable appendable)
    {
        this.delegate.asReversed().appendString(appendable);
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.delegate.asReversed().appendString(appendable, separator);
    }

    public void appendString(
            Appendable appendable,
            String start,
            String separator,
            String end)
    {
        this.delegate.asReversed().appendString(appendable, start, separator, end);
    }

    public MutableBooleanList toList()
    {
        return BooleanArrayList.newList(this);
    }

    public MutableBooleanSet toSet()
    {
        return BooleanHashSet.newSet(this);
    }

    public MutableBooleanBag toBag()
    {
        return BooleanHashBag.newBag(this);
    }

    public LazyBooleanIterable asLazy()
    {
        return new LazyBooleanIterableAdapter(this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size());
        BooleanIterator iterator = this.delegate.asReversed().booleanIterator();
        while (iterator.hasNext())
        {
            boolean each = iterator.next();
            out.writeBoolean(each);
        }
    }

    public void readExternal(ObjectInput in) throws IOException
    {
        int size = in.readInt();
        boolean[] array = new boolean[size];
        for (int i = size - 1; i >= 0; i--)
        {
            array[i] = in.readBoolean();
        }
        this.delegate = BooleanArrayList.newListWith(array);
    }
}
