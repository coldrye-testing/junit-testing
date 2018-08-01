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
import java.lang.reflect.AnnotatedElement;
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

    Annotation[] annotations = annotated.getAnnotations();
    for (Annotation annotation : annotations) {
      for (Class<? extends Annotation> requested : annotationClasses) {
        Class<? extends Annotation> type = annotation.annotationType();
        if (type.equals(requested) || type.isAnnotationPresent(requested)) {
          return true;
        }
      }
    }
    return false;
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

    Class<?> current = clazz;
    while (true) {
      Method method = ReflectionUtils.findMethod0(current, name, true, parameterTypes);
      if (!Objects.isNull(method) || Object.class.equals(current)) {
        return method;
      }
      current = current.getSuperclass();
    }
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

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getName().equals(name) && (!matchParameters
        || (parameterTypes.length > 0 && Arrays.equals(method.getParameterTypes(), parameterTypes)))) {

        return method;
      }
    }
    return null;
  }

  // must not be instantiated
  //NOSONAR
  private ReflectionUtils() {

  }
}
