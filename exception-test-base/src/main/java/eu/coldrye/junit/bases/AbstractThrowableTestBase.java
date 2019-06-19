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

package eu.coldrye.junit.bases;

import eu.coldrye.junit.util.GenericTypeParameterResolver;
import eu.coldrye.junit.util.ReflectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @param <T>
 */
public abstract class AbstractThrowableTestBase<T extends Throwable> {

  /**
   * This test is included for the purpose of getting full coverage for the default constructor, if it exists.
   * <p>
   * This will look for a constructor with no formal parameters.
   */
  @Test
  public void ensureFullCoverageForDefaultConstructor() throws Exception {

    Constructor<T> ctor = ReflectionUtils.resolveConstructor(getThrowableClass());
    if (ctor != null) {

      runConstructorAssertions(null, null, null, "standard default constructor");
    }
  }

  /**
   * This test is included for the purpose of getting full coverage for the default message only constructor, if it
   * exists.
   * <p>
   * This will look for a constructor with a single formal parameter of type {@code String}.
   */
  @Test
  public void ensureFullCoverageForStandardConstructorWithMessage() throws Exception {

    Constructor<T> ctor = ReflectionUtils.resolveConstructor(getThrowableClass(), false, String.class);
    if (ctor != null) {

      runConstructorAssertions(
        () -> new Object[]{"testing"},
        () -> new Class<?>[]{String.class},
        () -> new ThrowableAssertion[]{

          (instance, parameters, parameterTypes) -> Assertions.assertNotNull(instance),
          (instance, parameters, parameterTypes) ->
            Assertions.assertTrue(getThrowableClass().isAssignableFrom(instance.getClass())),
          (instance, parameters, parameterTypes) ->
            Assertions.assertEquals(parameters[0], instance.getMessage(), "message"),
        },
        "standard message constructor"
      );
    }
  }

  /**
   * This test is included for the purpose of getting full coverage for the default message and cause constructor, if
   * it exists.
   * <p>
   * This will look for a constructor with a two formal parameter of type {@code String} and {@code Throwable}.
   */
  @Test
  public void ensureFullCoverageForStandardConstructorWithMessageAndCause() throws Exception {

    Constructor<T> ctor = ReflectionUtils.resolveConstructor(getThrowableClass(), false, String.class, Throwable.class);
    if (ctor != null) {

      Class<?> parameterType = ReflectionUtils.getFormalParameterType(ctor, 1);
      runConstructorAssertions(
        () -> new Object[]{"testing", constructCauseFromFormalParameterType(parameterType)},
        () -> new Class<?>[]{String.class, parameterType},
        () -> new ThrowableAssertion[]{

          (instance, parameters, parameterTypes) ->
            Assertions.assertTrue(parameters[1] != null
              || parameterTypes[1].isAssignableFrom(instance.getCause().getClass()), "causeType"),
          (instance, parameters, parameterTypes) ->
            Assertions.assertEquals(parameters[0], instance.getMessage(), "message"),
          (instance, parameters, parameterTypes) ->
            Assertions.assertTrue(parameters[1] == null
              || ((Throwable) parameters[1]).getMessage().equals(instance.getCause().getMessage()), "cause#message")
        },
        "standard message and cause constructor"
      );
    }
  }

  /**
   * This test is included for the purpose of getting full coverage for the default cause only constructor, if it
   * exists.
   * <p>
   * This will look for a constructor with a single formal parameter of type {@code Throwable}.
   */
  @Test
  public void ensureFullCoverageForStandardConstructorWithCause() throws Exception {

    Constructor<T> ctor = ReflectionUtils.resolveConstructor(getThrowableClass(), false, Throwable.class);
    if (ctor != null) {

      Class<?> parameterType = ReflectionUtils.getFormalParameterType(ctor, 0);
      runConstructorAssertions(
        () -> new Object[]{constructCauseFromFormalParameterType(parameterType)},
        () -> new Class<?>[]{parameterType},
        () -> new ThrowableAssertion[]{

          (instance, parameters, parameterTypes) ->
            Assertions.assertTrue(
              parameterTypes[0].isAssignableFrom(instance.getCause().getClass()), "causeType"),
          (instance, parameters, parameterTypes) ->
            Assertions.assertEquals(
              ((Throwable) parameters[0]).getMessage(), instance.getCause().getMessage(), "cause#message")
        },
        "standard cause only constructor"
      );
    }
  }

  @SuppressWarnings("unchecked")
  protected final Class<T> getThrowableClass() {

    return (Class<T>) GenericTypeParameterResolver.resolve(this, AbstractThrowableTestBase.class, 0);
  }

  protected final void runConstructorAssertions(ParameterSupplier parameterSupplier,
                                                ParameterTypesSupplier parameterTypesSupplier,
                                                ThrowableAssertionsSupplier throwableAssertionsSupplier,
                                                String message) throws Exception {

    Class<T> clazz = getThrowableClass();
    try {

      Class<?>[] parameterTypes = Optional.ofNullable(parameterTypesSupplier).orElse(() -> new Class<?>[]{}).get();
      Constructor<T> ctor = ReflectionUtils.resolveConstructor(clazz, false, parameterTypes);
      if (ctor != null) {

        Object[] parameters = Optional.ofNullable(parameterSupplier).orElse(() -> new Object[]{}).get();
        ThrowableAssertion[] throwableAssertions =
          Optional.ofNullable(throwableAssertionsSupplier).orElse(() -> new ThrowableAssertion[]{}).get();

        ReflectionUtils.makeAccessible(ctor);
        T instance = ctor.newInstance(parameters);
        Executable[] executables = new Executable[throwableAssertions.length + 2];

        executables[0] = () -> Assertions.assertNotNull(instance);
        executables[1] = () -> Assertions.assertTrue(getThrowableClass().isAssignableFrom(instance.getClass()));

        int index = 2;
        for (ThrowableAssertion throwableAssertion : throwableAssertions) {

          executables[index++] = () -> throwableAssertion.apply(instance, parameters, parameterTypes);
        }

        Assertions.assertAll(executables);
      }
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {

      Assertions.fail(message + " of throwable " + clazz.getCanonicalName() + " failed.", ex);
    }
  }

  private Throwable constructCauseFromFormalParameterType(Class<?> parameterType) throws Exception {

    Throwable result = null;
    if (Throwable.class.isAssignableFrom(parameterType)) {

      // try for default constructor first
      Constructor<?> ctor = ReflectionUtils.resolveConstructor(parameterType, true);
      if (ctor != null) {

        result = (Throwable) ctor.newInstance();
      } else {

        // try for default message only ctor next
        ctor = ReflectionUtils.resolveConstructor(parameterType, true, String.class);

        if (ctor != null) {

          result = (Throwable) ctor.newInstance("testing");
        }
        /* and we will not do this, the user must implement his or her own test cases for that
        else {

          // try for default cause only ctor next (recursively)
        }
        */
      }
    }

    return result;
  }

  @FunctionalInterface
  protected interface ThrowableAssertionsSupplier extends Supplier<ThrowableAssertion[]> {

  }

  @FunctionalInterface
  protected interface ParameterTypesSupplier extends Supplier<Class<?>[]> {

  }

  @FunctionalInterface
  protected interface ThrowingSupplier<T> {

    T get() throws Exception;
  }

  @FunctionalInterface
  protected interface ParameterSupplier extends ThrowingSupplier<Object[]> {

  }

  @FunctionalInterface
  protected interface ThrowableAssertion {

    void apply(Throwable instance, Object[] parameters, Class<?>[] parameterTypes) throws Exception;
  }
}
