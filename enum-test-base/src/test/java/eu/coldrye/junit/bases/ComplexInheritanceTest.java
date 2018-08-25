package eu.coldrye.junit.bases;

import eu.coldrye.junit.bases.AbstractOuterTestBase.AbstractInnerTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComplexInheritanceTest extends AbstractInnerTestBase<String, EnumFixture> {

  @Test
  public void mustWorkWithComplexInheritance() {

    Assertions.assertEquals(EnumFixture.class, getEnumClass());
  }
}
