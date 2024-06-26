package com.sghqg1ft.test.ex106.analyzer;

import com.sghqg1ft.test.ex106.analyzer.result.AnalysisResult;
import com.sghqg1ft.test.ex106.analyzer.result.EmployeeIssue;
import com.sghqg1ft.test.ex106.analyzer.result.SalaryPolicyViolation;
import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerConfig;
import com.sghqg1ft.test.ex106.data.model.Employee;
import com.sghqg1ft.test.ex106.data.model.EmployeeTree;
import com.sghqg1ft.test.ex106.exception.InvalidTreeException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class DefaultEmployeeAnalyzer implements EmployeeAnalyzer {
  private static final Integer ZERO_NODE_LEVEL = 0;
  private static final Integer DEFAULT_SALARY_SCALE = 2;
  private static final Integer DEFAULT_PERCENT_SCALE = 2;
  private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

  private final EmployeeAnalyzerConfig config;

  public DefaultEmployeeAnalyzer(EmployeeAnalyzerConfig config) {
    this.config = config;
  }

  @Override
  public AnalysisResult analyze(EmployeeTree employeeTree) {
    // initialize process queue
    Employee root = employeeTree.getRoot();
    if (root == null) {
      throw new InvalidTreeException("Employee tree doesn't contain root node.");
    }
    Set<Employee> subordinates = employeeTree.getSubordinates(root.id());
    // There are two options of getting subordinates and report line length from implementation perspective:
    // 1. Add methods getSubordinates() and getReportLineLength() to Employee class and use reference to employeeTree
    //    for calculating the values (reference to employeeTree should be added to Employee class as well in this case)
    //    - this is better from OOP design prospective, but worse for performance because of possible multiple requests
    //    to employeeTree (even with adding subordinates and manager fields with lazy initialization to Employee class,
    //    because you have to retrieve manager instance for all levels of reporting line)
    // 2. Retrieve subordinates only once and avoid retrieving managers at all, wrap up employee with subordinates
    //    and report line length in intermediate object (EmpoyeeNode) for processing
    //    - this approach was selected because it is better from performance prospective regardless it makes code
    //    a little bit more procedural style and less readable. But impact of these downsides are restricted because
    //    this approach is isolated inside DefaultEmployeeAnalyzer and has no negative impact to other classes.
    EmployeeNode rootNode = new EmployeeNode(root, subordinates, ZERO_NODE_LEVEL);
    Queue<EmployeeNode> processQueue = new LinkedList<>();
    processQueue.add(rootNode);

    // process employees
    AnalysisResult analysisResult = new AnalysisResult();
    while (!processQueue.isEmpty()) {
      EmployeeNode currentNode = processQueue.poll();
      checkIssues(currentNode)
          .ifPresent(analysisResult::addIssue);
      updateProcessQueue(currentNode, employeeTree, processQueue);
    }

    return analysisResult;
  }

  private Optional<EmployeeIssue> checkIssues(EmployeeNode employeeNode) {
    Employee employee = employeeNode.employee();
    EmployeeIssue employeeIssue = new EmployeeIssue(employee);
    checkSalaryPolicyViolation(employeeNode)
        .ifPresent(employeeIssue::setSalaryPolicyViolation);
    checkReportLineLength(employeeNode)
        .ifPresent(employeeIssue::setReportLineLengthExcess);
    return employeeIssue.isEmpty() ?
        Optional.empty() : Optional.of(employeeIssue);
  }

  private void updateProcessQueue(EmployeeNode currentNode,
                                  EmployeeTree employeeTree,
                                  Queue<EmployeeNode> processQueue) {
    currentNode.subordinates().forEach(sub -> {
      Set<Employee> subSubordinates = employeeTree.getSubordinates(sub.id());
      Integer subNodeLevel = currentNode.nodeLevel + 1;
      EmployeeNode employeeNode = new EmployeeNode(sub, subSubordinates, subNodeLevel);
      processQueue.add(employeeNode);
    });
  }

  private Optional<SalaryPolicyViolation> checkSalaryPolicyViolation(EmployeeNode employeeNode) {
    Employee employee = employeeNode.employee();
    Set<Employee> subordinates = employeeNode.subordinates();

    if (!subordinates.isEmpty()) {
      BigDecimal avgSubSalary = getAverageSalary(subordinates);
      SalaryFork salaryFork = getSalaryFork(
          avgSubSalary, config.minSalaryRaisePercent(), config.maxSalaryRaisePercent());

      BigDecimal employeeSalary = employee.salary();
      if (salaryFork.isLessThanMinLimit(employeeSalary)) {
        BigDecimal delta = employeeSalary.subtract(salaryFork.minLimit());
        Double actualPercent = getActualPercent(employeeSalary, avgSubSalary);
        Double percentDelta = actualPercent - config.minSalaryRaisePercent();
        return Optional.of(new SalaryPolicyViolation(delta, percentDelta));
      } else if (salaryFork.isGreaterThanMaxLimit(employeeSalary)) {
        BigDecimal delta = employeeSalary.subtract(salaryFork.maxLimit());
        Double actualPercent = getActualPercent(employeeSalary, avgSubSalary);
        Double percentDelta = actualPercent - config.maxSalaryRaisePercent();
        return Optional.of(new SalaryPolicyViolation(delta, percentDelta));
      }
    }

    return Optional.empty();
  }

  private static BigDecimal getAverageSalary(Set<Employee> employees) {
    if (employees.isEmpty()) {
      throw new RuntimeException("Cannot calculate average salary for empty collection.");
    }

    BigDecimal totalSalary = BigDecimal.ZERO;
    for (Employee emp : employees) {
      totalSalary = totalSalary.add(emp.salary());
    }

    BigDecimal count = new BigDecimal(employees.size());
    return totalSalary.divide(count, DEFAULT_SALARY_SCALE, RoundingMode.HALF_UP);
  }

  private static SalaryFork getSalaryFork(BigDecimal baseSalary, Double minSalaryRaisePercent, Double maxSalaryRaisePercent) {
    BigDecimal minSalaryRaiseFactor = BigDecimal.valueOf(minSalaryRaisePercent / 100);
    BigDecimal maxSalaryRaiseFactor = BigDecimal.valueOf(maxSalaryRaisePercent / 100);
    BigDecimal minLimit = baseSalary.multiply(minSalaryRaiseFactor);
    BigDecimal maxLimit = baseSalary.multiply(maxSalaryRaiseFactor);
    return new SalaryFork(minLimit, maxLimit);
  }

  private static Double getActualPercent(BigDecimal employeeSalary, BigDecimal avgSubSalary) {
    return employeeSalary
        .divide(avgSubSalary, DEFAULT_PERCENT_SCALE, RoundingMode.HALF_UP)
        .multiply(ONE_HUNDRED)
        .doubleValue();
  }

  private Optional<Integer> checkReportLineLength(EmployeeNode employeeNode) {
    int reportLineLength = employeeNode.nodeLevel() - 1;
    if (reportLineLength > config.maxReportLineLength()) {
      Integer reportLineLengthDelta = reportLineLength - config.maxReportLineLength();
      return Optional.of(reportLineLengthDelta);
    }

    return Optional.empty();
  }

  record EmployeeNode(Employee employee, Set<Employee> subordinates, Integer nodeLevel) {}

  record SalaryFork(BigDecimal minLimit, BigDecimal maxLimit) {
    public boolean isLessThanMinLimit(BigDecimal value) {
      return value.compareTo(minLimit) == -1;
    }

    public boolean isGreaterThanMaxLimit(BigDecimal value) {
      return value.compareTo(maxLimit) == 1;
    }
  }
}
