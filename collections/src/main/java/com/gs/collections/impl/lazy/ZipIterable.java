/*
 * Copyright 2015 Goldman Sachs.
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

package com.gs.collections.impl.lazy;

import java.util.Iterator;

import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.lazy.iterator.ZipIterator;
import com.gs.collections.impl.utility.internal.IterableIterate;
import net.jcip.annotations.Immutable;

/**
 * A ZipIterable is an iterable that transforms a source iterable on a condition as it iterates.
 */
@Immutable
public class ZipIterable<X, Y>
        extends AbstractLazyIterable<Pair<X, Y>>
{
    private final Iterable<X> xs;
    private final Iterable<Y> ys;

    public ZipIterable(Iterable<X> xs, Iterable<Y> ys)
    {
        this.xs = xs;
        this.ys = ys;
    }

    public Iterator<Pair<X, Y>> iterator()
    {
        return new ZipIterator<X, Y>(this.xs, this.ys);
    }

    public void each(Procedure<? super Pair<X, Y>> procedure)
    {
        IterableIterate.forEach(this, procedure);
    }
}
