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

package com.gs.collections.impl.map.mutable.primitive;

import java.io.IOException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.collection.primitive.ImmutableBooleanCollection;
import com.gs.collections.api.collection.primitive.MutableBooleanCollection;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.map.primitive.MutableBooleanValuesMap;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.collection.mutable.primitive.SynchronizedBooleanCollection;
import com.gs.collections.impl.collection.mutable.primitive.UnmodifiableBooleanCollection;
import com.gs.collections.impl.factory.primitive.BooleanLists;
import com.gs.collections.impl.lazy.primitive.LazyBooleanIterableAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.primitive.AbstractBooleanIterable;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;

public abstract class AbstractMutableBooleanValuesMap extends AbstractBooleanIterable implements MutableBooleanValuesMap
{
    protected abstract int getOccupiedWithData();

    protected abstract SentinelValues getSentinelValues();

    protected abstract void setSentinelValuesNull();

    protected abstract boolean getEmptyValue();

    protected abstract boolean getValueAtIndex(int index);

    protected abstract int getTableSize();

    protected abstract boolean isNonSentinelAtIndex(int index);

    protected void addEmptyKeyValue(boolean value)
    {
        this.getSentinelValues().containsZeroKey = true;
        this.getSentinelValues().zeroValue = value;
    }

    protected void removeEmptyKey()
    {
        if (this.getSentinelValues().containsOneKey)
        {
            this.getSentinelValues().containsZeroKey = false;
            this.getSentinelValues().zeroValue = this.getEmptyValue();
        }
        else
        {
            this.setSentinelValuesNull();
        }
    }

    protected void addRemovedKeyValue(boolean value)
    {
        this.getSentinelValues().containsOneKey = true;
        this.getSentinelValues().oneValue = value;
    }

    protected void removeRemovedKey()
    {
        if (this.getSentinelValues().containsZeroKey)
        {
            this.getSentinelValues().containsOneKey = false;
            this.getSentinelValues().oneValue = this.getEmptyValue();
        }
        else
        {
            this.setSentinelValuesNull();
        }
    }

    public boolean contains(boolean value)
    {
        return this.containsValue(value);
    }

    @Override
    public boolean containsAll(BooleanIterable source)
    {
        return source.allSatisfy(new BooleanPredicate()
        {
            public boolean accept(boolean value)
            {
                return AbstractMutableBooleanValuesMap.this.contains(value);
            }
        });
    }

    public int size()
    {
        return this.getOccupiedWithData() + (this.getSentinelValues() == null ? 0 : this.getSentinelValues().size());
    }

    @Override
    public boolean isEmpty()
    {
        return this.getOccupiedWithData() == 0 && (this.getSentinelValues() == null || this.getSentinelValues().size() == 0);
    }

    @Override
    public boolean notEmpty()
    {
        return this.getOccupiedWithData() != 0 || (this.getSentinelValues() != null && this.getSentinelValues().size() != 0);
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
        this.forEachValue(procedure);
    }

    public void forEachValue(BooleanProcedure procedure)
    {
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey)
            {
                procedure.value(this.getSentinelValues().zeroValue);
            }
            if (this.getSentinelValues().containsOneKey)
            {
                procedure.value(this.getSentinelValues().oneValue);
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i))
            {
                procedure.value(this.getValueAtIndex(i));
            }
        }
    }

    public <V> V injectInto(V injectedValue, ObjectBooleanToObjectFunction<? super V, ? extends V> function)
    {
        V result = injectedValue;

        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey)
            {
                result = function.valueOf(result, this.getSentinelValues().zeroValue);
            }
            if (this.getSentinelValues().containsOneKey)
            {
                result = function.valueOf(result, this.getSentinelValues().oneValue);
            }
        }

        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i))
            {
                result = function.valueOf(result, this.getValueAtIndex(i));
            }
        }

        return result;
    }

    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        try
        {
            appendable.append(start);

            boolean first = true;

            if (this.getSentinelValues() != null)
            {
                if (this.getSentinelValues().containsZeroKey)
                {
                    appendable.append(String.valueOf(this.getSentinelValues().zeroValue));
                    first = false;
                }
                if (this.getSentinelValues().containsOneKey)
                {
                    if (!first)
                    {
                        appendable.append(separator);
                    }
                    appendable.append(String.valueOf(this.getSentinelValues().oneValue));
                    first = false;
                }
            }
            for (int i = 0; i < this.getTableSize(); i++)
            {
                if (this.isNonSentinelAtIndex(i))
                {
                    if (!first)
                    {
                        appendable.append(separator);
                    }
                    appendable.append(String.valueOf(this.getValueAtIndex(i)));
                    first = false;
                }
            }
            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public MutableBooleanCollection select(BooleanPredicate predicate)
    {
        MutableBooleanList result = BooleanLists.mutable.empty();

        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && predicate.accept(this.getSentinelValues().zeroValue))
            {
                result.add(this.getSentinelValues().zeroValue);
            }
            if (this.getSentinelValues().containsOneKey && predicate.accept(this.getSentinelValues().oneValue))
            {
                result.add(this.getSentinelValues().oneValue);
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
            {
                result.add(this.getValueAtIndex(i));
            }
        }

        return result;
    }

    public MutableBooleanCollection reject(BooleanPredicate predicate)
    {
        MutableBooleanList result = BooleanLists.mutable.empty();
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && !predicate.accept(this.getSentinelValues().zeroValue))
            {
                result.add(this.getSentinelValues().zeroValue);
            }
            if (this.getSentinelValues().containsOneKey && !predicate.accept(this.getSentinelValues().oneValue))
            {
                result.add(this.getSentinelValues().oneValue);
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && !predicate.accept(this.getValueAtIndex(i)))
            {
                result.add(this.getValueAtIndex(i));
            }
        }
        return result;
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean value)
    {
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && predicate.accept(this.getSentinelValues().zeroValue))
            {
                return this.getSentinelValues().zeroValue;
            }
            if (this.getSentinelValues().containsOneKey && predicate.accept(this.getSentinelValues().oneValue))
            {
                return this.getSentinelValues().oneValue;
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
            {
                return this.getValueAtIndex(i);
            }
        }
        return value;
    }

    public <V> MutableCollection<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        FastList<V> target = FastList.newList(this.size());
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey)
            {
                target.add(function.valueOf(this.getSentinelValues().zeroValue));
            }
            if (this.getSentinelValues().containsOneKey)
            {
                target.add(function.valueOf(this.getSentinelValues().oneValue));
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i))
            {
                target.add(function.valueOf(this.getValueAtIndex(i)));
            }
        }
        return target;
    }

    public int count(BooleanPredicate predicate)
    {
        int count = 0;
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && predicate.accept(this.getSentinelValues().zeroValue))
            {
                count++;
            }
            if (this.getSentinelValues().containsOneKey && predicate.accept(this.getSentinelValues().oneValue))
            {
                count++;
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
            {
                count++;
            }
        }
        return count;
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && predicate.accept(this.getSentinelValues().zeroValue))
            {
                return true;
            }
            if (this.getSentinelValues().containsOneKey && predicate.accept(this.getSentinelValues().oneValue))
            {
                return true;
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && predicate.accept(this.getValueAtIndex(i)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey && !predicate.accept(this.getSentinelValues().zeroValue))
            {
                return false;
            }
            if (this.getSentinelValues().containsOneKey && !predicate.accept(this.getSentinelValues().oneValue))
            {
                return false;
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i) && !predicate.accept(this.getValueAtIndex(i)))
            {
                return false;
            }
        }
        return true;
    }

    public boolean noneSatisfy(BooleanPredicate predicate)
    {
        return !this.anySatisfy(predicate);
    }

    public boolean[] toArray()
    {
        boolean[] array = new boolean[this.size()];
        int index = 0;

        if (this.getSentinelValues() != null)
        {
            if (this.getSentinelValues().containsZeroKey)
            {
                array[index] = this.getSentinelValues().zeroValue;
                index++;
            }
            if (this.getSentinelValues().containsOneKey)
            {
                array[index] = this.getSentinelValues().oneValue;
                index++;
            }
        }
        for (int i = 0; i < this.getTableSize(); i++)
        {
            if (this.isNonSentinelAtIndex(i))
            {
                array[index] = this.getValueAtIndex(i);
                index++;
            }
        }

        return array;
    }

    protected static class SentinelValues extends AbstractSentinelValues
    {
        protected boolean zeroValue;
        protected boolean oneValue;

        public boolean containsValue(boolean value)
        {
            boolean valueEqualsZeroValue = this.containsZeroKey && this.zeroValue == value;
            boolean valueEqualsOneValue = this.containsOneKey && this.oneValue == value;
            return valueEqualsZeroValue || valueEqualsOneValue;
        }
    }

    protected abstract class AbstractBooleanValuesCollection implements MutableBooleanCollection
    {
        public void clear()
        {
            AbstractMutableBooleanValuesMap.this.clear();
        }

        public MutableBooleanCollection select(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.select(predicate);
        }

        public MutableBooleanCollection reject(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.reject(predicate);
        }

        public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
        {
            return AbstractMutableBooleanValuesMap.this.detectIfNone(predicate, ifNone);
        }

        public <V> MutableCollection<V> collect(BooleanToObjectFunction<? extends V> function)
        {
            return AbstractMutableBooleanValuesMap.this.collect(function);
        }

        public <T> T injectInto(T injectedValue, ObjectBooleanToObjectFunction<? super T, ? extends T> function)
        {
            return AbstractMutableBooleanValuesMap.this.injectInto(injectedValue, function);
        }

        public MutableBooleanCollection with(boolean element)
        {
            throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
        }

        public MutableBooleanCollection without(boolean element)
        {
            throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
        }

        public MutableBooleanCollection withAll(BooleanIterable elements)
        {
            throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
        }

        public MutableBooleanCollection withoutAll(BooleanIterable elements)
        {
            throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
        }

        public MutableBooleanCollection asUnmodifiable()
        {
            return UnmodifiableBooleanCollection.of(this);
        }

        public MutableBooleanCollection asSynchronized()
        {
            return SynchronizedBooleanCollection.of(this);
        }

        public ImmutableBooleanCollection toImmutable()
        {
            return BooleanLists.immutable.withAll(this);
        }

        public boolean contains(boolean value)
        {
            return AbstractMutableBooleanValuesMap.this.containsValue(value);
        }

        public boolean containsAll(boolean... source)
        {
            return AbstractMutableBooleanValuesMap.this.containsAll(source);
        }

        public boolean containsAll(BooleanIterable source)
        {
            return AbstractMutableBooleanValuesMap.this.containsAll(source);
        }

        public MutableBooleanList toList()
        {
            return AbstractMutableBooleanValuesMap.this.toList();
        }

        public MutableBooleanSet toSet()
        {
            return AbstractMutableBooleanValuesMap.this.toSet();
        }

        public MutableBooleanBag toBag()
        {
            return AbstractMutableBooleanValuesMap.this.toBag();
        }

        public LazyBooleanIterable asLazy()
        {
            return new LazyBooleanIterableAdapter(this);
        }

        public boolean isEmpty()
        {
            return AbstractMutableBooleanValuesMap.this.isEmpty();
        }

        public boolean notEmpty()
        {
            return AbstractMutableBooleanValuesMap.this.notEmpty();
        }

        public String makeString()
        {
            return AbstractMutableBooleanValuesMap.this.makeString();
        }

        public String makeString(String separator)
        {
            return AbstractMutableBooleanValuesMap.this.makeString(separator);
        }

        public String makeString(String start, String separator, String end)
        {
            return AbstractMutableBooleanValuesMap.this.makeString(start, separator, end);
        }

        public void appendString(Appendable appendable)
        {
            AbstractMutableBooleanValuesMap.this.appendString(appendable);
        }

        public void appendString(Appendable appendable, String separator)
        {
            AbstractMutableBooleanValuesMap.this.appendString(appendable, separator);
        }

        public void forEach(BooleanProcedure procedure)
        {
            this.each(procedure);
        }

        public void each(BooleanProcedure procedure)
        {
            AbstractMutableBooleanValuesMap.this.forEach(procedure);
        }

        public int count(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.count(predicate);
        }

        public boolean anySatisfy(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.anySatisfy(predicate);
        }

        public boolean allSatisfy(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.allSatisfy(predicate);
        }

        public boolean noneSatisfy(BooleanPredicate predicate)
        {
            return AbstractMutableBooleanValuesMap.this.noneSatisfy(predicate);
        }

        public boolean add(boolean element)
        {
            throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
        }

        public boolean addAll(boolean... source)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        public boolean addAll(BooleanIterable source)
        {
            throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
        }

        public boolean removeAll(BooleanIterable source)
        {
            int oldSize = AbstractMutableBooleanValuesMap.this.size();

            BooleanIterator iterator = source.booleanIterator();
            while (iterator.hasNext())
            {
                this.remove(iterator.next());
            }
            return oldSize != AbstractMutableBooleanValuesMap.this.size();
        }

        public boolean removeAll(boolean... source)
        {
            int oldSize = AbstractMutableBooleanValuesMap.this.size();

            for (boolean item : source)
            {
                this.remove(item);
            }
            return oldSize != AbstractMutableBooleanValuesMap.this.size();
        }

        public boolean retainAll(boolean... source)
        {
            return this.retainAll(BooleanHashSet.newSetWith(source));
        }

        public int size()
        {
            return AbstractMutableBooleanValuesMap.this.size();
        }

        public boolean[] toArray()
        {
            return AbstractMutableBooleanValuesMap.this.toArray();
        }
    }
}
