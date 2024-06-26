package com.sghqg1ft.test.ex106.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeTree {
  private Employee root;
  private final Map<Integer, Employee> employeesMap = new HashMap<>();
  private final Map<Integer, List<Employee>> managerToSubordinates = new HashMap<>();

  public Employee getRoot() {
    return root;
  }

  public Employee getEmployee(Integer employeeId) {
    return employeesMap.get(employeeId);
  }

  public List<Employee> getSubordinates(Integer managerId) {
    return Collections.unmodifiableList(managerToSubordinates.getOrDefault(managerId, Collections.emptyList()));
  }

  public void addEmployee(Employee employee) {
    employeesMap.put(employee.id(), employee);
    Integer managerId = employee.managerId();
    if (managerId != null) {
      List<Employee> subordinates
          = managerToSubordinates.computeIfAbsent(managerId, key -> new ArrayList<>());
      subordinates.add(employee);
    } else {
      root = employee;
    }
  }

}
