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
 * A Predicate is a lambda or closure with a boolean result.  The method accept should be implemented to indicate the object
 * passed to the method meets the criteria of this Predicate.  A Predicate is also known as a Discriminator or Filter.
 */
public interface Predicate<T>
        extends Serializable
{
    boolean accept(T each);
}
