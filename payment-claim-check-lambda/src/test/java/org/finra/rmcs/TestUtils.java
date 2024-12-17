package org.finra.rmcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class TestUtils {
  public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  private static ObjectMapper objectMapper = null;

  public static String readFileAsString(String fileName) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
      if (is == null) {
        return null;
      }
      try (InputStreamReader isr = new InputStreamReader(is);
          BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }

  public static Map<String, Object> readJsonFileToMap(String fileName) throws Exception {
    return readJsonStringToMap(readFileAsString(fileName));
  }

  public static Map<String, Object> readJsonStringToMap(String jsonStr) throws Exception {
    return getObjectMapper().readerFor(Object.class).readValue(jsonStr);
  }

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      JavaTimeModule javaTimeModule = new JavaTimeModule();
      javaTimeModule.addSerializer(LocalDateTime.class,
          new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN)));
      objectMapper.registerModule(javaTimeModule);
    }
    return objectMapper;
  }
}
