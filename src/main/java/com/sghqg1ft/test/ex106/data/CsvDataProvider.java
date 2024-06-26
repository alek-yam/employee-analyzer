package com.sghqg1ft.test.ex106.data;

import com.sghqg1ft.test.ex106.data.model.Employee;
import com.sghqg1ft.test.ex106.data.model.EmployeeTree;
import com.sghqg1ft.test.ex106.exception.EmployeeAnalyzerException;
import com.sghqg1ft.test.ex106.exception.InvalidCsvFormat;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvDataProvider implements EmployeeDataProvider {
  private static final int EXPECTED_NUMBER_OF_COLUMNS = 5;
  private static final String[] EXPECTED_COLUMN_NAMES = {"Id", "firstName", "lastName", "salary", "managerId"};

  private final String csvFilepath;

  public CsvDataProvider(String csvFilepath) {
    this.csvFilepath = csvFilepath;
  }

  @Override
  public EmployeeTree getEmployeeTree() {
    EmployeeTree employeeTree = new EmployeeTree();

    Path pathToFile = Paths.get(csvFilepath);
    try (BufferedReader br = Files.newBufferedReader(pathToFile)) {
      String currentLine = br.readLine();
      validateHeaders(currentLine);
      currentLine = br.readLine();
      int currentLineNumber = 2;
      while (currentLine != null) {
        Employee employee = parseEmployee(currentLine, currentLineNumber);
        employeeTree.addEmployee(employee);
        currentLine = br.readLine();
        currentLineNumber++;
      }
    } catch(NoSuchFileException ex) {
      String message = String.format("Csv file not found '%s'", csvFilepath);
      throw new EmployeeAnalyzerException(message, ex);
    } catch (Exception ex) {
      String message = String.format("Cannot read csv file '%s'", csvFilepath);
      throw new EmployeeAnalyzerException(message, ex);
    }

    return employeeTree;
  }

  private void validateHeaders(String line) {
    String[] headers = line.split(",");

    if (headers.length != EXPECTED_NUMBER_OF_COLUMNS) {
      String message = String.format("Invalid number of columns: expected number = %s, actual number = %s",
          EXPECTED_NUMBER_OF_COLUMNS, headers.length);
      throw new InvalidCsvFormat(message);
    }

    for (int i = 0; i < EXPECTED_COLUMN_NAMES.length; i++) {
      if (!EXPECTED_COLUMN_NAMES[i].equals(headers[i])) {
        String message = String.format("Invalid header #%d: expected header = '%s', actual header = '%s'",
            i + 1, EXPECTED_COLUMN_NAMES[i], headers[i]);
        throw new InvalidCsvFormat(message);
      }
    }
  }

  private static Employee parseEmployee(String line, int lineNumber) {
    try {
      String[] attributes = line.split(",");
      Integer id = Integer.parseInt(attributes[0]);
      String firstName = attributes[1];
      String lastName = attributes[2];
      BigDecimal salary = new BigDecimal(attributes[3]);
      Integer managerId = (attributes.length >= 5) ? Integer.parseInt(attributes[4]) : null;
      return new Employee(id, firstName, lastName, salary, managerId);
    } catch (Exception ex) {
      String message = String.format("Error while reading line #%d", lineNumber);
      throw new InvalidCsvFormat(message, ex);
    }
  }
}
