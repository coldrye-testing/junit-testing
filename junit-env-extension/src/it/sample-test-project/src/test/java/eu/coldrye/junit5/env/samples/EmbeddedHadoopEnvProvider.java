package eu.coldrye.junit5.env.samples;

import eu.coldrye.junit.env.AbstractEnvProvider;
import eu.coldrye.junit.env.EnvPhase;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.AnnotatedElement;

public final class EmbeddedHadoopEnvProvider extends AbstractEnvProvider {

    @Override
    public boolean canProvideInstance(AnnotatedElement annotated) {
        return annotated.isAnnotationPresent(HadoopEnvProvided.class);
    }

    @Override
    public Object getOrCreateInstance(AnnotatedElement annotated, Class<?> classOrInterface) {
        Store store = getStore();

        throw new RuntimeException("not implemented yet");
        // TODO: must we create a new instance every time?
        //return new HadoopAbstractFactory().createNewInstance(classOrInterface);
    }

    @Override
    public void setUpEnvironment(EnvPhase phase) throws Exception {
        Store store = getStore();

        switch (phase) {
            case INIT: {
                break;
            }
        }
        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void tearDownEnvironment(EnvPhase phase) throws Exception {
        Store store = getStore();

        switch (phase) {
            case DEINIT: {
                break;
            }
        }
        throw new RuntimeException("not implemented yet");
    }
}
