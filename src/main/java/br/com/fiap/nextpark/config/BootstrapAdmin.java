// br/com/fiap/nextpark/config/BootstrapAdmin.java
package br.com.fiap.nextpark.config;

import br.com.fiap.nextpark.model.Usuario;
import br.com.fiap.nextpark.model.Role;
import br.com.fiap.nextpark.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapAdmin {
    @Bean
    CommandLineRunner ensureAdmin(UsuarioRepository repo, PasswordEncoder enc){
        return args -> {
            String adminUser = "admin";
            repo.findByUsername(adminUser).orElseGet(() -> {
                Usuario u = new Usuario();
                u.setUsername(adminUser);
                u.setPassword(enc.encode("admin123"));
                u.setRole(Role.ROLE_MANAGER);
                u.setEnabled(true);
                return repo.save(u);
            });
        };
    }
}
