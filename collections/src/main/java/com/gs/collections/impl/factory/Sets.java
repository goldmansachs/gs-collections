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

package com.gs.collections.impl.factory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.factory.set.FixedSizeSetFactory;
import com.gs.collections.api.factory.set.ImmutableSetFactory;
import com.gs.collections.api.factory.set.MutableSetFactory;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.set.fixed.FixedSizeSetFactoryImpl;
import com.gs.collections.impl.set.immutable.ImmutableSetFactoryImpl;
import com.gs.collections.impl.set.mutable.MutableSetFactoryImpl;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * Set algebra operations are available in this class as static utility.
 * <p>
 * Most operations are non-destructive, i.e. no input sets are modified during execution.
 * The exception is operations ending in "Into." These accept the target collection of
 * the final calculation as the first parameter.
 * <p>
 * Some effort is made to return a <tt>SortedSet</tt> if any input set is sorted, but
 * this is not guaranteed (e.g., this will not be the case for collections proxied by
 * Hibernate). When in doubt, specify the target collection explicitly with the "Into"
 * version.
 *
 * This class should be used to create instances of MutableSet, ImmutableSet and FixedSizeSet
 * <p>
 * Mutable Examples:
 *
 * <pre>
 * MutableSet&lt;String&gt; emptySet = Sets.mutable.empty();
 * MutableSet&lt;String&gt; setWith = Sets.mutable.with("a", "b", "c");
 * MutableSet&lt;String&gt; setOf = Sets.mutable.of("a", "b", "c");
 * </pre>
 *
 * Immutable Examples:
 *
 * <pre>
 * ImmutableSet&lt;String&gt; emptySet = Sets.immutable.empty();
 * ImmutableSet&lt;String&gt; setWith = Sets.immutable.with("a", "b", "c");
 * ImmutableSet&lt;String&gt; setOf = Sets.immutable.of("a", "b", "c");
 * </pre>
 *
 * FixedSize Examples:
 *
 * <pre>
 * FixedSizeList&lt;String&gt; emptySet = Sets.fixedSize.empty();
 * FixedSizeList&lt;String&gt; setWith = Sets.fixedSize.with("a", "b", "c");
 * FixedSizeList&lt;String&gt; setOf = Sets.fixedSize.of("a", "b", "c");
 * </pre>
 */
@SuppressWarnings("ConstantNamingConvention")
public final class Sets
{
    public static final ImmutableSetFactory immutable = new ImmutableSetFactoryImpl();
    public static final FixedSizeSetFactory fixedSize = new FixedSizeSetFactoryImpl();
    public static final MutableSetFactory mutable = new MutableSetFactoryImpl();

    private static final Predicate<Set<?>> INSTANCE_OF_SORTED_SET_PREDICATE = new Predicate<Set<?>>()
    {
        public boolean accept(Set<?> set)
        {
            return set instanceof SortedSet;
        }
    };

    private static final Predicate<Set<?>> HAS_NON_NULL_COMPARATOR = new Predicate<Set<?>>()
    {
        public boolean accept(Set<?> set)
        {
            return ((SortedSet<?>) set).comparator() != null;
        }
    };

    private Sets()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <E> MutableSet<E> union(
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return unionInto(newSet(setA, setB), setA, setB);
    }

    public static <E, R extends Set<E>> R unionInto(
            R targetSet,
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return setA.size() > setB.size() ? fillSet(targetSet, Sets.<E, R>addAllProcedure(), setA, setB)
                : fillSet(targetSet, Sets.<E, R>addAllProcedure(), setB, setA);
    }

    public static <E> MutableSet<E> unionAll(Set<? extends E>... sets)
    {
        return unionAllInto(newSet(sets), sets);
    }

    public static <E, R extends Set<E>> R unionAllInto(
            R targetSet,
            Set<? extends E>... sets)
    {
        Arrays.sort(sets, 0, sets.length, Comparators.descendingCollectionSizeComparator());
        return fillSet(targetSet, Sets.<E, R>addAllProcedure(), sets);
    }

    public static <E> MutableSet<E> intersect(
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return intersectInto(newSet(setA, setB), setA, setB);
    }

    public static <E, R extends Set<E>> R intersectInto(
            R targetSet,
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return setA.size() < setB.size() ? fillSet(targetSet, Sets.<E, R>retainAllProcedure(), setA, setB)
                : fillSet(targetSet, Sets.<E, R>retainAllProcedure(), setB, setA);
    }

    public static <E> MutableSet<E> intersectAll(Set<? extends E>... sets)
    {
        return intersectAllInto(newSet(sets), sets);
    }

    public static <E, R extends Set<E>> R intersectAllInto(
            R targetSet,
            Set<? extends E>... sets)
    {
        Arrays.sort(sets, 0, sets.length, Comparators.ascendingCollectionSizeComparator());
        return fillSet(targetSet, Sets.<E, R>retainAllProcedure(), sets);
    }

    public static <E> MutableSet<E> difference(
            Set<? extends E> minuendSet,
            Set<? extends E> subtrahendSet)
    {
        return differenceInto(newSet(minuendSet, subtrahendSet), minuendSet, subtrahendSet);
    }

    public static <E, R extends Set<E>> R differenceInto(
            R targetSet,
            Set<? extends E> minuendSet,
            Set<? extends E> subtrahendSet)
    {
        return fillSet(targetSet, Sets.<E, R>removeAllProcedure(), minuendSet, subtrahendSet);
    }

    public static <E> MutableSet<E> differenceAll(Set<? extends E>... sets)
    {
        return differenceAllInto(newSet(sets), sets);
    }

    public static <E, R extends Set<E>> R differenceAllInto(
            R targetSet,
            Set<? extends E>... sets)
    {
        return fillSet(targetSet, Sets.<E, R>removeAllProcedure(), sets);
    }

    public static <E> MutableSet<E> symmetricDifference(
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return symmetricDifferenceInto(newSet(setA, setB), setA, setB);
    }

    public static <E, R extends Set<E>> R symmetricDifferenceInto(
            R targetSet,
            Set<? extends E> setA,
            Set<? extends E> setB)
    {
        return unionInto(
                targetSet,
                differenceInto(newSet(setA, setB), setA, setB),
                differenceInto(newSet(setA, setB), setB, setA));
    }

    public static <E> boolean isSubsetOf(
            Set<? extends E> candidateSubset,
            Set<? extends E> candidateSuperset)
    {
        return candidateSubset.size() <= candidateSuperset.size()
                && candidateSuperset.containsAll(candidateSubset);
    }

    public static <E> boolean isProperSubsetOf(
            Set<? extends E> candidateSubset,
            Set<? extends E> candidateSuperset)
    {
        return candidateSubset.size() < candidateSuperset.size()
                && candidateSuperset.containsAll(candidateSubset);
    }

    private static <E> MutableSet<E> newSet(Set<? extends E>... sets)
    {
        Comparator<? super E> comparator = extractComparator(sets);
        if (comparator != null)
        {
            // TODO: this should return a SortedSetAdapter once implemented
            return SetAdapter.adapt(new TreeSet<E>(comparator));
        }
        return UnifiedSet.newSet();
    }

    private static <E> Comparator<? super E> extractComparator(Set<? extends E>... sets)
    {
        Collection<Set<? extends E>> sortedSetCollection = ArrayIterate.select(sets, INSTANCE_OF_SORTED_SET_PREDICATE);
        if (sortedSetCollection.isEmpty())
        {
            return null;
        }
        SortedSet<E> sortedSetWithComparator = (SortedSet<E>) Iterate.detect(sortedSetCollection, HAS_NON_NULL_COMPARATOR);
        if (sortedSetWithComparator != null)
        {
            return sortedSetWithComparator.comparator();
        }
        return Comparators.safeNullsLow(Comparators.naturalOrder());
    }

    private static <E, R extends Set<E>> R fillSet(
            R targetSet,
            Procedure2<Set<? extends E>, R> procedure,
            Set<? extends E>... sets)
    {
        targetSet.addAll(sets[0]);
        for (int i = 1; i < sets.length; i++)
        {
            procedure.value(sets[i], targetSet);
        }
        return targetSet;
    }

    private static <E, R extends Set<E>> Procedure2<Set<? extends E>, R> addAllProcedure()
    {
        return new Procedure2<Set<? extends E>, R>()
        {
            public void value(Set<? extends E> argumentSet, R targetSet)
            {
                targetSet.addAll(argumentSet);
            }
        };
    }

    private static <E, R extends Set<E>> Procedure2<Set<? extends E>, R> retainAllProcedure()
    {
        return new Procedure2<Set<? extends E>, R>()
        {
            public void value(Set<? extends E> argumentSet, R targetSet)
            {
                targetSet.retainAll(argumentSet);
            }
        };
    }

    private static <E, R extends Set<E>> Procedure2<Set<? extends E>, R> removeAllProcedure()
    {
        return new Procedure2<Set<? extends E>, R>()
        {
            public void value(Set<? extends E> argumentSet, R targetSet)
            {
                targetSet.removeAll(argumentSet);
            }
        };
    }

    public static <T> MutableSet<MutableSet<T>> powerSet(Set<T> set)
    {
        MutableSet<MutableSet<T>> seed = UnifiedSet.<MutableSet<T>>newSetWith(UnifiedSet.<T>newSet());
        return Iterate.injectInto(seed, set, new Function2<MutableSet<MutableSet<T>>, T, MutableSet<MutableSet<T>>>()
        {
            public MutableSet<MutableSet<T>> value(MutableSet<MutableSet<T>> accumulator, final T element)
            {
                return Sets.union(accumulator, accumulator.collect(new Function<MutableSet<T>, MutableSet<T>>()
                {
                    public MutableSet<T> valueOf(MutableSet<T> innerSet)
                    {
                        return innerSet.toSet().with(element);
                    }
                }));
            }
        });
    }

    public static <A, B> LazyIterable<Pair<A, B>> cartesianProduct(Set<A> set1, final Set<B> set2)
    {
        return LazyIterate.flatCollect(set1, new Function<A, LazyIterable<Pair<A, B>>>()
        {
            public LazyIterable<Pair<A, B>> valueOf(final A first)
            {
                return LazyIterate.collect(set2, new Function<B, Pair<A, B>>()
                {
                    public Pair<A, B> valueOf(B second)
                    {
                        return Tuples.pair(first, second);
                    }
                });
            }
        });
    }
}
