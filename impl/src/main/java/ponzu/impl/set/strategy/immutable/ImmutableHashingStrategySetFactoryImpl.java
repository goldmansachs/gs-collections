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

package ponzu.impl.set.strategy.immutable;

import ponzu.api.block.HashingStrategy;
import ponzu.api.factory.set.strategy.ImmutableHashingStrategySetFactory;
import ponzu.api.set.ImmutableSet;
import ponzu.impl.utility.Iterate;

public final class ImmutableHashingStrategySetFactoryImpl implements ImmutableHashingStrategySetFactory
{
    public <T> ImmutableSet<T> of(HashingStrategy<? super T> hashingStrategy)
    {
        return new ImmutableEmptySetWithHashingStrategy<T>(hashingStrategy);
    }

    public <T> ImmutableSet<T> of(HashingStrategy<? super T> hashingStrategy, T... items)
    {
        if (items == null || items.length == 0)
        {
            return this.of(hashingStrategy);
        }

        return ImmutableUnifiedSetWithHashingStrategy.newSetWith(hashingStrategy, items);
    }

    public <T> ImmutableSet<T> ofAll(HashingStrategy<? super T> hashingStrategy, Iterable<? extends T> items)
    {
        return this.of(hashingStrategy, (T[]) Iterate.toArray(items));
    }
}
