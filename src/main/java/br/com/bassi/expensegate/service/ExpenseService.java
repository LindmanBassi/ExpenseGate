package br.com.bassi.expensegate.service;

import br.com.bassi.expensegate.controller.dto.CreateExpenseDto;
import br.com.bassi.expensegate.controller.dto.DecisionDto;
import br.com.bassi.expensegate.controller.dto.ExpenseResponseDto;
import br.com.bassi.expensegate.entity.Expense;
import br.com.bassi.expensegate.entity.enuns.ExpenseStatus;
import br.com.bassi.expensegate.repository.ExpenseRepository;
import br.com.bassi.expensegate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    public Expense create(CreateExpenseDto dto,
                          Authentication auth) {

        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        var expense = new Expense();

        expense.setTitle(dto.title());
        expense.setAmount(dto.amount());
        expense.setDepartment(user.getDepartment());
        expense.setOwner(user);
        expense.setStatus(ExpenseStatus.SUBMITTED);

        return expenseRepository.save(expense);
    }

    public Expense findById(Long id, Authentication auth) {
        return expenseRepository.findById(id).orElseThrow();
    }
}
