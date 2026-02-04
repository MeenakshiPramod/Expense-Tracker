package com.expensetracker.expense.repository;

import com.expensetracker.expense.entity.Expense;
import com.expensetracker.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses for a specific user
    List<Expense> findByUser(User user);

    List<Expense> findByUserAndExpenseDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );

}
