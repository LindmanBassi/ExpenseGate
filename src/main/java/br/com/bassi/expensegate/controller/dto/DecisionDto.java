package br.com.bassi.expensegate.controller.dto;

import br.com.bassi.expensegate.entity.enuns.ExpenseStatus;

public record DecisionDto(ExpenseStatus decision) {
}
