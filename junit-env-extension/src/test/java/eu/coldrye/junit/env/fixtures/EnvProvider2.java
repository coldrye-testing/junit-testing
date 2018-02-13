package eu.coldrye.junit.env.fixtures;

import java.lang.reflect.AnnotatedElement;

public class EnvProvider2 extends AbstractTestEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return annotated.isAnnotationPresent(EnvProvider2Provided.class)
                   && EnvProvider2ProvidedBoundaryInterface.class.isAssignableFrom(classOrInterface);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        return new EnvProvider2ProvidedBoundaryInterface();
    }
}
