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

package com.gs.collections.impl.utility.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.tuple.Tuples;
import com.gs.collections.impl.utility.MapIterate;

/**
 * A utility/helper class for working with Classes and Reflection.
 */
public final class ReflectionHelper
{
    /**
     * @deprecated in 2.0. Will become private in a future version.
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public static final Class[] EMPTY_CLASS_ARRAY = {};

    /**
     * Mapping of iterator wrapper classes to iterator types
     */
    private static final ImmutableMap<Class<?>, Class<?>> WRAPPER_TO_PRIMATIVES = UnifiedMap.newMapWith(
            Tuples.<Class<?>>twin(Short.class, short.class),
            Tuples.<Class<?>>twin(Boolean.class, boolean.class),
            Tuples.<Class<?>>twin(Byte.class, byte.class),
            Tuples.<Class<?>>twin(Character.class, char.class),
            Tuples.<Class<?>>twin(Integer.class, int.class),
            Tuples.<Class<?>>twin(Float.class, float.class),
            Tuples.<Class<?>>twin(Long.class, long.class),
            Tuples.<Class<?>>twin(Double.class, double.class)).toImmutable();

    private static final ImmutableMap<Class<?>, Class<?>> PRIMATIVES_TO_WRAPPERS = MapIterate.reverseMapping(WRAPPER_TO_PRIMATIVES.castToMap()).toImmutable();

    private ReflectionHelper()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    // These are special methods that will not produce error messages if the getter method is not found

    public static <T> Constructor<T> getConstructor(Class<T> instantiable, Class<?>... constructorParameterTypes)
    {
        try
        {
            return instantiable.getConstructor(constructorParameterTypes);
        }
        catch (NoSuchMethodException ignored)
        {
            return ReflectionHelper.searchForConstructor(instantiable, constructorParameterTypes);
        }
    }

    private static <T> Constructor<T> searchForConstructor(Class<T> instantiable, Class<?>... constructorParameterTypes)
    {
        Constructor<?>[] candidates = instantiable.getConstructors();
        for (Constructor<?> candidate : candidates)
        {
            if (ReflectionHelper.parameterTypesMatch(candidate.getParameterTypes(), constructorParameterTypes))
            {
                return (Constructor<T>) candidate;
            }
        }
        return null;
    }

    public static boolean parameterTypesMatch(Class<?>[] candidateParamTypes, Class<?>... desiredParameterTypes)
    {
        boolean match = candidateParamTypes.length == desiredParameterTypes.length;
        for (int i = 0; i < candidateParamTypes.length && match; i++)
        {
            Class<?> candidateType = candidateParamTypes[i].isPrimitive() && !desiredParameterTypes[i].isPrimitive()
                    ? PRIMATIVES_TO_WRAPPERS.get(candidateParamTypes[i])
                    : candidateParamTypes[i];
            match = candidateType.isAssignableFrom(desiredParameterTypes[i]);
        }
        return match;
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... constructorArguments)
    {
        try
        {
            return constructor.newInstance(constructorArguments);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method may return null if the call to create a newInstance() fails.
     */
    public static <T> T newInstance(Class<T> aClass)
    {
        try
        {
            return aClass.getConstructor().newInstance();
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasDefaultConstructor(Class<?> aClass)
    {
        try
        {
            Constructor<?> constructor = aClass.getDeclaredConstructor(EMPTY_CLASS_ARRAY);
            return Modifier.isPublic(constructor.getModifiers());
        }
        catch (NoSuchMethodException ignored)
        {
            return false;
        }
    }
}
