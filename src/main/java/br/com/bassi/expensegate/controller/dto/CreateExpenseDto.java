package br.com.bassi.expensegate.controller.dto;

import java.math.BigDecimal;

public record CreateExpenseDto(String title, BigDecimal amount) {
}
