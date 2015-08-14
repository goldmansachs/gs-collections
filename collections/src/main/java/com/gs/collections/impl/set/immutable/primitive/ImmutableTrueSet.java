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
import java.util.NoSuchElementException;

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
import com.gs.collections.impl.lazy.primitive.LazyBooleanIterableAdapter;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import net.jcip.annotations.Immutable;

@Immutable
final class ImmutableTrueSet implements ImmutableBooleanSet, Serializable
{
    static final ImmutableBooleanSet INSTANCE = new ImmutableTrueSet();

    private ImmutableTrueSet()
    {
        // Singleton
    }

    public ImmutableBooleanSet newWith(boolean element)
    {
        return element ? this : ImmutableTrueFalseSet.INSTANCE;
    }

    public ImmutableBooleanSet newWithout(boolean element)
    {
        return element ? ImmutableBooleanEmptySet.INSTANCE : this;
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
        return elements.contains(true) ? ImmutableBooleanEmptySet.INSTANCE : this;
    }

    public BooleanIterator booleanIterator()
    {
        return new TrueIterator();
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
        procedure.value(true);
    }

    public <T> T injectInto(T injectedValue, ObjectBooleanToObjectFunction<? super T, ? extends T> function)
    {
        return function.valueOf(injectedValue, true);
    }

    public int count(BooleanPredicate predicate)
    {
        return predicate.accept(true) ? 1 : 0;
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return predicate.accept(true);
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return predicate.accept(true);
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        return !predicate.accept(true);
    }

    public ImmutableBooleanSet select(BooleanPredicate predicate)
    {
        return predicate.accept(true) ? this : ImmutableBooleanEmptySet.INSTANCE;
    }

    public ImmutableBooleanSet reject(BooleanPredicate predicate)
    {
        return predicate.accept(true) ? ImmutableBooleanEmptySet.INSTANCE : this;
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        return predicate.accept(true) || ifNone;
    }

    public <V> ImmutableSet<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return Sets.immutable.with(function.valueOf(true));
    }

    public boolean[] toArray()
    {
        return new boolean[]{true};
    }

    public boolean contains(boolean value)
    {
        return value;
    }

    public boolean containsAll(boolean... source)
    {
        for (boolean item : source)
        {
            if (!item)
            {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(BooleanIterable source)
    {
        for (BooleanIterator iterator = source.booleanIterator(); iterator.hasNext(); )
        {
            if (!iterator.next())
            {
                return false;
            }
        }
        return true;
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
        return 1;
    }

    public boolean isEmpty()
    {
        return false;
    }

    public boolean notEmpty()
    {
        return true;
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
        return !other.contains(false) && other.contains(true);
    }

    @Override
    public int hashCode()
    {
        return 1231;
    }

    @Override
    public String toString()
    {
        return "[true]";
    }

    public String makeString()
    {
        return "true";
    }

    public String makeString(String separator)
    {
        return "true";
    }

    public String makeString(String start, String separator, String end)
    {
        return start + "true" + end;
    }

    public void appendString(Appendable appendable)
    {
        try
        {
            appendable.append("true");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void appendString(Appendable appendable, String separator)
    {
        try
        {
            appendable.append("true");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            appendable.append("true");
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

    private static final class TrueIterator implements BooleanIterator
    {
        private int currentIndex;

        public boolean next()
        {
            if (this.currentIndex == 0)
            {
                this.currentIndex++;
                return true;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext()
        {
            return this.currentIndex == 0;
        }
    }

    private Object writeReplace()
    {
        return new ImmutableBooleanSetSerializationProxy(this);
    }
}
