package br.com.bassi.expensegate.service;

import br.com.bassi.expensegate.config.WildcardAuthority;
import br.com.bassi.expensegate.entity.Expense;
import br.com.bassi.expensegate.repository.ExpenseRepository;
import br.com.bassi.expensegate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


import static br.com.bassi.expensegate.entity.Authority.Values.EXPE_APPROVE;
import static br.com.bassi.expensegate.entity.Authority.Values.EXPE_READ_ANY;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final WildcardAuthority authz;

    public List<Expense> list(Authentication auth){

        if(authz.has(EXPE_READ_ANY)){
            return expenseRepository.findAll();
        }

        if(authz.has(EXPE_APPROVE)){
            var user = userRepository.findByUsername(auth.getName()).orElseThrow();
            return expenseRepository.findAllByDepartment(user.getDepartment());
        }
        return expenseRepository.findAllByOwnerUsernameEqualsIgnoreCase(auth.getName());
    }
}
