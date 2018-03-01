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

import org.junit.jupiter.api.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

public interface Fixtures {

  @Environment(EnvProvider2.class)
  interface AnotherEnvProvidingInterface extends SomethingInBetween {

  }

  @EnvProvided
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface EnvProvider1Provided {

  }

  @EnvProvided
  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface EnvProvider2Provided {

  }

  @Target({ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface NotEnvProvided {

  }

  @Environment(EnvProvider2.class)
  interface EnvProvidingInterface {

  }

  interface SomethingInBetween extends EnvProvidingInterface {

  }

  class EnvProvider1ProvidedBoundaryInterface {

  }

  class EnvProvider2ProvidedBoundaryInterface {

  }

  @Environment(EnvProvider1.class)
  abstract class AbstractTestCaseBase {

    @EnvProvider1Provided
    public EnvProvider1ProvidedBoundaryInterface service;

    public EnvProvider1ProvidedBoundaryInterface service1;

    @Test
    public void testing(@EnvProvider1Provided EnvProvider1ProvidedBoundaryInterface service1) {

      this.service1 = service1;
    }

    public void setService(EnvProvider1ProvidedBoundaryInterface service) {

      this.service = service;
    }
  }

  abstract class AbstractTestEnvProvider extends AbstractEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return false;
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return null;
    }

    @Override
    public void setUpEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    }

    @Override
    public void tearDownEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    }
  }

  class EnvProvider1 extends AbstractTestEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return annotated.isAnnotationPresent(EnvProvider1Provided.class)
        && EnvProvider1ProvidedBoundaryInterface.class.isAssignableFrom(classOrInterface);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return new EnvProvider1ProvidedBoundaryInterface();
    }
  }

  class EnvProvider2 extends AbstractTestEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return annotated.isAnnotationPresent(EnvProvider2Provided.class)
        && EnvProvider2ProvidedBoundaryInterface.class.isAssignableFrom(classOrInterface);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return new EnvProvider2ProvidedBoundaryInterface();
    }
  }

  class EnvProvider3 extends AbstractTestEnvProvider {

  }

  class EnvProvider4 extends AbstractTestEnvProvider {

  }

  class EnvProvider5 extends AbstractTestEnvProvider {

  }

  @Environment(EnvProvider3.class)
  class FirstTestCase extends AbstractTestCaseBase implements EnvProvidingInterface {

  }

  @Environment(EnvProvider2.class)
  @Environment(EnvProvider4.class)
  @Environment(EnvProvider5.class)
  class SecondTestCase extends AbstractTestCaseBase implements AnotherEnvProvidingInterface {

    @EnvProvider2Provided
    public EnvProvider2ProvidedBoundaryInterface service2;

    @EnvProvider1Provided
    public EnvProvider1ProvidedBoundaryInterface service3;

    public EnvProvider2ProvidedBoundaryInterface service4;

    public EnvProvider1ProvidedBoundaryInterface service5;

    @EnvProvider2Provided
    public EnvProvider1ProvidedBoundaryInterface service6;

    public Object notEnvProvided;

    @Test
    public void testing2(@EnvProvider2Provided EnvProvider2ProvidedBoundaryInterface service4) {

      this.service4 = service4;
    }

    @Test
    public void testing3(@EnvProvider2Provided EnvProvider1ProvidedBoundaryInterface service5) {

      this.service5 = service5;
    }

    @Test
    public void testing4(@NotEnvProvided Object notEnvProvided) {

      this.notEnvProvided = notEnvProvided;
    }
  }

  class ThirdTestCase {

  }

  @Environment(AbstractTestEnvProvider.class)
  class FourthTestCase {

  }

  @Environment(EnvProvider.class)
  class FifthTestCase {

  }

  class SimpleEnvProvider extends AbstractEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return annotated.isAnnotationPresent(EnvProvided.class) && String.class.isAssignableFrom(classOrInterface);
    }

    @Override
    public String getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {

      return "envProvided";
    }

    @Override
    public void setUpEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    }

    @Override
    public void tearDownEnvironment(EnvPhase phase, AnnotatedElement annotated) throws Exception {

    }
  }
}
