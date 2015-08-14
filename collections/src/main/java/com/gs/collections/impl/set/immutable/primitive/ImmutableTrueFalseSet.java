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
final class ImmutableTrueFalseSet implements ImmutableBooleanSet, Serializable
{
    static final ImmutableBooleanSet INSTANCE = new ImmutableTrueFalseSet();

    private ImmutableTrueFalseSet()
    {
        // Singleton
    }

    public ImmutableBooleanSet newWith(boolean element)
    {
        return ImmutableTrueFalseSet.INSTANCE;
    }

    public ImmutableBooleanSet newWithout(boolean element)
    {
        return element ? ImmutableFalseSet.INSTANCE : ImmutableTrueSet.INSTANCE;
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
        ImmutableBooleanSet result = this;
        BooleanIterator booleanIterator = elements.booleanIterator();
        while (booleanIterator.hasNext())
        {
            result = result.newWithout(booleanIterator.next());
        }
        return result;
    }

    public BooleanIterator booleanIterator()
    {
        return new FalseTrueIterator();
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
        procedure.value(false);
        procedure.value(true);
    }

    public <T> T injectInto(T injectedValue, ObjectBooleanToObjectFunction<? super T, ? extends T> function)
    {
        return function.valueOf(function.valueOf(injectedValue, false), true);
    }

    public int count(BooleanPredicate predicate)
    {
        int count = 0;
        if (predicate.accept(false))
        {
            count++;
        }
        if (predicate.accept(true))
        {
            count++;
        }
        return count;
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return predicate.accept(false) || predicate.accept(true);
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return predicate.accept(false) && predicate.accept(true);
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        return !predicate.accept(false) && !predicate.accept(true);
    }

    public ImmutableBooleanSet select(BooleanPredicate predicate)
    {
        if (predicate.accept(true))
        {
            return predicate.accept(false) ? this : ImmutableTrueSet.INSTANCE;
        }
        return predicate.accept(false) ? ImmutableFalseSet.INSTANCE : ImmutableBooleanEmptySet.INSTANCE;
    }

    public ImmutableBooleanSet reject(BooleanPredicate predicate)
    {
        if (predicate.accept(true))
        {
            return predicate.accept(false) ? ImmutableBooleanEmptySet.INSTANCE : ImmutableFalseSet.INSTANCE;
        }
        return predicate.accept(false) ? ImmutableTrueSet.INSTANCE : this;
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        if (predicate.accept(false))
        {
            return false;
        }
        if (predicate.accept(true))
        {
            return true;
        }
        return ifNone;
    }

    public <V> ImmutableSet<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return Sets.immutable.with(function.valueOf(false), function.valueOf(true));
    }

    public boolean[] toArray()
    {
        return new boolean[]{false, true};
    }

    public boolean contains(boolean value)
    {
        return true;
    }

    public boolean containsAll(boolean... source)
    {
        return true;
    }

    public boolean containsAll(BooleanIterable source)
    {
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
        return 2;
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
        return other.contains(false) && other.contains(true);
    }

    @Override
    public int hashCode()
    {
        return 2468;
    }

    @Override
    public String toString()
    {
        return "[false, true]";
    }

    public String makeString()
    {
        return "false, true";
    }

    public String makeString(String separator)
    {
        return "false" + separator + "true";
    }

    public String makeString(String start, String separator, String end)
    {
        return start + "false" + separator + "true" + end;
    }

    public void appendString(Appendable appendable)
    {
        try
        {
            appendable.append("false, true");
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
            appendable.append("false");
            appendable.append(separator);
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
            appendable.append("false");
            appendable.append(separator);
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

    private static final class FalseTrueIterator implements BooleanIterator
    {
        private int currentIndex;

        public boolean next()
        {
            switch (this.currentIndex)
            {
                case 0:
                    this.currentIndex++;
                    return false;
                case 1:
                    this.currentIndex++;
                    return true;
                default:
                    throw new NoSuchElementException();
            }
        }

        public boolean hasNext()
        {
            return this.currentIndex < 2;
        }
    }

    private Object writeReplace()
    {
        return new ImmutableBooleanSetSerializationProxy(this);
    }
}
