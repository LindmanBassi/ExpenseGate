package br.com.bassi.expensegate.config.spell;

import br.com.bassi.expensegate.repository.ExpenseRepository;
import br.com.bassi.expensegate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("expenseSec")
@RequiredArgsConstructor
public class ExpenseSecurity {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public boolean isOwner(Long id, Authentication auth){
        //validar se o usuario é dono da despesa
        return expenseRepository.existsByIdAndOwnerUsernameEqualsIgnoreCase(id, auth.getName());
    }

    public boolean isSameDept(Long id, Authentication auth){
        //validar se o usuario é do mesmo departamento da despesa
        var user = userRepository.findByUsername(auth.getName()).orElseThrow();
        return expenseRepository.existsByIdAndDepartment(id, user.getDepartment());
    }
}
