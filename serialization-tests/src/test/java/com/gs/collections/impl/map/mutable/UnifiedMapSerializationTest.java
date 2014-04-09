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

package com.gs.collections.impl.map.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnifiedMapSerializationTest
{
    public static final String UNIFIED_MAP_KEY_SET =
            "rO0ABXNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQubXV0YWJsZS5VbmlmaWVkU2V0AAAA\n"
                    + "AAAAAAEMAAB4cHcIAAAAAD9AAAB4";
    public static final String UNIFIED_MAP_ENTRY_SET =
            "rO0ABXNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5VbmlmaWVkTWFwJEVu\n"
                    + "dHJ5U2V0AAAAAAAAAAECAAFMAAZ0aGlzJDB0ADBMY29tL2dzL2NvbGxlY3Rpb25zL2ltcGwvbWFw\n"
                    + "L211dGFibGUvVW5pZmllZE1hcDt4cHNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0\n"
                    + "YWJsZS5VbmlmaWVkTWFwAAAAAAAAAAEMAAB4cHcIAAAAAD9AAAB4";
    public static final String UNIFIED_MAP_VALUES =
            "rO0ABXNyAC1jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAA\n"
                    + "AAAAAQwAAHhwdwQAAAAAeA==";

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5VbmlmaWVkTWFwAAAA\n"
                        + "AAAAAAEMAAB4cHcIAAAAAD9AAAB4",
                UnifiedMap.newMap());
    }

    @Test
    public void keySet()
    {
        Verify.assertSerializedForm(
                1L,
                UNIFIED_MAP_KEY_SET,
                UnifiedMap.newMap().keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                1L,
                UNIFIED_MAP_ENTRY_SET,
                UnifiedMap.newMap().entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                UNIFIED_MAP_VALUES,
                UnifiedMap.newMap().values());
    }
}
