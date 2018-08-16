// TBD:LICENSE

package eu.coldrye.junit.util;

import java.util.Objects;

public final class StringUtils {

  public static final String EMPTY = "";

  /**
   * Returns a simplified string where all non alpha numerical ascii characters have been replaced by a single
   * underscore character.
   *
   * @param str
   * @return
   */
  public static String slug(String str) {

    String result = EMPTY;

    if (Objects.nonNull(str)) {

      result = str.replaceAll("[^a-zA-Z0-9]", "_");
      result = result.replaceAll("_+", "_");
    }

    return result;
  }

  // must not be instantiated
  private StringUtils() {

  }
}
