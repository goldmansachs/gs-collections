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

package com.webguys.ponzu.api.factory.list;

import com.webguys.ponzu.api.list.FixedSizeList;

public interface FixedSizeListFactory
{
    <T> FixedSizeList<T> of();

    <T> FixedSizeList<T> of(T one);

    <T> FixedSizeList<T> of(T one, T two);

    <T> FixedSizeList<T> of(T one, T two, T three);

    <T> FixedSizeList<T> of(T one, T two, T three, T four);

    <T> FixedSizeList<T> of(T one, T two, T three, T four, T five);

    <T> FixedSizeList<T> of(T one, T two, T three, T four, T five, T six);

    <T> FixedSizeList<T> of(T... items);

    <T> FixedSizeList<T> ofAll(Iterable<? extends T> items);
}
