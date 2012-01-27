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

package ponzu.impl.test.domain;

import ponzu.api.block.function.Function;

public final class Person
{
    public static final Function<Person, String> FIRST = new Function<Person, String>()
    {
        public String valueOf(Person person)
        {
            return person.first;
        }
    };

    public static final Function<Person, String> LAST = new Function<Person, String>()
    {
        public String valueOf(Person person)
        {
            return person.last;
        }
    };

    public static final Function<Person, Integer> AGE = new Function<Person, Integer>()
    {
        public Integer valueOf(Person person)
        {
            return person.age;
        }
    };

    private final String first;
    private final String last;
    private final int age;

    public Person(String first, String last, int age)
    {
        this.first = first;
        this.last = last;
        this.age = age;
    }

    public Person(String first, String last)
    {
        this(first, last, 100);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Person");
        sb.append("{first='").append(this.first).append('\'');
        sb.append(", last='").append(this.last).append('\'');
        sb.append(", age=").append(this.age);
        sb.append('}');
        return sb.toString();
    }
}
