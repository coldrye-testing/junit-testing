package eu.coldrye.junit5.env.samples;

import eu.coldrye.junit.env.AbstractEnvProvider;
import eu.coldrye.junit.env.EnvPhase;
import eu.coldrye.junit.env.EnvProvided;

import java.lang.reflect.AnnotatedElement;
import java.util.logging.Logger;

public class SampleEnvProvider extends AbstractEnvProvider {

    Logger log = Logger.getLogger(SampleEnvProvider.class.getName());

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        System.out.println("canProvideInstance: " + annotated);
        return annotated.isAnnotationPresent(EnvProvided.class);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        System.out.println("getOrCreateInstance: " + annotated + " : " + classOrInterface);
        return new Object();
    }

    @Override
    public void setUpEnvironment(EnvPhase phase) throws Exception {
        System.out.println("setUpEnvironment: " + phase);
    }

    @Override
    public void tearDownEnvironment(EnvPhase phase) throws Exception {
        System.out.println("phase: " + phase);
        if (phase == EnvPhase.DEINIT) {
            throw new Exception("shize happened");
        }
    }
}
