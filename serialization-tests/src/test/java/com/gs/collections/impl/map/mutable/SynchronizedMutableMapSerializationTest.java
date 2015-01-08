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

package com.gs.collections.impl.map.mutable;

import com.gs.collections.impl.factory.Maps;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class SynchronizedMutableMapSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                2L,
                "rO0ABXNyAEVjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5TeW5jaHJvbml6ZWRN\n"
                        + "YXBTZXJpYWxpemF0aW9uUHJveHkAAAAAAAAAAQwAAHhwc3IALmNvbS5ncy5jb2xsZWN0aW9ucy5p\n"
                        + "bXBsLm1hcC5tdXRhYmxlLlVuaWZpZWRNYXAAAAAAAAAAAQwAAHhwdwgAAAAAP0AAAHh4",
                SynchronizedMutableMap.of(Maps.mutable.of()));
    }

    @Test
    public void keySet()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgAuY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuc2V0Lm11dGFibGUuU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVs\n"
                        + "ZWdhdGV0AA9MamF2YS91dGlsL1NldDt4cHNyAC5jb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQu\n"
                        + "bXV0YWJsZS5VbmlmaWVkU2V0AAAAAAAAAAEMAAB4cHcIAAAAAD9AAAB4eA==",
                SynchronizedMutableMap.of(Maps.mutable.of()).keySet());
    }

    @Test
    public void entrySet()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgAuY29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuc2V0Lm11dGFibGUuU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVs\n"
                        + "ZWdhdGV0AA9MamF2YS91dGlsL1NldDt4cHNyADdjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5tYXAu\n"
                        + "bXV0YWJsZS5VbmlmaWVkTWFwJEVudHJ5U2V0AAAAAAAAAAECAAFMAAZ0aGlzJDB0ADBMY29tL2dz\n"
                        + "L2NvbGxlY3Rpb25zL2ltcGwvbWFwL211dGFibGUvVW5pZmllZE1hcDt4cHNyAC5jb20uZ3MuY29s\n"
                        + "bGVjdGlvbnMuaW1wbC5tYXAubXV0YWJsZS5VbmlmaWVkTWFwAAAAAAAAAAEMAAB4cHcIAAAAAD9A\n"
                        + "AAB4eA==",
                SynchronizedMutableMap.of(Maps.mutable.of()).entrySet());
    }

    @Test
    public void values()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFibGUuU3luY2hy\n"
                        + "b25pemVkQ29sbGVjdGlvblNlcmlhbGl6YXRpb25Qcm94eQAAAAAAAAABDAAAeHBzcgA8Y29tLmdz\n"
                        + "LmNvbGxlY3Rpb25zLmltcGwuY29sbGVjdGlvbi5tdXRhYmxlLkNvbGxlY3Rpb25BZGFwdGVyAAAA\n"
                        + "AAAAAAECAAFMAAhkZWxlZ2F0ZXQAFkxqYXZhL3V0aWwvQ29sbGVjdGlvbjt4cHNyAC1jb20uZ3Mu\n"
                        + "Y29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuRmFzdExpc3QAAAAAAAAAAQwAAHhwdwQAAAAA\n"
                        + "eHg=",
                SynchronizedMutableMap.of(Maps.mutable.of()).values());
    }
}
