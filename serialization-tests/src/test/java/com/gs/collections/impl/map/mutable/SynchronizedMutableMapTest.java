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

package com.gs.collections.impl.map.mutable;

import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedMutableMapTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5TeW5jaHJvbml6ZWRN\n"
                        + "dXRhYmxlTWFwAAAAAAAAAAECAAB4cgAzY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLlN5bmNo\n"
                        + "cm9uaXplZE1hcEl0ZXJhYmxlAAAAAAAAAAECAAJMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7\n"
                        + "TAALbWFwSXRlcmFibGV0AChMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9tYXAvTWFwSXRlcmFibGU7\n"
                        + "eHBxAH4ABHNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5VbmlmaWVkTWFw\n"
                        + "AAAAAAAAAAEMAAB4cHcIAAAAAD9AAAB4",
                SynchronizedMutableMap.of(Maps.mutable.of()));
    }

    @Test
    public void keySet()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5TeW5jaHJvbml6ZWRN\n"
                        + "dXRhYmxlU2V0AAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29sbGVjdGlv\n"
                        + "bi5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAJMAApjb2xs\n"
                        + "ZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxlQ29sbGVj\n"
                        + "dGlvbjtMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuc2V0Lm11dGFibGUuU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVsZWdhdGV0AA9MamF2\n"
                        + "YS91dGlsL1NldDt4cHNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5Vbmlm\n"
                        + "aWVkU2V0AAAAAAAAAAEMAAB4cHcIAAAAAD9AAAB4c3IAOmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "Lm1hcC5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVNYXAAAAAAAAAAAQIAAHhyADNjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5tYXAuU3luY2hyb25pemVkTWFwSXRlcmFibGUAAAAAAAAAAQIAAkwA\n"
                        + "BGxvY2txAH4AA0wAC21hcEl0ZXJhYmxldAAoTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvbWFwL01h\n"
                        + "cEl0ZXJhYmxlO3hwcQB+AA1zcgAuY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLm11dGFibGUu\n"
                        + "VW5pZmllZE1hcAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeA==",
                SynchronizedMutableMap.of(Maps.mutable.of()).keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADpjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5TeW5jaHJvbml6ZWRN\n"
                        + "dXRhYmxlU2V0AAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29sbGVjdGlv\n"
                        + "bi5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAJMAApjb2xs\n"
                        + "ZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxlQ29sbGVj\n"
                        + "dGlvbjtMAARsb2NrdAASTGphdmEvbGFuZy9PYmplY3Q7eHBzcgAuY29tLmdzLmNvbGxlY3Rpb25z\n"
                        + "LmltcGwuc2V0Lm11dGFibGUuU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVsZWdhdGV0AA9MamF2\n"
                        + "YS91dGlsL1NldDt4cHNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5Vbmlm\n"
                        + "aWVkTWFwJEVudHJ5U2V0AAAAAAAAAAECAAFMAAZ0aGlzJDB0ADBMY29tL2dzL2NvbGxlY3Rpb25z\n"
                        + "L2ltcGwvbWFwL211dGFibGUvVW5pZmllZE1hcDt4cHNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1w\n"
                        + "bC5tYXAubXV0YWJsZS5VbmlmaWVkTWFwAAAAAAAAAAEMAAB4cHcIAAAAAD9AAAB4c3IAOmNvbS5n\n"
                        + "cy5jb2xsZWN0aW9ucy5pbXBsLm1hcC5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVNYXAAAAAA\n"
                        + "AAAAAQIAAHhyADNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAuU3luY2hyb25pemVkTWFwSXRl\n"
                        + "cmFibGUAAAAAAAAAAQIAAkwABGxvY2txAH4AA0wAC21hcEl0ZXJhYmxldAAoTGNvbS9ncy9jb2xs\n"
                        + "ZWN0aW9ucy9hcGkvbWFwL01hcEl0ZXJhYmxlO3hwcQB+ABBxAH4ADA==",
                SynchronizedMutableMap.of(Maps.mutable.of()).entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAkwACmNvbGxlY3Rpb250ADVMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xsZWN0aW9uO0wABGxvY2t0ABJM\n"
                        + "amF2YS9sYW5nL09iamVjdDt4cHNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9u\n"
                        + "Lm11dGFibGUuQ29sbGVjdGlvbkFkYXB0ZXIAAAAAAAAAAQIAAUwACGRlbGVnYXRldAAWTGphdmEv\n"
                        + "dXRpbC9Db2xsZWN0aW9uO3hwc3IALWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJs\n"
                        + "ZS5GYXN0TGlzdAAAAAAAAAABDAAAeHB3BAAAAAB4c3IAOmNvbS5ncy5jb2xsZWN0aW9ucy5pbXBs\n"
                        + "Lm1hcC5tdXRhYmxlLlN5bmNocm9uaXplZE11dGFibGVNYXAAAAAAAAAAAQIAAHhyADNjb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5tYXAuU3luY2hyb25pemVkTWFwSXRlcmFibGUAAAAAAAAAAQIAAkwA\n"
                        + "BGxvY2txAH4AAkwAC21hcEl0ZXJhYmxldAAoTGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvbWFwL01h\n"
                        + "cEl0ZXJhYmxlO3hwcQB+AAxzcgAuY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubWFwLm11dGFibGUu\n"
                        + "VW5pZmllZE1hcAAAAAAAAAABDAAAeHB3CAAAAAA/QAAAeA==",
                SynchronizedMutableMap.of(Maps.mutable.of()).values());
    }
}
