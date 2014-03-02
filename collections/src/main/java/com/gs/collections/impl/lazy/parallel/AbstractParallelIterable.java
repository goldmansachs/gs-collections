/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.ParallelIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.sorted.MutableSortedMap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.sorted.MutableSortedSet;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.procedure.AtomicCountProcedure;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.block.procedure.MapCollectProcedure;
import com.gs.collections.impl.block.procedure.MutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.NonMutatingAggregationProcedure;
import com.gs.collections.impl.block.procedure.checked.CheckedProcedure2;
import com.gs.collections.impl.list.mutable.CompositeFastList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.sorted.mutable.TreeSortedMap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.sorted.mutable.TreeSortedSet;

@Beta
public abstract class AbstractParallelIterable<T, B extends Batch<T>> implements ParallelIterable<T>
{
    protected static <T> void forEach(final AbstractParallelIterable<T, ? extends RootBatch<T>> parallelIterable, final Procedure<? super T> procedure)
    {
        LazyIterable<Future<?>> futures = parallelIterable.split().collect(new Function<RootBatch<T>, Future<?>>()
        {
            public Future<?> valueOf(final RootBatch<T> chunk)
            {
                return parallelIterable.getExecutorService().submit(new Runnable()
                {
                    public void run()
                    {
                        chunk.forEach(procedure);
                    }
                });
            }
        });
        // The call to to toList() is important to stop the lazy evaluation and force all the Runnables to start executing.
        MutableList<Future<?>> futuresList = futures.toList();
        for (Future<?> future : futuresList)
        {
            try
            {
                future.get();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    protected static <T> boolean anySatisfy(AbstractParallelIterable<T, ? extends RootBatch<T>> parallelIterable, final Predicate<? super T> predicate)
    {
        final CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(parallelIterable.getExecutorService());
        MutableSet<Future<Boolean>> futures = parallelIterable.split().collect(new Function<RootBatch<T>, Future<Boolean>>()
        {
            public Future<Boolean> valueOf(final RootBatch<T> batch)
            {
                return completionService.submit(new Callable<Boolean>()
                {
                    public Boolean call()
                    {
                        return batch.anySatisfy(predicate);
                    }
                });
            }
        }, UnifiedSet.<Future<Boolean>>newSet());

        while (futures.notEmpty())
        {
            try
            {
                Future<Boolean> future = completionService.take();
                if (future.get())
                {
                    for (Future<Boolean> eachFuture : futures)
                    {
                        eachFuture.cancel(true);
                    }
                    return true;
                }
                futures.remove(future);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    protected static <T> boolean allSatisfy(AbstractParallelIterable<T, ? extends RootBatch<T>> parallelIterable, final Predicate<? super T> predicate)
    {
        final CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(parallelIterable.getExecutorService());
        MutableSet<Future<Boolean>> futures = parallelIterable.split().collect(new Function<RootBatch<T>, Future<Boolean>>()
        {
            public Future<Boolean> valueOf(final RootBatch<T> batch)
            {
                return completionService.submit(new Callable<Boolean>()
                {
                    public Boolean call()
                    {
                        return batch.allSatisfy(predicate);
                    }
                });
            }
        }, UnifiedSet.<Future<Boolean>>newSet());

        while (futures.notEmpty())
        {
            try
            {
                Future<Boolean> future = completionService.take();
                if (!future.get())
                {
                    for (Future<Boolean> eachFuture : futures)
                    {
                        eachFuture.cancel(true);
                    }
                    return false;
                }
                futures.remove(future);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    protected static <T> T detect(final AbstractParallelIterable<T, ? extends RootBatch<T>> parallelIterable, final Predicate<? super T> predicate)
    {
        LazyIterable<? extends RootBatch<T>> chunks = parallelIterable.split();
        LazyIterable<Future<T>> futures = chunks.collect(new Function<RootBatch<T>, Future<T>>()
        {
            public Future<T> valueOf(final RootBatch<T> chunk)
            {
                return parallelIterable.getExecutorService().submit(new Callable<T>()
                {
                    public T call()
                    {
                        return chunk.detect(predicate);
                    }
                });
            }
        });
        // The call to to toList() is important to stop the lazy evaluation and force all the Runnables to start executing.
        MutableList<Future<T>> futuresList = futures.toList();
        for (Future<T> future : futuresList)
        {
            try
            {
                T eachResult = future.get();
                if (eachResult != null)
                {
                    for (Future<T> eachFutureToCancel : futuresList)
                    {
                        eachFutureToCancel.cancel(true);
                    }
                    return eachResult;
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public abstract ExecutorService getExecutorService();

    public abstract LazyIterable<B> split();

    protected abstract boolean isOrdered();

    protected <S, V> void collectCombine(Function<Batch<T>, V> function, Procedure2<S, V> combineProcedure, S state)
    {
        if (this.isOrdered())
        {
            this.collectCombineOrdered(function, combineProcedure, state);
        }
        else
        {
            this.collectCombineUnordered(function, combineProcedure, state);
        }
    }

    private <S, V> void collectCombineOrdered(final Function<Batch<T>, V> function, Procedure2<S, V> combineProcedure, S state)
    {
        LazyIterable<? extends Batch<T>> chunks = this.split();
        LazyIterable<Future<V>> futures = chunks.collect(new Function<Batch<T>, Future<V>>()
        {
            public Future<V> valueOf(final Batch<T> chunk)
            {
                return AbstractParallelIterable.this.getExecutorService().submit(new Callable<V>()
                {
                    public V call()
                    {
                        return function.valueOf(chunk);
                    }
                });
            }
        });
        // The call to to toList() is important to stop the lazy evaluation and force all the Runnables to start executing.
        MutableList<Future<V>> futuresList = futures.toList();
        for (Future<V> future : futuresList)
        {
            try
            {
                combineProcedure.value(state, future.get());
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private <S, V> void collectCombineUnordered(final Function<Batch<T>, V> function, Procedure2<S, V> combineProcedure, S state)
    {
        LazyIterable<? extends Batch<T>> chunks = this.split();
        MutableList<Callable<V>> callables = chunks.collect(new Function<Batch<T>, Callable<V>>()
        {
            public Callable<V> valueOf(final Batch<T> chunk)
            {
                return new Callable<V>()
                {
                    public V call()
                    {
                        return function.valueOf(chunk);
                    }
                };
            }
        }).toList();

        final ExecutorCompletionService<V> completionService = new ExecutorCompletionService<V>(this.getExecutorService());
        callables.forEach(new Procedure<Callable<V>>()
        {
            public void value(Callable<V> callable)
            {
                completionService.submit(callable);
            }
        });

        int numTasks = callables.size();
        while (numTasks > 0)
        {
            try
            {
                Future<V> future = completionService.take();
                combineProcedure.value(state, future.get());
                numTasks--;
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
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

    public void appendString(final Appendable appendable, String start, final String separator, String end)
    {
        try
        {
            appendable.append(start);
            Function<Batch<T>, String> map = new Function<Batch<T>, String>()
            {
                public String valueOf(Batch<T> batch)
                {
                    return batch.makeString(separator);
                }
            };
            Procedure2<Appendable, String> reduce = new CheckedProcedure2<Appendable, String>()
            {
                private boolean first = true;

                public void safeValue(Appendable accumulator, String each) throws IOException
                {
                    if ("".equals(each))
                    {
                        return;
                    }
                    if (this.first)
                    {
                        this.first = false;
                    }
                    else
                    {
                        appendable.append(separator);
                    }
                    appendable.append(each);
                }
            };
            this.collectCombine(map, reduce, appendable);

            appendable.append(end);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        this.forEach(Procedures.bind(procedure, parameter));
    }

    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.anySatisfy(Predicates.bind(predicate, parameter));
    }

    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.allSatisfy(Predicates.bind(predicate, parameter));
    }

    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return this.allSatisfy(Predicates.not(predicate));
    }

    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.noneSatisfy(Predicates.bind(predicate, parameter));
    }

    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.detect(Predicates.bind(predicate, parameter));
    }

    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
        return result == null ? function.value() : result;
    }

    public <P> T detectWithIfNone(Predicate2<? super T, ? super P> predicate, P parameter, Function0<? extends T> function)
    {
        return this.detectIfNone(Predicates.bind(predicate, parameter), function);
    }

    public Object[] toArray()
    {
        throw new UnsupportedOperationException();
    }

    public <E> E[] toArray(E[] array)
    {
        throw new UnsupportedOperationException();
    }

    public MutableList<T> toList()
    {
        Function<Batch<T>, FastList<T>> map = new Function<Batch<T>, FastList<T>>()
        {
            public FastList<T> valueOf(Batch<T> batch)
            {
                FastList<T> list = FastList.newList();
                batch.forEach(CollectionAddProcedure.on(list));
                return list;
            }
        };
        Procedure2<MutableList<T>, FastList<T>> reduce = new Procedure2<MutableList<T>, FastList<T>>()
        {
            public void value(MutableList<T> accumulator, FastList<T> each)
            {
                accumulator.addAll(each);
            }
        };
        MutableList<T> state = new CompositeFastList<T>();
        this.collectCombine(map, reduce, state);
        return state;
    }

    public MutableList<T> toSortedList()
    {
        throw new UnsupportedOperationException();
    }

    public MutableList<T> toSortedList(Comparator<? super T> comparator)
    {
        throw new UnsupportedOperationException();
    }

    public <V extends Comparable<? super V>> MutableList<T> toSortedListBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedList(Comparators.byFunction(function));
    }

    public MutableSet<T> toSet()
    {
        MutableSet<T> result = UnifiedSet.<T>newSet().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    public MutableSortedSet<T> toSortedSet()
    {
        MutableSortedSet<T> result = TreeSortedSet.<T>newSet().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    public <V extends Comparable<? super V>> MutableSortedSet<T> toSortedSetBy(Function<? super T, ? extends V> function)
    {
        return this.toSortedSet(Comparators.byFunction(function));
    }

    public MutableBag<T> toBag()
    {
        MutableBag<T> result = HashBag.<T>newBag().asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    public MutableSortedSet<T> toSortedSet(Comparator<? super T> comparator)
    {
        MutableSortedSet<T> result = TreeSortedSet.newSet(comparator).asSynchronized();
        this.forEach(CollectionAddProcedure.on(result));
        return result;
    }

    public <NK, NV> MutableMap<NK, NV> toMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        MutableMap<NK, NV> map = UnifiedMap.<NK, NV>newMap().asSynchronized();
        this.forEach(new MapCollectProcedure<T, NK, NV>(map, keyFunction, valueFunction));
        return map;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        MutableSortedMap<NK, NV> sortedMap = TreeSortedMap.<NK, NV>newMap().asSynchronized();
        this.forEach(new MapCollectProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public <NK, NV> MutableSortedMap<NK, NV> toSortedMap(Comparator<? super NK> comparator,
            Function<? super T, ? extends NK> keyFunction,
            Function<? super T, ? extends NV> valueFunction)
    {
        MutableSortedMap<NK, NV> sortedMap = TreeSortedMap.<NK, NV>newMap(comparator).asSynchronized();
        this.forEach(new MapCollectProcedure<T, NK, NV>(sortedMap, keyFunction, valueFunction));
        return sortedMap;
    }

    public <K, V> MapIterable<K, V> aggregateBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Function2<? super V, ? super T, ? extends V> nonMutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.<K, V>newMap().asSynchronized();
        this.forEach(new NonMutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, nonMutatingAggregator));
        return map;
    }

    public <K, V> MapIterable<K, V> aggregateInPlaceBy(
            Function<? super T, ? extends K> groupBy,
            Function0<? extends V> zeroValueFactory,
            Procedure2<? super V, ? super T> mutatingAggregator)
    {
        MutableMap<K, V> map = UnifiedMap.<K, V>newMap().asSynchronized();
        this.forEach(new MutatingAggregationProcedure<T, K, V>(map, groupBy, zeroValueFactory, mutatingAggregator));
        return map;
    }

    public int count(Predicate<? super T> predicate)
    {
        AtomicCountProcedure<T> procedure = new AtomicCountProcedure<T>(predicate);
        this.forEach(procedure);
        return procedure.getCount();
    }

    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.count(Predicates.bind(predicate, parameter));
    }

    public T min(Comparator<? super T> comparator)
    {
        throw new UnsupportedOperationException();
    }

    public T max(Comparator<? super T> comparator)
    {
        throw new UnsupportedOperationException();
    }

    public T min()
    {
        throw new UnsupportedOperationException();
    }

    public T max()
    {
        throw new UnsupportedOperationException();
    }

    public <V extends Comparable<? super V>> T minBy(Function<? super T, ? extends V> function)
    {
        throw new UnsupportedOperationException();
    }

    public <V extends Comparable<? super V>> T maxBy(Function<? super T, ? extends V> function)
    {
        throw new UnsupportedOperationException();
    }

    public long sumOfInt(IntFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    public double sumOfFloat(FloatFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    public long sumOfLong(LongFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }

    public double sumOfDouble(DoubleFunction<? super T> function)
    {
        throw new UnsupportedOperationException();
    }
}
