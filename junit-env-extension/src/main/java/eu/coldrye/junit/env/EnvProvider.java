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

import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.AnnotatedElement;

/**
 * TODO document
 */
public interface EnvProvider {

    boolean canProvideInstance(AnnotatedElement annotated);

    Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface);

    void setUpEnvironment(EnvPhase phase) throws Exception;

    void tearDownEnvironment(EnvPhase phase) throws Exception;

    void setStore(Store store);

    Store getStore();
}
