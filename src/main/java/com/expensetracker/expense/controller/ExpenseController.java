package com.expensetracker.expense.controller;

import com.expensetracker.common.util.SecurityUtil;
import com.expensetracker.expense.dto.CreateExpenseRequest;
import com.expensetracker.expense.dto.ExpenseResponse;
import com.expensetracker.expense.dto.UpdateExpenseRequest;
import com.expensetracker.expense.entity.Expense;
import com.expensetracker.expense.service.ExpenseService;
import com.expensetracker.user.entity.User;
import com.expensetracker.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<?> getMyExpenses() {

        // 1️⃣ Get logged-in user email
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Fetch user-specific expenses
        List<Expense> expenses = expenseService.getExpensesByUser(user);

        // 3️⃣ Map Entity → Response DTO
        List<ExpenseResponse> response = expenses.stream()
                .map(expense -> ExpenseResponse.builder()
                        .id(expense.getId())
                        .amount(expense.getAmount())
                        .description(expense.getDescription())
                        .category(expense.getCategory())
                        .expenseDate(expense.getExpenseDate())
                        .build()
                )
                .toList();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {

        //Get logged-in user
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Delete expense with ownership check
        expenseService.deleteExpense(id, user);

        return ResponseEntity.ok("Expense deleted successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {
        //Get logged-in user
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Update expense with ownership check
        expenseService.updateExpense(id, user, request);

        return ResponseEntity.ok("Expense updated successfully");
    }

}
