package com.sghqg1ft.test.ex106.analyzer.result;

import java.math.BigDecimal;

public record SalaryPolicyViolation(BigDecimal delta, Double percentDelta) {

  public boolean isBelowMinLimit() {
    return BigDecimal.ZERO.compareTo(delta) == 1;
  }

  public boolean isAboveMaxLimit() {
    return BigDecimal.ZERO.compareTo(delta) == -1;
  }

}
