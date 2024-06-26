package com.sghqg1ft.test.ex106.util;

import com.sghqg1ft.test.ex106.exception.EmployeeAnalyzerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

  public static Properties read(String filename) {
    Properties properties = new Properties();
    try (InputStream inputStream = PropertiesReader.class.getClassLoader().getResourceAsStream(filename)) {
      if (inputStream != null) {
        properties.load(inputStream);
        return properties;
      } else {
        throw new FileNotFoundException("Property file '" + filename + "' not found in the classpath");
      }
    } catch (IOException e) {
      throw new EmployeeAnalyzerException("Could not read property file '" + filename + "'", e);
    }
  }

}
