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

import eu.coldrye.junit.util.Fixtures.Custom1;
import eu.coldrye.junit.util.Fixtures.Custom2;
import eu.coldrye.junit.util.Fixtures.CustomProvided;
import eu.coldrye.junit.util.Fixtures.CustomProvided2;
import eu.coldrye.junit.util.Fixtures.FirstTestCase;
import eu.coldrye.junit.util.Fixtures.Provided;
import eu.coldrye.junit.util.Fixtures.SecondTestCase;
import eu.coldrye.junit.util.Fixtures.ThirdTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectionUtilsTest {

  @Test
  @SuppressWarnings("unchecked")
  public void getAllAnnotationsMustFindAllAnnotations() {

    List<Annotation> collected = ReflectionUtils.getAllAnnotations(FirstTestCase.class, Provided.class,
      CustomProvided.class);
    Assertions.assertEquals(2, collected.size());
  }

  @Test
  public void isAnnotatedByMustReturnFalseIfAnnotatedAndAnnotationIsNotPresentOrInherited() {

    Assertions.assertFalse(ReflectionUtils.isAnnotatedBy(SecondTestCase.class, CustomProvided2.class));
  }

  @Test
  public void isAnnotatedByMustReturnFalseIfAnnotationIsNotPresentOrInherited() {

    Assertions.assertFalse(ReflectionUtils.isAnnotatedBy(ThirdTestCase.class, Provided.class));
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsPresent() {

    Assertions.assertTrue(ReflectionUtils.isAnnotatedBy(FirstTestCase.class, Provided.class));
  }

  @Test
  public void isAnnotatedByMustReturnTrueIfAnnotationIsInherited() {

    Assertions.assertTrue(ReflectionUtils.isAnnotatedBy(SecondTestCase.class, Provided.class));
  }

  @Test
  public void getDeclaredFieldsMustReturnAllDeclaredFieldsIncludingInherited() {

    List<Field> fields = ReflectionUtils.getDeclaredFields(SecondTestCase.class, Provided.class);
    Assertions.assertEquals(2, fields.size());
  }

  @Test
  public void findMethodMustFindDeclaredInheritedMethod() {

    Assertions.assertNotNull(ReflectionUtils.findMethod(SecondTestCase.class, "setCustom2", Custom2.class));
  }

  @Test
  public void findMethodMustFindDeclaredMethod() {

    Assertions.assertNotNull(ReflectionUtils.findMethod(SecondTestCase.class, "setCustom1", Custom1.class));
  }

  @Test
  public void findMethodMustReturnNullForUnavailableMethod() throws Exception {

    Assertions.assertNull(ReflectionUtils.findMethod(FirstTestCase.class, "unavailable"));
  }

  @Test
  public void findMethodMustReturnNullForAvailableMethodAndNonMatchingParameterTypes() throws Exception {

    Assertions.assertNull(ReflectionUtils.findMethod(SecondTestCase.class, "setCustom1", Object.class));
  }

  @Test
  public void setterNameMustReturnTheExpectedName() throws Exception {

    Field field = SecondTestCase.class.getDeclaredField("custom");
    Assertions.assertEquals("setCustom", ReflectionUtils.setterName(field));
  }

//  @Test
//  public void resolveMethodMustResolveExpectedMethod() {
//
//    Method method = ReflectionUtils.resolveMethod(
//      "eu.coldrye.junit.assertions.file.util.ReflectionUtilsTest", "resolveMethodMustResolveExpectedMethod");
//
//    Assertions.assertEquals("resolveMethodMustResolveExpectedMethod", method.getName());
//  }
//
//  @Test
//  public void resolveMethodMustResolveExpectedMethodFromClass() {
//
//    Method method = ReflectionUtils.resolveMethod(
//      ReflectionUtilsTest.class, "resolveMethodMustResolveExpectedMethodFromClass");
//
//    Assertions.assertEquals("resolveMethodMustResolveExpectedMethodFromClass", method.getName());
//  }
//
//  @Test
//  public void resolveClassMustResolveExpectedClass() {
//
//    Class<?> clazz = ReflectionUtils.resolveClass("eu.coldrye.junit.assertions.file.util.ReflectionUtilsTest");
//
//    Assertions.assertEquals(ReflectionUtilsTest.class, clazz);
//  }
//
//  @Test
//  public void isAnnotatedByMustReturnTrueOnAvailableAnnotation() {
//
//    Method method = ReflectionUtils.resolveMethod(
//      "eu.coldrye.junit.assertions.file.util.ReflectionUtilsTest", "resolveMethodMustResolveExpectedMethod");
//
//    Assertions.assertTrue(ReflectionUtils.isAnnotatedBy(method, "Test"));
//  }
}
