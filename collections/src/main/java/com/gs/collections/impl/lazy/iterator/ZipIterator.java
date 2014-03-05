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

package com.gs.collections.impl.lazy.iterator;

import java.util.Iterator;

import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.tuple.Tuples;
import net.jcip.annotations.Immutable;

@Immutable
public final class ZipIterator<X, Y>
        implements Iterator<Pair<X, Y>>
{
    private final Iterator<X> xIterator;
    private final Iterator<Y> yIterator;

    public ZipIterator(Iterable<X> xs, Iterable<Y> ys)
    {
        this.xIterator = xs.iterator();
        this.yIterator = ys.iterator();
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    public boolean hasNext()
    {
        return this.xIterator.hasNext() && this.yIterator.hasNext();
    }

    public Pair<X, Y> next()
    {
        return Tuples.pair(this.xIterator.next(), this.yIterator.next());
    }
}
