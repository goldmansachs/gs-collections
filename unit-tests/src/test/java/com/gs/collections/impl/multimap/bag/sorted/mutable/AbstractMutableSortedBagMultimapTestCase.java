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

package com.gs.collections.impl.multimap.bag.sorted.mutable;

import java.util.Comparator;

import com.gs.collections.api.bag.sorted.MutableSortedBag;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.BagMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.multimap.sortedbag.ImmutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.MutableSortedBagMultimap;
import com.gs.collections.api.multimap.sortedbag.SortedBagMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.sorted.mutable.TreeBag;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractMutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.multimap.list.FastListMultimap;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link TreeBagMultimap}.
 */
public abstract class AbstractMutableSortedBagMultimapTestCase extends AbstractMutableMultimapTestCase
{
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimap(Comparator<V> comparator);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimap();

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimapWithKeyValue(K key, V value);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimap(Pair<K, V>... pairs);

    @Override
    protected abstract <K, V> MutableSortedBagMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable);

    @Override
    protected abstract <V> MutableSortedBag<V> createCollection(V... args);

    @Override
    @Test
    public void flip()
    {
        SortedBagMultimap<String, Integer> multimap = this.newMultimapWithKeysValues("Less than 2", 1, "Less than 3", 1, "Less than 3", 2, "Less than 3", 2);
        BagMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Bags.immutable.with("Less than 3", "Less than 3"), flipped.get(2));
        Assert.assertEquals(Bags.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void toImmutable()
    {
        Verify.assertInstanceOf(MutableSortedBagMultimap.class, this.newMultimap());
        Verify.assertInstanceOf(ImmutableSortedBagMultimap.class, this.newMultimap().toImmutable());
        Assert.assertFalse(this.newMultimap().toImmutable() instanceof MutableSortedBagMultimap);
    }

    @Override
    @Test
    public void serialization()
    {
        MutableSortedBagMultimap<Integer, Integer> map = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        map.putAll(1, FastList.newListWith(1, 2, 3, 4));
        map.putAll(2, FastList.newListWith(2, 3, 4, 5));
        Verify.assertPostSerializedEqualsAndHashCode(map);

        MutableSortedBagMultimap<Integer, Integer> deserialized = SerializeTestHelper.serializeDeserialize(map);
        Verify.assertSortedBagsEqual(TreeBag.newBagWith(Comparators.<Integer>reverseNaturalOrder(), 1, 2, 3, 4),
                deserialized.get(1));

        deserialized.putAll(3, FastList.newListWith(8, 9, 10));
        Verify.assertListsEqual(FastList.newListWith(10, 9, 8), deserialized.get(3).toList());
    }

    @Override
    public void testClear()
    {
        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        multimap.clear();
        Verify.assertEmpty(multimap);
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        MutableSortedBagMultimap<String, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        MutableSortedBagMultimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        MutableSortedBagMultimap<String, Integer> expectedMultimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("Two", FastList.newListWith(4, 2, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedBagsEqual(expectedMultimap.get("Two"), selectedMultimap.get("Two"));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        MutableSortedBagMultimap<String, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        MutableSortedBagMultimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        MutableSortedBagMultimap<String, Integer> expectedMultimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll("One", FastList.newListWith(3, 1, 1));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertSortedBagsEqual(expectedMultimap.get("One"), rejectedMultimap.get("One"));
        Assert.assertSame(expectedMultimap.comparator(), rejectedMultimap.comparator());
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        MutableSortedBagMultimap<Integer, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll(1, FastList.newListWith(4, 3, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        MutableSortedBagMultimap<Integer, Integer> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        MutableSortedBagMultimap<Integer, Integer> expectedMultimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedBagsEqual(expectedMultimap.get(2), selectedMultimap.get(2));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        MutableSortedBagMultimap<Integer, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll(1, FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        MutableSortedBagMultimap<Integer, Integer> selectedMultimap = multimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        MutableSortedBagMultimap<Integer, Integer> expectedMultimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        expectedMultimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSortedBagsEqual(expectedMultimap.get(3), selectedMultimap.get(3));
        Assert.assertSame(expectedMultimap.comparator(), selectedMultimap.comparator());
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        MutableSortedBagMultimap<String, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        MutableBagMultimap<Integer, String> collectedMultimap1 = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("5Value", "4Value", "3Value", "2Value", "2Value"));
        Assert.assertEquals(expectedMultimap1, collectedMultimap1);

        MutableBagMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("5Value", "4Value", "3Value", "2Value", "2Value"));
        Assert.assertEquals(expectedMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        MutableSortedBagMultimap<String, Integer> multimap = this.newMultimap(Comparators.<Integer>reverseNaturalOrder());
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        MutableListMultimap<String, String> collectedMultimap = multimap.collectValues(value -> value + "Value");
        MutableListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get("1"), collectedMultimap.get("1"));
        Verify.assertListsEqual(expectedMultimap.get("2"), collectedMultimap.get("2"));
    }
}
