package eu.coldrye.junit.env.examples;

public interface Response {

  default String data() {
    return "hello world";
  }
}
