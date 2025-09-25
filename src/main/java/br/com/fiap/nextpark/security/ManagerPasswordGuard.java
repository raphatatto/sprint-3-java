// src/main/java/br/com/fiap/nextpark/security/ManagerPasswordGuard.java
package br.com.fiap.nextpark.security;

import br.com.fiap.nextpark.model.Usuario;
import br.com.fiap.nextpark.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ManagerPasswordGuard {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final String managerUsername;

    public ManagerPasswordGuard(UsuarioRepository usuarios,
                                PasswordEncoder encoder,
                                @Value("${app.manager.username:manager}") String managerUsername) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.managerUsername = managerUsername;
    }

    public boolean isValid(String rawPassword) {
        return usuarios.findByUsername(managerUsername)
                .map(Usuario::getPassword)
                .map(hash -> encoder.matches(rawPassword == null ? "" : rawPassword, hash))
                .orElse(false);
    }
}
