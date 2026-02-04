package com.expensetracker.expense.service;

import com.expensetracker.expense.entity.Expense;

public interface ExpenseService {
    Expense saveExpense(Expense expense);
}
