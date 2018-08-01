package eu.coldrye.junit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

  private static final String EXPECTED = "_weird_string";
  private static final String FIXTURE = "\\ weird öÖä^ : , \n` \r  \r\n \" / string";

  @Test
  public void slugMustReturnExpectedResult() {

    Assertions.assertEquals(EXPECTED, StringUtils.slug(FIXTURE));
  }
}
