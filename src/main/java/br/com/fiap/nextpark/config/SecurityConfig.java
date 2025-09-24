// br/com/fiap/nextpark/config/SecurityConfig.java
package br.com.fiap.nextpark.config;

import br.com.fiap.nextpark.security.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // habilita @PreAuthorize
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authProvider(UsuarioDetailsService uds, PasswordEncoder enc) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception {
        http.authenticationProvider(provider);
        http.authorizeHttpRequests(auth -> auth
                // público
                .requestMatchers("/login", "/auth/registro", "/css/**", "/js/**").permitAll()

                // OPERATOR pode: ver/cadastrar motos e localizar
                .requestMatchers("/motos/**").hasAnyRole("OPERATOR", "MANAGER")     // GET/POST de motos
                .requestMatchers("/localizar/**").hasAnyRole("OPERATOR", "MANAGER") // GET/POST localizar

                // alocações: listar pode (OPERATOR), encerrar só MANAGER
                .requestMatchers("/alocacoes").hasAnyRole("OPERATOR", "MANAGER")
                .requestMatchers("/alocacoes/*/encerrar").hasRole("MANAGER")

                // vagas (CRUD) – apenas MANAGER
                .requestMatchers("/vagas/**").hasRole("MANAGER")

                // qualquer outra rota: autenticado
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        http.httpBasic(Customizer.withDefaults()); // útil para testar via curl

        return http.build();
    }
}
