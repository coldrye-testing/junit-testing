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

package eu.coldrye.junit.env;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The final class ReflectionHelper provides static helpers to deal with annotations.
 *
 * @since 1.0.0
 */
final class ReflectionHelper {

    /**
     * Must not be instantiated.
     */
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
    static List<Annotation> getAllAnnotations(Class<?> klass, Class<? extends Annotation>... annotationClasses) {
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
            if (!Objects.isNull(ifaces)) {
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

    static boolean isAnnotatedBy(AnnotatedElement annotated, Class<? extends Annotation>... annotationClasses) {
        boolean result = false;

        Annotation[] annotations = annotated.getAnnotations();
        for (Annotation annotation : annotations) {
            for (Class<? extends Annotation> requested : annotationClasses) {
                if (annotation.annotationType().isAnnotationPresent(requested)
                        || annotation.getClass().equals(requested)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * @param klass
     * @return
     */
    static List<Field> getDeclaredFields(Class<?> klass, Class<? extends Annotation>... annotationClasses) {
        List<Field> result = new ArrayList<>();

        Class<?> current = klass;
        while (true) {
            Field[] declaredFields = current.getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (ReflectionHelper.isAnnotatedBy(field, annotationClasses)) {
                        result.add(field);
                    }
                }
            }
            current = current.getSuperclass();
            if ((Object.class.equals(current))) {
                break;
            }
        }

        return result;
    }
}
