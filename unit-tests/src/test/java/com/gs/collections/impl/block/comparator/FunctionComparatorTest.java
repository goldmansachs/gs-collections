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

package com.gs.collections.impl.block.comparator;

import java.util.Comparator;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class FunctionComparatorTest
{
    private static final Band VAN_HALEN = new Band("Van Halen");
    private static final Band BON_JOVI = new Band("Bon Jovi");
    private static final Band METALLICA = new Band("Metallica");
    private static final Band SCORPIONS = new Band("Scorpions");

    private static final Band ACDC = new Band("AC/DC");
    private static final Band ZZTOP = new Band("ZZ Top");

    @Test
    public void comparator()
    {
        FunctionComparator<Band, String> comparator = new FunctionComparator<>(
                Band.TO_NAME,
                String::compareTo);

        Assert.assertEquals(comparator.compare(ACDC, ZZTOP), ACDC.getName().compareTo(ZZTOP.getName()));
        Assert.assertEquals(comparator.compare(ZZTOP, ACDC), ZZTOP.getName().compareTo(ACDC.getName()));
    }

    private MutableList<Band> createTestList()
    {
        return FastList.newListWith(VAN_HALEN, SCORPIONS, BON_JOVI, METALLICA);
    }

    @Test
    public void functionComparatorBuiltTheHardWay()
    {
        Comparator<Band> byName = (bandA, bandB) -> Band.TO_NAME.valueOf(bandA).compareTo(Band.TO_NAME.valueOf(bandB));
        MutableList<Band> sortedList = this.createTestList().sortThis(byName);
        Assert.assertEquals(FastList.newListWith(BON_JOVI, METALLICA, SCORPIONS, VAN_HALEN), sortedList);
    }

    @Test
    public void functionComparatorBuiltTheEasyWay()
    {
        Comparator<Band> byName = Comparators.byFunction(Band.TO_NAME, String::compareTo);
        MutableList<Band> sortedList = this.createTestList().sortThis(byName);
        Assert.assertEquals(FastList.newListWith(BON_JOVI, METALLICA, SCORPIONS, VAN_HALEN), sortedList);
    }

    private static final class Band
    {
        public static final Function<Band, String> TO_NAME = new Function<Band, String>()
        {
            public String valueOf(Band band)
            {
                return band.name;
            }
        };

        private final String name;

        private Band(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        @Override
        public boolean equals(Object other)
        {
            return this == other || other instanceof Band && this.name.equals(((Band) other).name);
        }

        @Override
        public int hashCode()
        {
            return this.name.hashCode();
        }
    }
}
