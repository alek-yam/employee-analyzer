package com.sghqg1ft.test.ex106.data.model;

import java.math.BigDecimal;

public record Employee(
    Integer id,
    String firstName,
    String lastName,
    BigDecimal salary,  //  BigDecimal to prevent loss of accuracy in financial operations
    Integer managerId) {}
