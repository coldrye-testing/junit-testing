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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO document
 *
 * @since 1.0.0
 */
final class AnnotationsHelper {

    /**
     * Must not be instantiated.
     */
    private AnnotationsHelper() {
    }

    /**
     * Gets the specified annotation from all interfaces implemented by and super classes of the specified class klass.
     *
     * @param klass
     * @param annotation
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    static <T extends Annotation> List<T> getAllAnnotations(Class<?> klass,
                                                            Class<? extends Annotation> annotation) {
        List<T> result = new ArrayList<>();

        List<Class<?>> todo = new ArrayList<>();
        todo.add(klass);
        while (!todo.isEmpty()) {
            Class<?> annotated = todo.remove(0);
            if (annotated.isAnnotationPresent(annotation)) {
                result.add((T) annotated.getAnnotation(annotation));
            }
            Class<?>[] ifaces = annotated.getInterfaces();
            if (ifaces != null) {
                todo.addAll(Arrays.asList(ifaces));
            }
            if (!annotated.isInterface()) {
                Class<?> superClass = annotated.getSuperclass();
                if (superClass != null) {
                    todo.add(superClass);
                }
            }
        }

        return result;
    }
}
