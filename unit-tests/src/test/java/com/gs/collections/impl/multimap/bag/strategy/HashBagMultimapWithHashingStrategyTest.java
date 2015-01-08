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
import com.gs.collections.impl.map.strategy.mutable.UnifiedMapWithHashingStrategy;
import com.gs.collections.impl.multimap.bag.AbstractMutableBagMultimapTestCase;
import com.gs.collections.impl.multimap.bag.HashBagMultimap;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link HashBagMultimap}.
 */
public class HashBagMultimapWithHashingStrategyTest extends AbstractMutableBagMultimapTestCase
{
    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimap()
    {
        return HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy());
    }

    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        MutableBagMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        MutableBagMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        MutableBagMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        MutableBagMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @Override
    protected final <K, V> MutableBagMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return HashBagMultimapWithHashingStrategy.newMultimap(HashingStrategies.defaultStrategy(), pairs);
    }

    @Override
    protected <K, V> MutableBagMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
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
    public void toImmutable()
    {
        super.toImmutable();

        MutableBagMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        ImmutableMultimap<String, Integer> actual = multimap.toImmutable();
        Assert.assertNotNull(actual);
        Assert.assertEquals(multimap, actual);
        // ideally this should go back to HashBagMultimapWithHashingStrategy
        Verify.assertInstanceOf(HashBagMultimap.class, actual.toMutable());
    }

    @Override
    @Test
    public void toMutable()
    {
        super.toMutable();

        MutableBagMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        MutableMultimap<String, Integer> mutableCopy = multimap.toMutable();
        Assert.assertNotSame(multimap, mutableCopy);
        Assert.assertEquals(multimap, mutableCopy);
        Verify.assertInstanceOf(HashBagMultimapWithHashingStrategy.class, mutableCopy);
    }

    @Override
    @Test
    public void toMap()
    {
        super.toMap();

        MutableBagMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 2);
        UnifiedMapWithHashingStrategy<String, RichIterable<Integer>> expected = UnifiedMapWithHashingStrategy.newMap(HashingStrategies.defaultStrategy());
        expected.put("One", this.createCollection(1));
        expected.put("Two", this.createCollection(2, 2));
        MutableMap<String, RichIterable<Integer>> actual = multimap.toMap();
        Assert.assertEquals(expected, actual);
        Verify.assertInstanceOf(UnifiedMapWithHashingStrategy.class, actual);
    }

    @Override
    @Test
    public void toMapWithTarget()
    {
        super.toMapWithTarget();

        MutableBagMultimap<String, Integer> multimap =
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
