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

package com.gs.collections.impl.parallel;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class AbstractPredicateBasedCombinerSerializationTest
{
    private static final AbstractPredicateBasedCombiner<Object, Procedure<Object>> ABSTRACT_PREDICATE_BASED_COMBINER = new AbstractPredicateBasedCombiner<Object, Procedure<Object>>(false, null, 0, null)
    {
        private static final long serialVersionUID = 1L;

        public void combineOne(Procedure<Object> thingToCombine)
        {
        }
    };

    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAFJjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5wYXJhbGxlbC5BYnN0cmFjdFByZWRpY2F0\n"
                        + "ZUJhc2VkQ29tYmluZXJTZXJpYWxpemF0aW9uVGVzdCQxAAAAAAAAAAECAAB4cgA/Y29tLmdzLmNv\n"
                        + "bGxlY3Rpb25zLmltcGwucGFyYWxsZWwuQWJzdHJhY3RQcmVkaWNhdGVCYXNlZENvbWJpbmVyAAAA\n"
                        + "AAAAAAECAAFMAAZyZXN1bHR0ABZMamF2YS91dGlsL0NvbGxlY3Rpb247eHIAOmNvbS5ncy5jb2xs\n"
                        + "ZWN0aW9ucy5pbXBsLnBhcmFsbGVsLkFic3RyYWN0UHJvY2VkdXJlQ29tYmluZXIAAAAAAAAAAQIA\n"
                        + "AVoADXVzZUNvbWJpbmVPbmV4cABzcgAtY29tLmdzLmNvbGxlY3Rpb25zLmltcGwubGlzdC5tdXRh\n"
                        + "YmxlLkZhc3RMaXN0AAAAAAAAAAEMAAB4cHcEAAAAAHg=",
                ABSTRACT_PREDICATE_BASED_COMBINER);
    }
}
