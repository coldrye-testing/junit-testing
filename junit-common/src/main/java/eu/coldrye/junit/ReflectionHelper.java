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

package eu.coldrye.junit;

import org.junit.platform.commons.util.Preconditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The final class ReflectionHelper provides static helpers to deal with annotations.
 *
 * @since 1.0.0
 */
public final class ReflectionHelper {

  private static final String PRECOND_CLASS_MUST_NOT_BE_NULL = "klass must not be null";

  private static final String PRECOND_ANNOTATION_CLASSES_MUST_NOT_BE_EMPTY = "annotationClasses must not be empty";

  /**
   * Must not be instantiated.
   */
  //NOSONAR
  private ReflectionHelper() {

  }

  /**
   * Gets the specified annotations from all interfaces implemented by and super classes of the specified class klass.
   *
   * @param klass
   * @param annotationClasses
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<Annotation> getAllAnnotations(Class<?> klass, Class<? extends Annotation>... annotationClasses) {

    Preconditions.notNull(klass, PRECOND_CLASS_MUST_NOT_BE_NULL);
    Preconditions.notEmpty(annotationClasses, PRECOND_ANNOTATION_CLASSES_MUST_NOT_BE_EMPTY);

    List<Annotation> result = new ArrayList<>();

    List<Class<?>> todo = new ArrayList<>();
    todo.add(klass);
    while (!todo.isEmpty()) {
      Class<?> annotated = todo.remove(0);
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
          todo.add(superClass);
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

    Preconditions.notNull(annotated, "annotated must not be null");
    Preconditions.notEmpty(annotationClasses, PRECOND_ANNOTATION_CLASSES_MUST_NOT_BE_EMPTY);

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

  /**
   * @param klass
   * @param annotationClasses
   * @return
   */
  public static List<Field> getDeclaredFields(Class<?> klass, Class<? extends Annotation>... annotationClasses) {

    Preconditions.notNull(klass, PRECOND_CLASS_MUST_NOT_BE_NULL);
    Preconditions.notEmpty(annotationClasses, PRECOND_ANNOTATION_CLASSES_MUST_NOT_BE_EMPTY);

    List<Field> result = new ArrayList<>();

    Class<?> current = klass;
    while (true) {
      Field[] declaredFields = current.getDeclaredFields();
      for (Field field : declaredFields) {
        if (ReflectionHelper.isAnnotatedBy(field, annotationClasses)) {
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
   * @param klass
   * @param name
   * @param parameterTypes
   * @return
   * @throws Exception
   */
  public static Method findMethod(Class<?> klass, String name, Class<?>... parameterTypes) {

    Preconditions.notNull(klass, PRECOND_CLASS_MUST_NOT_BE_NULL);
    Preconditions.notNull(name, "name must not be null");
    Preconditions.notBlank(name, "name must not be blank");

    Class<?> current = klass;
    while (true) {
      Method method = ReflectionHelper.findMethod0(current.getDeclaredMethods(), name, parameterTypes);
      if (!Objects.isNull(method) || Object.class.equals(current)) {
        return method;
      }
      current = current.getSuperclass();
    }
  }

  /**
   * @param methods
   * @param name
   * @param parameterTypes
   * @return
   */
  private static Method findMethod0(Method[] methods, String name, Class<?>... parameterTypes) {

    for (Method method : methods) {
      if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
        return method;
      }
    }
    return null;
  }

  /**
   * @param field
   * @return
   */
  public static String setterName(Field field) {

    Preconditions.notNull(field, "field must not be null");

    return "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
  }
}
