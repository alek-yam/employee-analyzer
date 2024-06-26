package com.sghqg1ft.test.ex106.config;

import com.sghqg1ft.test.ex106.util.PropertiesReader;
import java.util.Properties;

public record EmployeeAnalyzerConfig(Double minSalaryRaisePercent, Double maxSalaryRaisePercent, Integer maxReportLineLength) {
  private static final String APPLICATION_PROPERTIES_FILENAME = "application.properties";
  private static final String MIN_SALARY_RAISE_PERCENT = "employee.salary.min-raise-percent";
  private static final String MAX_SALARY_RAISE_PERCENT = "employee.salary.max-raise-percent";
  private static final String MAX_REPORT_LINE_LENGTH = "employee.report.max-line-length";
  private static final Double DEFAULT_MIN_SALARY_RAISE_PERCENT = 120.;
  private static final Double DEFAULT_MAX_SALARY_RAISE_PERCENT = 150.;
  private static final Integer DEFAULT_MAX_REPORT_LINE_LENGTH = 4;

  public static EmployeeAnalyzerConfig fromApplicationProperties() {
    Properties appProperties = PropertiesReader.read(APPLICATION_PROPERTIES_FILENAME);
    Double minSalaryRaisePercent = getMinSalaryRaisePercent(appProperties);
    Double maxSalaryRaisePercent = getMaxSalaryRaisePercent(appProperties);
    Integer maxReportLineLength = getMaxReportLineLength(appProperties);
    return new EmployeeAnalyzerConfig(minSalaryRaisePercent, maxSalaryRaisePercent, maxReportLineLength);
  }

  private static Double getMinSalaryRaisePercent(Properties properties) {
    Double propertyValue = Double.parseDouble(properties.getProperty(MIN_SALARY_RAISE_PERCENT));
    return getOrDefault(propertyValue, DEFAULT_MIN_SALARY_RAISE_PERCENT);
  }

  private static Double getMaxSalaryRaisePercent(Properties properties) {
    Double propertyValue = Double.parseDouble(properties.getProperty(MAX_SALARY_RAISE_PERCENT));
    return getOrDefault(propertyValue, DEFAULT_MAX_SALARY_RAISE_PERCENT);
  }

  private static Integer getMaxReportLineLength(Properties properties) {
    Integer propertyValue = Integer.parseInt(properties.getProperty(MAX_REPORT_LINE_LENGTH));
    return getOrDefault(propertyValue, DEFAULT_MAX_REPORT_LINE_LENGTH);
  }

  private static <T> T getOrDefault(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }
}
