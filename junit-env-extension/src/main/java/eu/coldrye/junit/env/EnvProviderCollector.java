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

import eu.coldrye.junit.util.ReflectionUtils;
import org.junit.platform.commons.util.Preconditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The final class EnvProviderCollector models a means to collect all {@link EnvProvider}S from the inheritance
 * hierarchy of a given test class.
 *
 * @since 1.0.0
 */
class EnvProviderCollector {

  /**
   * @param clazz
   * @return
   * @throws Exception
   */
  List<Class<? extends EnvProvider>> collect(Class<?> clazz) {

    Preconditions.notNull(clazz, "clazz must not be null");

    List<Class<? extends EnvProvider>> result = new ArrayList<>();

    List<Annotation> annotations = ReflectionUtils.getAllAnnotations(clazz, Environments.class, Environment.class);
    for (Annotation annotation : annotations) {
      if (annotation instanceof Environment) {
        Environment environment = (Environment) annotation;
        collect0(environment.value(), result);
      } else { // Environments
        Environments environments = (Environments) annotation;
        for (Environment environment : environments.value()) {
          collect0(environment.value(), result);
        }
      }
    }

    return result;
  }

  /**
   * @param providerClass
   * @param collectedProviderClasses
   */
  private void collect0(Class<? extends EnvProvider> providerClass,
                        List<Class<? extends EnvProvider>> collectedProviderClasses) {

    if (!collectedProviderClasses.contains(providerClass) && !providerClass.isInterface()
      && !Modifier.isAbstract(providerClass.getModifiers())) {
      collectedProviderClasses.add(providerClass);
    }
  }
}
