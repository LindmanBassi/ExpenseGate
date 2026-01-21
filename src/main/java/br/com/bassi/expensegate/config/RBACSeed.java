package br.com.bassi.expensegate.config;

import br.com.bassi.expensegate.entity.Authority;
import br.com.bassi.expensegate.entity.Role;
import br.com.bassi.expensegate.entity.User;
import br.com.bassi.expensegate.entity.enuns.Department;
import br.com.bassi.expensegate.repository.AuthorityRepository;
import br.com.bassi.expensegate.repository.RoleRepository;
import br.com.bassi.expensegate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static br.com.bassi.expensegate.entity.Authority.Values.*;

@Configuration
@RequiredArgsConstructor
public class RBACSeed implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RBACSeed.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("RBACSeed started");

        // 1.Authority
        var expCreate = ensureAuthority(EXPE_CREATE);
        var expRead = ensureAuthority(EXPE_READ);
        var expReadAny = ensureAuthority(EXPE_READ_ANY);
        var expApprove = ensureAuthority(EXPE_APPROVE);
        var expApproveAny = ensureAuthority(EXPE_APPROVE_ANY);
        var expWildcard = ensureAuthority(EXPE_WILDCARD);

        // 2.Role
        var roleEmployee = ensureRole("EMPLOYEE", Set.of(expCreate, expRead));
        var roleManager = ensureRole("MANAGER", Set.of(expCreate, expRead, expApprove));
        var roleAdmin = ensureRole("ADMIN", Set.of(expWildcard));

        // 3.User
        ensureUser("ana", "senha", Department.TI, roleEmployee);
        ensureUser("carlos", "senha", Department.TI, roleManager);
        ensureUser("bruno", "senha", Department.ENG, roleManager);
        ensureUser("admin", "admin", Department.ENG, roleAdmin);

        logger.info("RBACSeed ended");
    }

    public Authority ensureAuthority(String name) {
        return authorityRepository
                .findByName(name)
                .orElseGet(() -> authorityRepository.save(new Authority(null, name)));
    }

    public Role ensureRole(String name, Set<Authority> authorities) {
        return roleRepository
                .findByName(name)
                .map(existingRole -> {
                    existingRole.setAuthorities(authorities);
                    return roleRepository.save(existingRole);
                })
                .orElseGet(() -> roleRepository.save(new Role(null, name, authorities)));
    }

    private User ensureUser(String username, String password, Department department, Role role) {
        return userRepository
                .findByUsername(username)
                .map(existingUser -> {
                    existingUser.setDepartment(department);
                    existingUser.setPassword(passwordEncoder.encode(password));
                    existingUser.setRoles(Set.of(role));
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> userRepository.save(
                        new User(null, username, passwordEncoder.encode(password), department, Set.of(role))
                ));
    }
}

