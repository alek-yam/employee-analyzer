package com.sghqg1ft.test.ex106.exception;

public class InvalidCsvFormat extends EmployeeAnalyzerException {

  public InvalidCsvFormat(String message) {
    super(message);
  }

  public InvalidCsvFormat(String message, Throwable cause) {
    super(message, cause);
  }

}
