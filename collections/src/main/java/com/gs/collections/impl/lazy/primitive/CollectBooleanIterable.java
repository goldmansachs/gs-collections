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

package com.gs.collections.impl.lazy.primitive;

import java.io.IOException;
import java.util.Iterator;

import com.gs.collections.api.BooleanIterable;
import com.gs.collections.api.LazyBooleanIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.BooleanToObjectFunction;
import com.gs.collections.api.block.function.primitive.ObjectBooleanToObjectFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.primitive.BooleanPredicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.BooleanProcedure;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.iterator.BooleanIterator;
import com.gs.collections.api.list.primitive.MutableBooleanList;
import com.gs.collections.api.set.primitive.MutableBooleanSet;
import com.gs.collections.impl.bag.mutable.primitive.BooleanHashBag;
import com.gs.collections.impl.block.factory.primitive.BooleanPredicates;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.set.mutable.primitive.BooleanHashSet;
import net.jcip.annotations.Immutable;

/**
 * A CollectIntIterable is an iterable that transforms a source iterable using an IntFunction as it iterates.
 */
@Immutable
public class CollectBooleanIterable<T>
        implements LazyBooleanIterable
{
    private final LazyIterable<T> iterable;
    private final BooleanFunction<? super T> function;
    private final BooleanFunctionToProcedure booleanFunctionToProcedure = new BooleanFunctionToProcedure();

    public CollectBooleanIterable(LazyIterable<T> adapted, BooleanFunction<? super T> function)
    {
        this.iterable = adapted;
        this.function = function;
    }

    public BooleanIterator booleanIterator()
    {
        return new BooleanIterator()
        {
            private final Iterator<T> iterator = CollectBooleanIterable.this.iterable.iterator();

            public boolean next()
            {
                return CollectBooleanIterable.this.function.booleanValueOf(this.iterator.next());
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }
        };
    }

    public void forEach(BooleanProcedure procedure)
    {
        this.iterable.forEachWith(this.booleanFunctionToProcedure, procedure);
    }

    public int size()
    {
        return this.iterable.size();
    }

    public boolean isEmpty()
    {
        return this.iterable.isEmpty();
    }

    public boolean notEmpty()
    {
        return this.iterable.notEmpty();
    }

    public int count(final BooleanPredicate predicate)
    {
        return this.iterable.count(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    public boolean anySatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.anySatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    public boolean allSatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    public boolean noneSatisfy(final BooleanPredicate predicate)
    {
        return this.iterable.allSatisfy(new Predicate<T>()
        {
            public boolean accept(T each)
            {
                return !predicate.accept(CollectBooleanIterable.this.function.booleanValueOf(each));
            }
        });
    }

    public LazyBooleanIterable select(BooleanPredicate predicate)
    {
        return new SelectBooleanIterable(this, predicate);
    }

    public LazyBooleanIterable reject(BooleanPredicate predicate)
    {
        return new SelectBooleanIterable(this, BooleanPredicates.not(predicate));
    }

    public boolean detectIfNone(BooleanPredicate predicate, boolean ifNone)
    {
        BooleanIterator iterator = this.booleanIterator();
        while (iterator.hasNext())
        {
            boolean next = iterator.next();
            if (predicate.accept(next))
            {
                return next;
            }
        }
        return ifNone;
    }

    public <V> LazyIterable<V> collect(BooleanToObjectFunction<? extends V> function)
    {
        return new CollectBooleanToObjectIterable<V>(this, function);
    }

    public <T> T injectInto(T injectedValue, ObjectBooleanToObjectFunction<? super T, ? extends T> function)
    {
        T result = injectedValue;
        for (BooleanIterator iterator = this.booleanIterator(); iterator.hasNext(); )
        {
            result = function.valueOf(result, iterator.next());
        }
        return result;
    }

    public boolean[] toArray()
    {
        final boolean[] array = new boolean[this.size()];
        this.iterable.forEachWithIndex(new ObjectIntProcedure<T>()
        {
            public void value(T each, int index)
            {
                array[index] = CollectBooleanIterable.this.function.booleanValueOf(each);
            }
        });
        return array;
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

            BooleanIterator iterator = this.booleanIterator();
            if (iterator.hasNext())
            {
                appendable.append(String.valueOf(iterator.next()));
                while (iterator.hasNext())
                {
                    appendable.append(separator);
                    appendable.append(String.valueOf(iterator.next()));
                }
            }

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
        return this;
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

    public boolean containsAll(BooleanIterable source)
    {
        for (BooleanIterator iterator = source.booleanIterator(); iterator.hasNext(); )
        {
            if (!this.contains(iterator.next()))
            {
                return false;
            }
        }
        return true;
    }

    private final class BooleanFunctionToProcedure implements Procedure2<T, BooleanProcedure>
    {
        private static final long serialVersionUID = -4133872659735979655L;

        public void value(T each, BooleanProcedure parm)
        {
            parm.value(CollectBooleanIterable.this.function.booleanValueOf(each));
        }
    }
}
