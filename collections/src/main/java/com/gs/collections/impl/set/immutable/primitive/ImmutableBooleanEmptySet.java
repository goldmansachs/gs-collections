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

package com.gs.collections.impl.set.immutable.primitive;

import java.io.IOException;
import java.io.Serializable;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.primitive.BooleanSet;
import com.gs.collections.api.set.primitive.ImmutableBooleanSet;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.factory.Sets;
import com.gs.collections.impl.iterator.ImmutableEmptyBooleanIterator;
import com.gs.collections.impl.lazy.primitive.LazyBooleanIterableAdapter;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableBooleanEmptySet implements ImmutableBooleanSet, Serializable
{
    static final ImmutableBooleanSet INSTANCE = new ImmutableBooleanEmptySet();

    private static final boolean[] TO_ARRAY = new boolean[0];

    private ImmutableBooleanEmptySet()
    {
        // Singleton
    }

    public ImmutableBooleanSet newWith(boolean element)
    {
        return element ? ImmutableTrueSet.INSTANCE : ImmutableFalseSet.INSTANCE;
    }

    public ImmutableBooleanSet newWithout(boolean element)
    {
        return this;
    }

    public ImmutableBooleanSet newWithAll(BooleanIterable elements)
    {
        ImmutableBooleanSet result = this;
        BooleanIterator booleanIterator = elements.booleanIterator();
        while (booleanIterator.hasNext())
        {
            result = result.newWith(booleanIterator.next());
        }
        return result;
    }

    public ImmutableBooleanSet newWithoutAll(BooleanIterable elements)
    {
        return this;
    }

    public BooleanIterator booleanIterator()
    {
        return ImmutableEmptyBooleanIterator.INSTANCE;
    }

    public void forEach(BooleanProcedure procedure)
    {
    }

    /**
     * @since 7.0.
     */
    public void each(BooleanProcedure procedure)
    {
    }

    public <T> T injectInto(T injectedValue, ObjectBooleanToObjectFunction<? super T, ? extends T> function)
    {
        return injectedValue;
    }

    public int count(BooleanPredicate predicate)
    {
        return 0;
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return false;
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return true;
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        return false;
    }

    public ImmutableBooleanSet select(BooleanPredicate predicate)
    {
        return this;
    }

    public ImmutableBooleanSet reject(BooleanPredicate predicate)
    {
        return this;
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        return ifNone;
    }

    public <V> ImmutableSet<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return Sets.immutable.with();
    }

    public boolean[] toArray()
    {
        return TO_ARRAY;
    }

    public boolean contains(boolean value)
    {
        return false;
    }

    public boolean containsAll(boolean... source)
    {
        return source.length == 0;
    }

    public boolean containsAll(BooleanIterable source)
    {
        BooleanIterator iterator = source.booleanIterator();
        return !iterator.hasNext();
    }

    public BooleanSet freeze()
    {
        return this;
    }

    public ImmutableBooleanSet toImmutable()
    {
        return this;
    }

    public int size()
    {
        return 0;
    }

    public boolean isEmpty()
    {
        return true;
    }

    public boolean notEmpty()
    {
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof BooleanSet))
        {
            return false;
        }

        BooleanSet other = (BooleanSet) obj;
        return other.isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        return "[]";
    }

    public String makeString()
    {
        return "";
    }

    public String makeString(String separator)
    {
        return "";
    }

    public String makeString(String start, String separator, String end)
    {
        return start + end;
    }

    public void appendString(Appendable appendable)
    {
    }

    public void appendString(Appendable appendable, String separator)
    {
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
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

    private Object writeReplace()
    {
        return new ImmutableBooleanSetSerializationProxy(this);
    }
}
