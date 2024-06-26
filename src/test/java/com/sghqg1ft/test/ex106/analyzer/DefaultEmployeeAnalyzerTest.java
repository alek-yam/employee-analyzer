package com.sghqg1ft.test.ex106.analyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sghqg1ft.test.ex106.analyzer.result.AnalysisResult;
import com.sghqg1ft.test.ex106.analyzer.result.EmployeeIssue;
import com.sghqg1ft.test.ex106.analyzer.result.SalaryPolicyViolation;
import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerConfig;
import com.sghqg1ft.test.ex106.data.model.Employee;
import com.sghqg1ft.test.ex106.data.model.EmployeeTree;
import com.sghqg1ft.test.ex106.exception.InvalidTreeException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultEmployeeAnalyzerTest {
  private static final Double MIN_SALARY_RAISE_PERCENT = 120.;
  private static final Double MAX_SALARY_RAISE_PERCENT = 150.;
  private static final Integer MAX_REPORT_LINE_LENGTH = 4;
  private static final EmployeeAnalyzerConfig appConfig
      = new EmployeeAnalyzerConfig(MIN_SALARY_RAISE_PERCENT, MAX_SALARY_RAISE_PERCENT, MAX_REPORT_LINE_LENGTH);

  private EmployeeAnalyzer employeeAnalyzer;

  @BeforeEach
  public void setup() {
    employeeAnalyzer = new DefaultEmployeeAnalyzer(appConfig);
  }

  @Test
  public void testEmptyTreeThrowsException() {
    EmployeeTree emptyTree = new EmployeeTree();

    Exception exception = assertThrows(InvalidTreeException.class, () ->
      employeeAnalyzer.analyze(emptyTree)
    );

    assertTrue(exception.getMessage().contains("Employee tree doesn't contain root node."));
  }

  @Test
  public void testIssuesNotFound() {
    EmployeeTree perfectTree = getPerfectTree();

    AnalysisResult result = employeeAnalyzer.analyze(perfectTree);

    assertNotNull(result);
    assertNotNull(result.getEmployeeIssues());
    assertTrue(result.getEmployeeIssues().isEmpty());
  }

  @Test
  public void testIssuesFound() {
    EmployeeTree badTree = getBadTree();

    AnalysisResult result = employeeAnalyzer.analyze(badTree);

    assertNotNull(result);
    assertNotNull(result.getEmployeeIssues());
    assertFalse(result.getEmployeeIssues().isEmpty());
    List<EmployeeIssue> issues = result.getEmployeeIssues();
    assertEquals(4, issues.size());
    assertIssueIsExpected(
        createdExpectedIssue(125, 2000, 7.),
        issues.get(0));
    assertIssueIsExpected(
        createdExpectedIssue(124, -15000, -30.),
        issues.get(1));
    assertIssueIsExpected(
        createdExpectedIssue(308, -4000, -20., 1),
        issues.get(2));
    assertIssueIsExpected(
        createdExpectedIssue(309, 2),
        issues.get(3));
  }

  private static EmployeeTree getPerfectTree() {
    EmployeeTree tree = new EmployeeTree();
    tree.addEmployee(new Employee(123, "Joe", "Doe", new BigDecimal("60000"), null));
    tree.addEmployee(new Employee(124, "Martin", "Chekov", new BigDecimal("45000"), 123));
    tree.addEmployee(new Employee(125, "Bob", "Ronstad", new BigDecimal("47000"), 123));
    tree.addEmployee(new Employee(300, "Alice", "Hasacat", new BigDecimal("35000"), 124));
    tree.addEmployee(new Employee(305, "Brett", "Hardleaf", new BigDecimal("25000"), 300));
    return tree;
  }

  private static EmployeeTree getBadTree() {
    EmployeeTree tree = new EmployeeTree();
    tree.addEmployee(new Employee(123, "Joe", "Doe", new BigDecimal("60000"), null));
    tree.addEmployee(new Employee(124, "Martin", "Chekov", new BigDecimal("45000"), 123));
    tree.addEmployee(new Employee(125, "Bob", "Ronstad", new BigDecimal("47000"), 123));
    tree.addEmployee(new Employee(300, "Alice", "Hasacat", new BigDecimal("50000"), 124));
    tree.addEmployee(new Employee(305, "Brett", "Hardleaf", new BigDecimal("35000"), 300));
    tree.addEmployee(new Employee(306, "Ivan", "Ivanov", new BigDecimal("29000"), 305));
    tree.addEmployee(new Employee(307, "Petr", "Petrov", new BigDecimal("24000"), 306));
    tree.addEmployee(new Employee(308, "Mihail", "Smirnov", new BigDecimal("20000"), 307));
    tree.addEmployee(new Employee(309, "Dmitrii", "Dobrov", new BigDecimal("20000"), 308));
    tree.addEmployee(new Employee(400, "Nikita", "Kruglov", new BigDecimal("30000"), 125));
    return tree;
  }

  private static void assertIssueIsExpected(EmployeeIssue expectedIssue, EmployeeIssue actualIssue) {
    if (expectedIssue.getSalaryPolicyViolation() != null) {
      assertNotNull(actualIssue.getSalaryPolicyViolation());
      assertEquals(0, expectedIssue.getSalaryPolicyViolation().delta().compareTo(
          actualIssue.getSalaryPolicyViolation().delta())
      );
      assertEquals(expectedIssue.getSalaryPolicyViolation().percentDelta(),
          actualIssue.getSalaryPolicyViolation().percentDelta());
    } else {
      assertNull(actualIssue.getSalaryPolicyViolation());
    }

    assertEquals(expectedIssue.getReportLineLengthExcess(), actualIssue.getReportLineLengthExcess());
  }

  private static EmployeeIssue createdExpectedIssue(
      Integer employeeId, Integer delta, Double percentDelta) {
    Employee employee = new Employee(employeeId, null, null, null, null);
    SalaryPolicyViolation salaryPolicyViolation = new SalaryPolicyViolation(new BigDecimal(delta), percentDelta);
    return new EmployeeIssue(employee, salaryPolicyViolation, null);
  }

  private static EmployeeIssue createdExpectedIssue(
      Integer employeeId, Integer reportLineLengthExcess) {
    Employee employee = new Employee(employeeId, null, null, null, null);
    return new EmployeeIssue(employee, null, reportLineLengthExcess);
  }

  private static EmployeeIssue createdExpectedIssue(
      Integer employeeId, Integer delta, Double percentDelta, Integer reportLineLengthExcess) {
    Employee employee = new Employee(employeeId, null, null, null, null);
    SalaryPolicyViolation salaryPolicyViolation = new SalaryPolicyViolation(new BigDecimal(delta), percentDelta);
    return new EmployeeIssue(employee, salaryPolicyViolation, reportLineLengthExcess);
  }
}