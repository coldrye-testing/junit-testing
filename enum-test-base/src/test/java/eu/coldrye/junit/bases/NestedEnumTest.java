package eu.coldrye.junit.bases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class NestedEnumTest extends AbstractEnumTestBase<EnumFixture> {

  @Nested
  public class NestedOne {

    @Test
    public void getEnumClassMustReturnExpectedValue() {

      Assertions.assertEquals(EnumFixture.class, getEnumClass());
    }
  }
}
