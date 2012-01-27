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

package ponzu.impl.multimap;

import ponzu.api.block.HashingStrategy;
import ponzu.api.collection.MutableCollection;
import ponzu.api.list.ImmutableList;
import ponzu.api.multimap.Multimap;
import ponzu.api.multimap.set.MutableSetMultimap;
import ponzu.api.set.ImmutableSet;
import ponzu.api.set.MutableSet;
import ponzu.api.tuple.Pair;
import ponzu.impl.block.factory.HashingStrategies;
import ponzu.impl.factory.Lists;
import ponzu.impl.factory.Sets;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.merge.Person;
import ponzu.impl.multimap.set.UnifiedSetMultimap;
import ponzu.impl.multimap.set.strategy.UnifiedSetWithHashingStrategyMultimap;
import ponzu.impl.set.mutable.UnifiedSet;
import ponzu.impl.set.strategy.mutable.UnifiedSetWithHashingStrategy;
import ponzu.impl.test.SerializeTestHelper;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link UnifiedSetWithHashingStrategyMultimap}.
 */
public class UnifiedSetWithHashingStrategyMultimapTest extends AbstractMutableMultimapTestCase
{
    private static final HashingStrategy<Person> LAST_NAME_STRATEGY = HashingStrategies.fromFunction(Person.TO_LAST);

    private static final HashingStrategy<Person> FIRST_NAME_STRATEGY = HashingStrategies.fromFunction(Person.TO_FIRST);

    private static final Person JOHNSMITH = new Person("John", "Smith");
    private static final Person JANESMITH = new Person("Jane", "Smith");
    private static final Person JOHNDOE = new Person("John", "Doe");
    private static final Person JANEDOE = new Person("Jane", "Doe");
    private static final ImmutableList<Person> PEOPLE = Lists.immutable.of(JOHNSMITH, JANESMITH, JOHNDOE, JANEDOE);
    private static final ImmutableSet<Person> LAST_NAME_HASHED_SET = Sets.immutable.of(JOHNSMITH, JOHNDOE);

    @Override
    public <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimap()
    {
        return UnifiedSetWithHashingStrategyMultimap.newMultimap(HashingStrategies.<V>defaultStrategy());
    }

    @Override
    public <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        UnifiedSetWithHashingStrategyMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key, value);
        return mutableMultimap;
    }

    @Override
    public <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        UnifiedSetWithHashingStrategyMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        return mutableMultimap;
    }

    @Override
    public <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        UnifiedSetWithHashingStrategyMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        return mutableMultimap;
    }

    @Override
    public <K, V> UnifiedSetWithHashingStrategyMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        UnifiedSetWithHashingStrategyMultimap<K, V> mutableMultimap = this.newMultimap();
        mutableMultimap.put(key1, value1);
        mutableMultimap.put(key2, value2);
        mutableMultimap.put(key3, value3);
        mutableMultimap.put(key4, value4);
        return mutableMultimap;
    }

    @Override
    protected <V> MutableCollection<V> createCollection(V... args)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(HashingStrategies.<V>defaultStrategy(), args);
    }

    @Override
    public <K, V> Multimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return UnifiedSetWithHashingStrategyMultimap.newMultimap(HashingStrategies.<V>defaultStrategy(), pairs);
    }

    @Override
    @Test
    public void testClear()
    {
        UnifiedSetWithHashingStrategyMultimap<Integer, String> map = this.newMultimapWithKeysValues(1, "1", 1, "One", 2, "2", 2, "Two");
        map.clear();
        Verify.assertEmpty(map);
    }

    @Test
    public void testHashingStrategyConstructors()
    {
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> peopleMap = UnifiedSetWithHashingStrategyMultimap.newMultimap(HashingStrategies.<Person>defaultStrategy());
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> lastNameMap = UnifiedSetWithHashingStrategyMultimap.newMultimap(LAST_NAME_STRATEGY);
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> firstNameMap = UnifiedSetWithHashingStrategyMultimap.newMultimap(FIRST_NAME_STRATEGY);
        peopleMap.putAll(1, PEOPLE);
        lastNameMap.putAll(1, PEOPLE);
        firstNameMap.putAll(1, PEOPLE);

        Verify.assertSetsEqual(PEOPLE.toSet(), peopleMap.get(1));
        Verify.assertSetsEqual(UnifiedSet.newSetWith(JOHNSMITH, JANESMITH), firstNameMap.get(1));
        Verify.assertSetsEqual(LAST_NAME_HASHED_SET.castToSet(), lastNameMap.get(1));
    }

    @Test
    public void testMultimapConstructor()
    {
        MutableSetMultimap<Integer, Person> map = UnifiedSetMultimap.newMultimap();
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> map2 = UnifiedSetWithHashingStrategyMultimap.newMultimap(LAST_NAME_STRATEGY);
        for (Person person : PEOPLE)
        {
            map.put(1, person);
            map2.put(1, person);
        }

        UnifiedSetWithHashingStrategyMultimap<Integer, Person> hashingMap =
                UnifiedSetWithHashingStrategyMultimap.newMultimap(LAST_NAME_STRATEGY, map);
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> hashingMap2 = UnifiedSetWithHashingStrategyMultimap.newMultimap(map2);

        Verify.assertSetsEqual(hashingMap.get(1), hashingMap2.get(1));
        Assert.assertSame(hashingMap.getValueHashingStrategy(), hashingMap2.getValueHashingStrategy());
    }

    @Test
    public void testNewEmpty()
    {
        UnifiedMap<Integer, MutableSet<Person>> expected = UnifiedMap.newMap();
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> lastNameMap = UnifiedSetWithHashingStrategyMultimap.newMultimap(LAST_NAME_STRATEGY);
        UnifiedSetWithHashingStrategyMultimap<Integer, Person> newEmptyMap = lastNameMap.newEmpty();
        for (int i = 1; i < 4; ++i)
        {
            expected.put(i, LAST_NAME_HASHED_SET.toSet());
            lastNameMap.putAll(i, PEOPLE);
            newEmptyMap.putAll(i, PEOPLE);
        }

        Verify.assertMapsEqual(expected, lastNameMap.getMap());
        Verify.assertMapsEqual(expected, newEmptyMap.getMap());
        Assert.assertSame(LAST_NAME_STRATEGY, lastNameMap.getValueHashingStrategy());
        Assert.assertSame(LAST_NAME_STRATEGY, newEmptyMap.getValueHashingStrategy());
    }

    @Override
    @Test
    public void serialization()
    {
        super.serialization();

        UnifiedSetWithHashingStrategyMultimap<Object, Person> lastNameMap = UnifiedSetWithHashingStrategyMultimap.newMultimap(LAST_NAME_STRATEGY);
        lastNameMap.putAll(1, PEOPLE);
        lastNameMap.putAll(2, PEOPLE.toList().reverseThis());

        Verify.assertPostSerializedEqualsAndHashCode(lastNameMap);
        UnifiedSetWithHashingStrategyMultimap<Object, Person> deserialized = SerializeTestHelper.serializeDeserialize(lastNameMap);
        Verify.assertSetsEqual(LAST_NAME_HASHED_SET.castToSet(), deserialized.get(1));
        Verify.assertSetsEqual(UnifiedSet.newSetWith(JANEDOE, JANESMITH), deserialized.get(2));

        deserialized.putAll(3, PEOPLE);
        Verify.assertSetsEqual(LAST_NAME_HASHED_SET.castToSet(), deserialized.get(3));
    }
}
