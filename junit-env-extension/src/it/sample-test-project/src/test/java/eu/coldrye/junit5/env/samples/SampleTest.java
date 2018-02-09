package eu.coldrye.junit5.env.samples;

import eu.coldrye.junit.env.EnvExtension;
import eu.coldrye.junit.env.EnvProvided;
import eu.coldrye.junit.env.Environment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EnvExtension.class)
@Environment(SampleEnvProvider.class)
public class SampleTest {

    @EnvProvided
    Object provided1;

    @Test
    public void should(@EnvProvided Object provided2) {
    }
}
