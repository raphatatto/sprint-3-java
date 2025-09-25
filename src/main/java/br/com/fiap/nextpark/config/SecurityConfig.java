package br.com.fiap.nextpark.config;

import br.com.fiap.nextpark.security.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authProvider(UsuarioDetailsService uds, PasswordEncoder enc) {
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(enc);
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception {
        http.authenticationProvider(provider);

        // NÃO ignore o CSRF (seus formulários já têm o _csrf escondido)
        http.csrf(csrf -> {}); // mantém padrão

        http.authorizeHttpRequests(auth -> auth
                // público
                .requestMatchers("/login", "/auth/registro", "/css/**", "/js/**").permitAll()

                // permitir OPERATOR e MANAGER usarem as telas de motos e localizar
                .requestMatchers("/motos/**", "/localizar/**").hasAnyRole("OPERATOR","MANAGER")

                // deixe claro que os POSTs de excluir/atualizar também são permitidos
                .requestMatchers(HttpMethod.POST, "/motos/*/excluir", "/motos/*/atualizar")
                .hasAnyRole("OPERATOR","MANAGER")

                // alocações: listar (OPERATOR/MANAGER), encerrar só MANAGER
                .requestMatchers("/alocacoes").hasAnyRole("OPERATOR","MANAGER")
                .requestMatchers(HttpMethod.POST, "/alocacoes/*/encerrar").hasRole("MANAGER")

                // vagas somente MANAGER
                .requestMatchers("/vagas/**").hasRole("MANAGER")

                // resto autenticado
                .anyRequest().authenticated()
        );

        // Em vez de Whitelabel quando nega acesso, redireciona com mensagem
        http.exceptionHandling(ex -> ex
                .accessDeniedHandler((req, res, e) -> res.sendRedirect("/motos?err=Acesso+negado"))
        );

        http.formLogin(f -> f
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        http.logout(l -> l
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
