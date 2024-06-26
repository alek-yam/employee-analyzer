package com.sghqg1ft.test.ex106.analyzer.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalysisResult {

  private final List<EmployeeIssue> employeeIssues = new ArrayList<>();

  public List<EmployeeIssue> getEmployeeIssues() {
    return Collections.unmodifiableList(employeeIssues);
  }

  public boolean addIssue(EmployeeIssue issue) {
    return employeeIssues.add(issue);
  }

}
