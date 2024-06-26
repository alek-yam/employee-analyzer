package com.sghqg1ft.test.ex106.data.model;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTreeTest {

  @Test
  void testEmptyEmployeeTree() {
    EmployeeTree tree = new EmployeeTree();

    Employee root = tree.getRoot();
    Employee employee = tree.getEmployee(1);
    Set<Employee> subordinates = tree.getSubordinates(1);

    assertNull(root);
    assertNull(employee);
    assertNotNull(subordinates);
    assertTrue(subordinates.isEmpty());
  }

  @Test
  void testNullKeyIsAllowed() {
    EmployeeTree tree = new EmployeeTree();

    Employee employee = tree.getEmployee(null);
    Set<Employee> subordinates = tree.getSubordinates(null);

    assertNull(employee);
    assertTrue(subordinates.isEmpty());
  }

  @Test
  void testAddAndGetCeo() {
    EmployeeTree tree = new EmployeeTree();
    Employee ceo = new Employee(1, "John", "Doe", new BigDecimal("5000.00"), null);
    tree.addEmployee(ceo);

    Employee root = tree.getRoot();
    Employee employee = tree.getEmployee(1);

    assertEquals(ceo, root);
    assertEquals(ceo, employee);
  }

  @Test
  void testAddAndGetEmployee() {
    EmployeeTree tree = new EmployeeTree();
    Employee emp1 = new Employee(2, "Martin", "Chekov", new BigDecimal("5000.00"), 1);
    tree.addEmployee(emp1);

    Employee root = tree.getRoot();
    Employee employee = tree.getEmployee(2);
    Set<Employee> subordinates = tree.getSubordinates(1);

    assertNull(root);
    assertEquals(emp1, employee);
    assertEquals(singleton(emp1), subordinates);
  }

  @Test
  void testGetSubordinatesReturnsMultipleEmployees() {
    EmployeeTree tree = new EmployeeTree();
    Employee ceo = new Employee(1, "John", "Doe", new BigDecimal("5000.00"), null);
    Employee emp2 = new Employee(2, "Martin", "Chekov", new BigDecimal("4000.00"), 1);
    Employee emp3 = new Employee(3, "Bob", "Ronstad", new BigDecimal("3000.00"), 2);
    Employee emp4 = new Employee(4, "Alice", "Hasacat", new BigDecimal("3500.00"), 2);
    tree.addEmployee(ceo);
    tree.addEmployee(emp2);
    tree.addEmployee(emp3);
    tree.addEmployee(emp4);

    Set<Employee> subordinates = tree.getSubordinates(2);

    assertEquals(Set.of(emp3, emp4), subordinates);
  }
}