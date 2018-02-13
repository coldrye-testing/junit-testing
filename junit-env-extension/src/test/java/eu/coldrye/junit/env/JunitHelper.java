package eu.coldrye.junit.env;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ParameterContext;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

final class JunitHelper {

    private JunitHelper() {
    }

    public static ParameterContext createParameterContext(Class<?> testClass, String methodName, int index) {

        Method method = null;
        Method resolvedMethod;
        Parameter resolvedParameter;

        for (Method meth : testClass.getMethods()) {
            if (meth.getName().equals(methodName)) {
                method = meth;
                break;
            }
        }

        Assertions.assertNotNull(method, "method " + methodName + " was not found");

        Parameter[] parameters = method.getParameters();
        Assertions.assertTrue(index < parameters.length,
            "parameter index is out of bounds");

        resolvedParameter = parameters[index];
        resolvedMethod = method;

        return new ParameterContext() {

            @Override
            public Parameter getParameter() {
                return resolvedParameter;
            }

            @Override
            public int getIndex() {
                return index;
            }

            @Override
            public Executable getDeclaringExecutable() {
                return resolvedMethod;
            }

            @Override
            public Optional<Object> getTarget() {
                return Optional.of(testClass);
            }
        };
    }
}
