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

package com.gs.collections.impl.multimap;

import com.gs.collections.api.RichIterable;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.collection.MutableCollection;
import com.gs.collections.api.list.ListIterable;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.multimap.Multimap;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.SerializeTestHelper;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Helper class for testing {@link Multimap}s.
 */
public abstract class AbstractMultimapTestCase
{
    protected abstract <K, V> Multimap<K, V> newMultimap();

    protected abstract <K, V> Multimap<K, V> newMultimapWithKeyValue(
            K key, V value);

    protected abstract <K, V> Multimap<K, V> newMultimapWithKeysValues(
            K key1, V value1, K
            key2, V value2);

    protected abstract <K, V> Multimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3);

    protected abstract <K, V> Multimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4);

    protected abstract <K, V> Multimap<K, V> newMultimap(Pair<K, V>... pairs);

    protected abstract <K, V> Multimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable);

    protected abstract <V> MutableCollection<V> createCollection(V... args);

    @Test
    public void testNewMultimap()
    {
        Multimap<Integer, Integer> multimap = this.newMultimap();
        Verify.assertEmpty(multimap);
        Verify.assertSize(0, multimap);
    }

    @Test
    public void testNewMultimapWithKeyValue()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeyValue(1, "One");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(1, multimap);
        Verify.assertContainsEntry(1, "One", multimap);
    }

    @Test
    public void testNewMultimapWithWith()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "Two");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(2, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two");
    }

    @Test
    public void testNewMultimapWithWithWith()
    {
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(3, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two", 3, "Three");
    }

    @Test
    public void testNewMultimapWithWithWithWith()
    {
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        Verify.assertNotEmpty(multimap);
        Verify.assertSize(4, multimap);
        Verify.assertContainsAllEntries(multimap, 1, "One", 2, "Two", 3, "Three", 4, "Four");
    }

    @Test
    public void testNewMultimapWith()
    {
        Pair<Integer, String> pair1 = Tuples.pair(1, "One");
        Pair<Integer, String> pair2 = Tuples.pair(2, "Two");
        Pair<Integer, String> pair3 = Tuples.pair(3, "Three");
        Pair<Integer, String> pair4 = Tuples.pair(4, "Four");
        ListIterable<Pair<Integer, String>> pairs = FastList.newListWith(pair1, pair2, pair3, pair4);

        Multimap<Integer, String> expected = this.newMultimap(pair1, pair2, pair3, pair4);

        Multimap<Integer, String> actual = this.newMultimapFromPairs(pairs);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void newMultimapFromPairs()
    {
        Multimap<Integer, String> expected =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        Multimap<Integer, String> actual =
                this.newMultimap(Tuples.pair(1, "One"), Tuples.pair(2, "Two"), Tuples.pair(3, "Three"), Tuples.pair(4, "Four"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isEmpty()
    {
        Verify.assertEmpty(this.newMultimap());
        Verify.assertNotEmpty(this.newMultimapWithKeyValue(1, 1));
        Assert.assertTrue(this.newMultimapWithKeyValue(1, 1).notEmpty());
    }

    @Test
    public void forEachKeyValue()
    {
        MutableBag<String> collection = Bags.mutable.of();
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three");
        multimap.forEachKeyValue((key, value) -> collection.add(key + value));
        Assert.assertEquals(HashBag.newBagWith("1One", "2Two", "3Three"), collection);
    }

    @Test
    public void forEachKeyMultiValue()
    {
        MutableSet<Pair<Integer, Iterable<String>>> collection = UnifiedSet.newSet();
        Multimap<Integer, String> multimap =
                this.newMultimapWithKeysValues(2, "2", 2, "1", 3, "3", 3, "3");
        multimap.forEachKeyMultiValues((key, values) -> collection.add(Tuples.pair(key, values)));
        Assert.assertEquals(UnifiedSet.newSetWith(Tuples.pair(2, this.createCollection("2", "1")), Tuples.pair(3, this.createCollection("3", "3"))), collection);
    }

    @Test
    public void forEachValue()
    {
        MutableBag<String> collection = Bags.mutable.of();
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        multimap.forEachValue(CollectionAddProcedure.on(collection));
        Assert.assertEquals(HashBag.newBagWith("1", "2", "3"), collection);
    }

    @Test
    public void valuesView()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertEquals(Bags.mutable.of("1", "2", "3"), multimap.valuesView().toBag());
    }

    @Test
    public void multiValuesView()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertEquals(Bags.mutable.of("1", "2", "3"),
                multimap.multiValuesView().flatCollect(Functions.<RichIterable<String>>getPassThru()).toBag());
    }

    @Test
    public void forEachKey()
    {
        MutableList<Integer> collection = Lists.mutable.of();
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        multimap.forEachKey(CollectionAddProcedure.on(collection));
        Assert.assertEquals(FastList.newListWith(1, 2, 3), collection);
    }

    @Test
    public void notEmpty()
    {
        Assert.assertTrue(this.newMultimap().isEmpty());
        Assert.assertFalse(this.newMultimap().notEmpty());
        Assert.assertTrue(this.newMultimapWithKeysValues(1, "1", 2, "2").notEmpty());
    }

    @Test
    public void keysWithMultiValuesView()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertEquals(Bags.mutable.of(1, 2, 3),
                multimap.keyMultiValuePairsView().collect(Pair::getOne).toBag());
        Assert.assertEquals(
                Bags.mutable.of("1", "2", "3"),
                multimap.keyMultiValuePairsView().flatCollect(Functions.secondOfPair()).toBag());
    }

    @Test
    public void keyValuePairsView()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Assert.assertEquals(Bags.mutable.of(Tuples.pair(1, "1"), Tuples.pair(2, "2"), Tuples.pair(3, "3")),
                multimap.keyValuePairsView().toBag());
    }

    @Test
    public void keyBag()
    {
        Multimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "1", 2, "2", 2, "2.1");
        Assert.assertEquals(1, multimap.keyBag().occurrencesOf(1));
        Assert.assertEquals(2, multimap.keyBag().occurrencesOf(2));
    }

    @Test
    public void testEquals()
    {
        Multimap<Integer, String> map1 = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Multimap<Integer, String> map2 = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Multimap<Integer, String> map3 = this.newMultimapWithKeysValues(2, "2", 3, "3", 4, "4");
        Assert.assertEquals(map1, map2);
        Assert.assertNotEquals(map2, map3);
    }

    @Test
    public void testHashCode()
    {
        Multimap<Integer, String> map1 = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Multimap<Integer, String> map2 = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Verify.assertEqualsAndHashCode(map1, map2);
    }

    @Test
    public void serialization()
    {
        Multimap<Integer, String> original = this.newMultimapWithKeysValues(1, "1", 2, "2", 3, "3");
        Multimap<Integer, String> copy = SerializeTestHelper.serializeDeserialize(original);
        Verify.assertSize(3, copy);
        Verify.assertEqualsAndHashCode(original, copy);
    }

    @Test
    public void newEmpty()
    {
        Multimap<Object, Object> original = this.newMultimap();
        Multimap<Object, Object> newEmpty = original.newEmpty();
        Verify.assertEmpty(newEmpty);
        Assert.assertSame(original.getClass(), newEmpty.getClass());
        Verify.assertEqualsAndHashCode(original, newEmpty);
    }

    @Test
    public void keysView()
    {
        Multimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "Two", 2, "Three", 3);
        Assert.assertEquals(Bags.mutable.of("One", "Two", "Three"), multimap.keysView().toBag());
    }

    @Test
    public void sizeDistinct()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 1, "Two", 2, "Two", 3);
        Verify.assertSize(3, multimap);
        Assert.assertEquals(2, multimap.sizeDistinct());
    }

    @Test
    public void selectKeysValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 1, "One", 12, "Two", 2, "Two", 3);
        Multimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        Assert.assertEquals(this.newMultimapWithKeyValue("Two", 2), selectedMultimap);
    }

    @Test
    public void rejectKeysValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 1, "One", 12, "Two", 2, "Two", 4);
        Multimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        Assert.assertEquals(this.newMultimapWithKeyValue("One", 1), rejectedMultimap);
    }

    @Test
    public void selectKeysMultiValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 1, "One", 12, "Two", 2, "Two", 3);
        Multimap<String, Integer> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, 2));
        Assert.assertEquals(this.newMultimapWithKeysValues("Two", 2, "Two", 3), selectedMultimap);
    }

    @Test
    public void rejectKeysMultiValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 1, "One", 12, "Two", 2, "Two", 3);
        Multimap<String, Integer> rejectedMultimap = multimap.rejectKeysMultiValues((key, values) -> "Two".equals(key) && Iterate.contains(values, 2));
        Assert.assertEquals(this.newMultimapWithKeysValues("One", 1, "One", 12), rejectedMultimap);
    }

    @Test
    public void collectKeysValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("1", 1, "1", 12, "2", 2, "3", 3);
        Multimap<Integer, String> collectedMultimap = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        Multimap<Integer, String> expectedMultimap = this.newMultimapWithKeysValues(1, "1Value", 1, "12Value", 2, "2Value", 3, "3Value");
        Assert.assertEquals(expectedMultimap, collectedMultimap);
    }

    @Test
    public void collectValues()
    {
        Multimap<String, Integer> multimap = this.newMultimapWithKeysValues("1", 1, "1", 12, "2", 2, "3", 3);
        Multimap<String, String> collectedMultimap = multimap.collectValues(value -> value + "Value");
        Multimap<String, String> expectedMultimap = this.newMultimapWithKeysValues("1", "1Value", "1", "12Value", "2", "2Value", "3", "3Value");
        Assert.assertEquals(expectedMultimap, collectedMultimap);
    }

    @Test
    public abstract void flip();
}
