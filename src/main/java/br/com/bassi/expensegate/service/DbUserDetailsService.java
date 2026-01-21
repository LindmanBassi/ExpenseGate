package br.com.bassi.expensegate.service;

import br.com.bassi.expensegate.entity.Authority;
import br.com.bassi.expensegate.entity.Role;
import br.com.bassi.expensegate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService { // é o "tradutor" daquilo que esta no banco para o Security

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)); // busca usuario no banco e se nao achar da exception

        var authz = new HashSet<GrantedAuthority>(); //cria lista vazia de permissoes  no formato GrantedAuthority (afinal o spring so entende nesse formato)

        // 1. Passar a role para lista de authorities do Spring Security (GrantedAuthority)
        user.getRoles()
                .stream()
                .map(Role::getName)
                .map(String::toUpperCase)
                .forEach(roleName ->
                        authz.add(
                                new SimpleGrantedAuthority("ROLE_" + roleName)
                        )
                );
        // 2. Passar as authorities do projeto para o formato do Spring security(GrantedAuthority)
        user.getRoles()
                .stream()
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .forEach(authz::add);

        return new User(
                user.getUsername(),
                user.getPassword(),
                authz
        );

    }
}
