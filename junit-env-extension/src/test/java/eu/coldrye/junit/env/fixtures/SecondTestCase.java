/*
 * Copyright 2018 coldrye.eu, Carsten Klein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.coldrye.junit.env.fixtures;

import eu.coldrye.junit.env.Environment;
import org.junit.jupiter.api.Test;

@Environment(EnvProvider2.class)
@Environment(EnvProvider4.class)
@Environment(EnvProvider5.class)
public class SecondTestCase extends AbstractTestCaseBase implements AnotherEnvProvidingInterface {

    @EnvProvider2Provided
    public EnvProvider2ProvidedBoundaryInterface service2;

    @EnvProvider1Provided
    public EnvProvider1ProvidedBoundaryInterface service3;

    public EnvProvider2ProvidedBoundaryInterface service4;

    public EnvProvider1ProvidedBoundaryInterface service5;

    @EnvProvider2Provided
    public EnvProvider1ProvidedBoundaryInterface service6;

    @Test
    public void testing2(@EnvProvider2Provided EnvProvider2ProvidedBoundaryInterface service4) {
        this.service4 = service4;
    }

    @Test
    public void testing3(@EnvProvider2Provided EnvProvider1ProvidedBoundaryInterface service5) {
        this.service5 = service5;
    }
}
