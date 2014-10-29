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

package com.gs.collections.impl.utility.internal;

import java.util.Set;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.set.ImmutableSet;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.set.SetIterable;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.set.mutable.SetAdapter;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.LazyIterate;

/**
 * Set algebra operations.
 * <p>
 * Most operations are non-destructive, i.e. no input sets are modified during execution.
 * The exception is operations ending in "Into." These accept the target collection of
 * the final calculation as the first parameter.
 */
public final class SetIterables
{
    private SetIterables()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <E> MutableSet<E> union(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB)
    {
        return SetIterables.unionInto(setA, setB, UnifiedSet.<E>newSet());
    }

    public static <E, R extends Set<E>> R unionInto(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB,
            R targetSet)
    {
        Iterate.addAllIterable(setA, targetSet);
        Iterate.addAllIterable(setB, targetSet);
        return targetSet;
    }

    public static <E> MutableSet<E> intersect(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB)
    {
        return SetIterables.intersectInto(setA, setB, UnifiedSet.<E>newSet());
    }

    public static <E, R extends Set<E>> R intersectInto(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB,
            R targetSet)
    {
        MutableSet<E> adapted = SetAdapter.adapt(targetSet);
        adapted.addAllIterable(setA);
        adapted.retainAllIterable(setB);
        return targetSet;
    }

    public static <E> MutableSet<E> difference(
            SetIterable<? extends E> minuendSet,
            SetIterable<? extends E> subtrahendSet)
    {
        return SetIterables.differenceInto(minuendSet, subtrahendSet, UnifiedSet.<E>newSet());
    }

    public static <E, R extends Set<E>> R differenceInto(
            SetIterable<? extends E> minuendSet,
            SetIterable<? extends E> subtrahendSet,
            R targetSet)
    {
        MutableSet<E> adapted = SetAdapter.adapt(targetSet);
        adapted.addAllIterable(minuendSet);
        adapted.removeAllIterable(subtrahendSet);
        return targetSet;
    }

    public static <E> MutableSet<E> symmetricDifference(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB)
    {
        return SetIterables.symmetricDifferenceInto(setA, setB, UnifiedSet.<E>newSet());
    }

    public static <E, R extends Set<E>> R symmetricDifferenceInto(
            SetIterable<? extends E> setA,
            SetIterable<? extends E> setB,
            R targetSet)
    {
        return SetIterables.unionInto(
                SetIterables.difference(setA, setB),
                SetIterables.difference(setB, setA),
                targetSet);
    }

    public static <E> boolean isSubsetOf(
            SetIterable<? extends E> candidateSubset,
            SetIterable<? extends E> candidateSuperset)
    {
        return candidateSubset.size() <= candidateSuperset.size()
                && candidateSuperset.containsAllIterable(candidateSubset);
    }

    public static <E> boolean isProperSubsetOf(
            SetIterable<? extends E> candidateSubset,
            SetIterable<? extends E> candidateSuperset)
    {
        return candidateSubset.size() < candidateSuperset.size()
                && candidateSuperset.containsAllIterable(candidateSubset);
    }

    public static <T> MutableSet<MutableSet<T>> powerSet(Set<T> set)
    {
        MutableSet<MutableSet<T>> seed = UnifiedSet.<MutableSet<T>>newSetWith(UnifiedSet.<T>newSet());
        return powerSetWithSeed(set, seed);
    }

    public static <T> MutableSet<MutableSet<T>> powerSet(UnifiedSetWithHashingStrategy<T> set)
    {
        MutableSet<MutableSet<T>> seed = UnifiedSet.<MutableSet<T>>newSetWith(set.newEmpty());
        return powerSetWithSeed(set, seed);
    }

    private static <T> MutableSet<MutableSet<T>> powerSetWithSeed(Set<T> set, MutableSet<MutableSet<T>> seed)
    {
        return Iterate.injectInto(seed, set, new Function2<MutableSet<MutableSet<T>>, T, MutableSet<MutableSet<T>>>()
        {
            public MutableSet<MutableSet<T>> value(MutableSet<MutableSet<T>> accumulator, final T element)
            {
                return SetIterables.union(accumulator, accumulator.collect(new Function<MutableSet<T>, MutableSet<T>>()
                {
                    public MutableSet<T> valueOf(MutableSet<T> innerSet)
                    {
                        return innerSet.clone().with(element);
                    }
                }));
            }
        });
    }

    /**
     * Returns an Immutable version of powerset where the inner sets are also immutable.
     */
    public static <T> ImmutableSet<ImmutableSet<T>> immutablePowerSet(Set<T> set)
    {
        return powerSet(set).collect(new Function<MutableSet<T>, ImmutableSet<T>>()
        {
            public ImmutableSet<T> valueOf(MutableSet<T> set)
            {
                return set.toImmutable();
            }
        }).toImmutable();
    }

    public static <A, B> LazyIterable<Pair<A, B>> cartesianProduct(SetIterable<A> set1, final SetIterable<B> set2)
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
