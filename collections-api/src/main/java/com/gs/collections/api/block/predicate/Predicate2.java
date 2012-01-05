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

package com.gs.collections.api.block.predicate;

import java.io.Serializable;

/**
 * A Predicate2 is primarily used in methods like selectWith, detectWith, rejectWith.  The first argument
 * is the element of the collection being iterated over, and the second argument is a parameter passed into
 * the predicate from the calling method.
 */
public interface Predicate2<T1, T2>
        extends Serializable
{
    boolean accept(T1 argument1, T2 argument2);
}
