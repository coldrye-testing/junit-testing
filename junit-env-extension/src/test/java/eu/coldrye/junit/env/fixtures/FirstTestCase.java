package eu.coldrye.junit.env.fixtures;

import eu.coldrye.junit.env.Environment;

@Environment(EnvProvider3.class)
public class FirstTestCase extends AbstractTestCaseBase implements EnvProvidingInterface {
}
