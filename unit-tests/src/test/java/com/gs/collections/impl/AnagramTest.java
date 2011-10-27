/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.block.function.primitive.IntegerFunctionImpl;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.internal.IteratorIterate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnagramTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AnagramTest.class);

    private static final Procedure<String> LOGGING_PROCEDURE = new Procedure<String>()
    {
        public void value(String each)
        {
            LOGGER.info(each);
        }
    };

    private static final int SIZE_THRESHOLD = 10;

    private static final Function<RichIterable<String>, String> ITERABLE_TO_FORMATTED_STRING =
            new ListToStringFunction<String>();

    private static final Function<RichIterable<String>, Integer> ITERABLE_SIZE_FUNCTION = new ListSizeFunction<String>();

    private static final Comparator<RichIterable<String>> ASCENDING_ITERABLE_SIZE = Comparators.byFunction(ITERABLE_SIZE_FUNCTION);
    private static final Comparator<RichIterable<String>> DESCENDING_ITERABLE_SIZE = Collections.reverseOrder(ASCENDING_ITERABLE_SIZE);

    private static final Predicate<RichIterable<String>> ITERABLE_SIZE_AT_THRESHOLD =
            Predicates.attributeGreaterThanOrEqualTo(ITERABLE_SIZE_FUNCTION, SIZE_THRESHOLD);

    private static final Procedure<RichIterable<String>> OUTPUT_FORMATTED_ITERABLE =
            Functions.bind(LOGGING_PROCEDURE, ITERABLE_TO_FORMATTED_STRING);

    private static final Function<String, String> ALPHAGRAM_FUNCTION = new AlphagramFunction();

    private Iterator<String> getWords()
    {
        return FastList.newListWith(
                "alerts", "alters", "artels", "estral", "laster", "ratels", "salter", "slater", "staler", "stelar", "talers",
                "least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla"
        ).iterator();
    }

    @Test
    public void anagramsWithMultimapInlined()
    {
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION, FastListMultimap.<String, String>newMultimap())
                .multiValuesView()
                .select(ITERABLE_SIZE_AT_THRESHOLD)
                .toSortedList(DESCENDING_ITERABLE_SIZE)
                .asLazy()
                .collect(ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
    }

    @Test
    public void anagramsWithMultimapGSCollections1()
    {
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .select(ITERABLE_SIZE_AT_THRESHOLD)
                .toSortedList(DESCENDING_ITERABLE_SIZE)
                .collect(ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
    }

    private boolean listContainsTestGroupAtElementsOneOrTwo(MutableList<MutableList<String>> list)
    {
        return list.get(1).containsAll(this.getTestAnagramGroup())
                || list.get(2).containsAll(this.getTestAnagramGroup());
    }

    @Test
    public void anagramsWithMultiMapGSCollections3()
    {
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE)
                .collectIf(ITERABLE_SIZE_AT_THRESHOLD, ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
    }

    @Test
    public void anagramsWithMultimapGSCollections4()
    {
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE)
                .forEach(Procedures.ifTrue(ITERABLE_SIZE_AT_THRESHOLD, OUTPUT_FORMATTED_ITERABLE));
    }

    @Test
    public void anagramsWithMultiMapLazyIterable1()
    {
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION)
                .multiValuesView()
                .toSortedList(DESCENDING_ITERABLE_SIZE)
                .asLazy()
                .collectIf(ITERABLE_SIZE_AT_THRESHOLD, ITERABLE_TO_FORMATTED_STRING)
                .forEach(LOGGING_PROCEDURE);
    }

    @Test
    public void anagramsWithMultimapForEachMultiValue()
    {
        MutableList<RichIterable<String>> results = Lists.mutable.of();
        IteratorIterate.groupBy(this.getWords(), ALPHAGRAM_FUNCTION)
                .multiValuesView().forEach(Procedures.ifTrue(ITERABLE_SIZE_AT_THRESHOLD, CollectionAddProcedure.on(results)));
        results.sortThis(DESCENDING_ITERABLE_SIZE).forEach(OUTPUT_FORMATTED_ITERABLE);
    }

    /**
     * This code is from Joshua Bloch's tutorial on the Java Collection Framework but
     * updated to use GS Collections Containers
     */
    @Test
    public void anagramsWithJCFTutorialApproachAndGSCollectionsContainers()
    {
        // Read words from file and put into simulated multimap
        MutableMap<String, MutableList<String>> map = UnifiedMap.newMap();
        Iterator<String> iterator = this.getWords();
        while (iterator.hasNext())
        {
            String word = iterator.next();
            String alpha = alphabetize(word);
            MutableList<String> strings = map.get(alpha);
            if (strings == null)
            {
                strings = Lists.mutable.of();
                map.put(alpha, strings);
            }
            strings.add(word);
        }

        // Make a List of all permutation groups above size threshold
        MutableList<MutableList<String>> winners = Lists.mutable.of();
        for (MutableList<String> list : map.values())
        {
            if (list.size() >= SIZE_THRESHOLD)
            {
                winners.add(list);
            }
        }

        // Sort permutation groups according to size
        Collections.sort(winners, new Comparator<List<String>>()
        {
            public int compare(List<String> o1, List<String> o2)
            {
                return o2.size() - o1.size();
            }
        });

        // Print permutation groups
        for (MutableList<String> winner : winners)
        {
            LOGGER.info(winner.size() + ": " + winner);
        }

        Assert.assertTrue(this.listContainsTestGroupAtElementsOneOrTwo(winners));
        Verify.assertSize(SIZE_THRESHOLD, Iterate.getLast(winners));
    }

    private MutableList<String> getTestAnagramGroup()
    {
        return FastList.newListWith("least", "setal", "slate", "stale", "steal", "stela", "taels", "tales", "teals", "tesla");
    }

    private static String alphabetize(String s)
    {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }

    private static class ListToStringFunction<T> implements Function<RichIterable<T>, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(RichIterable<T> list)
        {
            return list.size() + ": " + list;
        }
    }

    private static class ListSizeFunction<T> extends IntegerFunctionImpl<RichIterable<T>>
    {
        private static final long serialVersionUID = 1L;

        public int intValueOf(RichIterable<T> list)
        {
            return list.size();
        }
    }

    private static class AlphagramFunction implements Function<String, String>
    {
        private static final long serialVersionUID = 1L;

        public String valueOf(String string)
        {
            char[] chars = string.toCharArray();
            Arrays.sort(chars);
            return String.valueOf(chars);
        }

        @Override
        public String toString()
        {
            return "alphagram";
        }
    }
}
