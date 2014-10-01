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

package com.gs.collections.impl.multimap.set;

import com.gs.collections.api.multimap.MutableMultimap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.Iterate;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link SynchronizedPutUnifiedSetMultimap}.
 */
public class SynchronizedPutUnifiedSetMultimapTest extends AbstractMutableSetMultimapTestCase
{
    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap()
    {
        return SynchronizedPutUnifiedSetMultimap.newMultimap();
    }

    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        SynchronizedPutUnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        SynchronizedPutUnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        SynchronizedPutUnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        SynchronizedPutUnifiedSetMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @SafeVarargs
    @Override
    protected final <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return SynchronizedPutUnifiedSetMultimap.newMultimap(pairs);
    }

    @Override
    protected <K, V> SynchronizedPutUnifiedSetMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
    {
        return SynchronizedPutUnifiedSetMultimap.newMultimap(inputIterable);
    }

    @SafeVarargs
    @Override
    protected final <V> UnifiedSet<V> createCollection(V... args)
    {
        return UnifiedSet.newSetWith(args);
    }

    @Test
    @Override
    public void testClear()
    {
        MutableMultimap<Integer, Object> multimap =
                this.<Integer, Object>newMultimapWithKeysValues(1, "One", 2, "Two", 3, "Three", 4, "Four");
        multimap.clear();
        Verify.assertEmpty(multimap);
    }

    @Test
    @Override
    public void testToString()
    {
        MutableMultimap<String, Integer> multimap =
                this.newMultimapWithKeysValues("One", 1, "One", 2);
        String toString = multimap.toString();
        Assert.assertTrue("{One=[1, 2]}".equals(toString) || "{One=[2, 1]}".equals(toString));
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        SynchronizedPutUnifiedSetMultimap<String, Integer> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        multimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        UnifiedSetMultimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> ("Two".equals(key) && (value % 2 == 0)));
        UnifiedSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(2, 4));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSetsEqual(expectedMultimap.get("Two"), selectedMultimap.get("Two"));
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        SynchronizedPutUnifiedSetMultimap<String, Integer> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll("One", FastList.newListWith(1, 1, 2, 3, 4));
        multimap.putAll("Two", FastList.newListWith(2, 2, 3, 4, 5));
        UnifiedSetMultimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> ("Two".equals(key) || (value % 2 == 0)));
        UnifiedSetMultimap<String, Integer> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(1, 3));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertSetsEqual(expectedMultimap.get("One"), rejectedMultimap.get("One"));
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        SynchronizedPutUnifiedSetMultimap<Integer, String> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "3", "4"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "5", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4"));
        UnifiedSetMultimap<Integer, String> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> (key % 2 == 0 && Iterate.sizeOf(values) > 3));
        UnifiedSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "2"));
        Assert.assertEquals(expectedMultimap, selectedMultimap);
        Verify.assertSetsEqual(expectedMultimap.get(2), selectedMultimap.get(2));
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        SynchronizedPutUnifiedSetMultimap<Integer, String> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll(1, FastList.newListWith("1", "2", "3", "4", "5", "1"));
        multimap.putAll(2, FastList.newListWith("2", "3", "4", "5", "1"));
        multimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        multimap.putAll(4, FastList.newListWith("1", "3", "4", "5"));
        UnifiedSetMultimap<Integer, String> rejectedMultimap = multimap.rejectKeysMultiValues((key, values) -> (key % 2 == 0 || Iterate.sizeOf(values) > 4));
        UnifiedSetMultimap<Integer, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("2", "3", "4", "2"));
        Assert.assertEquals(expectedMultimap, rejectedMultimap);
        Verify.assertSetsEqual(expectedMultimap.get(3), rejectedMultimap.get(3));
        Verify.assertSetsEqual(expectedMultimap.get(3), rejectedMultimap.get(3));
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        SynchronizedPutUnifiedSetMultimap<String, Integer> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));
        UnifiedSetMultimap<Integer, String> collectedMultimap1 = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value.toString() + "Value"));
        UnifiedSetMultimap<Integer, String> expectedMultimap1 = UnifiedSetMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap1, collectedMultimap1);
        Verify.assertSetsEqual(expectedMultimap1.get(1), collectedMultimap1.get(1));
        Verify.assertSetsEqual(expectedMultimap1.get(2), collectedMultimap1.get(2));

        UnifiedSetMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value.toString() + "Value"));
        UnifiedSetMultimap<Integer, String> expectedMultimap2 = UnifiedSetMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap2, collectedMultimap2);
        Verify.assertSetsEqual(expectedMultimap2.get(1), collectedMultimap2.get(1));
    }

    @Override
    @Test
    public void collectValues()
    {
        SynchronizedPutUnifiedSetMultimap<String, Integer> multimap = SynchronizedPutUnifiedSetMultimap.newMultimap();
        multimap.putAll("1", FastList.newListWith(1, 2, 3, 4, 4));
        multimap.putAll("2", FastList.newListWith(2, 3, 4, 5, 3, 2));
        UnifiedSetMultimap<String, String> collectedMultimap = multimap.collectValues(value -> value.toString() + "Value");
        UnifiedSetMultimap<String, String> expectedMultimap = UnifiedSetMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("1Value", "2Value", "3Value", "4Value", "4Value"));
        expectedMultimap.putAll("2", FastList.newListWith("2Value", "3Value", "4Value", "5Value", "3Value", "2Value"));
        Assert.assertEquals(expectedMultimap, collectedMultimap);
        Verify.assertSetsEqual(expectedMultimap.get("1"), collectedMultimap.get("1"));
        Verify.assertSetsEqual(expectedMultimap.get("2"), collectedMultimap.get("2"));
    }
}
