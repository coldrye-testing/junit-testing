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

package eu.coldrye.junit.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Fixtures {

  private Fixtures() {

  }

  @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface Provided {

  }

  @Provided
  @Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface CustomProvided {

  }

  @Provided
  @Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface CustomProvided2 {

  }

  @CustomProvided
  public interface Iface1 {

  }

  public interface DerivedIface extends Iface1 {

  }

  public static class Custom1 {

  }

  public static class Custom2 {

  }

  public static abstract class TestCaseBase {

    @CustomProvided
    Custom1 custom1;

    Custom2 custom2;

    public void setCustom2(@CustomProvided Custom2 custom2) {

      this.custom2 = custom2;
    }
  }

  @Provided
  public static class FirstTestCase implements DerivedIface {

  }

  @CustomProvided
  public static class SecondTestCase extends TestCaseBase {

    @Provided
    Custom1 custom;

    public void setCustom1(@CustomProvided Custom1 custom) {

      this.custom = custom;
    }
  }

  public static class ThirdTestCase {

  }
}
