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

package eu.coldrye.junit;

import eu.coldrye.junit.Fixtures.Custom1;
import eu.coldrye.junit.Fixtures.Custom2;
import eu.coldrye.junit.Fixtures.CustomProvided;
import eu.coldrye.junit.Fixtures.CustomProvided2;
import eu.coldrye.junit.Fixtures.FirstTestCase;
import eu.coldrye.junit.Fixtures.Provided;
import eu.coldrye.junit.Fixtures.SecondTestCase;
import eu.coldrye.junit.Fixtures.ThirdTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectionHelperTest {

  @Test
  @SuppressWarnings("unchecked")
  public void getAllAnnotationsMustFindAllAnnotations() {

    List<Annotation> collected = ReflectionHelper.getAllAnnotations(FirstTestCase.class, Provided.class,
      CustomProvided.class);
    Assertions.assertEquals(2, collected.size());
  }

  @Test
  public void isAnnotatedByMustReturnFalseIfAnnotatedAndAnnotationIsNotPresentOrInherited() {

    Assertions.assertFalse(ReflectionHelper.isAnnotatedBy(SecondTestCase.class, CustomProvided2.class));
  }

  @Test
  public void isAnnotatedByMustReturnFalseIfAnnotationIsNotPresentOrInherited() {

    Assertions.assertFalse(ReflectionHelper.isAnnotatedBy(ThirdTestCase.class, Provided.class));
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsPresent() {

    Assertions.assertTrue(ReflectionHelper.isAnnotatedBy(FirstTestCase.class, Provided.class));
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsInherited() {

    Assertions.assertTrue(ReflectionHelper.isAnnotatedBy(SecondTestCase.class, Provided.class));
  }

  @Test
  public void getDeclaredFieldsMustReturnAllDeclaredFieldsIncludingInherited() {

    List<Field> fields = ReflectionHelper.getDeclaredFields(SecondTestCase.class, Provided.class);
    Assertions.assertEquals(2, fields.size());
  }

  @Test
  public void findMethodMustFindDeclaredInheritedMethod() {

    Assertions.assertNotNull(ReflectionHelper.findMethod(SecondTestCase.class, "setCustom2", Custom2.class));
  }

  @Test
  public void findMethodMustFindDeclaredMethod() {

    Assertions.assertNotNull(ReflectionHelper.findMethod(SecondTestCase.class, "setCustom1", Custom1.class));
  }

  @Test
  public void findMethodMustReturnNullForUnavailableMethod() throws Exception {

    Assertions.assertNull(ReflectionHelper.findMethod(FirstTestCase.class, "unavailable"));
  }

  @Test
  public void findMethodMustReturnNullForAvailableMethodAndNonMatchingParameterTypes() throws Exception {

    Assertions.assertNull(ReflectionHelper.findMethod(SecondTestCase.class, "setCustom1", Object.class));
  }

  @Test
  public void setterNameMustReturnTheExpectedName() throws Exception {

    Field field = SecondTestCase.class.getDeclaredField("custom");
    Assertions.assertEquals("setCustom", ReflectionHelper.setterName(field));
  }
}
