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

package com.gs.collections.impl.test.domain;

import java.io.Serializable;

import com.gs.collections.api.block.function.Function;

public final class Person implements Comparable<Person>, Serializable
{
    public static final Function<Person, String> TO_FIRST = new Function<Person, String>()
    {
        public String valueOf(Person person)
        {
            return person.firstName;
        }
    };

    public static final Function<Person, String> TO_LAST = new Function<Person, String>()
    {
        public String valueOf(Person person)
        {
            return person.lastName;
        }
    };

    public static final Function<Person, Integer> TO_AGE = new Function<Person, Integer>()
    {
        public Integer valueOf(Person person)
        {
            return person.age;
        }
    };
    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String lastName;
    private final int age;

    public Person(String firstName, String lastName)
    {
        this(firstName, lastName, 100);
    }

    public Person(String firstName, String lastName, int age)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        Person person = (Person) o;

        if (this.age != person.age)
        {
            return false;
        }
        if (!this.firstName.equals(person.firstName))
        {
            return false;
        }
        if (!this.lastName.equals(person.lastName))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = this.firstName.hashCode();
        result = 31 * result + this.lastName.hashCode();
        result = 31 * result + this.age;
        return result;
    }

    @Override
    public int compareTo(Person other)
    {
        return this.lastName.compareTo(other.lastName);
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public int getAge()
    {
        return this.age;
    }

    @Override
    public String toString()
    {
        return "Person{first='" + this.firstName
                + "', last='" + this.lastName
                + "', age=" + this.age + '}';
    }
}
