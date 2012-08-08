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

package com.webguys.ponzu.impl.set.sorted.immutable;

import java.util.Comparator;

import com.webguys.ponzu.api.set.sorted.ImmutableSortedSet;
import com.webguys.ponzu.api.set.sorted.SortedSetIterable;
import com.webguys.ponzu.impl.block.factory.Comparators;
import com.webguys.ponzu.impl.factory.SortedSets;
import com.webguys.ponzu.impl.list.mutable.FastList;
import com.webguys.ponzu.impl.set.sorted.mutable.TreeSortedSet;
import com.webguys.ponzu.impl.test.Verify;
import org.junit.Test;

public class ImmutableTreeSetTest
        extends AbstractImmutableSortedSetTestCase
{
    @Override
    protected ImmutableSortedSet<Integer> classUnderTest()
    {
        return ImmutableTreeSet.newSetWith(1, 2, 3, 4);
    }

    @Override
    protected ImmutableSortedSet<Integer> classUnderTest(Comparator<? super Integer> comparator)
    {
        return ImmutableTreeSet.newSetWith(comparator, 1, 2, 3, 4);
    }

    @Test
    public void serialization()
    {
        ImmutableSortedSet<Integer> set = this.classUnderTest();
        Verify.assertPostSerializedEqualsAndHashCode(set);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void subSet()
    {
        this.classUnderTest().castToSortedSet().subSet(1, 4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void headSet()
    {
        this.classUnderTest().castToSortedSet().headSet(4);
    }

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void tailSet()
    {
        this.classUnderTest().castToSortedSet().tailSet(1);
    }

    @Override
    @Test
    public void powerSet()
    {
        ImmutableSortedSet<SortedSetIterable<Integer>> intPowerSet = SortedSets.immutable.of(1, 2, 3).powerSet();
        ImmutableSortedSet<SortedSetIterable<Integer>> revPowerSet = SortedSets.immutable.of(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3).powerSet();

        FastList<TreeSortedSet<Integer>> expectedSortedSet = FastList.newListWith(TreeSortedSet.<Integer>newSet(), TreeSortedSet.newSetWith(1), TreeSortedSet.newSetWith(2),
                TreeSortedSet.newSetWith(3), TreeSortedSet.newSetWith(1, 2), TreeSortedSet.newSetWith(1, 3), TreeSortedSet.newSetWith(2, 3), TreeSortedSet.newSetWith(1, 2, 3));
        FastList<TreeSortedSet<Integer>> expectedRevSortedSet = FastList.newListWith(TreeSortedSet.<Integer>newSet(), TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 3),
                TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 2), TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1),
                TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 2, 3), TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 3),
                TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2), TreeSortedSet.newSetWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3));

        Verify.assertListsEqual(expectedSortedSet, intPowerSet.toList());
        Verify.assertListsEqual(expectedRevSortedSet, revPowerSet.toList());
    }
}
