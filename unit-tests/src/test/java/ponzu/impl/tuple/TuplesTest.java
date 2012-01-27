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

package ponzu.impl.tuple;

import java.util.Map;

import ponzu.api.map.MutableMap;
import ponzu.api.tuple.Pair;
import ponzu.api.tuple.Twin;
import ponzu.impl.block.factory.Functions;
import ponzu.impl.map.mutable.UnifiedMap;
import ponzu.impl.test.Verify;
import org.junit.Assert;
import org.junit.Test;

public class TuplesTest
{
    @Test
    public void pairFrom()
    {
        Pair<String, String> pair = Tuples.pair("1", "2");
        Map.Entry<String, String> entry = pair.toEntry();
        Pair<String, String> pair2 = Tuples.pairFrom(entry);
        Assert.assertEquals(pair, pair2);
    }

    @Test
    public void pair()
    {
        Pair<String, String> pair = Tuples.pair("1", "2");
        Assert.assertEquals("1", pair.getOne());
        Assert.assertEquals("2", pair.getTwo());
    }

    @Test
    public void twin()
    {
        Twin<String> twin = Tuples.twin("1", "2");
        Assert.assertEquals("1", twin.getOne());
        Assert.assertEquals("2", twin.getTwo());
    }

    @Test
    public void equalsHashCode()
    {
        Twin<String> pair1 = Tuples.twin("1", "1");
        Pair<String, String> pair1a = Tuples.pair("1", "1");
        Pair<String, String> pair2 = Tuples.pair("2", "2");

        Verify.assertEqualsAndHashCode(pair1, pair1);
        Verify.assertEqualsAndHashCode(pair1, pair1a);
        Verify.assertNotEquals(pair1, pair2);
        Verify.assertNotEquals(pair1, new Object());
    }

    @Test
    public void putToMap()
    {
        Pair<String, Integer> pair = Tuples.pair("1", 2);
        MutableMap<String, Integer> map = UnifiedMap.newMap();
        pair.put(map);
        Verify.assertContainsAllKeyValues(map, "1", 2);
        Verify.assertSize(1, map);
    }

    @Test
    public void testToString()
    {
        Pair<String, String> pair1 = Tuples.pair("1", "1");

        Assert.assertEquals("1:1", pair1.toString());
    }

    @Test
    public void pairFunctions()
    {
        Integer two = 2;
        Pair<String, Integer> pair = Tuples.pair("One", two);
        Assert.assertEquals("One", Functions.<String>firstOfPair().valueOf(pair));
        Assert.assertSame(two, Functions.<Integer>secondOfPair().valueOf(pair));
    }
}
