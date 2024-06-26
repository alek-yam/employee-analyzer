package com.sghqg1ft.test.ex106.util;

import com.sghqg1ft.test.ex106.config.EmployeeAnalyzerRunConfig;
import com.sghqg1ft.test.ex106.exception.InvalidCommandLineArguments;
import java.util.Arrays;

public class CommandLineReader {
  private static final String[] CSV_FILEPATH_ARG_NAMES = { "-f", "--filename" };

  public static EmployeeAnalyzerRunConfig parseArguments(String[] args) {
    validateArguments(args);
    String csvFilepath = getArgumentValue(args, CSV_FILEPATH_ARG_NAMES);
    return new EmployeeAnalyzerRunConfig(csvFilepath);
  }

  private static void validateArguments(String[] args) {
    if (args.length == 0) {
      String errorMessage = String.format("Run command doesn't contain information about csv file path." +
          " Use one of the following arguments and run it again: %s", Arrays.toString(CSV_FILEPATH_ARG_NAMES));
      throw new InvalidCommandLineArguments(errorMessage);
    }
  }

  private static String getArgumentValue(String[] args, String... argNames) {
    for (int i = 0; i < args.length; i++) {
      String currentArg = args[i];
      if (hasMatches(currentArg, argNames)) {
        int nextArgIndex = i + 1;
        if (nextArgIndex <= args.length) {
          return args[nextArgIndex];
        } else {
          String errorMessage = String.format("Value for the run command argument was not found: %s", currentArg);
          throw new InvalidCommandLineArguments(errorMessage);
        }
      }
    }

    String errorMessage = String.format("Required run command argument was not found: %s", Arrays.toString(argNames));
    throw new InvalidCommandLineArguments(errorMessage);
  }

  private static boolean hasMatches(String arg, String[] possibleNameOptions) {
    for (String name : possibleNameOptions) {
      if (name.equals(arg)) {
        return true;
      }
    }

    return false;
  }
}
