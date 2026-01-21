package br.com.bassi.expensegate.controller;

import br.com.bassi.expensegate.controller.dto.CreateExpenseDto;
import br.com.bassi.expensegate.controller.dto.DecisionDto;
import br.com.bassi.expensegate.controller.dto.ExpenseResponseDto;
import br.com.bassi.expensegate.service.DecisionService;
import br.com.bassi.expensegate.service.ExpenseService;
import br.com.bassi.expensegate.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.com.bassi.expensegate.entity.Authority.Values.*;

@RestController
@RequestMapping("/expense")
@EnableMethodSecurity
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final DecisionService decisionService;
    private final ListService listService;

    @PostMapping
    @PreAuthorize("@authz.has('"+ EXPE_CREATE + "')")
    public ExpenseResponseDto create(@RequestBody CreateExpenseDto dto,
                                     Authentication auth){
        return ExpenseResponseDto.from(expenseService.create(dto,auth));
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "@authz.has('" + EXPE_READ + "') and (" +
                    "@expenseSec.isOwner(#id, authentication) or " +
                    "@expenseSec.isSameDept(#id, authentication) or " +
                    "@authz.has('" + EXPE_READ_ANY + "')" +
                    ")"
    )
    public ExpenseResponseDto findById(@P("id") @PathVariable("id") Long id,
                                     Authentication auth){
        return ExpenseResponseDto.from(expenseService.findById(id,auth));
    }


    @PostMapping("/{id}/decisions")
    @PreAuthorize("(@authz.has('" + EXPE_APPROVE + "') and @expenseSec.isSameDept(#id, authentication)) " +
            "or @authz.has('" + EXPE_APPROVE_ANY + "')")
    public ExpenseResponseDto decisions(@P("id") @PathVariable("id") Long id,
                                       @RequestBody DecisionDto decision,
                                       Authentication auth){
        return ExpenseResponseDto.from(decisionService.decide(id,decision));
    }

    @GetMapping
    @PreAuthorize("@authz.has('" + EXPE_READ_ANY + "') or @authz.has ('"+ EXPE_READ +"')")
    public List<ExpenseResponseDto> list(Authentication auth){

        return listService.list(auth)
                .stream()
                .map(ExpenseResponseDto::from)
                .toList();
    }
}
