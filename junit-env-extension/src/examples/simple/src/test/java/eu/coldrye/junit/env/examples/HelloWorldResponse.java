package eu.coldrye.junit.env.examples;

public interface HelloWorldResponse {

  default String data() {
    return "hello world";
  }
}
