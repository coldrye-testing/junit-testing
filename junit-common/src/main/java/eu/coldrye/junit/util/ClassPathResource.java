// TBD:LICENSE

package eu.coldrye.junit.util;

import org.junit.platform.commons.util.Preconditions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public final class ClassPathResource {

  private Class<?> clazz;
  private Path path;

  public static ClassPathResource of(Class<?> clazz, Path path) {

    Preconditions.notNull(clazz, "clazz must not be null"); // NOSONAR
    PreconditionsEx.notBlank(path, "path must not be blank"); // NOSONAR

    return new ClassPathResource(clazz, path);
  }

  public boolean exists() {

    boolean result = false;

    try {

      result = file().exists();
    } catch (IOException ex) {

      // just catch
    }

    return result;
  }

  public File file() throws IOException {

    URL url = clazz.getResource(path.toString());
    if (Objects.isNull(url)) {

      throw new FileNotFoundException(path.toString());
    }

    return new File(url.getFile());
  }

  private ClassPathResource(Class<?> clazz, Path path) {

    this.clazz = clazz;
    this.path = path;
  }
}
