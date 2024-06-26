package com.sghqg1ft.test.ex106;

import com.sghqg1ft.test.ex106.analyzer.result.AnalysisResult;
import com.sghqg1ft.test.ex106.analyzer.DefaultEmployeeAnalyzer;
import com.sghqg1ft.test.ex106.analyzer.EmployeeAnalyzer;
import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerConfig;
import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerRunConfig;
import com.sghqg1ft.test.ex106.data.CsvDataProvider;
import com.sghqg1ft.test.ex106.data.EmployeeDataProvider;
import com.sghqg1ft.test.ex106.util.CommandLineReader;
import com.sghqg1ft.test.ex106.data.model.EmployeeTree;
import com.sghqg1ft.test.ex106.util.CommandLineWriter;
import java.util.logging.Logger;

public class EmployeeAnalyzerApp {

  private static final Logger LOG = Logger.getLogger(EmployeeAnalyzerApp.class.getName());

  public static void main(String[] args) {
    try {
      EmployeeAnalyzerRunConfig runConfig = CommandLineReader.parseArguments(args);
      EmployeeDataProvider dataProvider = new CsvDataProvider(runConfig.cvsFilepath());
      EmployeeAnalyzerConfig appConfig = EmployeeAnalyzerConfig.fromApplicationProperties();
      EmployeeAnalyzer employeeAnalyzer = new DefaultEmployeeAnalyzer(appConfig);
      EmployeeTree employeeTree = dataProvider.getEmployeeTree();
      AnalysisResult result = employeeAnalyzer.analyze(employeeTree);
      CommandLineWriter.printReport(appConfig, result);
    } catch (Exception ex) {
      LOG.severe("Cannot analyze organizational structure of employees. Reason: " + ex.getMessage());
    }
  }
}