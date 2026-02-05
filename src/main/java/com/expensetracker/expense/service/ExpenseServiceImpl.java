package com.expensetracker.expense.service;

import com.expensetracker.expense.entity.Expense;
import com.expensetracker.expense.repository.ExpenseRepository;
import com.expensetracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    @Override
    public void deleteExpense(Long expenseId, User user){

        Expense expense = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found or access denied")
                        );

        expenseRepository.delete(expense);
    }

}
