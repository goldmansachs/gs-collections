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

package com.gs.collections.impl.bag.mutable;

import com.gs.collections.impl.factory.Bags;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

/**
 * @since 4.2
 */
public class SynchronizedBagTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADNjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5iYWcubXV0YWJsZS5TeW5jaHJvbml6ZWRC\n"
                        + "YWcAAAAAAAAAAQIAAHhyAEhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5jb2xsZWN0aW9uLm11dGFi\n"
                        + "bGUuU3luY2hyb25pemVkTXV0YWJsZUNvbGxlY3Rpb24AAAAAAAAAAQIAAkwACmNvbGxlY3Rpb250\n"
                        + "ADVMY29tL2dzL2NvbGxlY3Rpb25zL2FwaS9jb2xsZWN0aW9uL011dGFibGVDb2xsZWN0aW9uO0wA\n"
                        + "BGxvY2t0ABJMamF2YS9sYW5nL09iamVjdDt4cHNyACtjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5i\n"
                        + "YWcubXV0YWJsZS5IYXNoQmFnAAAAAAAAAAEMAAB4cHcEAAAAAHhxAH4ABA==",
                SynchronizedBag.of(Bags.mutable.of()));
    }
}
