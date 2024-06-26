package com.sghqg1ft.test.ex106.analyzer.result;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SalaryPolicyViolationTest {

  @Test
  void testIsBelowMinLimit() {
    SalaryPolicyViolation salaryPolicyViolation = new SalaryPolicyViolation(new BigDecimal(-100), -10.);
    assertTrue(salaryPolicyViolation.isBelowMinLimit());
    assertFalse(salaryPolicyViolation.isAboveMaxLimit());
  }

  @Test
  void testIsAboveMaxLimit() {
    SalaryPolicyViolation salaryPolicyViolation = new SalaryPolicyViolation(new BigDecimal(100), 10.);
    assertFalse(salaryPolicyViolation.isBelowMinLimit());
    assertTrue(salaryPolicyViolation.isAboveMaxLimit());
  }
}