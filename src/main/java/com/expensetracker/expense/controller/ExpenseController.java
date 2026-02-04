package com.expensetracker.expense.controller;

import com.expensetracker.common.util.SecurityUtil;
import com.expensetracker.expense.dto.CreateExpenseRequest;
import com.expensetracker.expense.entity.Expense;
import com.expensetracker.expense.service.ExpenseService;
import com.expensetracker.user.entity.User;
import com.expensetracker.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createExpense(
            @Valid @RequestBody CreateExpenseRequest request
    ) {
        // Get logged-in user's email from JWT
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map DTO → Entity
        Expense expense = Expense.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .category(request.getCategory())
                .expenseDate(request.getExpenseDate())
                .user(user)
                .build();

        // Save expense
        expenseService.saveExpense(expense);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Expense added successfully");
    }
}
