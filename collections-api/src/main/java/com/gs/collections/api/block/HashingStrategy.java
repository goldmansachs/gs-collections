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

package com.gs.collections.api.block;

import java.io.Serializable;

/**
 * Interface for supporting user defined hashing strategies in Sets and Maps
 */
public interface HashingStrategy<E>
        extends Serializable
{
    /**
     * Computes the hashCode of the object as defined by the user.
     */
    int computeHashCode(E object);

    /**
     * Checks two objects for equality. The equality check can use the objects own equals() method or
     * a custom method defined by the user. It should be consistent with the computeHashCode() method.
     */
    boolean equals(E object1, E object2);
}
