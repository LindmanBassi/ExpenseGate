package br.com.bassi.expensegate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //define bean de configuracao
@EnableWebSecurity// ativa o modulo de seguranca "web" do Securtiy(sem ela o "SecurityFilterChain" nem seria considerado)
@EnableMethodSecurity //permite seguranca a nivel de metodo
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //desabilitando o csrf (nao ira ser usado autenticacao por cookies automaticos)
                .sessionManagement(AbstractHttpConfigurer::disable) //desabilitando a sessao http (logo sera stateless)
                .httpBasic(Customizer.withDefaults()) //usar "default" do basic
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) //toda request precisa de autenticacao
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ //metodo para criptografar a senha
        return new BCryptPasswordEncoder();  // vai ser criptografada em BCryopt
    }
}
