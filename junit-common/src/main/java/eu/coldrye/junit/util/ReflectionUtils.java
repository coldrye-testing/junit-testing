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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * The final class ReflectionUtils provides static helpers to deal with annotations.
 *
 * @since 1.0.0
 */
public final class ReflectionUtils {

  // must not be instantiated
  private ReflectionUtils() {

  }

  /**
   * Gets the specified annotations from all interfaces implemented by and super classes of the specified class clazz.
   *
   * @param clazz
   * @param annotationClasses
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Annotation> getAllAnnotations(Class<?> clazz, Class<? extends Annotation>... annotationClasses) {

    Preconditions.notNull(clazz, "clazz must not be null"); // NOSONAR
    Preconditions.notEmpty(annotationClasses, "annotationClasses must not be empty"); // NOSONAR

    List<Annotation> result = new ArrayList<>();

    Deque<Class<?>> todo = new ArrayDeque<>();
    todo.push(clazz);
    while (!todo.isEmpty()) {

      Class<?> annotated = todo.pop();
      for (Class<? extends Annotation> annotationClass : annotationClasses) {

        if (annotated.isAnnotationPresent(annotationClass)) {

          result.add(annotated.getAnnotation(annotationClass));
        }
      }

      Class<?>[] ifaces = annotated.getInterfaces();
      if (ifaces.length > 0) {

        todo.addAll(Arrays.asList(ifaces));
      }
      if (!annotated.isInterface()) {

        Class<?> superClass = annotated.getSuperclass();
        if (!Objects.isNull(superClass)) {

          todo.push(superClass);
        }
      }
    }

    return result;
  }

  /**
   * @param annotated
   * @param annotationClasses
   * @return
   */
  public static boolean isAnnotatedBy(AnnotatedElement annotated, Class<? extends Annotation>... annotationClasses) {

    Preconditions.notNull(annotated, "annotated must not be null"); // NOSONAR
    Preconditions.notEmpty(annotationClasses, "annotationClasses must not be empty"); // NOSONAR

    boolean result = false;

    Annotation[] annotations = annotated.getAnnotations();
    for (Annotation annotation : annotations) {

      for (Class<? extends Annotation> requested : annotationClasses) {

        Class<? extends Annotation> type = annotation.annotationType();
        if (type.equals(requested) || type.isAnnotationPresent(requested)) {

          result = true;
          break;
        }
      }
    }

    return result;
  }

  public static boolean isAnnotatedBy(AnnotatedElement annotated, String annotationName) {

    Preconditions.notNull(annotated, "annotated must not be null"); // NOSONAR
    Preconditions.notBlank(annotationName, "annotationName must not be blank"); // NOSONAR

    for (Annotation annotation : annotated.getAnnotations()) {

      if (annotationName.equals(annotation.annotationType().getSimpleName())) {

        return true;
      }
    }

    return false;
  }

  /**
   * @param clazz
   * @param annotationClasses
   * @return
   */
  public static List<Field> getDeclaredFields(Class<?> clazz, Class<? extends Annotation>... annotationClasses) {

    Preconditions.notNull(clazz, "clazz must not be null"); // NOSONAR
    Preconditions.notEmpty(annotationClasses, "annotationClasses must not be empty"); // NOSONAR

    List<Field> result = new ArrayList<>();

    Class<?> current = clazz;
    while (true) {

      Field[] declaredFields = current.getDeclaredFields();
      for (Field field : declaredFields) {

        if (ReflectionUtils.isAnnotatedBy(field, annotationClasses)) {

          result.add(field);
        }
      }
      current = current.getSuperclass();

      // we must break on Object.class
      if ((Object.class.equals(current))) {

        break;
      }
    }

    return result;
  }

  /**
   * @param clazz
   * @param name
   * @param parameterTypes
   * @return
   * @throws Exception
   */
  public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {

    Preconditions.notNull(clazz, "clazz must not be null"); // NOSONAR
    Preconditions.notBlank(name, "name must not be blank"); // NOSONAR

    Method result = null;

    Class<?> current = clazz;
    while (true) {

      Method method = ReflectionUtils.findMethod0(current, name, true, parameterTypes);
      if (!Objects.isNull(method) || Object.class.equals(current)) {

        result = method;
        break;
      }
      current = current.getSuperclass();
    }

    return result;
  }

  public static Method findMethod(String className, String methodName) {

    return findMethod0(loadClass(className), methodName, false);
  }

  public static Class<?> loadClass(String className) {

    Preconditions.notBlank(className, "className must not be blank"); // NOSONAR

    try {

      return Class.forName(className);
    } catch (ClassNotFoundException ex) {

      throw new UnexpectedError("Class " + className + " cannot be loaded.", ex);
    }
  }

  public static boolean isLambda(String name) {

    Preconditions.notBlank(name, "name must not be blank"); // NOSONAR

    return name.startsWith("lambda$");
  }

  /**
   * @param field
   * @return
   */
  public static String setterName(Field field) {

    Preconditions.notNull(field, "field must not be null"); // NOSONAR

    return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
  }

  /**
   * @param clazz
   * @param name
   * @param matchParameters
   * @param parameterTypes
   * @return
   */
  private static Method findMethod0(Class<?> clazz, String name, boolean matchParameters, Class<?>... parameterTypes) {

    Method result = null;

    for (Method method : clazz.getDeclaredMethods()) {

      if (method.getName().equals(name)
        && (!matchParameters || (formalAndRequiredParameterTypesMatch(matchParameters, method.getParameterTypes(), parameterTypes)))) {

        result = method;
        break;
      }
    }

    return result;
  }

  public static boolean hasEnclosingInstance(Object instance) {

    return Arrays.stream(instance.getClass().getDeclaredFields()).anyMatch(f -> "this$0".equals(f.getName()));
  }

  public static Object getEnclosingInstance(Object instance) {

    Object result = null;

    if (hasEnclosingInstance(instance)) {

      try {

        Field field = instance.getClass().getDeclaredField("this$0");
        field.setAccessible(true);

        result = field.get(instance);
      } catch (IllegalAccessException | NoSuchFieldException ex) {

        // ignore
      }
    }

    return result;
  }

  public static <T> Constructor<T> resolveConstructor(Class<T> clazz, Class<?>... parameterTypes) {

    return resolveConstructor(clazz, true, parameterTypes);
  }

  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> resolveConstructor(Class<T> clazz, boolean strict, Class<?>... parameterTypes) {

    Constructor<T> result = null;

    for (Constructor<?> ctor : clazz.getConstructors()) {

      if (formalAndRequiredParameterTypesMatch(strict, ctor.getParameterTypes(), parameterTypes)) {

        result = (Constructor<T>) ctor;
        break;
      }
    }

    return result;
  }

  public static boolean formalAndRequiredParameterTypesMatch(boolean strict, Class<?>[] formalParameterTypes, Class<?>[] requiredParameterTypes) {

    boolean result = false;

    // the default constructor
    if (formalParameterTypes.length == 0 && requiredParameterTypes.length == 0) {

      result = true;
    } else if (formalParameterTypes.length == requiredParameterTypes.length) {

      result = true;
      for (int index = 0; index < formalParameterTypes.length; index++) {

        Class<?> formalParameterType = formalParameterTypes[index];
        Class<?> requiredParameterType = requiredParameterTypes[index];

        if (formalParameterType != requiredParameterType) {

          if (strict && !formalParameterType.isAssignableFrom(requiredParameterType)
            || !strict && !requiredParameterType.isAssignableFrom(formalParameterType)) {

            result = false;
            break;
          }
        }
      }
    }

    return result;
  }

  public static void makeAccessible(AccessibleObject accessibleObject) {

    accessibleObject.setAccessible(true);
  }

  public static Class<?> getFormalParameterType(Executable executable, int index) {

    return executable.getParameters()[index].getType();
  }
}
