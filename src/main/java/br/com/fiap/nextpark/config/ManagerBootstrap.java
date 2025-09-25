// br/com/fiap/nextpark/config/ManagerBootstrap.java
package br.com.fiap.nextpark.config;

import br.com.fiap.nextpark.model.Role;
import br.com.fiap.nextpark.model.Usuario;
import br.com.fiap.nextpark.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ManagerBootstrap {

    @Bean
    CommandLineRunner seedManager(UsuarioRepository repo,
                                  PasswordEncoder enc,
                                  @Value("${app.manager.username:manager}") String user,
                                  @Value("${app.manager.password:manager123}") String pass) {
        return args -> repo.findByUsername(user).orElseGet(() -> {
            var u = new Usuario();
            u.setUsername(user);
            u.setPassword(enc.encode(pass));
            u.setRole(Role.ROLE_MANAGER);
            u.setEnabled(true);
            return repo.save(u);
        });
    }
}
