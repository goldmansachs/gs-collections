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

package ponzu.api.tuple;

import java.io.Serializable;
import java.util.Map;

/**
 * A Pair is a container that holds two related objects.  It is the equivalent of an Association in Smalltalk, or an
 * implementation of Map.Entry in the JDK.
 */
public interface Pair<T1, T2>
        extends Serializable, Comparable<Pair<T1, T2>>
{
    T1 getOne();

    T2 getTwo();

    void put(Map<T1, T2> map);

    Map.Entry<T1, T2> toEntry();
}
