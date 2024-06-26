package com.sghqg1ft.test.ex106.analyzer.result;

import static org.junit.jupiter.api.Assertions.*;

import com.sghqg1ft.test.ex106.data.model.Employee;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AnalysisResultTest {

  @Test
  void testEmptyResult() {
    AnalysisResult result = new AnalysisResult();

    assertNotNull(result.getEmployeeIssues());
    assertTrue(result.getEmployeeIssues().isEmpty());
  }

  @Test
  void testAddAndGetIssue() {
    Employee fakeEmployee = createEmployee();
    EmployeeIssue fakeIssue = new EmployeeIssue(fakeEmployee);
    AnalysisResult result = new AnalysisResult();
    result.addIssue(fakeIssue);

    assertNotNull(result.getEmployeeIssues());
    assertEquals(1, result.getEmployeeIssues().size());
    assertEquals(fakeIssue, result.getEmployeeIssues().get(0));
  }

  private static Employee createEmployee() {
    return new Employee(1, "Aleks", "Bone", new BigDecimal(50000), 123);
  }
}