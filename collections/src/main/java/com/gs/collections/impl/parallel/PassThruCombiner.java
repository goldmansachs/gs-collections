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

/**
 * A PassThruCombiner doesn't do anything.  It can be used for operations that require no combination, as in a fork
 * with no join step.
 */
public final class PassThruCombiner<T>
        implements Combiner<T>
{
    private static final long serialVersionUID = 1L;

    public void combineAll(Iterable<T> thingsToCombine)
    {
    }

    public void combineOne(T thingToCombine)
    {
    }

    public boolean useCombineOne()
    {
        return true;
    }
}
