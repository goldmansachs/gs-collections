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

package ponzu.impl.block.predicate.checked;

import ponzu.impl.test.Verify;
import org.junit.Test;

public class CheckedPredicateTest
{
    private static final CheckedPredicate CHECKED_PREDICATE = new CheckedPredicate()
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
                "rO0ABXNyAEZjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5ibG9jay5wcmVkaWNhdGUuY2hlY2tlZC5D\n"
                        + "aGVja2VkUHJlZGljYXRlVGVzdCQxAAAAAAAAAAECAAB4cgBAY29tLmdzLmNvbGxlY3Rpb25zLmlt\n"
                        + "cGwuYmxvY2sucHJlZGljYXRlLmNoZWNrZWQuQ2hlY2tlZFByZWRpY2F0ZQAAAAAAAAABAgAAeHA=\n",
                CHECKED_PREDICATE);
    }
}
