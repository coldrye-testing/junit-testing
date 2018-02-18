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
 * The interface EnvProvider models a provider for means to both set up and
 * tear down environments during individual {@link EnvPhase}S and also to
 * provide instances of boundary services that the environment provides.
 *
 * @since 1.0.0
 */
public interface EnvProvider {

  /**
   * Returns true whether this can provide an instance for the annotated
   * element, which can be either a {@link java.lang.reflect.Field} or a
   * {@link java.lang.reflect.Parameter}.
   * <p>
   * This will determine from the available annotations and also the
   * classOrInterface, whether an instance can be provided.
   *
   * @param annotated        the annotated element
   * @param classOrInterface the class or interface
   * @return
   */
  boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface);

  /**
   * Once it was determined by {@link #canProvideInstance(AnnotatedElement, Class)}
   * that this can actually provide an instance for the requested classOrInterface,
   * this will be called in order to resolve the instance.
   *
   * @param annotated
   * @param classOrInterface
   * @return
   */
  Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface);

  /**
   * During specific {@link EnvPhase}S, namely {@link EnvPhase#INIT}, {@link EnvPhase#BEFORE_ALL},
   * and {@link EnvPhase#BEFORE_EACH}, this will be called and the provider is
   * free to set up the environment as is requested, or simply ignore the specified
   * phase and take no action.
   *
   * @param phase
   * @param annotated
   * @throws Exception
   */
  void setUpEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception;

  /**
   * During specific {@link EnvPhase}S, namely {@link EnvPhase#AFTER_EACH}, {@link EnvPhase#AFTER_ALL},
   * and {@link EnvPhase#DEINIT}, this will be called and the provider is
   * free to tear down the environment as is requested, or simply ignore the
   * specified phase and take no action.
   *
   * @param phase
   * @param annotated
   * @throws Exception
   */
  void tearDownEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception;

  /**
   * Gets the underlying store.
   *
   * @return
   */
  Store getStore();

  /**
   * Sets the underlying store, which is specific for each instance of this
   * and for a specific {@link Thread}.
   * <p>
   * The provided store is shared between multiple instances of the same
   * provider that have been instantiated by the same thread.
   *
   * @param store
   */
  void setStore(Store store);
}
