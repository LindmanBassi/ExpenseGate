package br.com.bassi.expensegate.controller.dto;

import br.com.bassi.expensegate.entity.Expense;
import br.com.bassi.expensegate.entity.enuns.Department;

import java.math.BigDecimal;

public record ExpenseResponseDto(Long id,
                                 String title,
                                 BigDecimal amount,
                                 String department,
                                 String status) {
    public static ExpenseResponseDto from(Expense expense) {
        return new ExpenseResponseDto(expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getDepartment().name(),
                expense.getStatus().name());
    }
}
