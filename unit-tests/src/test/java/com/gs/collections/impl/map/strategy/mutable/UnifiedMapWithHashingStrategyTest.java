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

package com.gs.collections.impl.map.strategy.mutable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.block.function.PassThruFunction0;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.UnifiedMapTestCase;
import com.gs.collections.impl.math.IntegerSum;
import com.gs.collections.impl.math.Sum;
import com.gs.collections.impl.math.SumProcedure;
import com.gs.collections.impl.merge.Person;
import com.gs.collections.impl.parallel.BatchIterable;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.tuple.ImmutableEntry;
import com.gs.collections.impl.tuple.Tuples;
import org.junit.Assert;
import org.junit.Test;

public class UnifiedMapWithHashingStrategyTest extends UnifiedMapTestCase
{
    //Not using the static factor method in order to have concrete types for test cases
    private static final HashingStrategy<Integer> INTEGER_HASHING_STRATEGY = HashingStrategies.nullSafeHashingStrategy(new HashingStrategy<Integer>()
    {
        public int computeHashCode(Integer object)
        {
            return object.hashCode();
        }

        public boolean equals(Integer object1, Integer object2)
        {
            return object1.equals(object2);
        }
    });
    private static final HashingStrategy<String> STRING_HASHING_STRATEGY = HashingStrategies.nullSafeHashingStrategy(new HashingStrategy<String>()
    {
        public int computeHashCode(String object)
        {
            return object.hashCode();
        }

        public boolean equals(String object1, String object2)
        {
            return object1.equals(object2);
        }
    });

    private static final HashingStrategy<Person> FIRST_NAME_HASHING_STRATEGY = HashingStrategies.fromFunction(Person.TO_FIRST);
    private static final HashingStrategy<Person> LAST_NAME_HASHING_STRATEGY = HashingStrategies.fromFunction(Person.TO_LAST);

    private static final Person JOHNSMITH = new Person("John", "Smith");
    private static final Person JANESMITH = new Person("Jane", "Smith");
    private static final Person JOHNDOE = new Person("John", "Doe");
    private static final Person JANEDOE = new Person("Jane", "Doe");

    @Override
    public <K, V> MutableMap<K, V> newMap()
    {
        return UnifiedMapWithHashingStrategy.newMap(
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy()));
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeyValue(K key, V value)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy()), key, value);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy()), key1, value1, key2, value2);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(
            K key1, V value1, K key2, V value2, K key3,
            V value3)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy()), key1, value1, key2, value2, key3, value3);
    }

    @Override
    public <K, V> MutableMap<K, V> newMapWithKeysValues(
            K key1, V value1, K key2, V value2, K key3,
            V value3, K key4, V value4)
    {
        return UnifiedMapWithHashingStrategy.newWithKeysValues(
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<K>defaultStrategy()), key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Test
    public void constructorOfPairs()
    {
        Assert.assertEquals(
                UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, 1, "one", 2, "two", 3, "three"),
                UnifiedMapWithHashingStrategy.newMapWith(INTEGER_HASHING_STRATEGY, Tuples.pair(1, "one"), Tuples.pair(2, "two"), Tuples.pair(3, "three")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void newMap_throws()
    {
        UnifiedMapWithHashingStrategy.newMap(INTEGER_HASHING_STRATEGY, -1);
    }

    @Override
    @Test
    public void select()
    {
        super.select();

        UnifiedMapWithHashingStrategy<Person, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4);
        Assert.assertEquals(UnifiedMap.newWithKeysValues(JOHNDOE, 2), map.select(new Predicate2<Person, Integer>()
        {
            public boolean accept(Person argument1, Integer argument2)
            {
                return "Doe".equals(argument1.getLastName());
            }
        }));
    }

    @Override
    @Test
    public void reject()
    {
        super.reject();

        UnifiedMapWithHashingStrategy<Person, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4);
        Assert.assertEquals(UnifiedMap.newWithKeysValues(JOHNDOE, 2), map.reject(new Predicate2<Person, Integer>()
        {
            public boolean accept(Person argument1, Integer argument2)
            {
                return "Smith".equals(argument1.getLastName());
            }
        }));
    }

    @Override
    @Test
    public void collect()
    {
        super.collect();

        UnifiedMapWithHashingStrategy<Person, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JOHNSMITH, 2, JANEDOE, 3, JANESMITH, 4);
        MutableMap<Integer, Person> collect = map.collect(new Function2<Person, Integer, Pair<Integer, Person>>()
        {
            public Pair<Integer, Person> value(Person argument1, Integer argument2)
            {
                return Tuples.pair(argument2, argument1);
            }
        });
        Verify.assertSetsEqual(UnifiedSet.newSetWith(3, 4), collect.keySet());
        Verify.assertContainsAll(collect.values(), JOHNDOE, JOHNSMITH);
    }

    @Test
    public void contains_with_hashing_strategy()
    {
        UnifiedMapWithHashingStrategy<Person, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4);
        Assert.assertTrue(map.containsKey(JOHNDOE));
        Assert.assertTrue(map.containsValue(2));
        Assert.assertTrue(map.containsKey(JOHNSMITH));
        Assert.assertTrue(map.containsValue(4));
        Assert.assertTrue(map.containsKey(JANEDOE));
        Assert.assertTrue(map.containsKey(JANESMITH));

        Assert.assertFalse(map.containsValue(1));
        Assert.assertFalse(map.containsValue(3));
    }

    @Test
    public void remove_with_hashing_strategy()
    {
        UnifiedMapWithHashingStrategy<Person, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4);

        //Testing removing people
        Assert.assertEquals(2, map.remove(JANEDOE).intValue());
        Assert.assertEquals(4, map.remove(JOHNSMITH).intValue());

        Verify.assertEmpty(map);

        //Testing removing from a chain
        UnifiedMapWithHashingStrategy<Integer, Integer> map2 =
                UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, COLLISION_4, 4);
        Assert.assertEquals(4, map2.remove(COLLISION_4).intValue());
        Assert.assertEquals(1, map2.remove(COLLISION_1).intValue());
        Verify.assertSize(2, map2);

        //Testing removing null from a chain
        UnifiedMapWithHashingStrategy<Integer, Integer> map3 =
                UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, COLLISION_1, 1, null, 2, 3, 3, 4, null);
        Assert.assertEquals(2, map3.remove(null).intValue());
        Verify.assertSize(3, map3);
        Assert.assertNull(map3.remove(4));
        Verify.assertSize(2, map3);
    }

    @Test
    public void keySet_isEmpty()
    {
        Set<Integer> keySet = UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, 1, 1, 2, 2).keySet();
        Assert.assertFalse(keySet.isEmpty());
        keySet.clear();
        Verify.assertEmpty(keySet);
    }

    @Test
    public void keySet_with_hashing_strategy()
    {
        Set<Person> people = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4).keySet();

        Verify.assertSize(2, people);

        Verify.assertContains(JANEDOE, people);
        Verify.assertContains(JOHNDOE, people);
        Verify.assertContains(JANESMITH, people);
        Verify.assertContains(JOHNSMITH, people);
    }

    @Test
    public void keySet_Iterator_removeFromNonChain()
    {
        Set<Integer> keys = UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, 1, 1, 2, 2, 3, 3, 4, 4).keySet();
        Iterator<Integer> keysIterator = keys.iterator();
        keysIterator.next();
        keysIterator.remove();
        Verify.assertSetsEqual(UnifiedSet.newSetWith(2, 3, 4), keys);
    }

    @Test
    public void weakEntryToString()
    {
        Iterator<Map.Entry<Integer, Integer>> iterator = UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, 1, 1).entrySet().iterator();
        Map.Entry<Integer, Integer> element = iterator.next();
        Assert.assertEquals("1=1", element.toString());
    }

    @Override
    @Test
    public void valuesCollection_Iterator_remove()
    {
        // a map with a chain, remove one
        UnifiedMapWithHashingStrategy<Integer, Integer> map = this.mapWithCollisionsOfSize(3);
        Iterator<Integer> iterator = map.iterator();
        iterator.next();
        iterator.remove();
        Verify.assertSize(2, map);

        // remove all values in chain
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();
        Verify.assertEmpty(map);
    }

    @Test
    public void entry_equals_with_hashingStrategy()
    {
        UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                INTEGER_HASHING_STRATEGY, 1, 1, 2, 2, 3, 3, 4, 4);
        Iterator<Map.Entry<Integer, Integer>> entryIterator = map.entrySet().iterator();
        Map.Entry<Integer, Integer> entry = entryIterator.next();
        ImmutableEntry<Integer, Integer> immutableEntry = ImmutableEntry.of(entry.getKey(), entry.getValue());
        Verify.assertEqualsAndHashCode(immutableEntry, entry);
    }

    @Test
    public void entrySet_with_hashing_strategy()
    {
        Set<Map.Entry<Person, Integer>> entries = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4).entrySet();

        Verify.assertSize(2, entries);

        Verify.assertContains(ImmutableEntry.of(JOHNDOE, 2), entries);
        Verify.assertContains(ImmutableEntry.of(JANEDOE, 2), entries);
        Verify.assertContains(ImmutableEntry.of(JOHNSMITH, 4), entries);
        Verify.assertContains(ImmutableEntry.of(JANESMITH, 4), entries);
        Verify.assertNotContains(ImmutableEntry.of(JOHNDOE, 1), entries);
        Verify.assertNotContains(ImmutableEntry.of(JANESMITH, 3), entries);

        Assert.assertTrue(entries.remove(ImmutableEntry.of(JANESMITH, 4)));
        Verify.assertNotContains(ImmutableEntry.of(JOHNSMITH, 4), entries);

        Assert.assertTrue(entries.remove(ImmutableEntry.of(JOHNDOE, 2)));
        Verify.assertEmpty(entries);
    }

    @Test
    public void valuesCollection_containsAll()
    {
        Collection<Integer> values = this.newMapWithKeysValues(1, 1, 2, 2, 3, 3, 4, 4).values();
        Assert.assertTrue(values.containsAll(FastList.newListWith(1, 2)));
        Assert.assertTrue(values.containsAll(FastList.newListWith(1, 2, 3, 4)));
        Assert.assertFalse(values.containsAll(FastList.newListWith(1, 2, 3, 4, 5)));
    }

    @Test
    public void batchForEach()
    {
        UnifiedMapWithHashingStrategy<String, Integer> map = UnifiedMapWithHashingStrategy.<String, Integer>newMap(
                STRING_HASHING_STRATEGY, 5).withKeysValues("1", 1, "2", 2, "3", 3, "4", 4);
        this.batchForEachTestCases(map, 10);

        UnifiedMapWithHashingStrategy<Integer, Integer> collisions = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 5).withKeysValues(COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, 1, 4).withKeysValues(2, 5, 3, 6);
        this.batchForEachChains(collisions, 21);

        UnifiedMapWithHashingStrategy<Integer, Integer> nulls = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 100).withKeysValues(null, 10, 1, null, 2, 11, 3, 12).withKeysValues(4, null, 5, null);
        this.batchForEachNullHandling(nulls, 36);

        this.batchForEachEmptyBatchIterable(UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(INTEGER_HASHING_STRATEGY));
    }

    @Test
    public void batchForEachKey()
    {
        Set<Integer> keys = UnifiedMapWithHashingStrategy.<Integer, String>newMap(
                INTEGER_HASHING_STRATEGY, 5).withKeysValues(1, "1", 2, "2", 3, "3", 4, "4").keySet();
        this.batchForEachTestCases((BatchIterable<Integer>) keys, 10);

        Set<Integer> collisions = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 5).withKeysValues(COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, 1, 4).withKeysValues(2, 5, 3, 6).keySet();
        this.batchForEachChains((BatchIterable<Integer>) collisions, 57);

        Set<Integer> nulls = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 100).withKeysValues(null, 10, 1, null, 2, 11, 3, 12).withKeysValues(4, null, 5, null).keySet();
        this.batchForEachNullHandling((BatchIterable<Integer>) nulls, 16);

        this.batchForEachEmptyBatchIterable((BatchIterable<Integer>) UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(INTEGER_HASHING_STRATEGY).keySet());
    }

    @Test
    public void batchForEachValue()
    {
        Collection<Integer> values = UnifiedMapWithHashingStrategy.<String, Integer>newMap(
                STRING_HASHING_STRATEGY, 5).withKeysValues("1", 1, "2", 2, "3", 3, "4", 4).values();
        this.batchForEachTestCases((BatchIterable<Integer>) values, 10);

        Collection<Integer> collisions = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 5).withKeysValues(COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, 1, 4).withKeysValues(2, 5, 3, 6).values();
        this.batchForEachChains((BatchIterable<Integer>) collisions, 21);

        Collection<Integer> nulls = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 100).withKeysValues(null, 10, 1, null, 2, 11, 3, 12).withKeysValues(4, null, 5, null).values();
        this.batchForEachNullHandling((BatchIterable<Integer>) nulls, 36);

        this.batchForEachEmptyBatchIterable(
                (BatchIterable<Integer>) UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(INTEGER_HASHING_STRATEGY).values());
    }

    @Test
    public void batchForEachEntry()
    {
        //Testing batch size of 1 to 16 with no chains
        BatchIterable<Map.Entry<Integer, Integer>> entries =
                (BatchIterable<Map.Entry<Integer, Integer>>) UnifiedMapWithHashingStrategy.newWithKeysValues(
                        INTEGER_HASHING_STRATEGY, 1, 1, 2, 2, 3, 3, 4, 4).entrySet();
        for (int sectionCount = 1; sectionCount <= 16; ++sectionCount)
        {
            Sum sum = new IntegerSum(0);
            for (int sectionIndex = 0; sectionIndex < sectionCount; ++sectionIndex)
            {
                entries.batchForEach(new EntrySumProcedure(sum), sectionIndex, sectionCount);
            }
            Assert.assertEquals(20, sum.getValue());
        }
    }

    @Test
    public void batchForEachEntry_chains()
    {
        BatchIterable<Map.Entry<Integer, Integer>> collisions =
                (BatchIterable<Map.Entry<Integer, Integer>>) UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                        INTEGER_HASHING_STRATEGY, 5).withKeysValues(
                        COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, 1, 4).withKeysValues(2, 5, 3, 6).entrySet();
        //Testing 1 batch with chains
        Sum sum2 = new IntegerSum(0);
        //testing getBatchCount returns 1
        int batchCount = collisions.getBatchCount(100000);
        for (int i = 0; i < batchCount; ++i)
        {
            collisions.batchForEach(new EntrySumProcedure(sum2), i, batchCount);
        }
        Assert.assertEquals(1, batchCount);
        Assert.assertEquals(78, sum2.getValue());

        //Testing 3 batches with chains and uneven last batch
        Sum sum3 = new IntegerSum(0);
        for (int i = 0; i < 5; ++i)
        {
            collisions.batchForEach(new EntrySumProcedure(sum3), i, 5);
        }
        Assert.assertEquals(78, sum3.getValue());
    }

    @Test
    public void batchForEachEntry_null_handling()
    {
        //Testing batchForEach handling null keys and null values
        final Sum sum4 = new IntegerSum(0);
        BatchIterable<Map.Entry<Integer, Integer>> nulls =
                (BatchIterable<Map.Entry<Integer, Integer>>) UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                        INTEGER_HASHING_STRATEGY, 100).withKeysValues(
                        null, 10, 1, null, 2, 11, 3, 12).withKeysValues(4, null, 5, null).entrySet();
        int numBatches = nulls.getBatchCount(7);
        for (int i = 0; i < numBatches; ++i)
        {
            nulls.batchForEach(new Procedure<Map.Entry<Integer, Integer>>()
            {
                public void value(Map.Entry<Integer, Integer> each)
                {
                    sum4.add(each.getKey() == null ? 1 : each.getKey());
                    sum4.add(each.getValue() == null ? 1 : each.getValue());
                }
            }, i, numBatches);
        }
        Assert.assertEquals(52, sum4.getValue());
    }

    @Test
    public void batchForEachEntry_emptySet()
    {
        //Test batchForEach on empty set, it should simply do nothing and not throw any exceptions
        Sum sum5 = new IntegerSum(0);
        BatchIterable<Map.Entry<Integer, Integer>> empty =
                (BatchIterable<Map.Entry<Integer, Integer>>) UnifiedMapWithHashingStrategy.newMap(INTEGER_HASHING_STRATEGY).entrySet();
        empty.batchForEach(new EntrySumProcedure(sum5), 0, empty.getBatchCount(1));
        Assert.assertEquals(0, sum5.getValue());
    }

    private void batchForEachTestCases(BatchIterable<Integer> batchIterable, int expectedValue)
    {
        //Testing batch size of 1 to 16 with no chains
        for (int sectionCount = 1; sectionCount <= 16; ++sectionCount)
        {
            Sum sum = new IntegerSum(0);
            for (int sectionIndex = 0; sectionIndex < sectionCount; ++sectionIndex)
            {
                batchIterable.batchForEach(new SumProcedure<Integer>(sum), sectionIndex, sectionCount);
            }
            Assert.assertEquals(expectedValue, sum.getValue());
        }
    }

    private void batchForEachChains(BatchIterable<Integer> batchIterable, int expectedValue)
    {
        //Testing 1 batch with chains
        Sum sum = new IntegerSum(0);
        //testing getBatchCount returns 1
        int numBatches = batchIterable.getBatchCount(100000);
        for (int i = 0; i < numBatches; ++i)
        {
            batchIterable.batchForEach(new SumProcedure<Integer>(sum), i, numBatches);
        }
        Assert.assertEquals(1, numBatches);
        Assert.assertEquals(expectedValue, sum.getValue());

        //Testing 3 batches with chains and uneven last batch
        Sum sum2 = new IntegerSum(0);
        for (int i = 0; i < 5; ++i)
        {
            batchIterable.batchForEach(new SumProcedure<Integer>(sum2), i, 5);
        }
        Assert.assertEquals(expectedValue, sum2.getValue());
    }

    private void batchForEachNullHandling(BatchIterable<Integer> batchIterable, int expectedValue)
    {
        //Testing batchForEach handling null keys and null values
        final Sum sum = new IntegerSum(0);

        for (int i = 0; i < batchIterable.getBatchCount(7); ++i)
        {
            batchIterable.batchForEach(new Procedure<Integer>()
            {
                public void value(Integer each)
                {
                    sum.add(each == null ? 1 : each);
                }
            }, i, batchIterable.getBatchCount(7));
        }
        Assert.assertEquals(expectedValue, sum.getValue());
    }

    private void batchForEachEmptyBatchIterable(BatchIterable<Integer> batchIterable)
    {
        //Test batchForEach on empty set, it should simply do nothing and not throw any exceptions
        Sum sum = new IntegerSum(0);
        batchIterable.batchForEach(new SumProcedure<Integer>(sum), 0, batchIterable.getBatchCount(1));
        Assert.assertEquals(0, sum.getValue());
    }

    @Override
    @Test
    public void forEachKeyValue()
    {
        super.forEachKeyValue();

        // Testing full chain
        final UnifiedSet<Integer> keys = UnifiedSet.newSet();
        final UnifiedSet<Integer> values = UnifiedSet.newSet();
        UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(
                INTEGER_HASHING_STRATEGY, COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, COLLISION_4, 4).withKeysValues(1, 5);

        map.forEachKeyValue(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                keys.add(argument1);
                values.add(argument2);
            }
        });
        Verify.assertSetsEqual(UnifiedSet.newSetWith(COLLISION_1, COLLISION_2, COLLISION_3, COLLISION_4, 1), keys);
        Verify.assertSetsEqual(UnifiedSet.newSetWith(1, 2, 3, 4, 5), values);

        // Testing when chain contains null
        final UnifiedSet<Integer> keys2 = UnifiedSet.newSet();
        final UnifiedSet<Integer> values2 = UnifiedSet.newSet();
        UnifiedMapWithHashingStrategy<Integer, Integer> map2 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                INTEGER_HASHING_STRATEGY, COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, 1, 4);

        map2.forEachKeyValue(new Procedure2<Integer, Integer>()
        {
            public void value(Integer argument1, Integer argument2)
            {
                keys2.add(argument1);
                values2.add(argument2);
            }
        });
        Verify.assertSetsEqual(UnifiedSet.newSetWith(COLLISION_1, COLLISION_2, COLLISION_3, 1), keys2);
        Verify.assertSetsEqual(UnifiedSet.newSetWith(1, 2, 3, 4), values2);
    }

    @Test
    public void getMapMemoryUsedInWords()
    {
        UnifiedMapWithHashingStrategy<String, String> map = UnifiedMapWithHashingStrategy.newMap(STRING_HASHING_STRATEGY);
        Assert.assertEquals(34, map.getMapMemoryUsedInWords());
        map.put("1", "1");
        Assert.assertEquals(34, map.getMapMemoryUsedInWords());

        UnifiedMapWithHashingStrategy<Integer, Integer> map2 = this.mapWithCollisionsOfSize(2);
        Assert.assertEquals(16, map2.getMapMemoryUsedInWords());
    }

    @Test
    public void getHashingStrategy()
    {
        UnifiedMapWithHashingStrategy<Integer, Object> map = UnifiedMapWithHashingStrategy.newMap(INTEGER_HASHING_STRATEGY);
        Assert.assertSame(INTEGER_HASHING_STRATEGY, map.hashingStrategy());
    }

    @Test
    public void getCollidingBuckets()
    {
        UnifiedMapWithHashingStrategy<Object, Object> map = UnifiedMapWithHashingStrategy.newMap(HashingStrategies.<Object>defaultStrategy());
        Assert.assertEquals(0, map.getCollidingBuckets());

        UnifiedMapWithHashingStrategy<Integer, Integer> map2 = this.mapWithCollisionsOfSize(2);
        Assert.assertEquals(1, map2.getCollidingBuckets());

        map2.put(42, 42);
        Assert.assertEquals(1, map2.getCollidingBuckets());

        UnifiedMapWithHashingStrategy<String, String> map3 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                STRING_HASHING_STRATEGY, "Six", "6", "Bar", "-", "Three", "3", "Five", "5");
        Assert.assertEquals(2, map3.getCollidingBuckets());
    }

    @Override
    @Test
    public void getIfAbsentPut()
    {
        super.getIfAbsentPut();

        // this map is deliberately small to force a rehash to occur from the put method, in a map with a chained bucket
        final UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newMap(
                INTEGER_HASHING_STRATEGY, 2, 0.75f);
        MORE_COLLISIONS.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                map.getIfAbsentPut(each, new PassThruFunction0<Integer>(each));
            }
        });

        Assert.assertEquals(this.mapWithCollisionsOfSize(9), map);

        //Testing getting element present in chain
        UnifiedMapWithHashingStrategy<Integer, Integer> map2 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                INTEGER_HASHING_STRATEGY, COLLISION_1, 1, COLLISION_2, 2, COLLISION_3, 3, COLLISION_4, 4);
        Assert.assertEquals(2, map2.getIfAbsentPut(COLLISION_2, new Function0<Integer>()
        {
            public Integer value()
            {
                Assert.fail();
                return null;
            }
        }).intValue());

        //Testing rehashing while creating a new chained key
        UnifiedMapWithHashingStrategy<Integer, Integer> map3 = UnifiedMapWithHashingStrategy.<Integer, Integer>newMap(
                INTEGER_HASHING_STRATEGY, 2, 0.75f).withKeysValues(COLLISION_1, 1, 2, 2, 3, 3);
        Assert.assertEquals(4, map3.getIfAbsentPut(COLLISION_2, new PassThruFunction0<Integer>(4)).intValue());
    }

    @Test
    public void equals_with_hashing_strategy()
    {
        UnifiedMapWithHashingStrategy<Person, Integer> map1 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 1, JOHNSMITH, 1, JANESMITH, 1);
        UnifiedMapWithHashingStrategy<Person, Integer> map2 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                FIRST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 1, JOHNSMITH, 1, JANESMITH, 1);

        Assert.assertEquals(map1, map2);
        Assert.assertEquals(map2, map1);
        Verify.assertNotEquals(map1.hashCode(), map2.hashCode());

        UnifiedMapWithHashingStrategy<Person, Integer> map3 = UnifiedMapWithHashingStrategy.newWithKeysValues(
                LAST_NAME_HASHING_STRATEGY, JOHNDOE, 1, JANEDOE, 2, JOHNSMITH, 3, JANESMITH, 4);
        UnifiedMapWithHashingStrategy<Person, Integer> map4 = UnifiedMapWithHashingStrategy.newMap(map3);
        HashMap<Person, Integer> hashMap = new HashMap<Person, Integer>(map3);

        Verify.assertEqualsAndHashCode(map3, map4);
        Assert.assertTrue(map3.equals(hashMap) && hashMap.equals(map3) && map3.hashCode() != hashMap.hashCode());

        UnifiedMap<Person, Integer> unifiedMap = UnifiedMap.newWithKeysValues(JOHNDOE, 1, JANEDOE, 1, JOHNSMITH, 1, JANESMITH, 1);
        UnifiedMapWithHashingStrategy<Person, Integer> map5 = UnifiedMapWithHashingStrategy.newMap(LAST_NAME_HASHING_STRATEGY, unifiedMap);
        Verify.assertNotEquals(map5, unifiedMap);
    }

    @Override
    @Test
    public void put()
    {
        super.put();

        // this map is deliberately small to force a rehash to occur from the put method, in a map with a chained bucket
        final UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newMap(
                INTEGER_HASHING_STRATEGY, 2, 0.75f);
        COLLISIONS.forEach(0, 4, new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                Assert.assertNull(map.put(each, each));
            }
        });

        Assert.assertEquals(this.mapWithCollisionsOfSize(5), map);
    }

    @Test
    public void put_get_with_hashing_strategy()
    {
        UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newMap(INTEGER_HASHING_STRATEGY);

        //Testing putting values in non chains
        Assert.assertNull(map.put(1, 1));
        Assert.assertNull(map.put(2, 2));
        Assert.assertNull(map.put(3, 3));
        Assert.assertNull(map.put(4, 4));
        Assert.assertNull(map.put(5, null));

        //Testing getting values from no chains
        Assert.assertEquals(1, map.get(1).intValue());
        Assert.assertEquals(2, map.get(2).intValue());
        Assert.assertNull(map.get(5));

        //Testing putting and getting elements in a chain
        Assert.assertNull(map.put(COLLISION_1, 1));
        Assert.assertNull(map.get(COLLISION_2));

        Assert.assertNull(map.put(COLLISION_2, 2));
        Assert.assertNull(map.get(COLLISION_3));

        Assert.assertNull(map.put(COLLISION_3, null));
        Assert.assertNull(map.put(COLLISION_4, 4));
        Assert.assertNull(map.put(COLLISION_5, 5));

        Assert.assertEquals(1, map.get(COLLISION_1).intValue());
        Assert.assertEquals(5, map.get(COLLISION_5).intValue());
        Assert.assertNull(map.get(COLLISION_3));

        map.remove(COLLISION_2);
        Assert.assertNull(map.get(COLLISION_2));

        //Testing for casting exceptions
        HashingStrategy<Person> lastName = new HashingStrategy<Person>()
        {
            public int computeHashCode(Person object)
            {
                return object.getLastName().hashCode();
            }

            public boolean equals(Person object1, Person object2)
            {
                return object1.equals(object2);
            }
        };

        UnifiedMapWithHashingStrategy<Person, Integer> map2 = UnifiedMapWithHashingStrategy.newMap(lastName);
        Assert.assertNull(map2.put(new Person("abe", "smith"), 1));
        Assert.assertNull(map2.put(new Person("brad", "smith"), 2));
        Assert.assertNull(map2.put(new Person("charlie", "smith"), 3));
    }

    @Test
    public void hashingStrategy()
    {
        UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newWithKeysValues(INTEGER_HASHING_STRATEGY, 1, 1, 2, 2);
        Assert.assertSame(INTEGER_HASHING_STRATEGY, map.hashingStrategy());
    }

    @Override
    protected UnifiedMapWithHashingStrategy<Integer, Integer> mapWithCollisionsOfSize(int size)
    {
        final UnifiedMapWithHashingStrategy<Integer, Integer> map = UnifiedMapWithHashingStrategy.newMap(
                INTEGER_HASHING_STRATEGY, size);
        MORE_COLLISIONS.subList(0, size).forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                map.put(each, each);
            }
        });
        return map;
    }

    private static final class EntrySumProcedure implements Procedure<Map.Entry<Integer, Integer>>
    {
        private static final long serialVersionUID = 1L;
        private final Sum sum;

        private EntrySumProcedure(Sum sum)
        {
            this.sum = sum;
        }

        public void value(Map.Entry<Integer, Integer> each)
        {
            this.sum.add(each.getKey());
            this.sum.add(each.getValue());
        }
    }
}
