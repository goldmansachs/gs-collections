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

package com.gs.collections.impl.block.predicate.checked;

import java.io.Serializable;

/**
 * A functional interface that can be represented by a Lambda that can throw a CheckedException.
 */
public interface ThrowingPredicate<T> extends Serializable
{
    @SuppressWarnings("ProhibitedExceptionDeclared")
    boolean safeAccept(T object) throws Exception;
}
