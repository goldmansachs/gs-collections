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

package com.gs.collections.impl.block.function;

import com.gs.collections.api.block.function.Function;
import com.gs.collections.impl.block.factory.Comparators;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class CaseFunctionTest
{
    @Test
    public void noopCase()
    {
        CaseFunction<Integer, Integer> function = new CaseFunction<>();
        Assert.assertNull(function.valueOf(42));
    }

    @Test
    public void basicCase()
    {
        CaseFunction<Integer, Integer> function = new CaseFunction<>();
        function.addCase(ignored -> true, Functions.getIntegerPassThru());
        Integer fortyTwo = 42;
        Assert.assertEquals(fortyTwo, function.valueOf(fortyTwo));
    }

    @Test
    public void defaultValue()
    {
        CaseFunction<Foo, String> function = Functions.caseDefault(
                Functions.getFixedValue("Yow!"),
                Predicates.attributeGreaterThan(Foo.TO_VALUE, 5.0D),
                Functions.getFixedValue("Patience, grasshopper"));

        Assert.assertEquals("Yow!", function.valueOf(new Foo("", 1.0D)));

        function.setDefault(Functions.getFixedValue("Patience, young grasshopper"));
        Assert.assertEquals("Patience, grasshopper", function.valueOf(new Foo("", 6.0D)));
        Assert.assertEquals("Patience, young grasshopper", function.valueOf(new Foo("", 1.0D)));

        Verify.assertContains("CaseFunction", function.toString());
    }

    public static final class Foo implements Comparable<Foo>
    {
        public static final Function<Foo, Double> TO_VALUE = new Function<Foo, Double>()
        {
            public Double valueOf(Foo foo)
            {
                return foo.value;
            }
        };

        private final String description;
        private final double value;

        private Foo(String description, double value)
        {
            this.description = description;
            this.value = value;
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

            Foo foo = (Foo) o;

            if (Double.compare(foo.value, this.value) != 0)
            {
                return false;
            }
            return Comparators.nullSafeEquals(this.description, foo.description);
        }

        @Override
        public int hashCode()
        {
            int result = this.description == null ? 0 : this.description.hashCode();
            long l = Double.doubleToLongBits(this.value);
            result = 31 * result + (int) (l ^ l >>> 32);
            return result;
        }

        @Override
        public int compareTo(Foo o)
        {
            throw new RuntimeException("compareTo not implemented");
        }
    }
}
