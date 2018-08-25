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

package eu.coldrye.junit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.PreconditionViolationException;

public class GenericTypeParameterResolverTest {

  @Test
  public void resolveMustReturnNullForNonGenericType() {

    Assertions.assertNull(GenericTypeParameterResolver.resolve("", String.class, 0));
  }

  @Test
  public void resolveMustFailOnIllegalArguments() {

    Assertions.assertAll(
      () -> Assertions.assertThrows(
        PreconditionViolationException.class,
        () -> GenericTypeParameterResolver.resolve(null, Fixtures.Abstract.class, 0), "null instance"),
      () -> Assertions.assertThrows(
        PreconditionViolationException.class,
        () -> GenericTypeParameterResolver.resolve(new Object(), null, 0), "null superType"),
      () -> Assertions.assertThrows(
        PreconditionViolationException.class,
        () -> GenericTypeParameterResolver.resolve(new Object(), Fixtures.Abstract.class, -1), "negative paramIndex"),
      () -> Assertions.assertThrows(
        PreconditionViolationException.class,
        () -> GenericTypeParameterResolver.resolve(new Object(), Fixtures.Abstract.class, 0), "not instanceof")
    );
  }

  @Test
  public void resolveMustNotFailOnSimpleConcreteForAbstractBase() {

    Object instance = new Fixtures.SimpleConcrete();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.Abstract.class, 0);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  @Test
  public void resolveMustNotFailOnConcreteOuterForAbstractBase() {

    Object instance = new Fixtures.ConcreteOuter();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.Abstract.class, 0);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  @Test
  public void resolveMustNotFailOnConcreteInnerForAbstractBase() {

    Object instance = new Fixtures.ConcreteInner();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.Abstract.class, 0);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  @Test
  public void resolveMustNotFailOnConcreteInnerForAbstractOuter() {

    Object instance = new Fixtures.ConcreteInner();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.AbstractOuter.class, 0);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  @Test
  public void resolveMustNotFailOnInnerConcreteForAbstractOuter() {

    Object instance = new Fixtures.WithInnerInstanceConcrete().new InnerConcrete();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.AbstractOuter.class, 0);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  @Test
  public void resolveMustNotFailOnConcreteInnerForAbstractInner() {

    Object instance = new Fixtures.ConcreteInner();
    Object resolved = GenericTypeParameterResolver.resolve(instance, Fixtures.AbstractOuter.AbstractInner.class, 1);

    Assertions.assertEquals(Fixtures.TestEnum.class, resolved);
  }

  public interface Fixtures {

    enum TestEnum {
      ECONST;
    }

    abstract class Abstract<V extends Enum<V>> {

    }

    abstract class AbstractOuter<V extends Enum<V>> extends Abstract<V> {

      public static abstract class AbstractInner<O, X extends Enum<X>> extends AbstractOuter<X> {

      }
    }

    class WithInnerInstance<R extends Enum<R>> extends Abstract<R> {

      public class InnerConcrete extends AbstractOuter<R> {

      }
    }

    class WithInnerInstanceConcrete extends WithInnerInstance<TestEnum> {

    }

    class SimpleConcrete extends Abstract<TestEnum> {

    }

    class ConcreteInner extends AbstractOuter.AbstractInner<String, TestEnum> {

    }

    class ConcreteOuter extends AbstractOuter<TestEnum> {

    }
  }
}
