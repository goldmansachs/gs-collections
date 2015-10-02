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

package com.gs.collections.impl.map.mutable.primitive;

import java.util.Set;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.map.primitive.MutableObjectBooleanMap;
import com.gs.collections.impl.block.factory.HashingStrategies;
import com.gs.collections.impl.test.Verify;
import com.gs.collections.impl.test.domain.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit test for {@link ObjectBooleanHashMapWithHashingStrategy#keySet()}.
 */
public class ObjectBooleanHashMapWithHashingStrategyKeySetTest extends ObjectBooleanHashMapKeySetTestCase
{
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

    private static final HashingStrategy<Person> LAST_NAME_HASHING_STRATEGY = HashingStrategies.fromFunction(Person.TO_LAST);

    private static final Person JOHNSMITH = new Person("John", "Smith");
    private static final Person JANESMITH = new Person("Jane", "Smith");
    private static final Person JOHNDOE = new Person("John", "Doe");
    private static final Person JANEDOE = new Person("Jane", "Doe");

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(STRING_HASHING_STRATEGY, key1, value1);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(STRING_HASHING_STRATEGY, key1, value1, key2, value2);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2, String key3, boolean value3)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(STRING_HASHING_STRATEGY, key1, value1, key2, value2, key3, value3);
    }

    @Override
    public MutableObjectBooleanMap<String> newMapWithKeysValues(String key1, boolean value1, String key2, boolean value2, String key3, boolean value3, String key4, boolean value4)
    {
        return ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(STRING_HASHING_STRATEGY, key1, value1, key2, value2, key3, value3, key4, value4);
    }

    @Override
    public MutableObjectBooleanMap<String> newEmptyMap()
    {
        return ObjectBooleanHashMapWithHashingStrategy.newMap(STRING_HASHING_STRATEGY);
    }

    @Override
    @Test
    public void contains()
    {
        super.contains();

        Set<Person> people = ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(LAST_NAME_HASHING_STRATEGY, JOHNDOE, true, JANEDOE, false, JOHNSMITH, true, JANESMITH, false).keySet();

        Verify.assertSize(2, people);

        Verify.assertContains(JANEDOE, people);
        Verify.assertContains(JOHNDOE, people);
        Verify.assertContains(JANESMITH, people);
        Verify.assertContains(JOHNSMITH, people);
    }

    @Override
    @Test
    public void removeFromKeySet()
    {
        super.removeFromKeySet();

        ObjectBooleanHashMapWithHashingStrategy<Person> map = ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(LAST_NAME_HASHING_STRATEGY, JOHNDOE, true, JANEDOE, false, JOHNSMITH, true, JANESMITH, false);
        Set<Person> people = map.keySet();
        people.remove(JOHNDOE);
        Assert.assertEquals(map, ObjectBooleanHashMapWithHashingStrategy.newWithKeysValues(LAST_NAME_HASHING_STRATEGY, JOHNSMITH, false));
    }
}
