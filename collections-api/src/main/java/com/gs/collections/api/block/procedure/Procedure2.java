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

package com.gs.collections.api.block.procedure;

import java.io.Serializable;

/**
 * A Procedure2 is used by forEachWith() methods and for MapIterate.forEachKeyValue().  In the forEachKeyValue()
 * case the procedure takes the key as the first argument, and the value as the second.   In the forEachWith() case
 * the procedure takes the the element of the collection as the first argument, and the specified parameter as the
 * second argument.
 */
public interface Procedure2<T1, T2>
        extends Serializable
{
    void value(T1 argument1, T2 argument2);
}
