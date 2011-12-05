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

package com.gs.collections.api.block.function;

import java.io.Serializable;

/**
 * A Function2 is used by injectInto() methods. It takes the injected argument as the first argument, and the
 * current item of the collection as the second argument, for the first item in the collection.  The result of each
 * subsequent iteration is passed in as the first argument.
 *
 * @since 1.0
 */
public interface Function2<T1, T2, R>
        extends Serializable
{
    R value(T1 argument1, T2 argument2);
}
