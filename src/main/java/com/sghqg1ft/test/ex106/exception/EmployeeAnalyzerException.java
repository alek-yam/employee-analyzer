package com.sghqg1ft.test.ex106.exception;

public class EmployeeAnalyzerException extends RuntimeException {

  public EmployeeAnalyzerException(String message) {
    super(message);
  }

  public EmployeeAnalyzerException(String message, Throwable cause) {
    super(message, cause);
  }

}
