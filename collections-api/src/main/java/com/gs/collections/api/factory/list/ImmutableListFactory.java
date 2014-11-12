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

package com.gs.collections.api.factory.list;

import com.gs.collections.api.list.ImmutableList;

public interface ImmutableListFactory
{
    /**
     * @since 6.0
     */
    <T> ImmutableList<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableList<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> ImmutableList<T> with();

    /**
     * Same as {@link #with(Object)}.
     */
    <T> ImmutableList<T> of(T one);

    <T> ImmutableList<T> with(T one);

    /**
     * Same as {@link #with(Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two);

    <T> ImmutableList<T> with(T one, T two);

    /**
     * Same as {@link #with(Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three);

    <T> ImmutableList<T> with(T one, T two, T three);

    /**
     * Same as {@link #with(Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four);

    <T> ImmutableList<T> with(T one, T two, T three, T four);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five, T six);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five, T six, T seven);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five, T six, T seven, T eight);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine);

    /**
     * Same as {@link #with(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}.
     */
    <T> ImmutableList<T> of(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine, T ten);

    <T> ImmutableList<T> with(T one, T two, T three, T four, T five, T six, T seven, T eight, T nine, T ten);

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> ImmutableList<T> of(T... items);

    <T> ImmutableList<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> ImmutableList<T> ofAll(Iterable<? extends T> items);

    <T> ImmutableList<T> withAll(Iterable<? extends T> items);
}
