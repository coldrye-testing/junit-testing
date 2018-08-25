/*
 * Copyright 2018 coldrye.eu, Carsten Klein
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

package eu.coldrye.junit.util;

import org.junit.platform.commons.util.Preconditions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GenericTypeParameterResolver {

  public static Class<?> resolve(Object instance, Class<?> superType, int paramIndex) {

    Preconditions.notNull(instance, "instance must not be null");
    Preconditions.notNull(superType, "superType must not be null");
    Preconditions.condition(paramIndex >= 0, "paramIndex must be >= 0");

    Preconditions.condition(
      superType.isAssignableFrom(instance.getClass()), "instance must be an instance of superType");

    Map<Type, Map<Type, Type>> lookup = new HashMap<>();
    processInstancesRecursively(instance, lookup);

    if (lookup.containsKey(superType)) {

      return (Class<?>) lookup.get(superType).values().toArray()[paramIndex];
    }

    return null;
  }

  private static void processInstancesRecursively(Object instance, Map<Type, Map<Type, Type>> lookup) {

    Deque<Object> stack = new ArrayDeque<>();
    Object currentInstance = instance;
    boolean hasEnclosingInstance;
    do {

      stack.push(currentInstance);
      hasEnclosingInstance = ReflectionUtils.hasEnclosingInstance(currentInstance);
      if (hasEnclosingInstance) {

        currentInstance = ReflectionUtils.getEnclosingInstance(currentInstance);
      }
    } while (hasEnclosingInstance);

    while(!stack.isEmpty()) {

      processInstance(stack.pop(), lookup);
    }
  }

  private static void processInstance(Object instance, Map<Type, Map<Type, Type>> lookup) {

    Class<?> clazz = instance.getClass();
    while (!Object.class.equals(clazz)) {

      Type type = clazz.getGenericSuperclass();
      if (type instanceof ParameterizedType) {

        ParameterizedType parameterizedType = (ParameterizedType) type;
        populateClazzLookup((Class<?>) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments(), lookup);
        parameterizedType.getOwnerType();
      }

      clazz = clazz.getSuperclass();
    }
  }

  private static void populateClazzLookup(Class<?> clazz, Type[] actualTypeArguments, Map<Type,
    Map<Type, Type>> lookup) {

    Map<Type, Type> clazzLookup = lookup.get(clazz);
    if (Objects.isNull(clazzLookup)) {

      clazzLookup = new LinkedHashMap<>();
      lookup.put(clazz, clazzLookup);
    }

    int index = 0;
    for (TypeVariable<?> typeVariable : clazz.getTypeParameters()) {

      clazzLookup.put(typeVariable, resolveType(actualTypeArguments[index], lookup));
      index++;
    }
  }

  private static Type resolveType(Type key, Map<Type, Map<Type, Type>> lookup) {

    if (key instanceof TypeVariable) {

      for (Map<Type, Type> clazzLookup : lookup.values()) {

        if (clazzLookup.containsKey(key)) {

          return clazzLookup.get(key);
        }
      }
    }

    return key;
  }

  // must not be instantiated
  private GenericTypeParameterResolver() {

  }
}
