package lab.nice.excel.bloom.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public final class FileHelper {
    private FileHelper() {

    }

    /**
     * Read properties value from file
     *
     * @param file the properties file
     * @return properties with values read from file
     * @throws IOException if failed to read properties file
     */
    public static Properties readProperties(String file) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get(file), StandardOpenOption.READ)) {
            properties.load(inputStream);
        }
        return properties;
    }
}
