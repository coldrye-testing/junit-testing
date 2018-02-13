package eu.coldrye.junit.env.fixtures;

import eu.coldrye.junit.env.AbstractEnvProvider;
import eu.coldrye.junit.env.EnvPhase;

import java.lang.reflect.AnnotatedElement;

public abstract class AbstractTestEnvProvider extends AbstractEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return false;
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return null;
    }

    @Override
    public void setUpEnvironment(EnvPhase phase) throws Exception {
    }

    @Override
    public void tearDownEnvironment(EnvPhase phase) throws Exception {
    }
}
