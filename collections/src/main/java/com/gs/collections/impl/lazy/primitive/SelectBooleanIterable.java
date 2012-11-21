/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.lazy.primitive;

import java.util.NoSuchElementException;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;

public class SelectBooleanIterable
        implements BooleanIterable
{
    private final BooleanIterable delegate;
    private final BooleanPredicate predicate;

    public SelectBooleanIterable(BooleanIterable delegate, BooleanPredicate predicate)
    {
        this.delegate = delegate;
        this.predicate = predicate;
    }

    public BooleanIterator booleanIterator()
    {
        return new SelectBooleanIterator(this.delegate, this.predicate);
    }

    public void forEach(BooleanProcedure procedure)
    {
        this.delegate.forEach(new IfBooleanProcedure(procedure));
    }

    public int size()
    {
        return this.delegate.count(this.predicate);
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean notEmpty()
    {
        return this.size() > 0;
    }

    public int count(BooleanPredicate predicate)
    {
        CountBooleanProcedure countBooleanProcedure = new CountBooleanProcedure(predicate);
        this.forEach(countBooleanProcedure);
        return countBooleanProcedure.getCount();
    }

    public boolean anySatisfy(BooleanPredicate predicate)
    {
        return this.delegate.anySatisfy(BooleanPredicates.and(this.predicate, predicate));
    }

    public boolean allSatisfy(BooleanPredicate predicate)
    {
        return !this.anySatisfy(BooleanPredicates.not(predicate));
    }

    public BooleanIterable select(BooleanPredicate predicate)
    {
        return new SelectBooleanIterable(this, predicate);
    }

    public BooleanIterable reject(BooleanPredicate predicate)
    {
        return new SelectBooleanIterable(this, BooleanPredicates.not(predicate));
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        for (BooleanIterator booleanIterator = this.booleanIterator(); booleanIterator.hasNext(); )
        {
            boolean item = booleanIterator.next();
            if (predicate.accept(item))
            {
                return item;
            }
        }
        return ifNone;
    }

    public <V> RichIterable<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return new CollectBooleanToObjectIterable<V>(this, function);
    }

    public boolean[] toArray()
    {
        final boolean[] array = new boolean[this.size()];
        this.forEach(new BooleanProcedure()
        {
            @SuppressWarnings("FieldMayBeFinal")
            private int index = 0;

            public void value(boolean each)
            {
                array[this.index++] = each;
            }
        });
        return array;
    }

    public boolean contains(boolean value)
    {
        return this.anySatisfy(BooleanPredicates.equal(value));
    }

    public boolean containsAll(boolean... source)
    {
        for (boolean value : source)
        {
            if (!this.contains(value))
            {
                return false;
            }
        }
        return true;
    }

    private static final class CountBooleanProcedure implements BooleanProcedure
    {
        private static final long serialVersionUID = 1L;
        private final BooleanPredicate predicate;
        private int counter = 0;

        private CountBooleanProcedure(BooleanPredicate predicate)
        {
            this.predicate = predicate;
        }

        public void value(boolean each)
        {
            if (this.predicate.accept(each))
            {
                this.counter++;
            }
        }

        public int getCount()
        {
            return this.counter;
        }
    }

    private final class IfBooleanProcedure implements BooleanProcedure
    {
        private static final long serialVersionUID = 1L;
        private final BooleanProcedure procedure;

        private IfBooleanProcedure(BooleanProcedure procedure)
        {
            this.procedure = procedure;
        }

        public void value(boolean each)
        {
            if (SelectBooleanIterable.this.predicate.accept(each))
            {
                this.procedure.value(each);
            }
        }
    }

    private static final class SelectBooleanIterator
            implements BooleanIterator
    {
        private final BooleanIterator iterator;
        private final BooleanPredicate predicate;
        private boolean next = false;
        private boolean verifiedHasNext = false;

        private SelectBooleanIterator(BooleanIterable iterable, BooleanPredicate predicate)
        {
            this(iterable.booleanIterator(), predicate);
        }

        private SelectBooleanIterator(BooleanIterator iterator, BooleanPredicate predicate)
        {
            this.iterator = iterator;
            this.predicate = predicate;
        }

        public boolean hasNext()
        {
            if (this.verifiedHasNext)
            {
                return true;
            }
            while (this.iterator.hasNext())
            {
                boolean temp = this.iterator.next();
                if (this.predicate.accept(temp))
                {
                    this.next = temp;
                    this.verifiedHasNext = true;
                    return true;
                }
            }
            return false;
        }

        public boolean next()
        {
            if (this.verifiedHasNext || this.hasNext())
            {
                this.verifiedHasNext = false;
                return this.next;
            }
            throw new NoSuchElementException();
        }
    }
}
