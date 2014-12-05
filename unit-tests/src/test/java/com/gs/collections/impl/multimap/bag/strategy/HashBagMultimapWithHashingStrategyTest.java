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

package com.gs.collections.impl.multimap.bag.strategy;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.multimap.ImmutableMultimap;
import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;
import com.gs.collections.impl.multimap.bag.AbstractMutableBagMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link HashBagMultimap}.
 */
public class HashBagMultimapWithHashingStrategyTest extends AbstractMutableBagMultimapTestCase
{
    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimap()
    {
        return HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy());
    }

    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimapWithKeyValue(K key, V value)
    {
        HashBagMultimapWithHashingStrategy<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        HashBagMultimapWithHashingStrategy<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        HashBagMultimapWithHashingStrategy<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        HashBagMultimapWithHashingStrategy<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @Override
    protected final <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy(), pairs);
    }

    @Override
    protected <K, V> HashBagMultimapWithHashingStrategy<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
    {
        return HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy(), inputIterable);
    }

    @Override
    protected final <V> MutableBag<V> createCollection(V... args)
    {
        return Bags.mutable.of(args);
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        super.selectKeysValues();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("Two", FastList.newListWith(2, 3, 4, 5, 3, 2));
        HashBagMultimapWithHashingStrategy<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        HashBagMultimapWithHashingStrategy<String, Integer> expectedMultimap = this.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4, 2));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        super.rejectKeysValues();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 2, 3, 4, 1));
        multimap.putAll("Two", FastList.newListWith(2, 3, 4, 5));
        HashBagMultimapWithHashingStrategy<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        HashBagMultimapWithHashingStrategy<String, Integer> expectedMultimap = this.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3, 1));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        super.selectKeysMultiValues();

        HashBagMultimapWithHashingStrategy<Integer, String> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "3", "4"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4"));
        HashBagMultimapWithHashingStrategy<Integer, String> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        HashBagMultimapWithHashingStrategy<Integer, String> expectedMultimap = this.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        super.rejectKeysMultiValues();

        HashBagMultimapWithHashingStrategy<Integer, String> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "1"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        HashBagMultimapWithHashingStrategy<Integer, String> rejectedMultimap = multimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        HashBagMultimapWithHashingStrategy<Integer, String> expectedMultimap = this.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        super.collectKeysValues();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));
        MutableBagMultimap<Integer, String> collectedMultimap = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap = HashBagMultimap.newMultimap();
        expectedMultimap.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);

        MutableBagMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        HashBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        super.collectValues();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));
        HashBagMultimapWithHashingStrategy<String, String> collectedMultimap = (HashBagMultimapWithHashingStrategy<String, String>) multimap.collectValues(value -> value + "Value");
        HashBagMultimapWithHashingStrategy<String, String> expectedMultimap = this.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap.putAll("2", FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);
    }

    @Override
    public void toImmutable()
    {
        super.toImmutable();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        ImmutableMultimap<String, Integer> actual = multimap.toImmutable();
        Assert.assertNotNull(actual);
        Assert.assertEquals(multimap, actual);
        // ideally this should go back to HashBagMultimapWithHashingStrategy
        Verify.assertInstanceOf(HashBagMultimap.class, actual.toMutable());
    }

    @Override
    public void toMutable()
    {
        super.toMutable();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        MutableMultimap<String, Integer> mutableCopy = multimap.toMutable();
        Assert.assertNotSame(multimap, mutableCopy);
        Assert.assertEquals(multimap, mutableCopy);
        Verify.assertInstanceOf(HashBagMultimapWithHashingStrategy.class, mutableCopy);
    }

    @Override
    public void toMap()
    {
        super.toMap();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        UnifiedMapWithHashingStrategy<String, RichIterable<Integer>> expected = UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy());
        expected.put("One", this.createCollection(1));
        expected.put("Two", this.createCollection(2, 2));
        MutableMap<String, RichIterable<Integer>> actual = multimap.toMap();
        Assert.assertEquals(expected, actual);
        Verify.assertInstanceOf(UnifiedMapWithHashingStrategy.class, actual);
    }

    @Override
    public void toMapWithTarget()
    {
        super.toMapWithTarget();

        HashBagMultimapWithHashingStrategy<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        UnifiedMapWithHashingStrategy<String, RichIterable<Integer>> expected = UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy());
        expected.put("One", UnifiedSet.newSetWith(1));
        expected.put("Two", UnifiedSet.newSetWith(2, 2));
        MutableMap<String, MutableSet<Integer>> actual = multimap.toMap(UnifiedSet::new);
        Assert.assertEquals(expected, actual);
        Verify.assertInstanceOf(UnifiedMapWithHashingStrategy.class, actual);
    }

    @Test
    public void testHashingStrategyConstructor()
    {
        HashBagMultimapWithHashingStrategy<Integer, Integer> multimapWithIdentity = HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.identityStrategy());

        multimapWithIdentity.put(new Integer(1), 1);
        multimapWithIdentity.putAll(new Integer(1), Lists.fixedSize.of(2, 20, 1));
        multimapWithIdentity.put(new Integer(1), 3);

        Assert.assertEquals(3, multimapWithIdentity.sizeDistinct());
        Verify.assertSize(5, multimapWithIdentity);

        HashBagMultimapWithHashingStrategy<Integer, Integer> multimapWithDefault = HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy(), multimapWithIdentity);

        Assert.assertEquals(1, multimapWithDefault.sizeDistinct());
        Verify.assertSize(5, multimapWithDefault);

        Verify.assertIterablesEqual(multimapWithIdentity.valuesView().toBag(), multimapWithDefault.valuesView().toBag());

        HashBagMultimapWithHashingStrategy<Integer, Integer> copyOfMultimapWithDefault = HashBagMultimapWithHashingStrategy.newMultimap(multimapWithDefault);

        Verify.assertMapsEqual(multimapWithDefault.toMap(), copyOfMultimapWithDefault.toMap());
    }

    @Test
    public void testKeyHashingStrategy()
    {
        HashBagMultimapWithHashingStrategy<Integer, Integer> multimap = HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.identityStrategy());
        Assert.assertEquals(HashingStrategies.identityStrategy(), multimap.getKeyHashingStrategy());
    }
}
