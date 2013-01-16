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

package com.gs.collections.impl.set.mutable.primitive;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.set.mutable.UnifiedSet;

public class BooleanHashSet implements MutableBooleanSet, Externalizable
{
    private static final long serialVersionUID = 1L;

    /* state = 0 ==> []
       state = 1 ==> [F]
       state = 2 ==> [T]
       state = 3 ==> [T, F]
     */
    private int state;

    private static class EmptyBooleanIterator implements BooleanIterator
    {
        public boolean next()
        {
            throw new NoSuchElementException();
        }

        public boolean hasNext()
        {
            return false;
        }
    }

    private static class FalseBooleanIterator implements BooleanIterator
    {
        private int currentIndex;

        public boolean next()
        {
            if (this.currentIndex == 0)
            {
                this.currentIndex++;
                return false;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext()
        {
            return this.currentIndex == 0;
        }
    }

    private static class TrueBooleanIterator implements BooleanIterator
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

    private static class TrueFalseBooleanIterator implements BooleanIterator
    {
        private int currentIndex;

        public boolean next()
        {
            switch (this.currentIndex)
            {
                case 0:
                    this.currentIndex++;
                    return true;
                case 1:
                    this.currentIndex++;
                    return false;
                default:
                    throw new NoSuchElementException();
            }
        }

        public boolean hasNext()
        {
            return this.currentIndex < 2;
        }
    }

    public BooleanHashSet()
    {
    }

    public BooleanHashSet(BooleanHashSet set)
    {
        this.state = set.state;
    }

    public static BooleanHashSet newSetWith(boolean... source)
    {
        BooleanHashSet hashSet = new BooleanHashSet();
        hashSet.addAll(source);
        return hashSet;
    }

    public static BooleanHashSet newSet(BooleanIterable source)
    {
        if (source instanceof BooleanHashSet)
        {
            return new BooleanHashSet((BooleanHashSet) source);
        }

        return BooleanHashSet.newSetWith(source.toArray());
    }

    public boolean add(boolean element)
    {
        if (this.contains(element))
        {
            return false;
        }
        this.state |= element ? 2 : 1;
        return true;
    }

    public boolean addAll(boolean... source)
    {
        int initialState = this.state;
        for (boolean item : source)
        {
            if (this.state == 3)
            {
                return this.state != initialState;
            }
            this.add(item);
        }
        return this.state != initialState;
    }

    public boolean remove(boolean value)
    {
        if (!this.contains(value))
        {
            return false;
        }
        int initialState = this.state;
        this.state &= value ? ~2 : ~1;
        return initialState != this.state;
    }

    public boolean removeAll(BooleanIterable source)
    {
        if (this.isEmpty() || source.isEmpty())
        {
            return false;
        }
        boolean modified = false;
        BooleanIterator iterator = source.booleanIterator();
        while (iterator.hasNext())
        {
            if (this.state == 0)
            {
                return modified;
            }
            boolean item = iterator.next();
            if (this.remove(item))
            {
                modified = true;
            }
        }
        return modified;
    }

    public boolean removeAll(boolean... source)
    {
        if (this.isEmpty() || source.length == 0)
        {
            return false;
        }
        boolean modified = false;
        for (boolean item : source)
        {
            if (this.state == 0)
            {
                return modified;
            }
            if (this.remove(item))
            {
                modified = true;
            }
        }
        return modified;
    }

    public void clear()
    {
        this.state = 0;
    }

    public BooleanIterator booleanIterator()
    {
        switch (this.state)
        {
            case 0:
                return new EmptyBooleanIterator();
            case 1:
                return new FalseBooleanIterator();
            case 2:
                return new TrueBooleanIterator();
            case 3:
                return new TrueFalseBooleanIterator();
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public void forEach(BooleanProcedure procedure)
    {
        switch (this.state)
        {
            case 0:
                return;
            case 1:
                procedure.value(false);
                return;
            case 2:
                procedure.value(true);
                return;
            case 3:
                procedure.value(true);
                procedure.value(false);
                return;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public int count(BooleanPredicate predicate)
    {
        switch (this.state)
        {
            case 0:
                return 0;
            case 1:
                return predicate.accept(false) ? 1 : 0;
            case 2:
                return predicate.accept(true) ? 1 : 0;
            case 3:
                int count = 0;
                if (predicate.accept(true))
                {
                    count++;
                }
                if (predicate.accept(false))
                {
                    count++;
                }
                return count;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return this.count(predicate) > 0;
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return this.state != 0 && this.count(predicate) == this.size();
    }

    public BooleanHashSet select(BooleanPredicate predicate)
    {
        BooleanHashSet set = new BooleanHashSet();
        switch (this.state)
        {
            case 0:
                return set;
            case 1:
                if (predicate.accept(false))
                {
                    set.add(false);
                }
                return set;
            case 2:
                if (predicate.accept(true))
                {
                    set.add(true);
                }
                return set;
            case 3:
                if (predicate.accept(true))
                {
                    set.add(true);
                }
                if (predicate.accept(false))
                {
                    set.add(false);
                }
                return set;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public BooleanHashSet reject(BooleanPredicate predicate)
    {
        return this.select(BooleanPredicates.not(predicate));
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        switch (this.state)
        {
            case 0:
                return ifNone;
            case 1:
                return !predicate.accept(false) && ifNone;
            case 2:
                return predicate.accept(true) || ifNone;
            case 3:
                if (predicate.accept(true))
                {
                    return true;
                }
                if (predicate.accept(false))
                {
                    return false;
                }
                return ifNone;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public <V> RichIterable<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        UnifiedSet<V> target = UnifiedSet.newSet(this.size());
        switch (this.state)
        {
            case 0:
                return target;
            case 1:
                return target.with(function.valueOf(false));
            case 2:
                return target.with(function.valueOf(true));
            case 3:
                target.add(function.valueOf(true));
                target.add(function.valueOf(false));
                return target;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public boolean[] toArray()
    {
        switch (this.state)
        {
            case 0:
                return new boolean[0];
            case 1:
                return new boolean[]{false};
            case 2:
                return new boolean[]{true};
            case 3:
                return new boolean[]{true, false};
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public boolean contains(boolean value)
    {
        if (this.state == 3)
        {
            return true;
        }
        if (value)
        {
            return this.state == 2;
        }
        return this.state == 1;
    }

    public boolean containsAll(boolean... source)
    {
        if (this.state == 3)
        {
            return true;
        }
        for (boolean item : source)
        {
            if (!this.contains(item))
            {
                return false;
            }
        }
        return true;
    }

    public BooleanHashSet with(boolean element)
    {
        if (this.state == 3)
        {
            return this;
        }
        this.add(element);
        return this;
    }

    public BooleanHashSet without(boolean element)
    {
        if (this.state == 0)
        {
            return this;
        }
        this.remove(element);
        return this;
    }

    public BooleanHashSet withAll(BooleanIterable elements)
    {
        if (this.state == 3)
        {
            return this;
        }
        this.addAll(elements.toArray());
        return this;
    }

    public BooleanHashSet withoutAll(BooleanIterable elements)
    {
        if (this.state == 0)
        {
            return this;
        }
        this.removeAll(elements);
        return this;
    }

    public int size()
    {
        switch (this.state)
        {
            case 0:
                return 0;
            case 1:
            case 2:
                return 1;
            case 3:
                return 2;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public boolean isEmpty()
    {
        return this.state == 0;
    }

    public boolean notEmpty()
    {
        return this.state != 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof BooleanHashSet))
        {
            return false;
        }

        BooleanHashSet other = (BooleanHashSet) obj;
        return this.state == other.state;
    }

    @Override
    public int hashCode()
    {
        return (int) this.state;
    }

    @Override
    public String toString()
    {
        return this.makeString("[", ", ", "]");
    }

    public String makeString()
    {
        return this.makeString(", ");
    }

    public String makeString(String separator)
    {
        return this.makeString("", separator, "");
    }

    public String makeString(String start, String separator, String end)
    {
        Appendable stringBuilder = new StringBuilder();
        this.appendString(stringBuilder, start, separator, end);
        return stringBuilder.toString();
    }

    public void appendString(Appendable appendable)
    {
        this.appendString(appendable, ", ");
    }

    public void appendString(Appendable appendable, String separator)
    {
        this.appendString(appendable, "", separator, "");
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);
            switch (this.state)
            {
                case 0:
                    break;
                case 1:
                    appendable.append(String.valueOf(false));
                    break;
                case 2:
                    appendable.append(String.valueOf(true));
                    break;
                case 3:
                    appendable.append(String.valueOf(true));
                    appendable.append(separator);
                    appendable.append(String.valueOf(false));
                    break;
                default:
                    throw new AssertionError("Invalid state");
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size());
        switch (this.state)
        {
            case 0:
                return;
            case 1:
                out.writeBoolean(false);
                return;
            case 2:
                out.writeBoolean(true);
                return;
            case 3:
                out.writeBoolean(true);
                out.writeBoolean(false);
                return;
            default:
                throw new AssertionError("Invalid state");
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int size = in.readInt();
        for (int i = 0; i < size; i++)
        {
            this.add(in.readBoolean());
        }
    }
}
