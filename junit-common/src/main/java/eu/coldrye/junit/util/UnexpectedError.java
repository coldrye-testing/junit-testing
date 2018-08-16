// TBD:LICENSE

package eu.coldrye.junit.util;

public class UnexpectedError extends Error {

  public UnexpectedError(String message) {

    super(message);
  }

  public UnexpectedError(String message, Throwable cause) {

    super(message, cause);
  }
}
