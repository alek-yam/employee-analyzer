package com.sghqg1ft.test.ex106.analyzer.result;

import static org.junit.jupiter.api.Assertions.*;

import com.sghqg1ft.test.ex106.data.model.Employee;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EmployeeIssueTest {

  @Test
  void testIsEmpty() {
    Employee fakeEmployee = createEmployee();
    EmployeeIssue employeeIssue = new EmployeeIssue(fakeEmployee, null, null);
    assertTrue(employeeIssue.isEmpty());
  }

  @ParameterizedTest
  @MethodSource("createNotEmptyIssues")
  void testIsNotEmpty(EmployeeIssue employeeIssue) {
    assertFalse(employeeIssue.isEmpty());
  }

  private static Employee createEmployee() {
    return new Employee(1, "Aleks", "Bone", new BigDecimal(50000), 123);
  }

  private static Stream<EmployeeIssue> createNotEmptyIssues() {
    Employee fakeEmployee = createEmployee();
    SalaryPolicyViolation salaryPolicyViolation = new SalaryPolicyViolation(new BigDecimal(1000), 10.);
    return Stream.of(
        new EmployeeIssue(fakeEmployee, salaryPolicyViolation, null),
        new EmployeeIssue(fakeEmployee, null, 1),
        new EmployeeIssue(fakeEmployee, salaryPolicyViolation, 1));
  }
}