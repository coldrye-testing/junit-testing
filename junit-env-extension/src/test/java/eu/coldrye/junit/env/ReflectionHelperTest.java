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

import eu.coldrye.junit.env.Fixtures.SecondTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectionHelperTest {

  @Test
  @SuppressWarnings("unchecked")
  public void getAllAnnotationsMustFindAllAnnotations() {

    List<Annotation> collected = ReflectionHelper.getAllAnnotations(SecondTestCase.class, Environments.class,
      Environment.class);
    Assertions.assertEquals(4, collected.size());
  }

  @Test
  public void isAnnotatedByMustReturnFalseIfAnnotationIsNotPresentOrInherited() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsPresent() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsInherited() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void getDeclaredFieldsMustReturnAllDeclaredFieldsIncludingInherited() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void findMethodMustFindDeclaredMethod() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void findMethodMustFindInheritedMethod() {

    Assertions.fail("not implemented yet");
  }

  @Test
  public void findMethodMustReturnNullForUnavailableMethod() throws Exception {

    Assertions.assertNull(ReflectionHelper.findMethod(SecondTestCase.class, "unavailable"));
  }

  @Test
  public void findMethodMustReturnNullForAvailableMethodAndNonMatchingParameterTypes() throws Exception {

    Assertions.assertNull(ReflectionHelper.findMethod(SecondTestCase.class, "testing2", Object.class));
  }

  @Test
  public void setterNameMustReturnTheExpectedName() throws Exception {

    Field field = SecondTestCase.class.getDeclaredField("service2");
    Assertions.assertEquals("setService2", ReflectionHelper.setterName(field));
  }
}
