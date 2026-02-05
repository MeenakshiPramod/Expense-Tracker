package com.expensetracker.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateExpenseRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate expenseDate;
}
