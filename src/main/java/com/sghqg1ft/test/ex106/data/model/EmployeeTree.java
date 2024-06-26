package com.sghqg1ft.test.ex106.data.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmployeeTree {
  private Employee root;
  private final Map<Integer, Employee> employeesMap = new HashMap<>();
  private final Map<Integer, Set<Employee>> managerToSubordinates = new HashMap<>();

  public Employee getRoot() {
    return root;
  }

  public Employee getEmployee(Integer employeeId) {
    return employeesMap.get(employeeId);
  }

  public Set<Employee> getSubordinates(Integer managerId) {
    return Collections.unmodifiableSet(managerToSubordinates.getOrDefault(managerId, Collections.emptySet()));
  }

  public void addEmployee(Employee employee) {
    employeesMap.put(employee.id(), employee);
    Integer managerId = employee.managerId();
    if (managerId != null) {
      Set<Employee> subordinates
          = managerToSubordinates.computeIfAbsent(managerId, key -> new HashSet<>());
      subordinates.add(employee);
    } else {
      root = employee;
    }
  }
}
