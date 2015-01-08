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

package com.gs.collections.impl.multimap.list;

import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.multimap.bag.UnsortedBagMultimap;
import com.gs.collections.api.multimap.list.ListMultimap;
import com.gs.collections.api.multimap.list.MutableListMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.multimap.AbstractMutableMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractMutableListMultimapTestCase extends AbstractMutableMultimapTestCase
{
    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimap();

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimapWithKeyValue(K key, V value);

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2);

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimap(Pair<K, V>... pairs);

    @Override
    public abstract <K, V> MutableListMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable);

    @Override
    protected abstract <V> MutableList<V> createCollection(V... args);

    @Override
    @Test
    public void flip()
    {
        ListMultimap<String, Integer> multimap = this.newMultimapWithKeysValues("Less than 2", 1, "Less than 3", 1, "Less than 3", 2, "Less than 3", 2);
        UnsortedBagMultimap<Integer, String> flipped = multimap.flip();
        Assert.assertEquals(Bags.immutable.with("Less than 3", "Less than 3"), flipped.get(2));
        Assert.assertEquals(Bags.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Test
    @Override
    public void testToString()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "One", 2);
        Assert.assertEquals("{One=[1, 2]}", multimap.toString());
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        super.selectKeysValues();

        MutableListMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("Two", FastList.newListWith(2, 3, 4, 5, 3, 2));
        MutableListMultimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        MutableListMultimap<String, Integer> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get("Two"), selectedMultimap.get("Two"));
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        super.rejectKeysValues();

        MutableListMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 1));
        multimap.putAll("Two", FastList.newListWith(2, 3, 4, 5));
        MutableListMultimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        MutableListMultimap<String, Integer> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3, 1));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get("One"), rejectedMultimap.get("One"));
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        super.selectKeysMultiValues();

        MutableListMultimap<Integer, String> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "3", "4"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4"));
        MutableListMultimap<Integer, String> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        MutableListMultimap<Integer, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get(2), selectedMultimap.get(2));
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        super.rejectKeysMultiValues();

        MutableListMultimap<Integer, String> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "1"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        MutableListMultimap<Integer, String> rejectedMultimap = multimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        MutableListMultimap<Integer, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get(3), rejectedMultimap.get(3));
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        MutableListMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));

        MutableBagMultimap<Integer, String> collectedMultimap1 = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap1, collectedMultimap1);

        MutableBagMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        super.collectValues();

        MutableListMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));
        MutableListMultimap<String, String> collectedMultimap = multimap.collectValues(value -> value + "Value");
        MutableListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap.putAll("2", FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);
        Verify.assertListsEqual(expectedMultimap.get("1"), collectedMultimap.get("1"));
        Verify.assertListsEqual(expectedMultimap.get("2"), collectedMultimap.get("2"));
    }
}
