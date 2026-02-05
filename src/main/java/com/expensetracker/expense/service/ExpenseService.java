package com.expensetracker.expense.service;

import com.expensetracker.expense.entity.Expense;
import com.expensetracker.user.entity.User;
import java.util.List;

public interface ExpenseService {
    Expense saveExpense(Expense expense);

    List<Expense> getExpensesByUser(User user);

    void deleteExpense(Long expenseId, User user);
}
