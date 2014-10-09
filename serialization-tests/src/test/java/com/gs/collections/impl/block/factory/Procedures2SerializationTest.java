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

package com.gs.collections.impl.block.factory;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class Procedures2SerializationTest
{
    @Test
    public void throwing()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMy\n"
                        + "JFRocm93aW5nUHJvY2VkdXJlMkFkYXB0ZXIAAAAAAAAAAQIAAUwAEnRocm93aW5nUHJvY2VkdXJl\n"
                        + "MnQARExjb20vZ3MvY29sbGVjdGlvbnMvaW1wbC9ibG9jay9wcm9jZWR1cmUvY2hlY2tlZC9UaHJv\n"
                        + "d2luZ1Byb2NlZHVyZTI7eHIAQWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmJsb2NrLnByb2NlZHVy\n"
                        + "ZS5jaGVja2VkLkNoZWNrZWRQcm9jZWR1cmUyAAAAAAAAAAECAAB4cHA=",
                Procedures2.throwing(null));
    }

    @Test
    public void addToCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEFjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMy\n"
                        + "JEFkZFRvQ29sbGVjdGlvbgAAAAAAAAABAgAAeHA=",
                Procedures2.addToCollection());
    }

    @Test
    public void removeFromCollection()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5mYWN0b3J5LlByb2NlZHVyZXMy\n"
                        + "JFJlbW92ZUZyb21Db2xsZWN0aW9uAAAAAAAAAAECAAB4cA==",
                Procedures2.removeFromCollection());
    }
}
