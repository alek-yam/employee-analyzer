package com.sghqg1ft.test.ex106.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sghqg1ft.test.ex106.data.model.Employee;
import com.sghqg1ft.test.ex106.data.model.EmployeeTree;
import java.math.BigDecimal;
import java.net.URL;
import org.junit.jupiter.api.Test;

class CsvDataProviderTest {

  @Test
  void testReadCsvFile() {
    // given
    URL csvUrl = getClass().getResource("/employees.csv");
    assertNotNull(csvUrl);
    String csvFilepath = csvUrl.getFile();
    CsvDataProvider dataProvider = new CsvDataProvider(csvFilepath);

    // when
    EmployeeTree actual = dataProvider.getEmployeeTree();
    EmployeeTree expected = getExpectedTree();

    // then
    assertEquals(expected.getRoot(), actual.getRoot());
    assertEquals(expected.getEmployee(123), actual.getEmployee(123));
    assertEquals(expected.getEmployee(124), actual.getEmployee(124));
    assertEquals(expected.getEmployee(125), actual.getEmployee(125));
    assertEquals(expected.getEmployee(300), actual.getEmployee(300));
    assertEquals(expected.getEmployee(305), actual.getEmployee(305));
    assertEquals(expected.getSubordinates(123), actual.getSubordinates(123));
    assertEquals(expected.getSubordinates(124), actual.getSubordinates(124));
    assertEquals(expected.getSubordinates(125), actual.getSubordinates(125));
    assertEquals(expected.getSubordinates(300), actual.getSubordinates(300));
    assertEquals(expected.getSubordinates(305), actual.getSubordinates(304));
  }

  private static EmployeeTree getExpectedTree() {
    EmployeeTree tree = new EmployeeTree();
    tree.addEmployee(new Employee(123, "Joe", "Doe", new BigDecimal("60000"), null));
    tree.addEmployee(new Employee(124, "Martin", "Chekov", new BigDecimal("45000"), 123));
    tree.addEmployee(new Employee(125, "Bob", "Ronstad", new BigDecimal("47000"), 123));
    tree.addEmployee(new Employee(300, "Alice", "Hasacat", new BigDecimal("50000"), 124));
    tree.addEmployee(new Employee(305, "Brett", "Hardleaf", new BigDecimal("34000"), 300));
    return tree;
  }
}