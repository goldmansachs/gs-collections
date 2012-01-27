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

package ponzu.impl.set.strategy.mutable;

import ponzu.api.block.HashingStrategy;
import ponzu.api.factory.set.strategy.MutableHashingStrategySetFactory;
import ponzu.api.set.MutableSet;

public final class MutableHashingStrategySetFactoryImpl implements MutableHashingStrategySetFactory
{
    public <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(hashingStrategy);
    }

    public <T> MutableSet<T> of(HashingStrategy<? super T> hashingStrategy, T... items)
    {
        return UnifiedSetWithHashingStrategy.newSetWith(hashingStrategy, items);
    }

    public <T> MutableSet<T> ofAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items)
    {
        return UnifiedSetWithHashingStrategy.newSet(hashingStrategy, items);
    }
}
