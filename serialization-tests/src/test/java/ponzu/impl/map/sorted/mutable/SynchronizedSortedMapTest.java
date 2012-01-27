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

package ponzu.impl.map.sorted.mutable;

import ponzu.impl.factory.SortedMaps;
import ponzu.impl.test.Verify;
import org.junit.Test;

public class SynchronizedSortedMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc29ydGVkLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkU29ydGVkTWFwAAAAAAAAAAECAAB4cgAzY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFw\n"
                        + "LlN5bmNocm9uaXplZE1hcEl0ZXJhYmxlAAAAAAAAAAECAAJMAARsb2NrdAASTGphdmEvbGFuZy9P\n"
                        + "YmplY3Q7TAALbWFwSXRlcmFibGV0AChMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9tYXAvTWFwSXRl\n"
                        + "cmFibGU7eHBxAH4ABHNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuc29ydGVkLm11dGFi\n"
                        + "bGUuVHJlZVNvcnRlZE1hcAAAAAAAAAABDAAAeHBwdwQAAAAAeA==",
                SynchronizedSortedMap.of(SortedMaps.mutable.of()));
    }
}
