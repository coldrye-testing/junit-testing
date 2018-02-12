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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO document
 *
 * @since 1.0.0
 */
final class EnvProviderCollector {

    /**
     *
     * @param testClass
     * @return
     * @throws Exception
     */
    List<Class<? extends EnvProvider>> collect(Class<?> testClass) throws Exception {

        List<Class<? extends EnvProvider>> result = new ArrayList<>();

        /*
         * Make sure that inherited env providers come first, from top to bottom
         * the very least we can do here is to provide for a hierarchical ordering
         * the user must make sure that the provided EnvProviders on the interface
         * level are side effect free.
         */
        List<Class<? extends EnvProvider>> collectedEnvProviderClasses = new ArrayList<>();
        List<Environments> environmentsContainers = AnnotationsHelper.getAllAnnotations(testClass, Environments.class);
        for (Environments environmentsContainer : environmentsContainers) {
            for (Environment environment : environmentsContainer.value()) {
                collectedEnvProviderClasses.add(environment.value());
            }
        }
        Collections.reverse(collectedEnvProviderClasses);

        for (Class<? extends EnvProvider> envProviderClass : collectedEnvProviderClasses) {
            if (!result.contains(envProviderClass)) {
                result.add(envProviderClass);
            }
        }

        return result;
    }
}
