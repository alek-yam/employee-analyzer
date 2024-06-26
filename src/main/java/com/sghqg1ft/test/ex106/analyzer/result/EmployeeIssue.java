package com.sghqg1ft.test.ex106.analyzer.result;

import com.sghqg1ft.test.ex106.data.model.Employee;

public class EmployeeIssue {
  private final Employee employee;
  private SalaryPolicyViolation salaryPolicyViolation;
  private Integer reportLineLengthExcess;

  public EmployeeIssue(Employee employee) {
    this.employee = employee;
  }

  public EmployeeIssue(Employee employee, SalaryPolicyViolation salaryPolicyViolation, Integer reportLineLengthExcess) {
    this.employee = employee;
    this.salaryPolicyViolation = salaryPolicyViolation;
    this.reportLineLengthExcess = reportLineLengthExcess;
  }

  public Employee getEmployee() {
    return employee;
  }

  public SalaryPolicyViolation getSalaryPolicyViolation() {
    return salaryPolicyViolation;
  }

  public void setSalaryPolicyViolation(SalaryPolicyViolation salaryPolicyViolation) {
    this.salaryPolicyViolation = salaryPolicyViolation;
  }

  public Integer getReportLineLengthExcess() {
    return reportLineLengthExcess;
  }

  public void setReportLineLengthExcess(Integer reportLineLengthExcess) {
    this.reportLineLengthExcess = reportLineLengthExcess;
  }

  public boolean isEmpty() {
    return salaryPolicyViolation == null && reportLineLengthExcess == null;
  }
}
