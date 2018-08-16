//package eu.coldrye.junit.util;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Method;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class PathUtilsTest {
//
//  private static final Path ROOT = FileAssertions.DEFAULT_OUTPUT_PATH;
//
//  @Test
//  public void determineRootPathMustReturnExpectedPath() {
//
//    Path actual = PathUtils.determineRootPath(ROOT);
//    Path expected = Paths.get(Paths.get(ROOT.toString(), PathUtilsTest.class.getName().split("[.]")).toString(),
//      "determineRootPathMustReturnExpectedPath");
//
//    Assertions.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void determineReportRootMustReturnExpectedPath() {
//
//    Method method = StackTraceUtils.resolveCaller();
//    Path actual = PathUtils.determineReportRoot(ROOT, method);
//    Path expected = Paths.get(PathUtils.determineRootPath(ROOT, method).toString(), "1");
//    Assertions.assertEquals(expected, actual);
//  }
//}
