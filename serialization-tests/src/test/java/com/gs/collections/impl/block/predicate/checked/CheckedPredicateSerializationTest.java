/*
 * Copyright 2013 Goldman Sachs.
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

package com.gs.collections.impl.block.predicate.checked;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class CheckedPredicateSerializationTest
{
    private static final CheckedPredicate<?> CHECKED_PREDICATE = new CheckedPredicate<Object>()
    {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean safeAccept(Object object) throws Exception
        {
            return false;
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuY2hlY2tlZC5D\n"
                        + "aGVja2VkUHJlZGljYXRlU2VyaWFsaXphdGlvblRlc3QkMQAAAAAAAAABAgAAeHIAQGNvbS5ncy5j\n"
                        + "b2xsZWN0aW9ucy5pbXBsLmJsb2NrLnByZWRpY2F0ZS5jaGVja2VkLkNoZWNrZWRQcmVkaWNhdGUA\n"
                        + "AAAAAAAAAQIAAHhw",
                CHECKED_PREDICATE);
    }
}
