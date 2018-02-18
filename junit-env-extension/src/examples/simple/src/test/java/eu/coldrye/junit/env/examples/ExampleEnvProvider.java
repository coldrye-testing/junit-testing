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

package eu.coldrye.junit.env.examples;

import eu.coldrye.junit.env.AbstractEnvProvider;
import eu.coldrye.junit.env.EnvPhase;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.lang.reflect.AnnotatedElement;
import java.util.Objects;
import java.util.Optional;

public class ExampleEnvProvider extends AbstractEnvProvider {

  @Override
  public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

    return annotated.isAnnotationPresent(ExampleEnvProvided.class)
      && ExampleClient.class.isAssignableFrom(classOrInterface);
  }

  @Override
  public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

    return getStore().getOrComputeIfAbsent(ExampleClient.class, k -> new ExampleClient());
  }

  @Override
  public void setUpEnvironment(EnvPhase phase, Optional<AnnotatedElement> annotated) throws Exception {

    switch (phase) {
      case INIT:
        break;
      case BEFORE_ALL:
        break;
      case BEFORE_EACH:
        break;
      case AFTER_ALL:
        break;
      case AFTER_EACH:
        break;
      case DEINIT:
        break;
    }
  }

  @Override
  public void tearDownEnvironment(EnvPhase phase, Optional<AnnotatedElement> annotated) throws Exception {

    switch (phase) {
      case INIT:
        break;
      case BEFORE_ALL:
        break;
      case BEFORE_EACH:
        break;
      case AFTER_ALL:
        break;
      case AFTER_EACH:
        break;
      case DEINIT:
        break;
    }
  }
}
