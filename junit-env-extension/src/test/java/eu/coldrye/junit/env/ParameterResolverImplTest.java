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

import eu.coldrye.junit.JunitTestHelper;
import eu.coldrye.junit.env.Fixtures.EnvProvider1;
import eu.coldrye.junit.env.Fixtures.EnvProvider1ProvidedBoundaryInterface;
import eu.coldrye.junit.env.Fixtures.EnvProvider2;
import eu.coldrye.junit.env.Fixtures.EnvProvider2ProvidedBoundaryInterface;
import eu.coldrye.junit.env.Fixtures.FirstTestCase;
import eu.coldrye.junit.env.Fixtures.SecondTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.Arrays;
import java.util.List;

public class ParameterResolverImplTest {

  private ParameterResolverImpl sut;

  private List<EnvProvider> providers;

  @BeforeEach
  public void setUp() {

    sut = new ParameterResolverImpl();
    providers = Arrays.asList(new EnvProvider[]{
      new EnvProvider1(), new EnvProvider2()
    });
  }

  @AfterEach
  public void tearDown() {

    sut = null;
  }

  @Test
  public void resolveParameterMustReturnExpectedResultForInheritedMethodAndCustomAnnotation() throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(FirstTestCase.class,
      "testing", 0, EnvProvider1ProvidedBoundaryInterface.class);
    Object instance = sut.resolveParameter(context, null, providers);
    Assertions.assertTrue(instance instanceof EnvProvider1ProvidedBoundaryInterface);
  }

  @Test
  public void supportsParameterMustReturnTrueForInheritedMethodForParameterWithCustomAnnotationAndSupportedType()
    throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing", 0, EnvProvider1ProvidedBoundaryInterface.class);
    Assertions.assertTrue(sut.supportsParameter(context, null, providers));
  }

  @Test
  public void supportsParameterMustReturnFalseForParameterNotAnnotatedWithEnvProvided()
    throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing4", 0, Object.class);
    Assertions.assertFalse(sut.supportsParameter(context, null, providers));
  }

  @Test
  public void resolveParameterMustReturnExpectedResultForCustomAnnotation() throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing2", 0, EnvProvider2ProvidedBoundaryInterface.class);
    Object instance = sut.resolveParameter(context, null, providers);
    Assertions.assertTrue(instance instanceof EnvProvider2ProvidedBoundaryInterface);
  }

  @Test
  public void supportsParameterMustReturnTrueForParameterWithCustomAnnotationAndSupportedType() throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing2", 0, EnvProvider2ProvidedBoundaryInterface.class);
    Assertions.assertTrue(sut.supportsParameter(context, null, providers));
  }

  @Test
  public void resolveParameterMustFailForParameterWithExpectedCustomAnnotationButUnsupportedType() throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing3", 0, EnvProvider1ProvidedBoundaryInterface.class);
    Assertions.assertThrows(ParameterResolutionException.class, () -> {
      sut.resolveParameter(context, null, providers);
    });
  }

  @Test
  public void supportsParameterMustReturnFalseForParameterWithExpectedCustomAnnotationButUnsupportedType()
    throws Exception {

    ParameterContext context = JunitTestHelper.createParameterContextMock(SecondTestCase.class,
      "testing3", 0, EnvProvider1ProvidedBoundaryInterface.class);
    Assertions.assertFalse(sut.supportsParameter(context, null, providers));
  }
}
