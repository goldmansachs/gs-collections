/*
 * Copyright 2011 Goldman Sachs.
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

package ponzu.impl.list.immutable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import net.jcip.annotations.Immutable;
import ponzu.api.block.function.Function;
import ponzu.api.block.function.Function2;
import ponzu.api.block.function.Generator;
import ponzu.api.block.function.primitive.DoubleObjectToDoubleFunction;
import ponzu.api.block.function.primitive.IntObjectToIntFunction;
import ponzu.api.block.function.primitive.LongObjectToLongFunction;
import ponzu.api.block.predicate.Predicate;
import ponzu.api.block.predicate.Predicate2;
import ponzu.api.block.procedure.ObjectIntProcedure;
import ponzu.api.block.procedure.Procedure;
import ponzu.api.block.procedure.Procedure2;
import ponzu.api.list.ImmutableList;
import ponzu.api.partition.list.PartitionImmutableList;
import ponzu.api.tuple.Pair;
import ponzu.impl.factory.Lists;
import ponzu.impl.list.mutable.FastList;
import ponzu.impl.partition.list.PartitionFastList;

/**
 * This is a zero element {@link ImmutableList} which is created by calling the Lists.immutable.of() method.
 */
@Immutable
final class ImmutableEmptyList<T>
        extends AbstractImmutableList<T>
        implements Serializable, RandomAccess
{
    static final ImmutableList<?> INSTANCE = new ImmutableEmptyList();
    private static final PartitionImmutableList<?> EMPTY = PartitionFastList.of(FastList.newList(), null).toImmutable();

    private static final long serialVersionUID = 1L;

    private Object readResolve()
    {
        return INSTANCE;
    }

    @Override
    public ImmutableList<T> newWithout(T element)
    {
        return this;
    }

    @Override
    public ImmutableList<T> newWithoutAll(Iterable<? extends T> elements)
    {
        return this;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean contains(Object obj)
    {
        return false;
    }

    @Override
    public T get(int index)
    {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
    }

    @Override
    public void forEach(Procedure<? super T> procedure)
    {
    }

    @Override
    public void reverseForEach(Procedure<? super T> procedure)
    {
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
    }

    @Override
    public ImmutableList<T> newWith(T newItem)
    {
        return Lists.immutable.of(newItem);
    }

    @Override
    public T min(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max(Comparator<? super T> comparator)
    {
        throw new NoSuchElementException();
    }

    @Override
    public T min()
    {
        throw new NoSuchElementException();
    }

    @Override
    public T max()
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new NoSuchElementException();
    }

    @Override
    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return Lists.immutable.of();
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return target;
    }

    @Override
    public ImmutableList<Pair<T, Integer>> zipWithIndex()
    {
        return Lists.immutable.of();
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return target;
    }

    @Override
    public ImmutableList<T> filter(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public <P, R extends Collection<T>> R filterWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    @Override
    public ImmutableList<T> filterNot(Predicate<? super T> predicate)
    {
        return this;
    }

    @Override
    public <P, R extends Collection<T>> R filterNotWith(Predicate2<? super T, ? super P> predicate, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    @Override
    public PartitionImmutableList<T> partition(Predicate<? super T> predicate)
    {
        return (PartitionImmutableList<T>) EMPTY;
    }

    @Override
    public <V> ImmutableList<V> transform(Function<? super T, ? extends V> function)
    {
        return Lists.immutable.of();
    }

    @Override
    public <V> ImmutableList<V> transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return Lists.immutable.of();
    }

    @Override
    public <P, V, R extends Collection<V>> R transformWith(Function2<? super T, ? super P, ? extends V> function, P parameter, R targetCollection)
    {
        return targetCollection;
    }

    @Override
    public <V> ImmutableList<V> flatTransform(Function<? super T, ? extends Iterable<V>> function)
    {
        return Lists.immutable.of();
    }

    @Override
    public <V, R extends Collection<V>> R flatTransform(Function<? super T, ? extends Iterable<V>> function, R targetCollection)
    {
        return targetCollection;
    }

    @Override
    public T find(Predicate<? super T> predicate)
    {
        return null;
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return 0;
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return false;
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return true;
    }

    @Override
    public <IV> IV foldLeft(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return injectedValue;
    }

    @Override
    public int foldLeft(int injectedValue, IntObjectToIntFunction<? super T> intObjectToIntFunction)
    {
        return injectedValue;
    }

    @Override
    public long foldLeft(long injectedValue, LongObjectToLongFunction<? super T> longObjectToLongFunction)
    {
        return injectedValue;
    }

    @Override
    public double foldLeft(double injectedValue, DoubleObjectToDoubleFunction<? super T> doubleObjectToDoubleFunction)
    {
        return injectedValue;
    }

    @Override
    public T getFirst()
    {
        return null;
    }

    @Override
    public T getLast()
    {
        return null;
    }

    @Override
    public int indexOf(Object object)
    {
        return -1;
    }

    @Override
    public boolean equals(Object otherList)
    {
        if (otherList == this)
        {
            return true;
        }
        if (!(otherList instanceof List))
        {
            return false;
        }
        List<T> list = (List<T>) otherList;
        return list.isEmpty();
    }

    @Override
    public int hashCode()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public boolean notEmpty()
    {
        return false;
    }

    @Override
    public <R extends Collection<T>> R filter(Predicate<? super T> predicate, R target)
    {
        return target;
    }

    @Override
    public <R extends Collection<T>> R filterNot(Predicate<? super T> predicate, R target)
    {
        return target;
    }

    @Override
    public <V, R extends Collection<V>> R transform(Function<? super T, ? extends V> function, R target)
    {
        return target;
    }

    @Override
    public <V, R extends Collection<V>> R transformIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return target;
    }

    @Override
    public T findIfNone(Predicate<? super T> predicate, Generator<? extends T> function)
    {
        return function.value();
    }

    @Override
    public String toString()
    {
        return "[]";
    }

    @Override
    public String makeString()
    {
        return "";
    }

    @Override
    public String makeString(String separator)
    {
        return "";
    }

    @Override
    public String makeString(String start, String separator, String end)
    {
        return start + end;
    }

    @Override
    public void appendString(Appendable appendable)
    {
    }

    @Override
    public void appendString(Appendable appendable, String separator)
    {
    }

    @Override
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
}
