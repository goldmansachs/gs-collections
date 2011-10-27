/*
 * Copyright 2011 Goldman Sachs & Co.
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

package com.gs.collections.impl.block.factory;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.impl.merge.Person;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class HashingStrategiesTest
{
    @Test
    public void defaultStrategy()
    {
        HashingStrategy<String> stringHashingStrategy = HashingStrategies.defaultStrategy();
        Assert.assertEquals("TEST".hashCode(), stringHashingStrategy.computeHashCode("TEST"));
        Assert.assertEquals("1TeSt1".hashCode(), stringHashingStrategy.computeHashCode("1TeSt1"));
        Assert.assertTrue(stringHashingStrategy.equals("lowercase", "lowercase"));
        Assert.assertFalse(stringHashingStrategy.equals("lowercase", "LOWERCASE"));
        Assert.assertFalse(stringHashingStrategy.equals("12321", "abcba"));
    }

    @Test
    public void nullSafeStrategy()
    {
        HashingStrategy<Integer> integerHashingStrategy =
                HashingStrategies.nullSafeHashingStrategy(HashingStrategies.<Integer>defaultStrategy());

        Assert.assertEquals(0, integerHashingStrategy.computeHashCode(null));
        Assert.assertEquals(5, integerHashingStrategy.computeHashCode(5));

        Assert.assertTrue(integerHashingStrategy.equals(null, null));
        Assert.assertFalse(integerHashingStrategy.equals(null, 1));
        Assert.assertFalse(integerHashingStrategy.equals(1, null));
        Assert.assertTrue(integerHashingStrategy.equals(1, 1));
    }

    @Test
    public void fromFunction()
    {
        Person john = new Person("John", "Smith");
        Person jane = new Person("Jane", "Smith");
        HashingStrategy<Person> lastHashingStrategy = HashingStrategies.fromFunction(Person.TO_LAST);
        HashingStrategy<Person> firstHashingStrategy = HashingStrategies.fromFunction(Person.TO_FIRST);

        Assert.assertEquals("John".hashCode(), firstHashingStrategy.computeHashCode(john));
        Verify.assertNotEquals(john.hashCode(), firstHashingStrategy.computeHashCode(john));
        Assert.assertFalse(firstHashingStrategy.equals(john, jane));

        Assert.assertEquals("Smith".hashCode(), lastHashingStrategy.computeHashCode(john));
        Verify.assertNotEquals(john.hashCode(), lastHashingStrategy.computeHashCode(john));
        Assert.assertTrue(lastHashingStrategy.equals(john, jane));

        Verify.assertNotEquals(lastHashingStrategy.computeHashCode(john), firstHashingStrategy.computeHashCode(john));
        Verify.assertNotEquals(lastHashingStrategy.computeHashCode(john), firstHashingStrategy.computeHashCode(jane));
        Assert.assertEquals(lastHashingStrategy.computeHashCode(john), lastHashingStrategy.computeHashCode(jane));
    }
}


