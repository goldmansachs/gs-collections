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

package com.gs.collections.impl.merge;

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
    private static final long serialVersionUID = 1L;

    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
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

        return this.lastName.equals(person.lastName) && this.firstName.equals(person.firstName);
    }

    @Override
    public int hashCode()
    {
        return 31 * this.firstName.hashCode() + this.lastName.hashCode();
    }

    public int compareTo(Person other)
    {
        return this.lastName.compareTo(other.lastName);
    }

    public String getLastName()
    {
        return this.lastName;
    }

    @Override
    public String toString()
    {
        return this.firstName + ' ' + this.lastName;
    }
}
