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

package com.gs.collections.impl.set.sorted.immutable;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.list.primitive.ImmutableBooleanList;
import com.gs.collections.api.list.primitive.ImmutableByteList;
import com.gs.collections.api.list.primitive.ImmutableCharList;
import com.gs.collections.api.list.primitive.ImmutableDoubleList;
import com.gs.collections.api.list.primitive.ImmutableFloatList;
import com.gs.collections.api.list.primitive.ImmutableIntList;
import com.gs.collections.api.list.primitive.ImmutableLongList;
import com.gs.collections.api.list.primitive.ImmutableShortList;
import com.gs.collections.api.multimap.sortedset.ImmutableSortedSetMultimap;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.partition.set.sorted.PartitionImmutableSortedSet;
import com.gs.collections.api.partition.set.sorted.PartitionMutableSortedSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.set.sorted.ImmutableSortedSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.api.set.sorted.ParallelSortedSetIterable;
import com.gs.collections.api.set.sorted.SortedSetIterable;
import com.gs.collections.api.stack.MutableStack;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectIfProcedure;
import com.gs.collections.impl.block.procedure.CollectProcedure;
import com.gs.collections.impl.block.procedure.FlatCollectProcedure;
import com.gs.collections.impl.block.procedure.PartitionPredicate2Procedure;
import com.gs.collections.impl.block.procedure.PartitionProcedure;
import com.gs.collections.impl.block.procedure.RejectProcedure;
import com.gs.collections.impl.block.procedure.SelectInstancesOfProcedure;
import com.gs.collections.impl.block.procedure.SelectProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectBooleanProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectByteProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectCharProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectDoubleProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectFloatProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectIntProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectLongProcedure;
import com.gs.collections.impl.block.procedure.primitive.CollectShortProcedure;
import com.gs.collections.impl.collection.immutable.AbstractImmutableCollection;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.lazy.parallel.set.sorted.NonParallelSortedSetIterable;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.primitive.BooleanArrayList;
import com.gs.collections.impl.list.mutable.primitive.ByteArrayList;
import com.gs.collections.impl.list.mutable.primitive.CharArrayList;
import com.gs.collections.impl.list.mutable.primitive.DoubleArrayList;
import com.gs.collections.impl.list.mutable.primitive.FloatArrayList;
import com.gs.collections.impl.list.mutable.primitive.IntArrayList;
import com.gs.collections.impl.list.mutable.primitive.LongArrayList;
import com.gs.collections.impl.list.mutable.primitive.ShortArrayList;
import com.gs.collections.impl.multimap.set.sorted.TreeSortedSetMultimap;
import com.gs.collections.impl.partition.set.sorted.PartitionTreeSortedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;
import com.gs.collections.impl.stack.mutable.ArrayStack;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.OrderedIterate;
import com.gs.collections.impl.utility.internal.IterableIterate;
import com.gs.collections.impl.utility.internal.SetIterables;
import com.gs.collections.impl.utility.internal.SortedSetIterables;
import net.jcip.annotations.Immutable;

/**
 * This class is the parent class for all ImmutableSortedSets.  All implementations of ImmutableSortedSet must implement the SortedSet
 * interface so an TreeSet.equals(anImmutableSortedSet) can return true when the contents are the same.
 */
@Immutable
abstract class AbstractImmutableSortedSet<T> extends AbstractImmutableCollection<T>
        implements ImmutableSortedSet<T>, SortedSet<T>
{
    public SortedSet<T> castToSortedSet()
    {
        return this;
    }

    public ImmutableSortedSet<T> newWith(T element)
    {
        if (!this.contains(element))
        {
            return TreeSortedSet.newSet(this).with(element).toImmutable();
        }
        return this;
    }

    public ImmutableSortedSet<T> newWithout(T element)
    {
        if (this.contains(element))
        {
            TreeSortedSet<T> result = TreeSortedSet.newSet(this);
            result.remove(element);
            return result.toImmutable();
        }
        return this;
    }

    public ImmutableSortedSet<T> newWithAll(Iterable<? extends T> elements)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this);
        result.addAllIterable(elements);
        return result.toImmutable();
    }

    public ImmutableSortedSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this);
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    public T getFirst()
    {
        return this.first();
    }

    public T getLast()
    {
        return this.last();
    }

    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return TreeSortedSet.newSet(this.comparator());
    }

    public ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        BooleanArrayList result = new BooleanArrayList(this.size());
        this.forEach(new CollectBooleanProcedure<T>(booleanFunction, result));
        return result.toImmutable();
    }

    public ImmutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        ByteArrayList result = new ByteArrayList(this.size());
        this.forEach(new CollectByteProcedure<T>(byteFunction, result));
        return result.toImmutable();
    }

    public ImmutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        CharArrayList result = new CharArrayList(this.size());
        this.forEach(new CollectCharProcedure<T>(charFunction, result));
        return result.toImmutable();
    }

    public ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        DoubleArrayList result = new DoubleArrayList(this.size());
        this.forEach(new CollectDoubleProcedure<T>(doubleFunction, result));
        return result.toImmutable();
    }

    public ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        FloatArrayList result = new FloatArrayList(this.size());
        this.forEach(new CollectFloatProcedure<T>(floatFunction, result));
        return result.toImmutable();
    }

    public ImmutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        IntArrayList result = new IntArrayList(this.size());
        this.forEach(new CollectIntProcedure<T>(intFunction, result));
        return result.toImmutable();
    }

    public ImmutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        LongArrayList result = new LongArrayList(this.size());
        this.forEach(new CollectLongProcedure<T>(longFunction, result));
        return result.toImmutable();
    }

    public ImmutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        ShortArrayList result = new ShortArrayList(this.size());
        this.forEach(new CollectShortProcedure<T>(shortFunction, result));
        return result.toImmutable();
    }

    public ImmutableSortedSet<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return OrderedIterate.corresponds(this, other, predicate);
    }

    public ImmutableSortedSet<T> select(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new SelectProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public <P> ImmutableSortedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    public ImmutableSortedSet<T> reject(Predicate<? super T> predicate)
    {
        TreeSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        this.forEach(new RejectProcedure<T>(predicate, result));
        return result.toImmutable();
    }

    public <P> ImmutableSortedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    public PartitionImmutableSortedSet<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableSortedSet<T> partitionTreeSortedSet = new PartitionTreeSortedSet<T>(this.comparator());
        this.forEach(new PartitionProcedure<T>(predicate, partitionTreeSortedSet));
        return partitionTreeSortedSet.toImmutable();
    }

    public <P> PartitionImmutableSortedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableSortedSet<T> partitionTreeSortedSet = new PartitionTreeSortedSet<T>(this.comparator());
        this.forEach(new PartitionPredicate2Procedure<T, P>(predicate, parameter, partitionTreeSortedSet));
        return partitionTreeSortedSet.toImmutable();
    }

    public PartitionImmutableSortedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionTreeSortedSet<T> result = new PartitionTreeSortedSet<T>(this.comparator());
        return IterableIterate.partitionWhile(this, predicate, result).toImmutable();
    }

    public <S> ImmutableSortedSet<S> selectInstancesOf(Class<S> clazz)
    {
        TreeSortedSet<S> result = TreeSortedSet.newSet((Comparator<? super S>) this.comparator());
        this.forEach(new SelectInstancesOfProcedure<S>(clazz, result));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.empty();
        this.forEach(new CollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    public <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        MutableList<V> result = Lists.mutable.empty();
        this.forEach(new CollectIfProcedure<T, V>(result, function, predicate));
        return result.toImmutable();
    }

    public <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        MutableList<V> result = Lists.mutable.empty();
        this.forEach(new FlatCollectProcedure<T, V>(function, result));
        return result.toImmutable();
    }

    public int detectIndex(Predicate<? super T> predicate)
    {
        return Iterate.detectIndex(this, predicate);
    }

    public <V> ImmutableSortedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <V> ImmutableSortedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, TreeSortedSetMultimap.<V, T>newMultimap(this.comparator())).toImmutable();
    }

    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return Iterate.zip(this, that, FastList.<Pair<T, S>>newList()).toImmutable();
    }

    public ImmutableSortedSet<Pair<T, Integer>> zipWithIndex()
    {
        Comparator<? super T> comparator = this.comparator();
        if (comparator == null)
        {
            TreeSortedSet<Pair<T, Integer>> pairs = TreeSortedSet.newSet(Comparators.<Pair<T, Integer>, T>byFunction(Functions.<T>firstOfPair(), Comparators.<T>naturalOrder()));
            return Iterate.zipWithIndex(this, pairs).toImmutable();
        }
        return Iterate.zipWithIndex(this, TreeSortedSet.<Pair<T, Integer>>newSet(Comparators.byFirstOfPair(comparator))).toImmutable();
    }

    public ImmutableSortedSet<T> distinct()
    {
        return this;
    }

    public ImmutableSortedSet<T> takeWhile(Predicate<? super T> predicate)
    {
        MutableSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        return IterableIterate.takeWhile(this, predicate, result).toImmutable();
    }

    public ImmutableSortedSet<T> dropWhile(Predicate<? super T> predicate)
    {
        MutableSortedSet<T> result = TreeSortedSet.newSet(this.comparator());
        return IterableIterate.dropWhile(this, predicate, result).toImmutable();
    }

    public MutableStack<T> toStack()
    {
        return ArrayStack.newStack(this);
    }

    public ImmutableSortedSet<T> union(SetIterable<? extends T> set)
    {
        return SetIterables.unionInto(this, set, TreeSortedSet.newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    public ImmutableSortedSet<T> intersect(SetIterable<? extends T> set)
    {
        return SetIterables.intersectInto(this, set, TreeSortedSet.newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    public ImmutableSortedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, TreeSortedSet.newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    public ImmutableSortedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return SetIterables.symmetricDifferenceInto(this, setB, TreeSortedSet.newSet(this.comparator())).toImmutable();
    }

    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    public ImmutableSortedSet<SortedSetIterable<T>> powerSet()
    {
        return (ImmutableSortedSet<SortedSetIterable<T>>) (ImmutableSortedSet<?>) SortedSetIterables.immutablePowerSet(this);
    }

    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    public SortedSet<T> subSet(T fromElement, T toElement)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".subSet() not implemented yet");
    }

    public SortedSet<T> headSet(T toElement)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".headSet() not implemented yet");
    }

    public SortedSet<T> tailSet(T fromElement)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".tailSet() not implemented yet");
    }

    public ImmutableSortedSet<T> toImmutable()
    {
        return this;
    }

    public ParallelSortedSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        if (executorService == null)
        {
            throw new NullPointerException();
        }
        if (batchSize < 1)
        {
            throw new IllegalArgumentException();
        }
        return new NonParallelSortedSetIterable<T>(this);
    }

    public void reverseForEach(Procedure<? super T> procedure)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".reverseForEach() not implemented yet");
    }

    public LazyIterable<T> asReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asReversed() not implemented yet");
    }

    public ImmutableSortedSet<T> toReversed()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".toReversed() not implemented yet");
    }

    public int detectLastIndex(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".detectLastIndex() not implemented yet");
    }
}
