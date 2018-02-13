package eu.coldrye.junit.env.fixtures;

import eu.coldrye.junit.env.Environment;
import org.junit.jupiter.api.Test;

@Environment(EnvProvider1.class)
public abstract class AbstractTestCaseBase {

    @EnvProvider1Provided
    public EnvProvider1ProvidedBoundaryInterface service;

    public EnvProvider1ProvidedBoundaryInterface service1;

    @Test
    public void testing(@EnvProvider1Provided EnvProvider1ProvidedBoundaryInterface service1) {
        this.service1 = service1;
    }
}
