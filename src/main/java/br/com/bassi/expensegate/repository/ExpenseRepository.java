package br.com.bassi.expensegate.repository;

import br.com.bassi.expensegate.entity.Expense;
import br.com.bassi.expensegate.entity.enuns.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long>{

    boolean existsByIdAndOwnerUsernameEqualsIgnoreCase(Long id, String name);

    boolean existsByIdAndDepartment(Long id, Department department);

    List<Expense> findAllByDepartment(Department department);

    List<Expense> findAllByOwnerUsernameEqualsIgnoreCase(String name);
}
