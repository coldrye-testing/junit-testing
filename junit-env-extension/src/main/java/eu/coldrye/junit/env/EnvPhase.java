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

/**
 * The enum EnvPhase models a means to control the behaviour of implementations of {@link EnvProvider} during the
 * various phases of both a {@link org.junit.platform.launcher.TestPlan TestPlan}'s execution and the preparation and
 * execution of individual {@link org.junit.jupiter.api.TestInstance TestInstance}S.
 *
 * @since 1.0.0
 */
public enum EnvPhase {

  /**
   * During initialization, i.e. before any tests are being run and also before any test instances will be prepared,
   * {@code EnvProvider}S are instructed to set up their provided environments by a call to
   * {@link EnvProvider#setUpEnvironment(EnvPhase, java.lang.reflect.AnnotatedElement) EnvProvider#setUpEnvironment()}.
   * <p>
   * Implementations of {@code EnvProvider} must prepare themselves for being called multiple times during the
   * {@code INIT} phase when setting up environments that have to be reused by multiple threads.
   */
  INIT,

  /**
   * During shutdown, i.e. all tests have been run, {@code EnvProvider}S are instructed to tear down
   * their provided environments by a call to {@link EnvProvider#tearDownEnvironment(EnvPhase, java.lang.reflect.AnnotatedElement) EnvProvider#tearDownEnvironment()}.
   * <p>
   * Implementations of {@code EnvProvider} must prepare themselves for being called multiple times during the
   * {@code DEINIT} phase when tearing down environments that have to be reused by multiple threads.
   */
  DEINIT,

  /**
   * Before any tests in a given test class will be run, {@code EnvProvider}S are instructed to set up their provided
   * environments by a call to {@code EnvProvider#setUpEnvironment(EnvPhase, Optional)}.
   * <p>
   * It is up to the implementer of the {@code EnvProvider} to decide whether to set up all or only parts of the
   * environment.
   */
  BEFORE_ALL,

  /**
   * After all tests in a given test class have been run, {@code EnvProvider}S are instructed to tear down their
   * provided environments by a call to {@code EnvProvider#tearDownEnvironment(EnvPhase, Optional)}.
   * <p>
   * It is up to the implementer of the {@code EnvProvider} to decide whether to tear down all or only parts of the
   * environment.
   */
  AFTER_ALL,

  /**
   * Before each test in a given test class, {@code EnvProvider}S are instructed to set up their provided
   * environments by a call to {@code EnvProvider#setUpEnvironment(EnvPhase, Optional)}.
   * <p>
   * It is up to the implementer of the {@code EnvProvider} to decide whether to set up all or only parts of the
   * environment.
   */
  BEFORE_EACH,

  /**
   * After each test in a given test class, {@code EnvProvider}S are instructed to tear down their
   * provided environments by a call to {@code EnvProvider#tearDownEnvironment(EnvPhase, Optional)}.
   * <p>
   * It is up to the implementer of the {@code EnvProvider} to decide whether to tear down all
   * or only parts of the environment.
   */
  AFTER_EACH,

  /**
   * This is the phase where instance post processing and method parameter resolution takes place.
   */
  PREPARE
}
