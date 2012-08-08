/*
 * Copyright 2012 kmb.
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

/**
 * Created: 2012-08-07 18:29
 */

package com.webguys.ponzu.impl.tuple;

import java.io.Serializable;

/**
 * A Triple is a container that holds three related types.
 *
 * @param <T1> The first type.
 * @param <T2> The second type.
 * @param <T3> The third type.
 */
public interface Triple<T1, T2, T3>
        extends Serializable, Comparable<Triple<T1, T2, T3>>
{
    T1 getOne();

    T2 getTwo();

    T3 getThree();
}
