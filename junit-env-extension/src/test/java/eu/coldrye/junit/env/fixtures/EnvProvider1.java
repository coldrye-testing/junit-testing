package eu.coldrye.junit.env.fixtures;

import java.lang.reflect.AnnotatedElement;

public class EnvProvider1 extends AbstractTestEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return annotated.isAnnotationPresent(EnvProvider1Provided.class)
                   && EnvProvider1ProvidedBoundaryInterface.class.isAssignableFrom(classOrInterface);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return new EnvProvider1ProvidedBoundaryInterface();
    }
}
