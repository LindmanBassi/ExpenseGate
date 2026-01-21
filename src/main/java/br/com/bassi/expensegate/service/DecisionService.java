package br.com.bassi.expensegate.service;

import br.com.bassi.expensegate.controller.dto.DecisionDto;
import br.com.bassi.expensegate.entity.Expense;
import br.com.bassi.expensegate.entity.enuns.ExpenseStatus;
import br.com.bassi.expensegate.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecisionService {

    private final ExpenseRepository expenseRepository;

    public Expense decide(Long id, DecisionDto decision) {

       var expense = expenseRepository.findById(id)
                .orElseThrow();

       if (expense.getStatus() != ExpenseStatus.SUBMITTED){
           throw new IllegalArgumentException("Expense not in submitted status");
       }

       expense.setStatus(decision.decision());

       return expenseRepository.save(expense);
    }
}
