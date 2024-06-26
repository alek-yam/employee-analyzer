package com.sghqg1ft.test.ex106.util;

import com.sghqg1ft.test.ex106.analyzer.result.AnalysisResult;
import com.sghqg1ft.test.ex106.analyzer.result.EmployeeIssue;
import com.sghqg1ft.test.ex106.analyzer.result.SalaryPolicyViolation;
import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerConfig;
import com.sghqg1ft.test.ex106.data.model.Employee;

public class CommandLineWriter {

  public static void printReport(EmployeeAnalyzerConfig config, AnalysisResult result) {
    if (!result.getEmployeeIssues().isEmpty()) {
      System.out.println("Found issues:");
      result.getEmployeeIssues().forEach(issue -> printLine(config, issue));
    } else {
      System.out.println("Issues not found");
    }
  }

  private static void printLine(EmployeeAnalyzerConfig config, EmployeeIssue issue) {
    StringBuilder reportBuilder = new StringBuilder();
    Employee employee = issue.getEmployee();
    reportBuilder.append(String.format("Employee ID: %d [%s %s]",
        employee.id(), employee.firstName(), employee.lastName()));

    SalaryPolicyViolation salaryPolicyViolation = issue.getSalaryPolicyViolation();
    if (salaryPolicyViolation != null) {
      if (salaryPolicyViolation.isBelowMinLimit()) {
        reportBuilder.append(String.format(", salary is below min limit (%s%%): %s%% / %.2f",
            config.minSalaryRaisePercent(), salaryPolicyViolation.percentDelta(), salaryPolicyViolation.delta()));
      } else if (salaryPolicyViolation.isAboveMaxLimit()) {
        reportBuilder.append(String.format(", salary is above max limit (%s%%): %s%% / %.2f",
            config.maxSalaryRaisePercent(), salaryPolicyViolation.percentDelta(), salaryPolicyViolation.delta()));
      }
    }

    Integer reportLineLengthExcess = issue.getReportLineLengthExcess();
    if (reportLineLengthExcess != null && reportLineLengthExcess > 0) {
      reportBuilder.append(String.format(", report line length is above max limit (%s) by %s additional level(s)",
          config.maxReportLineLength(), reportLineLengthExcess));
    }

    reportBuilder.append(".");
    String employeeIssueReport = reportBuilder.toString();
    System.out.println(employeeIssueReport);
  }
}
