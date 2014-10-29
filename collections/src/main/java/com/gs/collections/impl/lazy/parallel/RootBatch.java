/*
 * Copyright 2014 Goldman Sachs.
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

package com.gs.collections.impl.lazy.parallel;

import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.block.predicate.Predicate;

/**
 * A {@link Batch} that must be at the root of the chain, not wrapped in other Batches.
 */
@Beta
public interface RootBatch<T> extends Batch<T>
{
    boolean anySatisfy(Predicate<? super T> predicate);

    boolean allSatisfy(Predicate<? super T> predicate);

    T detect(Predicate<? super T> predicate);
}
